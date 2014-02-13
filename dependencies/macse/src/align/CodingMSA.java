package align;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Vector;

import javax.swing.tree.DefaultMutableTreeNode;

//import org.apache.commons.cli.CommandLine;
//import org.apache.commons.cli.Options;

//import com.sun.tools.javac.util.Pair;

import codesInterfaces.MacsEparamCode;

import utils.AlignmentParameterWrappers;
import utils.MacsE_param;
import utils.Named;


import bioObject.CodingDnaSeq;
import bioObject.Ribosome;
import bioObject.Sequence;

public class CodingMSA {
	public static String readLine() throws InputMismatchException
	{
		Scanner in = new Scanner(System.in);  
		String str;
		System.out.println("wait until typing ");
		str = in.nextLine();
		return str;
	}

	
	private ArrayList<CodingDnaSeq> sequences;
	private DefaultMutableTreeNode guideRoot;
	private BasicProfile initialRefAlignment=null;
	private boolean debug=false;

	Hashtable<String, ArrayList<CodingDnaSeq> > profileDB;
	ProfileAligner profileAligner;
	ElementaryCost cost;
	private Vector<String> subtrees;

	public CodingMSA(ArrayList<CodingDnaSeq> sequences, ElementaryCost cost) {
		this.sequences = sequences;
		this.profileDB = new Hashtable<String, ArrayList<CodingDnaSeq> >();
		this.cost = cost;

		int lgMax=0;
		for (CodingDnaSeq seq : sequences) {
			if (seq.length()> lgMax)
				lgMax= seq.length();
		}
		this.subtrees = new Vector<String>();
		this.profileAligner= new ProfileAligner(lgMax,cost);
		
		cleanProfileDb();		

	}

	protected float sumDist(float dist[][])
	{
		float res =0;
		for (int i = 0; i < dist.length; i++) {
			for (int j = i+1; j < dist[0].length; j++) {
				res += dist[i][j];
			}
		}
		return res;
	}
	private void cleanProfileDb()
	{
		profileDB.clear();
		if(initialRefAlignment!=null)
			profileDB.put(initialRefAlignment.getName(), initialRefAlignment.getSequences());
		for (CodingDnaSeq seq : sequences) {
			ArrayList<CodingDnaSeq> tmp = new ArrayList<CodingDnaSeq>();
			tmp.add(seq);
			//System.out.println("add to profileDB "+ seq.getName());
			profileDB.put(seq.getName(), tmp);
		}
	}

	private float partialSP(ArrayList<CodingDnaSeq> alignedSeq, boolean normed, int [][]profId)
	{
		float sum = 0;
		
		for(int i=0; i<profId[0].length;i++)
			for(int j=0; j<profId[1].length;j++)
			{
				CodingDnaSeq s1 = alignedSeq.get(profId[0][i]);
				CodingDnaSeq s2 = alignedSeq.get(profId[1][j]);
				//s1.debug=true;
				double d = -1*s1.getNormCost(s2, normed);
				sum += d;
				
				
			}
		return sum;
	}
	
	private float fullSP(ArrayList<CodingDnaSeq> alignedSeq, boolean normed)
	{
		float sum = 0;
		
		for(int i=0; i<alignedSeq.size();i++)
			for(int j=i+1; j<alignedSeq.size();j++)
			{
				CodingDnaSeq s1 = alignedSeq.get(i);
				CodingDnaSeq s2 = alignedSeq.get(j);
				double d1 = s1.getNormCost(s2, normed);
				//double d2 = s2.getNormCost(s1, normed);
				sum += d1;		
			}
		return sum;
	}

	protected float[][] distSP(ArrayList<CodingDnaSeq> alignedSeq, boolean normed)
	{
		Hashtable<String, CodingDnaSeq> alignedSeq2id = new Hashtable<String, CodingDnaSeq>();
		for(int i=0; i<alignedSeq.size();i++)
		{
			alignedSeq2id.put(alignedSeq.get(i).getName(), alignedSeq.get(i));
		}

		float dist[][]= new float[sequences.size()][sequences.size()];
		for(int i=0; i<sequences.size();i++)
			for(int j=i+1; j<sequences.size();j++)
			{
				CodingDnaSeq s1 = alignedSeq2id.get(sequences.get(i).getName());
				CodingDnaSeq s2 = alignedSeq2id.get(sequences.get(j).getName());

				float d = -1*s1.getNormCost(s2, normed);
				dist[i][j]= dist[j][i]=d;
			}
		return dist;
	}

	private float[][] reduceDist(Vector < ArrayList<CodingDnaSeq> >subProblems, ArrayList<CodingDnaSeq> sequences, float[][] dist)
	{
		float [][]res = new float[subProblems.size()][subProblems.size()];
		Hashtable<String , Integer> Seq2origId = new Hashtable<String , Integer> ();
		int [] origId2clustId = new int[sequences.size()];
		int id=0;

		for (CodingDnaSeq seq : sequences) {
			Seq2origId.put(seq.getName(), id);
			id++;
		}


		for (int clustId = 0; clustId < subProblems.size(); clustId++) {
			ArrayList<CodingDnaSeq> subPbSeq = subProblems.get(clustId);
			for (int j = 0; j < subPbSeq.size(); j++) {
				origId2clustId[Seq2origId.get(subPbSeq.get(j).getName())]= clustId;
			}
		}

		for (int i = 0; i < sequences.size(); i++) {
			for (int j = i+1; j < sequences.size(); j++) {
				res[origId2clustId[i]][origId2clustId[j]] += dist[i][j];
				res[origId2clustId[j]][origId2clustId[i]] += dist[i][j];
			}
		}
		return res;
	}

	public BasicProfile buildBigProfile(int sizeMax)
	{
		//System.out.println("compute kmer dist ");
		float dist[][]=KmerFreq.dist(sequences, 6);
		float dist2[][]=KmerFreq.dist(sequences, 10);
		for (int i = 0; i < dist.length; i++) {
			for (int j = 0; j < dist[i].length; j++) {
				dist[i][j]= (dist[i][j] + dist2[i][j])/2.f;
				//System.out.println("dist : "+i+" "+j+" "+dist[i][j]);
			}
		}
		dist2=null;
		//System.out.println("call forest Builder");
		Vector < ArrayList<CodingDnaSeq> > subProblems =UPGMA.getUPGMAmax(dist, sequences, sizeMax);
		dist2 = reduceDist(subProblems, sequences, dist);
		dist=null;
		ArrayList <BasicProfile> subRes =new ArrayList<BasicProfile>();
		int numSubpro=1;
		
		for (ArrayList<CodingDnaSeq>  subPro : subProblems) {
			
			numSubpro++;
			CodingMSA tmp = new CodingMSA(subPro,this.cost);
			subRes.add(tmp.buildProfile(10));
		}
		
		UPGMA upgma = new UPGMA(dist2,subRes);
		
		this.guideRoot = upgma.buildTree();
		// tester
		upgma=null; dist2=null;
	
		cleanProfileDb();
		for (BasicProfile profile : subRes) {
			profileDB.put(profile.getName(), profile.getSequences());	
		}
		
		subRes=null;
		buildAlignement(this.guideRoot);
		ArrayList<CodingDnaSeq> res = profileDB.get(guideRoot.getUserObject().toString());
		BasicProfile resRef = new BasicProfile(res);
		
		return resRef;

	}

	public  ScoredProfiled test2cut(BasicProfile bestAlignment, String clade, float bestSP_AA_score)
	{
		ScoredProfiled newAlignment=null;
		BasicProfile alignment=null;
		
		Vector <BasicProfile> splitProfiles= bestAlignment.splitProfile(clade);
		if (splitProfiles!= null)
		{
			Profile p1 = new Profile(splitProfiles.get(0));
			
			Profile p2 = new Profile(splitProfiles.get(1));
			
			
			ArrayList<String> templates = profileAligner.alignProfiles(p1, p2);
			/*System.out.println("\n\n templates");
			for (Sequence s1 : p1.getSequences()) {
				System.out.println(s1.getRealFullName());
				System.out.println(s1.getSeq());
			}
			System.out.println("\n");
			for (Sequence s1 : p2.getSequences()) {
				System.out.println(s1.getRealFullName());
			}
			
			System.out.println(templates.get(0));
			System.out.println(p1.getTemplate());
			System.out.println();
			System.out.println(templates.get(1));
			System.out.println(p2.getTemplate());
			System.out.println();
			*/
			if(templates.get(0).equals(p1.getTemplate()) && templates.get(1).equals(p2.getTemplate()))
			{
				//System.out.println("temp =");
			}
			else
			{
				//System.out.println("temp diff");
				int [][] profSplitId = bestAlignment.getSplitId(clade);
				float partialSPold= partialSP(bestAlignment.getSequences(), false, profSplitId);
				//System.out.println("temp #");
				alignment = new BasicProfile(bestAlignment.getName(),profileAligner.backTrack());
				//sequence order may change
				int [][] profSplitIdNew = alignment.getSplitId(clade);
				
				// compute new cost
				float partialSP= partialSP(alignment.getSequences(), false, profSplitIdNew);
			
				float internalCost =0;
				for (CodingDnaSeq seq : alignment.getSequences()) {
					internalCost -= seq.getInternalCost() * (alignment.nbSeq()-1);
				}
				
				// Brackets prevent approximation errors
				float newScore = bestSP_AA_score  + (partialSP - partialSPold);
				/*float realNewScore = fullSP(alignment.getSequences(), false);
				if(Math.abs(Math.abs(newScore) - Math.abs(realNewScore))> 0.0000000001 )
						{
						System.out.println("BUGGGGGGGG 11111111111");
						System.out.println(newScore);
						System.out.println(realNewScore);
						//System.exit(1);
						}
						*/
				
				newAlignment = new ScoredProfiled(alignment, newScore, internalCost);
				
			}
			
			
			
		}
		
		//else
		//	System.out.println("split null for clade "+ clade);
		return newAlignment;
	}
	
	
	
	public BasicProfile refine2cut(BasicProfile currentProf, int maxIter)
	{

		int refine =1;
		boolean improve=true;
		float sumDistOld = sumDist(distSP(currentProf.getSequences(),false));
		float internalCostOld =0;
		for (CodingDnaSeq seq : currentProf.getSequences()) {
			internalCostOld -= seq.getInternalCost() * (currentProf.nbSeq()-1);
		}
		
		ScoredProfiled currentSol = new ScoredProfiled(currentProf,sumDistOld,internalCostOld);
		
		
		Vector<String> clades;
		HashSet<String> testedClades= new HashSet<String>();
		while(refine <maxIter && improve == true)
		{
			float dist [][] =  distSP(currentSol.getProfile().getSequences(),true);
			//System.out.println("dist "+dist.length +" X "+dist[0].length);
			//System.out.println("nbSeq "+currentSol.getProfile().getSequences().size());
			
			UPGMA upgma = new UPGMA(dist,currentSol.getProfile().getSequences());
			//System.out.println("nbUsed upgma "+upgma.nbUsed);
			dist = null;
			DefaultMutableTreeNode tree = upgma.buildTree();
			System.out.println(tree.getUserObject().toString());
			clades = UPGMA.getSubClades(upgma.buildTree());
			// a tester
			upgma = null;
			
			improve = false;
			for (int i = 0; i < clades.size(); i++) {
				if(testedClades.contains(clades.get(i)) )
					{System.out.println("-");
					System.out.println(clades.get(i));
					}
				else
				{
					testedClades.add(clades.get(i));
					
				
					ScoredProfiled prof_2cut = test2cut(currentSol.getProfile(), clades.get(i), currentSol.SP_AA_score);
					
					/*System.out.println(currentSol.SP_AA_score+ " ---- "+ prof_2cut.SP_AA_score);
					System.out.println("\tFS* "+currentSol.SP_FS_STOP_score+ " ---- "+ prof_2cut.SP_FS_STOP_score);
					System.out.println(currentSol.getTotalSP()+ " ---- "+ prof_2cut.getTotalSP());*/
					if(prof_2cut!=null && (prof_2cut.getTotalSP() + 0.005 < currentSol.getTotalSP()))
					{
						if(debug)
						try {
							currentSol.profile.exportToFasta("/Users/vranwez/TMP/sol_"+refine+"_"+i);
							prof_2cut.profile.exportToFasta("/Users/vranwez/TMP/sol_"+refine+"_"+i+"_new");
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						currentSol = prof_2cut;
						improve=true;
						System.out.print("+");
						
						testedClades.clear();	
					}	
					else
						System.out.print(".");
					//System.out.println(clades.get(i));
				}
				//System.exit(1);
			}
			System.out.println("\nRefine 2 cut : sum of pair========= "+ refine+" => "+currentSol.getTotalSP() );
			
			refine ++;	
		}

		return currentSol.profile;
	}

	public float evalProfileScore(BasicProfile currentProf)
	{
		return sumDist(distSP(currentProf.getSequences(),false));
	}
	
	public BasicProfile refine2cutLeavesOnly(BasicProfile currentProf, int maxIter)
	{

		int refine =1;
		int nbImprove=0;
		boolean improve=true;
		float sumDistOld = sumDist(distSP(currentProf.getSequences(),false));
		float internalCostOld =0;
		for (CodingDnaSeq seq : currentProf.getSequences()) {
			internalCostOld -= seq.getInternalCost() * (currentProf.nbSeq()-1);
		}
		
		ScoredProfiled currentSol = new ScoredProfiled(currentProf,sumDistOld,internalCostOld);
		
		
		Vector<String> clades = new Vector<String>();
		HashSet<String> testedClades= new HashSet<String>();
		while(refine <maxIter && improve == true)
		{
			
			for (int i = 0; i < currentProf.nbSeq(); i++) {
				clades.add("("+currentProf.getSeq(i).getName()+")");
			}
			
			improve = false;
			for (int i = 0; i < clades.size(); i++) {
				if(testedClades.contains(clades.get(i)) )
					System.out.print("-");
				else
				{
					testedClades.add(clades.get(i));
					System.out.print(".");
						
					ScoredProfiled prof_2cut = test2cut(currentSol.getProfile(), clades.get(i), currentSol.getSP_AA_score());
					
					
					if(prof_2cut!=null && (prof_2cut.getTotalSP() < currentSol.getTotalSP()))
					{
						currentSol = prof_2cut;
						improve=true;
						nbImprove++;
						System.out.print("+");
						/*try {
							currentSol.getProfile().exportToFasta(nbImprove+"align_NT.fasta", nbImprove+"align_AA.fasta");
							
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}*/
						testedClades.clear();	
					}		
				}
				
			}
			System.out.println("\nRefine 2 cut : sum of pair========= "+ refine+" => "+currentSol.getTotalSP() );
			
			refine ++;	
		}

		return currentSol.getProfile();
	}
	
	

	public BasicProfile refine(ArrayList<CodingDnaSeq> alignment, int maxIter )
	{
		int refine =1;
		boolean improve=true;
		float sumDistOld = Float.POSITIVE_INFINITY;
		ArrayList<CodingDnaSeq> bestAlignment = alignment;
		float sumDist = sumDist(distSP(alignment,false));
		float [][]dist = distSP(alignment,true);

		while(refine < maxIter && improve == true)
		{
			subtrees.clear();
			UPGMA upgma = new UPGMA(dist,sequences);
			this.guideRoot = upgma.buildTree();

			dist = null;
			upgma = null;
			System.out.println("build UPGMA done ");

			cleanProfileDb();
			
			buildAlignement(this.guideRoot);
			
			alignment = profileDB.get(guideRoot.getUserObject().toString());
			
			this.profileAligner = new ProfileAligner(10,cost);
			System.gc();
			sumDist = sumDist(distSP(alignment,false));
			dist = distSP(alignment,true);
			System.out.println(" sum of pair guide tree ========= "+ sumDist);
			if(sumDist >= sumDistOld)
				improve = false;
			else
				bestAlignment = alignment;
			sumDistOld = sumDist;
			refine++;
		}
		return new BasicProfile("best", bestAlignment);
	}

	public BasicProfile buildProfile( int maxRefine2cut)
	{
		System.out.println("compute kmer dist ");
		float dist[][]=KmerFreq.dist(sequences, 4);
		float dist2[][]=KmerFreq.dist(sequences, 6);
		for (int i = 0; i < dist.length; i++) {
			for (int j = 0; j < dist[i].length; j++) {
				dist[i][j]= (dist[i][j] + dist2[i][j])/2.f;
			}
		}

		UPGMA upgma = new UPGMA(dist,sequences);
		dist=dist2=null;//free memory
		this.guideRoot = upgma.buildTree();
		dist = null;
		upgma = null;
		System.out.println("build UPGMA done ");

		cleanProfileDb();
		buildAlignement(this.guideRoot);
		BasicProfile alignment = new BasicProfile(profileDB.get(guideRoot.getUserObject().toString()));
		System.out.println("start refining the alignment");
		//Profile align = refine(alignment.getSequences(), maxRefineTree);
		alignment = refine2cut(new BasicProfile(alignment.getSequences()), maxRefine2cut);
		return new Profile(alignment.getName(),alignment.getSequences());	
	}
	
/*
	public Profile buildProfileFromDistance(float [][] dist)
	{
		System.out.println("build UPGMA");
		UPGMA upgma = new UPGMA(dist,sequences);

		DefaultMutableTreeNode guideRoot = upgma.buildTree();
		System.out.println("build UPGMA done ");
		this.guideRoot = guideRoot;
		System.out.println("build alignment ");
		buildAlignement(this.guideRoot);
		ArrayList<CodingDnaSeq> res = profileDB.get(guideRoot.getUserObject().toString());
	
		guideRoot.setUserObject(null);
		this.profileAligner = new ProfileAligner(10,cost);
		System.gc();
		profileDB.clear();
		Hashtable<String, CodingDnaSeq> alignedSeq2id = new Hashtable<String, CodingDnaSeq>();
		for(int i=0; i<res.size();i++)
		{
			alignedSeq2id.put(res.get(i).getName(), res.get(i));
		}

		for(int i=0; i<sequences.size();i++)
			for(int j=i; j<sequences.size();j++)
			{
				CodingDnaSeq s1 = alignedSeq2id.get(sequences.get(i).getName());
				CodingDnaSeq s2 = alignedSeq2id.get(sequences.get(j).getName());
				float d = -1*s1.getNormCost(s2,true);
				dist[i][j]= dist[j][i]=d;
			}

		// 2nd loop
		upgma = new UPGMA(dist,sequences);

		guideRoot = upgma.buildTree();
		System.out.println("build UPGMA done ");
		this.guideRoot = guideRoot;
		System.out.println("build alignment ");
		alignedSeq2id=null;
		res=null;

		for (CodingDnaSeq seq : sequences) {
			//System.out.println("add profile for: "+seq.getName());
			ArrayList<CodingDnaSeq> tmp = new ArrayList<CodingDnaSeq>();
			tmp.add(seq);
			profileDB.put(seq.getName(), tmp);
		}

		buildAlignement(this.guideRoot);
		res = profileDB.get(guideRoot.getUserObject().toString());
		return new Profile(guideRoot.getUserObject().toString(), res );

	}
*/
	public BasicProfile buildProfileFromDistance(float [][] dist, ArrayList<CodingDnaSeq> initialSeq)
	{
		seqAddGuideTree seqAdd_upgma = new seqAddGuideTree(dist,sequences,initialSeq);
		//DefaultMutableTreeNode guideRoot = seqAdd_upgma.buildTree(true);
		DefaultMutableTreeNode guideRoot = seqAdd_upgma.buildTreeUsingAdded(true,"");
		this.guideRoot = guideRoot;
		System.out.println(guideRoot.getUserObject().toString());
		buildAlignement(this.guideRoot);
		ArrayList<CodingDnaSeq> res = profileDB.get(guideRoot.getUserObject().toString());
		return new BasicProfile(guideRoot.getUserObject().toString(), res );
	}

	public BasicProfile addLessReliableSeq(float [][] dist, ArrayList<CodingDnaSeq> sequences, ArrayList<CodingDnaSeq> AlignedinitialSeq, String AlignedSeqLabel)
	{
		seqAddGuideTree seqAdd_upgma = new seqAddGuideTree(dist,sequences,AlignedinitialSeq);
		//DefaultMutableTreeNode guideRoot = seqAdd_upgma.buildTree(true);
		this.initialRefAlignment= new BasicProfile(AlignedSeqLabel,AlignedinitialSeq);
		cleanProfileDb();
		DefaultMutableTreeNode guideRoot = seqAdd_upgma.buildTreeUsingAdded(false, AlignedSeqLabel);

		this.guideRoot = guideRoot;
		//System.out.println(guideRoot.getUserObject().toString());
		buildAlignement(this.guideRoot);
		ArrayList<CodingDnaSeq> res = profileDB.get(guideRoot.getUserObject().toString());
		return new BasicProfile(guideRoot.getUserObject().toString(), res );
	}


	public  void buildAlignement(DefaultMutableTreeNode node)
	{
		

		if(! profileDB.containsKey(node.getUserObject().toString()))
		{
			System.out.print("+");
			//System.out.println("traite: "+ node.getUserObject().toString());
			DefaultMutableTreeNode son1 = (DefaultMutableTreeNode)node.getChildAt(0);
			DefaultMutableTreeNode son2 = (DefaultMutableTreeNode)node.getChildAt(1);
			buildAlignement(son1);
			buildAlignement(son2);
			Profile p1 = new Profile(son1.getUserObject().toString(),profileDB.get(son1.getUserObject().toString()));
			Profile p2 = new Profile(son2.getUserObject().toString(),profileDB.get(son2.getUserObject().toString()));
			profileAligner.alignProfiles(p1,p2);
			ArrayList<CodingDnaSeq> pSeq = profileAligner.backTrack();
			profileDB.put(UPGMA.normalizedClusterLabel(p1.getName(), p2.getName()), pSeq);
			// do dot stock previous result prevent fast 2nd iteration but free some memory
			profileDB.remove(son1.getUserObject().toString()); son1.setUserObject(null);
			profileDB.remove(son2.getUserObject().toString()); son2.setUserObject(null);		
			
			
		}
		subtrees.add(node.toString());
		
		

	}
	
	public static BasicProfile buildAlignmentReliable(ArrayList<CodingDnaSeq> sequences, ElementaryCost cost)
	{
		System.out.println("Build draft alignment using greedy strategy based on UPGMA tree");
		CodingMSA msa =new CodingMSA(sequences,cost);
		//Profile currentAlign = msa.buildProfile();
		BasicProfile currentAlign;
		
		if(sequences.size() > 50)
		{
		int sizeClust=30;
		if(sequences.size()>300)
			sizeClust= (int)(sequences.size()/10);
		 System.out.println("Start building smaller alignments "+ sizeClust);
		 currentAlign= msa.buildBigProfile(sizeClust);	 
		 System.out.println("Start refining the whole alignment of reliable sequences "+ currentAlign.getSequences().size());
		 msa =new CodingMSA(currentAlign.getSequences(),cost);	
		currentAlign = msa.refine2cut(new BasicProfile(currentAlign.getSequences()), 500);
		}
		else
		{
			currentAlign = msa.buildProfile(500);
		}
		
		return currentAlign;
	}

	public static BasicProfile run(AlignmentParameterWrappers parameters) throws Exception {
		
		double t1= System.currentTimeMillis();
		
		BasicProfile currentAlign = CodingMSA.buildAlignmentReliable(parameters.getSequences(), parameters.getCost());
		
		if(parameters.hasLessReliableSequences())
		{
			System.out.println("Start adding less reliable sequences");
			// cost n'est utilisé que pour le beg_end factor il faudrait le virer des paramètres ...
			CodingMSA msaLR =new CodingMSA(parameters.getAllSequences(),parameters.getCost());
			currentAlign = msaLR.addLessReliableSeq( KmerFreq.dist(parameters.getAllSequences(), 6),parameters.getAllSequences(),currentAlign.getSequences(), currentAlign.getName());		
			msaLR=null;// to free memory
		
			CodingMSA msa =new CodingMSA(currentAlign.getSequences(),parameters.getCost());
			System.out.println("Start refining the whole alignment");
			currentAlign = msa.refine2cut(new BasicProfile(currentAlign.getSequences()), 500);
		}
		
		
		double t2= System.currentTimeMillis();
		
		System.out.println("total time:\t"+ (t2-t1));
		//System.out.println("align time:\t"+ProfileAligner.time_spend);
		//System.out.println("align min:\t"+ProfileAligner.time_min);
		
		return currentAlign;
		
	}
	
	public static void main(String[] args) throws Exception {
		
		MacsE_param macseOption = new MacsE_param(args);
		double t1 = System.currentTimeMillis();
		//CommandLine cmd = macseOption.parse(args);

		float fs = macseOption.getFsCost();
		float gapE = macseOption.getGapOpExt();
		float gapO=macseOption.getGapOpCost();
		float gapC=macseOption.getGapCloseCost();
		float stop = macseOption.getStopCost();
		float begEndGapFact = macseOption.getBegEndGapFactor();
		float optPessFact = macseOption.getOptimisticPesssimisticGap_factor();
		float fsLR = macseOption.getLessReliableFsCost();
		float stopLR = macseOption.getLessReliableStopCost();
	
		
		//String initialAlignmentFile = macseOption.get_initial_alignment();

		gapO += gapC;
		gapC =0;
		
		ElementaryCost cost = new ElementaryCost(fs,gapE,gapO,gapC,stop,begEndGapFact,optPessFact);
		ElementaryCost costLR = new ElementaryCost(fsLR,gapE,gapO,gapC,stopLR,begEndGapFact,optPessFact);

		Ribosome.defaultCode= macseOption.get_default_GC_code();
		SubstitutionScore.defaultMatrix = macseOption.getSubstMatrix();
		/*String fullCmd=args[0];
		for (int i = 1; i < args.length; i++) {
			fullCmd= fullCmd+" "+args[i];
		}
		System.out.println("your command line: "+fullCmd+"\n");*/
		System.out.println("parameters:");
		System.out.println("");
		System.out.println("general parameters");
		System.out.println("\tsubstitution Matrix \t"+ SubstitutionScore.defaultMatrix);
		System.out.println("\tdefault genetic code \t"+Ribosome.defaultCode+" ");
		//System.out.println("\tinitial Alignment file \t"+initialAlignmentFile);
		System.out.println("");
		System.out.println("gap opening and gap extension");
		System.out.println("\topening \t\t" + gapO);
		System.out.println("\textension \t\t" + gapE);
		System.out.println("");
		System.out.println("stop codon and frameshift in standard sequences");
		System.out.println("\tstop     \t\t" + stop);
		System.out.println("\tframeshift \t\t" + fs);
		System.out.println("");
		System.out.println("stop codon and frameshift in less reliable sequences");
		System.out.println("\tstop codon\t\t" + stopLR);
		System.out.println("\tframeshift \t\t" + fsLR);
		System.out.println("");
		/*System.out.println("other parameters regarding gap");
		System.out.println("\tmultiplicative factor for underpenalizing gap at extremities \t" + begEndGapFact);
		System.out.println("\tration for optimistic / Pesiimistic factor \t" + optPessFact);
		System.out.println("");*/
		System.out.println("Genetic code used:");
	

		Hashtable<String, Ribosome> seq2Ribo = new Hashtable<String, Ribosome>();
		if(macseOption.get_GC_file() !=null) 
			seq2Ribo= Ribosome.parseGCfile(macseOption.get_GC_file());

		ArrayList<CodingDnaSeq> sequences= CodingDnaSeq.readFasta(macseOption.getInputReliableFile(),seq2Ribo,true,cost);
		ArrayList<CodingDnaSeq> lessReliableSequences = new ArrayList<CodingDnaSeq>();
		
		if( macseOption.getInputLessReliableFile() !=null )
		{
			lessReliableSequences= CodingDnaSeq.readFasta(macseOption.getInputLessReliableFile(),seq2Ribo,true,costLR);
		}
		
		int nb=1;
		for (CodingDnaSeq s : lessReliableSequences) {
			s.setNames("seq_lr_"+nb, s.getRealFullName());
			nb++;
		}
		nb=1;
		for (CodingDnaSeq s : sequences) {
			s.setNames("seq_"+nb, s.getRealFullName());
			nb++;
		}
		
		//sequences.addAll(lessReliableSequences);


		System.out.println("\naligning sequences ...");
		
		
		BasicProfile currentAlign=null;
		/*if(initialAlignmentFile!=null)
		{
			System.out.println("read alignment "+ initialAlignmentFile);
			currentAlign = new BasicProfile("toto",CodingDnaSeq.readFasta(initialAlignmentFile, seq2Ribo, false, new ElementaryCost(fs,gapE,gapO,gapC,stop,begEndGapFact,optPessFact)));
			System.out.println("initial alignment read from file "+initialAlignmentFile+"\n refined ...");
			CodingMSA msa =new CodingMSA(currentAlign.getSequences(),new ElementaryCost(fs,gapE,gapO,gapC,stop,begEndGapFact,optPessFact));
			
			currentAlign = msa.refine2cut(new BasicProfile(currentAlign.getSequences()), 500);		
		}
		else*/
		{
		
		currentAlign =CodingMSA.buildAlignmentReliable(sequences, cost);
		
		
		if(lessReliableSequences.size()>0)
		{
			System.out.println("Start adding less reliable sequences");
			sequences.addAll(lessReliableSequences);
			lessReliableSequences = null;
			CodingMSA msaLR =new CodingMSA(sequences,new ElementaryCost(fs,gapE,gapO,gapC,stop,begEndGapFact,optPessFact));
			currentAlign = msaLR.addLessReliableSeq( KmerFreq.dist(sequences, 6),sequences,currentAlign.getSequences(), currentAlign.getName());
			
			msaLR=null;// to free memory
			//currentAlign = msa.profileAligner.alignProfiles_2profile(currentAlign, alignLessRel);
			CodingMSA msa =new CodingMSA(currentAlign.getSequences(),new ElementaryCost(fs,gapE,gapO,gapC,stop,begEndGapFact,optPessFact));
			
			System.out.println("Start refining the whole alignment");
			currentAlign = msa.refine2cut(new BasicProfile(currentAlign.getSequences()), 500);
			//currentAlign = msa.refine2cut(new BasicProfile(currentAlign.getSequences()), 1);
		}
		}
		
		double t2= System.currentTimeMillis();
		
		System.out.println("total time:\t"+ (t2-t1));
		//System.out.println("align time:\t"+ProfileAligner.time_spend);
		//System.out.println("align min:\t"+ProfileAligner.time_min);
		
		
			currentAlign.exportToFasta(macseOption.getNTOutputFile(), macseOption.getAAOutputFile());
		
	}

}
