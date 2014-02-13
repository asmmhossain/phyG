package align;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.Vector;

import codesInterfaces.CostCode;
import codesInterfaces.DimerCodes;
import codesInterfaces.NT_AAsymbols;

import utils.Named;

import bioObject.CodingDnaSeq;



// faire un profile dedie a 1 sequence pour optimiser

public class Profile extends BasicProfile{

	//private CharFreqManager[] siteFreq;// shift by 1
	public float[][] dimerFreq; // dimer freq at this position
	private Vector <CharFreqManager> aaFreq;
	private int [] refSite;
	
	ElementaryCost cost;
	private float[][] insertFSCost; // [i][di] cost of FS from i-(di+1) to i
	private float[] insertGapCost; // cost of inserting a gap after site i
	private float[] internalCost; // cost of inserting a gap after site i
	
	private boolean fsAllowed;
	
	public String dimerCodes(int site)
	{
		String res="";
		res+=" d0:"+dimerFreq[site][DimerCodes.d0]+"\t";
		res+="d0E:"+dimerFreq[site][DimerCodes.d0E]+"\t";
		res+=" d1:"+dimerFreq[site][DimerCodes.d1]+"\t";
		res+="d01:"+dimerFreq[site][DimerCodes.d01]+"\t";
		res+="d00:"+dimerFreq[site][DimerCodes.d00]+"\t";
		res+="d11:"+dimerFreq[site][DimerCodes.d11]+"\t";
		res+="d10:"+dimerFreq[site][DimerCodes.d10]+"\t";
		return res;
	}
	
	public boolean isFsAllowed() {
		return fsAllowed;
	}

	public int[] getRefSite()
	{
		return refSite;
	}
	
	private void initFreq()
	{
		int seqLength = getSeq(0).length();
		aaFreq = new Vector<CharFreqManager>(seqLength + 1);
		dimerFreq = new float[seqLength +1][DimerCodes.nbDimerCode];
		insertFSCost = new float [seqLength +1][2];
		insertGapCost = new float [seqLength +1];
		internalCost = new float [seqLength +1];
		
		int curAA, prevAA;
		float [] letFreq= new float[NT_AAsymbols.NB_AA_cost_indep];
		int [] presentAA = new int [NT_AAsymbols.NB_AA_cost_indep];
		int nbDistinctAA;
		
		// the first site of the profile is a col of ?
		presentAA[0]=NT_AAsymbols.AA_UKN_int;
		letFreq[NT_AAsymbols.AA_UKN_int] = this.nbSeq();
		aaFreq.add(new CharFreqManager(presentAA,letFreq,1,0,cost));
		nbDistinctAA =1;
		dimerFreq[0][DimerCodes.d0]=0;
		dimerFreq[0][DimerCodes.d1]=this.nbSeq();//dimerFreq[0][DimerCodes.dX]=nbSeq();
		dimerFreq[0][DimerCodes.d01]=dimerFreq[0][DimerCodes.d10]=dimerFreq[0][DimerCodes.d00]=0;
		dimerFreq[0][DimerCodes.d11]= nbSeq(); // ghost column are filled of ?
		
		
		// change to - to avoid cost of gapOpening (no gap closing cost)
		/*aaFreq.add(new CharFreqManager(presentAA,letFreq,0,0,cost));
		nbDistinctAA =0;
		dimerFreq[0][DimerCodes.d0]=this.nbSeq();;
		dimerFreq[0][DimerCodes.d1]=0;//dimerFreq[0][DimerCodes.dX]=nbSeq();
		dimerFreq[0][DimerCodes.d01]=dimerFreq[0][DimerCodes.d10]=dimerFreq[0][DimerCodes.d11]=0;
		dimerFreq[0][DimerCodes.d00]= nbSeq(); // ghost column are filled of -
		*/
		
		for (int seqId =0; seqId< this.nbSeq(); seqId++)
		{
			insertFSCost[0][0]=getSeq(seqId).getCost().get(CostCode.FS) *  CostCode.FSE_FACTOR;
			insertFSCost[0][1]=getSeq(seqId).getCost().get(CostCode.FS) *  CostCode.FSE_FACTOR;
		}
		insertGapCost[0]=cost.get(CostCode.GAP_E) * cost.get(CostCode.BEG_END_GAP_FACTOR) *nbSeq();
		// end of first site special management
		
		float internalCost ;
		for (int site = 1; site < seqLength+1; site++) {
			
			// clear before dealing an other site
			for(int i=0;i<nbDistinctAA;i++)
			{
				letFreq[presentAA[i]]=0;
			}
			nbDistinctAA =0;
			internalCost =0;
			
			int nbGap=0;
			int nbGapE=0;
			// compute current site AA Freq
			for (int seqId =0; seqId< this.nbSeq(); seqId++)
			{
				curAA = getSeq(seqId).getAA(site-1); // shift
				try {
					prevAA = getSeq(seqId).getAA(site-1 -3); //previous AA is 3 cases (a codon size) before current one
				} catch (Exception e) {
					prevAA = NT_AAsymbols.AA_UKN_int;
				}
				
				if(curAA<NT_AAsymbols.NB_AA_cost_indep)
				{
					if(letFreq[curAA]==0)
						presentAA[nbDistinctAA++]=curAA;
					letFreq[curAA]++;
				}
				if(curAA == NT_AAsymbols.AA_STOP_int)
					internalCost += getSeq(seqId).getCost().get(CostCode.STOP);

				if(curAA == NT_AAsymbols.AA_FS_int)
					internalCost += getSeq(seqId).getCost().get(CostCode.FS);
				
				if(curAA == NT_AAsymbols.AA_FSE_int)
					internalCost += getSeq(seqId).getCost().get(CostCode.GAP_E) * cost.get(CostCode.BEG_END_GAP_FACTOR);;
					//internalCost += getSeq(seqId).getCost().get(CostCode.FS) *  CostCode.FSE_FACTOR;
				
				if(curAA == NT_AAsymbols.AA_GAP_int)
					nbGap++;
				if(curAA == NT_AAsymbols.AA_GAPE_int)
					nbGapE++;
				
				// compute current site DimerFreq on the fly 
				if((NT_AAsymbols.aaSymbolTab[prevAA] == NT_AAsymbols.AA_GAP) && (NT_AAsymbols.aaSymbolTab[curAA] == NT_AAsymbols.AA_GAP))
					dimerFreq[site][DimerCodes.d00]++;
				else if((NT_AAsymbols.aaSymbolTab[prevAA] == NT_AAsymbols.AA_GAP) && (NT_AAsymbols.aaSymbolTab[curAA] != NT_AAsymbols.AA_GAP))
					dimerFreq[site][DimerCodes.d01]++;
				else if((NT_AAsymbols.aaSymbolTab[prevAA] != NT_AAsymbols.AA_GAP) && (NT_AAsymbols.aaSymbolTab[curAA] == NT_AAsymbols.AA_GAP))
					dimerFreq[site][DimerCodes.d10]++;
				else if((NT_AAsymbols.aaSymbolTab[prevAA] != NT_AAsymbols.AA_GAP) && (NT_AAsymbols.aaSymbolTab[curAA] != NT_AAsymbols.AA_GAP))
					dimerFreq[site][DimerCodes.d11]++;
						

				// compute the gap extending cost related to inserting a gap column after this site
				// BUG V6 > au lieu de >=s
				if( ((site-1) >= getSeq(seqId).getLastNonGap()) || ((site-1)<getSeq(seqId).getFirstNonGap()) )
					insertGapCost[site] += cost.get(CostCode.GAP_E) * cost.get(CostCode.BEG_END_GAP_FACTOR);
				else
					insertGapCost[site] += cost.get(CostCode.GAP_E);
			
			
				for (int di=1; di<=2;di++)
				{
					
					/*if( ((site-1) >= getSeq(seqId).getLastNonGap()) || ((site-1-di)<getSeq(seqId).getFirstNonGap()) )
						insertFSCost[site][di-1] += getSeq(seqId).getCost().get(CostCode.FS) *  CostCode.FSE_FACTOR;//* getSeq(seqId).getCost().get(CostCode.BEG_END_GAP_FACTOR);
					else
						insertFSCost[site][di-1] += getSeq(seqId).getCost().get(CostCode.FS);
						*/
					//	il faudrait gerer l'ajout d'un FS apres un FS plus correctement
					if(getSeq(seqId).get(site-1)==NT_AAsymbols.NT_FS)
						insertFSCost[site][di-1] += insertGapCost[site];
					else
					{
					if( ((site-1) >= getSeq(seqId).getLastNonGap()) || ((site-1-di)<getSeq(seqId).getFirstNonGap()) )
						insertFSCost[site][di-1] += getSeq(seqId).getCost().get(CostCode.FS) *  CostCode.FSE_FACTOR;//* getSeq(seqId).getCost().get(CostCode.BEG_END_GAP_FACTOR);
					else
						insertFSCost[site][di-1] += getSeq(seqId).getCost().get(CostCode.FS);
					}
				}
				
			}
			this.internalCost[site]=internalCost;
			aaFreq.add(new CharFreqManager(presentAA,letFreq,nbDistinctAA,site,cost));
			dimerFreq[site][DimerCodes.d0]= nbGap+ nbGapE;
			dimerFreq[site][DimerCodes.d0E]= nbGapE;
			dimerFreq[site][DimerCodes.d1]= this.nbSeq() - dimerFreq[site][DimerCodes.d0];
			
		}
		
	}

	
	
	private void init()
	{
		initFreq();
		
		this.fsAllowed = true;

	}	
	
	
	
	public CharFreqManager getCharFreq(int site)
	{
		return aaFreq.get(site);
	}
	
	
	
	
	public float getInsertFSCost(int site, int di)
	{
		return insertFSCost[site][di-1];
	}
	
	public float getInsertGapCost(int site)
	{
		return insertGapCost[site];
	}
	
	public float getInternalCost(int site)
	{
		return internalCost[site];
	}
	
	public float getGapCost(int site)
	{
		return cost.get(CostCode.GAP_E)*(dimerFreq[site][DimerCodes.d0]-dimerFreq[site][DimerCodes.d0E])
		+  cost.get(CostCode.GAP_E)*cost.get(CostCode.BEG_END_GAP_FACTOR)*dimerFreq[site][DimerCodes.d0E];
	}
	
	public Profile( BasicProfile basic)
	{		
		super(basic.getName(),basic.getSequences());
		setTemplate(basic.getTemplate());
		this.cost = getSeq(0).getCost();
		
		init();
	}
	
	Profile( String profName, CodingDnaSeq seq)
	{		
		super(profName,seq);
		this.cost = getSeq(0).getCost();
		init();
	}

	public Profile( String profName, ArrayList<CodingDnaSeq> profSeq)
	{
		super(profName,profSeq);
		this.cost = getSeq(0).getCost();
		init();	
	}
	
	public  int nbSites()
	{
		return aaFreq.size(); 
	}
	
	
	public final float[] getDimerFreq(int site)
	{
		if(site < 0)
			return dimerFreq[0];
		else
			return dimerFreq[site];
	}
	
	public char getChar(int seq, int site)
	{
		return getSeq(seq).getDNA(site-1); // shift
	}

	public void affiche()
	{
		affiche(false);
	}
	
	/*public char getChar(int seq, int site)
	{
		return getSeq(seq).getDNA(site-1); // shift
	}*/
	
	public void affiche(boolean detailed)
	{
		System.out.println("DNA");
		for (int i = 0; i < nbSeq(); i++) {
			CodingDnaSeq seq = getSeq(i);
			System.out.print(">"+seq.getName()+ "\n" + seq.getSeq());
			System.out.println();
		}
		
	/*	System.out.println("\n\nAA");
		
		for (CodingDnaSeq seq : sequences) {
			if(detailed==true)
				System.out.println(seq.toString());
			else
				System.out.print(">"+seq.getName()+ "\n" + seq.getAAtranslation(1));
			System.out.println();
		}*/
		
		
	}
	
	
	public String toString() {
		return getName();
	}

	
}
