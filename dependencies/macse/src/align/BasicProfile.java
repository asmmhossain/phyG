package align;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Vector;

import codesInterfaces.NT_AAsymbols;


import bioObject.CodingDnaSeq;
import utils.Named;

public class BasicProfile implements Named{

	private ArrayList<CodingDnaSeq> sequences;
	private String template;
	private String name;
	private int nbSites;
	private ElementaryCost cost;

	protected void setTemplate(String template)
	{
		this.template=template;
	}
	public String getTemplate()
	{
		return template;
	}
	public ArrayList<CodingDnaSeq> getSequences()
	{
		return sequences;
	}

	public String getName() {
		return name;
	}

	public final int nbSeq()
	{
		return sequences.size();
	}


	public CodingDnaSeq getSeq(int seqNum)
	{
		return sequences.get(seqNum);
	}

	public char getChar(int seq, int site)
	{
		return sequences.get(seq).getDNA(site); 
	}

	public  int nbSites()
	{
		return nbSites; 
	}

	
	BasicProfile( String profName, CodingDnaSeq seq)
	{		
		ArrayList<CodingDnaSeq> profSeq = new ArrayList<CodingDnaSeq>();
		profSeq.add(seq);
		this.name = profName;
		this.sequences=profSeq;
		this.nbSites=seq.length();
		this.cost =getSeq(0).getCost();
	}

	public BasicProfile(ArrayList<CodingDnaSeq> profSeq) {
		StringBuffer nameBf = new StringBuffer();
		for (int i = 0; i < profSeq.size(); i++) {
			if(i>0)
				nameBf.append(",");
			nameBf.append(profSeq.get(i).getName());
		}
		this.name = "("+nameBf.toString()+")"; // CONSTRUCT NAME FROM SEQ
		this.sequences=profSeq;
		if(nbSeq() >0)
		{
			this.cost =getSeq(0).getCost();
			this.nbSites=getSeq(0).length();
		}
	}

	public BasicProfile(String profName, ArrayList<CodingDnaSeq> profSeq) {
		this.name = profName;
		this.sequences=profSeq;
		if(nbSeq() >0)
		{
			this.cost =getSeq(0).getCost();
			this.nbSites=getSeq(0).length();
		}
	}

	
	public void exportToFasta(String fileName) throws IOException
	{
		String AAfile = fileName+"_AA.fasta";
		String NTfile = fileName+"_NT.fasta";
		exportToFasta(NTfile, AAfile);
		
	}
	
	public Hashtable<String,String> standartizationOfSeqNames(String prefix)
	{
		Hashtable nameDictionnary = new Hashtable<String, String>();
		int id_nb=1;
		CodingDnaSeq stmp;
		for (int seqNum=0; seqNum<this.nbSeq();seqNum++,id_nb++) {
			stmp = this.getSeq(seqNum);
			stmp.setNames(prefix+"_"+id_nb, stmp.getRealFullName());
			nameDictionnary.put(stmp.getRealFullName(), stmp.getName());
			id_nb++;
		}
		return nameDictionnary;
	}
	
	public void exportStat(String statFile) throws IOException
	{
		BufferedWriter bwStat = new BufferedWriter(new FileWriter(statFile));
		
		for (CodingDnaSeq seq : sequences) {
			int[] nb_FS_stop_gap_inSeq = seq.nb_internal_FS_STOP_Gap(1);
			bwStat.append(">"+seq.getRealFullName()+";FS="+nb_FS_stop_gap_inSeq[0]+";STOP="+nb_FS_stop_gap_inSeq[1]+";INS="+nb_FS_stop_gap_inSeq[2]+";\n");
		}
		bwStat.close();
	}
	
	public void exportperSiteNTStat(String statFile) throws IOException
	{
		// A C G T - ! other
		int[][] stat = new int[7][this.nbSites];
	
		
		for (CodingDnaSeq seq : sequences) {
			for(int cPos=0; cPos<this.nbSites;cPos++)
			{
				switch (seq.get(cPos)) {
				case 'A': stat[0][cPos]++;
					break;
				case 'C': stat[1][cPos]++;
					break;
				case 'G': stat[2][cPos]++;
					break;
				case 'T': stat[3][cPos]++;
					break;
				case '-': stat[4][cPos]++;
					break;
				case '!': stat[5][cPos]++;
					break;
				default:stat[6][cPos]++;
					break;
				}
			}
		}
		
		BufferedWriter bwStat = new BufferedWriter(new FileWriter(statFile));
		bwStat.append("pos\t#A\t#C\t#G\t#T\t#gap(-)\t#FS(!)\tOther\ttotal");
		for(int cPos=0; cPos<this.nbSites;cPos++)
		{
			bwStat.append("\n"+(cPos+1)+"");
			for (int i = 0; i < stat.length; i++) {
				bwStat.append("\t"+stat[i][cPos]);
			}
			bwStat.append("\t"+this.nbSites);
		}
		bwStat.close();
	}
	
	public void exportToFasta(String NTfile, String AAfile) throws IOException
	{
		BufferedWriter bwAA = new BufferedWriter(new FileWriter(AAfile));
		BufferedWriter bwDNA = new BufferedWriter(new FileWriter(NTfile));
		for (CodingDnaSeq seq : sequences) {
			bwDNA.append(seq.toFasta()+"\n");
			bwAA.append(seq.toAAfasta(1)+"\n");
		}
		bwDNA.close();
		bwAA.close();

	}
	
	
	public String toString() {
		StringBuffer br = new StringBuffer();
		br.append("\n\n NT Alignment\n");
		for (CodingDnaSeq seq : sequences) {
			br.append(seq.toFasta()+"\n");
		}
		br.append("\n\n AA Alignment\n");
		for (CodingDnaSeq seq : sequences) {
			br.append(seq.toAAfasta(1)+"\n");
		}
		return br.toString();
	}
	
	public HashSet<String> getSeqNameFromTree(String tree)
	{
		tree= tree.replaceAll("\\(", "");
		tree= tree.replaceAll("\\)", "");
		String[]seqTab= tree.split(","); 
		HashSet<String> seqInTree = new HashSet<String>();
		for (int i = 0; i < seqTab.length; i++) {
			seqInTree.add(seqTab[i]);
		}
		return seqInTree;
	}
	
	public int[][] getSplitId(String subtree)
	{
		//System.out.println("\n splitting "+ subtree);
		int profId[][] = new int[2][];
		subtree= subtree.replaceAll("\\(", "");
		subtree= subtree.replaceAll("\\)", "");
		
		String[]seqProf1 = subtree.split(","); // return a "" if seq is empty !? 
		if (subtree.equals(""))
			seqProf1 = new String[0];
		HashSet<String> seqInP1 = new HashSet<String>();
		for (int i = 0; i < seqProf1.length; i++) {
			seqInP1.add(seqProf1[i]);
			//System.out.println("add in P1:"+ seqProf1[i]+"@");
		}
		profId[0]= new int[seqInP1.size()];
		profId[1]= new int[nbSeq()-seqInP1.size()];
		//System.out.println("getSplit "+ nbSeq()+" -> "+ profId[0].length+" | "+profId[1].length);
		int idProf1 = 0;
		int idProf2=0;
		for (int i = 0; i < nbSeq(); i++) {
			if(seqInP1.contains(getSeq(i).getName() ) )
				{
				//System.out.println("add in P1 seq "+ getSeq(i).getName()+"@@ id:"+i);
				profId[0][idProf1++]=i;
				}
			else
			{
				//System.out.println("add in P2 seq "+ getSeq(i).getName()+"@@ id:"+i);
				profId[1][idProf2++]=i;
			}
		}
		return profId;
	}
	/*
	 * usefull for 3 cut refinement
	 * return seqId of the 2 subtrees + remaining taxa in order subtree1 remaining subtree2
	 * for 2-cut just set subtree2 to ""
	 */
	public int[][] getSplitId(String subtree1, String subtree2)
	{
		int profId[][] = new int[3][];
		HashSet<String> seqInP1 = getSeqNameFromTree(subtree1);
		HashSet<String> seqInP2 = getSeqNameFromTree(subtree2);
		profId[0]= new int[seqInP1.size()];
		profId[1]= new int[nbSeq()-seqInP1.size() - seqInP2.size()];
		profId[2]= new int[seqInP2.size()];
		int idProf1,idProf2,idProfRemain;
		idProf1=idProf2=idProfRemain=0;
		for (int i = 0; i < nbSeq(); i++) {
			if(seqInP1.contains(getSeq(i).getName() ) )
			{
				profId[0][idProf1++]=i;
			}
			else
			{
				if(seqInP2.contains(getSeq(i).getName() ) )
				{
					profId[2][idProf2++]=i;
				}
				else
				{
					profId[1][idProfRemain++]=i;
				}
			}
		}
		return profId;
	}

	public Vector<BasicProfile> splitProfile(String subtree)
	{
		Vector<BasicProfile> res = new Vector<BasicProfile>();
		
		int [][] profId = getSplitId(subtree);
		if(profId[0].length==0 || profId[1].length==0)
			return null;
		res.add(extractSubProfile(profId[0]));
		res.add(extractSubProfile(profId[1]));
		return res;

	}

	public Vector<BasicProfile> splitProfile(String subtree1, String subtree2)
	{
		Vector<BasicProfile> res = new Vector<BasicProfile>();
		
		int [][] profId = getSplitId(subtree1,subtree2);
		if(profId[0].length==0 || profId[1].length==0)
			return null;
		res.add(extractSubProfile(profId[0])); // subtree1
		res.add(extractSubProfile(profId[1])); // remaining
		res.add(extractSubProfile(profId[2])); // subtree2
		
		return res;

	}
	
	public BasicProfile extractSubProfile(int firstSite, int lastSite) {
		StringBuffer [] newSeq = new StringBuffer[this.nbSeq()];
		if(firstSite <=0)
			firstSite =1;
		if(lastSite <0)
			lastSite =this.nbSites();
		firstSite--;// user provide index starting at 1 while first char in String is at index 0
		lastSite--;
		
		ArrayList<CodingDnaSeq> res = new ArrayList<CodingDnaSeq>();
		for (int seqNum = 0; seqNum < newSeq.length; seqNum++) {
			res.add(new CodingDnaSeq(this.getSeq(seqNum).getSeq().substring(firstSite, lastSite+1), this.getSeq(seqNum)));
				
		}
		return new BasicProfile(res);
		
	}
	//
	public BasicProfile extractSubProfile(float maxPcGap) {
		
		//System.out.println("in extract, nb Seq="+this.nbSeq());
		float nbGapInt[] = new float [this.nbSites()];
		float nbSeq[] = new float [this.nbSites()];
		boolean keepSites[]= new boolean [this.nbSites()];
		int seqToKeep [] = new int [this.nbSeq()];
		for (int site = 0; site < nbSites(); site+=1) {
			nbSeq[site]=0f;
		}
		
		
		for (int seq = 0; seq < nbSeq() ; seq++) {
			CodingDnaSeq s1 = this.getSeq(seq);
			for(int site=s1.getFirstNonGap(); site<=s1.getLastNonGap();site++)
			{
				nbSeq[site]+=1f;
			}
			for (int site = 2; site < nbSites(); site+=3) {
				if(s1.getAA(site)==NT_AAsymbols.AA_GAP_int || s1.getAA(site)==NT_AAsymbols.AA_FS_int || s1.getAA(site)==NT_AAsymbols.AA_GAPE_int || s1.getAA(site)==NT_AAsymbols.AA_FSE_int)
				{
					nbGapInt[site]=nbGapInt[site]+1f ;
				}
			}
			
		}
		for (int site = 2; site < nbSites(); site+=3) {
			nbSeq[site]=this.nbSeq(); // !!!!!!!!!!!!!! since conunting all gaps all seq are in the percentage
			float pcGap = nbGapInt[site]/nbSeq[site];
			//System.out.println(site+"\t"+ nbGapInt[site]+"\t"+nbSeq[site]+"\t"+pcGap +"\t"+maxPcGap);
			if(nbGapInt[site]/nbSeq[site] >= maxPcGap)
			{
				keepSites[site]=false;
				//System.out.println("gappy site\t"+site );
			}
			else
				keepSites[site]=true;
		}
		int nbKeepSeq=0;
		for (int seq = 0; seq < nbSeq() ; seq++) {
			CodingDnaSeq s1 = this.getSeq(seq);
			boolean keepSeq =true;
			for (int site = 2; site < nbSites() && keepSeq==true; site+=3) {
				if( !(s1.getAA(site)==NT_AAsymbols.AA_GAP_int || s1.getAA(site)==NT_AAsymbols.AA_GAPE_int|| s1.getAA(site)==NT_AAsymbols.AA_FS_int|| s1.getAA(site)==NT_AAsymbols.AA_FSE_int) && keepSites[site]==false )
					keepSeq=false;
			}
			if(keepSeq==true)
				seqToKeep[nbKeepSeq++]=seq;
			//else
				//System.out.println("remove:\t"+s1.getRealFullName());
		}
		
		int seqToKeepFinal[]= new int[nbKeepSeq];
		for (int i = 0; i < seqToKeepFinal.length; i++) {
			seqToKeepFinal[i]=seqToKeep[i];
		}
		return extractSubProfile(seqToKeepFinal);
	}
	
	//
	
	
	private BasicProfile extractSubProfile(int[] profId) {
		StringBuffer templateTmp= new StringBuffer();
		StringBuffer [] newSeq = new StringBuffer[profId.length];
		for (int i = 0; i < newSeq.length; i++) {
			newSeq[i]= new StringBuffer("");
		}

		char tmp;
		int nbG, nbFS;
		boolean keep;
		for (int site = 0; site < nbSites(); site++) {
			nbG=nbFS=0;
			keep = false;
			for (int seq = 0; seq < profId.length && keep==false; seq++) {
				tmp = getChar(profId[seq], site);
				if(tmp==NT_AAsymbols.NT_GAP)
					nbG++;
				else 
				{
					if(tmp==NT_AAsymbols.NT_FS)
						nbFS++;
					else
						keep=true;
				}
			}
			// c'est nul on vire les site meme si il y a un melange de gap et de FS
			// oui mais si on ne fait pas ca il faut reinserer les FS dans le profil ce qui est quasi impossible et bloque
			// les amelioration (le mix gap FS est normalement gerer par le nettoyage des coding sequences
			
			if (nbG!=profId.length && nbFS !=profId.length) // we remove sites containing only gap or only frameshift
				keep=true;
			else
				keep =false;
			
			if(keep==true)
			{
				for (int seq = 0; seq < profId.length ; seq++) {
					newSeq[seq].append(getChar(profId[seq], site));
				}
				templateTmp.append("N");
			}
			else
				templateTmp.append("-");
		}
		template = (CodingDnaSeq.correctFS(templateTmp.toString())).toString();
		ArrayList<CodingDnaSeq> res = new ArrayList<CodingDnaSeq>();
		for (int seq = 0; seq < profId.length ; seq++) {
			try {
				
				res.add(new CodingDnaSeq(newSeq[seq].toString(),getSeq(profId[seq])));
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
		/*	CodingDnaSeq s0 = res.get(0);
		if(res.size()==1)
			System.out.println("seq Name: "+s0.getName());
		if(s0.getName().equals(">Ps_Balaenoptera_bonaerensis_JG_pseudo") && res.size()==1)
			System.out.println(s0.getSeq());*/
		BasicProfile bp = new BasicProfile(res);
		bp.setTemplate(template);
		return bp;

	}
}
