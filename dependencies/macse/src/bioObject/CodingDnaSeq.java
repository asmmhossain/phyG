package bioObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;

import align.ElementaryCost;

import codesInterfaces.CostCode;
import codesInterfaces.NT_AAsymbols;


//import exception.RiboException;
//import exception.SeqException;

public class CodingDnaSeq extends Sequence implements NT_AAsymbols{

	private boolean fsAllowed;
	private Ribosome ribosome;
	private int [] aaTranslation;
	public boolean debug=false;
	private float internalCost;
	
	private ElementaryCost cost;
	private int firstNonGap;
	private int lastNonGap;
	public int getFirstNonGap()
	{
		return firstNonGap;	
	}
	public int getLastNonGap()
	{
		return lastNonGap;
	}

	public float getInternalCost()
	{
		return internalCost;
	}
	
	private static int computeFirstNonGap(String seq1)
	{
		int firstNonGap=0;
		boolean find = false;
		for (int i = 0; find == false && i < seq1.length(); i++) {
			if(seq1.charAt(i)==NT_GAP /*&& seq.charAt(i)!=NT_FS*/)
				firstNonGap++;
			else
				find = true;
		}
		return firstNonGap;
	}
	
	private static int computeLastNonGap(String seq1)
	{
		boolean find = false;
		int lastNonGap = seq1.length()-1;
		for (int i = seq1.length()-1; find == false && i >=0 ; i--) {
			if(seq1.charAt(i)==NT_GAP /*&& seq.charAt(i)!='!'*/)
				lastNonGap--;
			else
				find = true;
		}
		return lastNonGap;
	}
	
	public void swapLastFS()
	{
		if(this.getLastNonGap()<this.length())
		{
			int lastCodonStart = this.getLastNonGap() - this.getLastNonGap()%3;
			String codon =this.seq.substring(lastCodonStart, lastCodonStart+3);
			codon=codon.replaceAll("!", "");
			
			if(codon.length()<3)
				{
				while (codon.length()<3)			
					codon +="!";
				System.out.println(codon);
				this.seq=(this.seq.substring(0,lastCodonStart)+codon+ this.seq.substring(lastCodonStart+3,this.length()));
				this.init();
				}
		}
	}
	
	public void replaceNonStandartCodon(String intFSCodon, String finalStopCodon, String intStopCodon, String extFSCodon, String charForRemainingFS)
	{
		//System.out.println("internalCodonFS\t"+intFSCodon);
		//System.out.println("externalCodonFS\t"+extFSCodon);
		
		StringBuffer cleanSeq = new StringBuffer("");
		boolean onlyGapBefore = true;
		for (int nt_id=2;nt_id<this.length(); nt_id+=3)
		{
			String current_codon = this.seq.substring(nt_id-2, nt_id+1);
			String codon2add = current_codon;
			if(getAA(nt_id)==AA_STOPE_int && finalStopCodon != null)
				codon2add = finalStopCodon;
			if(getAA(nt_id)==AA_STOP_int && intStopCodon != null)
				codon2add = intStopCodon;
			
			if(getAA(nt_id)==AA_FS_int && intFSCodon!=null)
				codon2add = intFSCodon;		
			if(getAA(nt_id)==AA_FSE_int && extFSCodon!=null)//use the whole alignment to better place the FS
				codon2add = extFSCodon;
			if(getAA(nt_id)==AA_FSE_int && extFSCodon==null)
			{
				codon2add =current_codon.replace("!", "");
				if(onlyGapBefore) // if first FS => put gap at the begining
					while (codon2add.length()<3)			
						codon2add ="!"+codon2add;		
				else //if last FS => put gap at the end
					while (codon2add.length()<3)			
						codon2add +="!";		
			}
			if(!(getAA(nt_id)==AA_GAP_int || getAA(nt_id)==AA_GAPE_int))
				onlyGapBefore = false;
		cleanSeq.append(codon2add);
		}
		this.seq=cleanSeq.toString();
		if(charForRemainingFS!=null)
			this.seq = this.seq.replaceAll("!", charForRemainingFS);
		this.init();
		
	}
	
	
	
	public int[] nb_internal_FS_STOP_Gap(int readingFrame)
	{
		int [] res =new int[4];
		for( int i=readingFrame+1;i<this.aaTranslation.length;i+=3)
		{
			if(this.aaTranslation[i]==AA_FS_int)
				res[0]++;
			if(this.aaTranslation[i]==AA_STOP_int)
				res[1]++;
			if(this.aaTranslation[i]==AA_GAP_int)
				res[2]++;
			if(this.aaTranslation[i]==AA_GAPE_int)
				res[3]++;
			
		}
		return res;
	}
	private void init() 
	{
		aaTranslation = new int[seq.length()];
		
		this.firstNonGap = CodingDnaSeq.computeFirstNonGap(this.seq);
		this.lastNonGap = CodingDnaSeq.computeLastNonGap(this.seq);

		String codon;
		for (int i = 0; i < seq.length(); i++) {		
			codon = seq.substring(Math.max(i-2, 0), i+1);			
			try {
				aaTranslation[i] =  ribosome.translateInt(codon);
				
				if(NT_AAsymbols.aaSymbolTab[aaTranslation[i]]==AA_GAP)
				{
					if(i<firstNonGap || i>lastNonGap)
						aaTranslation[i]= AA_GAPE_int;
					else
						aaTranslation[i]= AA_GAP_int;
				}
				
				if(NT_AAsymbols.aaSymbolTab[aaTranslation[i]]==AA_STOP)
				{
					if(i==lastNonGap)
						aaTranslation[i]= AA_STOPE_int;
					else
						aaTranslation[i]= AA_STOP_int;
				}
				
				if(NT_AAsymbols.aaSymbolTab[aaTranslation[i]]==AA_FS)
				{
		
					// verifier -3 ou -di ?? dans profile
					if(i>=lastNonGap || (i-3)<firstNonGap )
						aaTranslation[i]= AA_FSE_int;
					else
						aaTranslation[i]= AA_FS_int;
				}
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.err.println("pb with translation of "+getName()+" site:"+i);
				System.exit(1);
			}
		}
		
		this.internalCost =0;
		for (int i=2;i<aaTranslation.length;i+=3)
		{
			
			int curAA= aaTranslation[i];
			if(curAA==NT_AAsymbols.AA_STOP_int)
				internalCost+= cost.get(CostCode.STOP);
			if(curAA==NT_AAsymbols.AA_FS_int)
				internalCost+= cost.get(CostCode.FS);
			if(curAA==NT_AAsymbols.AA_FSE_int)
				internalCost+= cost.get(CostCode.GAP_E) * cost.get(CostCode.BEG_END_GAP_FACTOR);	
				//internalCost+= cost.get(CostCode.FS) * CostCode.FSE_FACTOR;			
		}
		//System.out.println("internalcost:"+internalCost+" "+getInternalCost());
	}

	public ElementaryCost getCost()
	{
		return cost;
	}
	
//  remove useless FS of an aligned sequence	
	public static String correctFS(String alignedSeq)
	{
		StringBuffer res = new StringBuffer("");
		char codon[]=new char[3];
		String fsComplement[]= new String[4];
		fsComplement[1]="!!";
		fsComplement[2]="!";
		fsComplement[3]="";
		for(int i=0; 3*i +2 <alignedSeq.length();i++)
		{
			
			int nbFS=0;
			int nbGap=0;
			int nbReal=0;
			
			for(int j=0;j<3;j++)
			{
				switch(alignedSeq.charAt(3*i+j))
				{
				case NT_FS:
					nbFS++;
					break;
				case NT_GAP:
					nbGap++;
					break;
				default:
					codon[nbReal++] = 	alignedSeq.charAt(3*i+j);
					break;
				}	
			}
			
			if(nbReal==0)
			{
				res.append("---");
			}
			else 
			{
				res.append(fsComplement[nbReal]);
				for(int j=0;j<nbReal;j++)
					res.append(codon[j]);	
			}
		}
		// lors d'un split il peut rester un ou 2 char a la fin
		if(res.length() != alignedSeq.length())
		{
			for(int i= res.length(); i<alignedSeq.length();i++)
			{
				if(alignedSeq.charAt(i)==NT_GAP)
					res.append(NT_FS);
				else
					res.append(alignedSeq.charAt(i));
			}
			/*System.out.println(res.length()+" "+ alignedSeq.length());
			System.out.println("res:"+res.toString());
			System.out.println("seq:"+alignedSeq);*/
		}
		return res.toString();
	}
	/*
	//  remove useless FS of an aligned sequence	
	public static String removeUselessFS(String alignedSeq)
	{
		StringBuffer res = new StringBuffer("");
		for(int i=0; 3*i +2 <alignedSeq.length();i++)
		{
			
			int nbFS=0;
			int nbGap=0;
			String codon="";
			
			for(int j=0;j<3;j++)
			{
				switch(alignedSeq.charAt(3*i+j))
				{
				case NT_FS:
					nbFS++;
					break;
				case NT_GAP:
					nbGap++;
					break;
				}
				codon += 	alignedSeq.charAt(3*i+j);
			}
			
			if(nbFS+nbGap==3)
			{
				res.append("---");
			}
			else 
				res.append(codon);		
		}
		if(res.length() != alignedSeq.length())
		{
			System.out.println(res.length()+" "+ alignedSeq.length());
			System.out.println("res:"+res.toString());
			System.out.println("seq:"+alignedSeq);
		}
		return res.toString();
	}*/
	
	public float getNormCost(CodingDnaSeq s2, boolean normed)
	{
		int [][] aa = new int [2][];
		aa[0] = this.aaTranslation;
		aa[1] = s2.aaTranslation;
		float res=0;
		float nbSites =0;
		ElementaryCost cost2 =s2.getCost();
		
		float cost_gapO_beg_end = cost.get(CostCode.GAP_O)*cost.get(CostCode.BEG_END_GAP_FACTOR);
		cost_gapO_beg_end = ((float) ((int) (cost_gapO_beg_end*100))) / 100;
		
		float cost_gap_beg_end = cost.get(CostCode.GAP_E)*cost.get(CostCode.BEG_END_GAP_FACTOR);
		cost_gap_beg_end = ((float) ((int) (cost_gap_beg_end*100))) / 100;
		
		boolean inGap[]= new boolean[2];
		inGap[0]= inGap[1]=false;

		for(int i=2; i<aa[0].length; i+=3)
		{
			if(! (NT_AAsymbols.aaSymbolTab[aa[0][i]] == AA_GAP  && NT_AAsymbols.aaSymbolTab[aa[1][i]]==AA_GAP)) // skip - -
			{
				//System.out.print("+");
				nbSites ++;
				if(aa[0][i]<NT_AAsymbols.NB_AA_cost_indep && aa[1][i]<NT_AAsymbols.NB_AA_cost_indep )
				{
					res += cost.getSubstScore(aa[0][i], aa[1][i],cost2);
				}
				
				for(int seq =0; seq<2;seq++)
				{
					if(NT_AAsymbols.aaSymbolTab[aa[seq][i]] == AA_GAP)
					{					
						if(inGap[seq]==false) // gap start
						{
							//System.out.println("gap Start "+ seq+" "+i);
							if(aa[seq][i]==AA_GAP_int)
								res += cost.get(CostCode.GAP_O);
							else if (aa[seq][i]==AA_GAPE_int)
								res += cost_gapO_beg_end;
								//res += cost.get(CostCode.GAP_O)*cost.get(CostCode.BEG_END_GAP_FACTOR); // bug il manquait le cost.get
							inGap[seq] = true;
						}
						
						// in all cases cost of gap extension
						if(aa[seq][i]==AA_GAP_int)
							res += cost.get(CostCode.GAP_E);
						else if (aa[seq][i]==AA_GAPE_int)
							res += cost_gap_beg_end;
							//res += cost.get(CostCode.GAP_E)*cost.get(CostCode.BEG_END_GAP_FACTOR);
						
					}
					else inGap[seq]= false;
				}
				if(debug)
					System.out.println(i+ " "+ NT_AAsymbols.aaSymbolTab[aa[0][i]]+ " "+ NT_AAsymbols.aaSymbolTab[aa[1][i]]+"\t"+res);
				
			}
		}
	//	res = ((float) ((int) (res*100))) / 100f; // avoid rouding trouble related to .95 factor for begin/end gap 
		 
		if(normed)
			res = res / (float)nbSites;
		
		return res;
	}

	
	public float getNormCostNew(CodingDnaSeq s2, boolean normed)
	{
		int [][] aa = new int [2][];
		aa[0] = this.aaTranslation;
		aa[1] = s2.aaTranslation;
		float res=0;
		float nbSites =0;
		ElementaryCost cost2 =s2.getCost();
		
		boolean inGap[]= new boolean[2];
		inGap[0]= inGap[1]=false;
		int nbGap[] = new int[2];
		boolean beg_end [] = new boolean[2];
		beg_end[0]=beg_end[1]=false;
		for(int i=2; i<aa[0].length; i+=3)
		{
			if(! (NT_AAsymbols.aaSymbolTab[aa[0][i]] == AA_GAP  && NT_AAsymbols.aaSymbolTab[aa[1][i]]==AA_GAP)) // skip - -
			{
				//System.out.print("+");
				nbSites ++;
				if(aa[0][i]<NT_AAsymbols.NB_AA_cost_indep && aa[1][i]<NT_AAsymbols.NB_AA_cost_indep )
				{
					res += cost.getSubstScore(aa[0][i], aa[1][i],cost2);
				}
				
				for(int seq =0; seq<2;seq++)
				{
					if(NT_AAsymbols.aaSymbolTab[aa[seq][i]] == AA_GAP)
					{		

						nbGap[seq] ++;
						inGap[seq] = true;
						if(aa[seq][i]==AA_GAPE_int)
							beg_end[seq]=true;
						else
							beg_end[seq]=false;
					}
					else 
					{
						if(inGap[seq] == true) // ending a gap
						{
						inGap[seq]= false;
						nbGap[seq]=0;
						float gap_cost= nbGap[seq] *  cost.get(CostCode.GAP_E) + cost.get(CostCode.GAP_O);
						if(beg_end[seq])
							gap_cost *=  cost.get(CostCode.BEG_END_GAP_FACTOR);
						
						res += gap_cost;
						}
					}
				}
				
				if(debug)
					System.out.println(i+ " "+ NT_AAsymbols.aaSymbolTab[aa[0][i]]+ " "+ NT_AAsymbols.aaSymbolTab[aa[1][i]]+"\t"+res);
				
			}
		}
		//res = ((float) ((int) (res*100))) / 100f; // avoid rouding trouble related to .95 factor for begin/end gap 
		// gap at the end of the sequences
		for(int seq =0; seq<2;seq++)
			if(inGap[seq] == true) // ending a gap
			{
			inGap[seq]= false;
			float gap_cost= nbGap[seq] *  cost.get(CostCode.GAP_E) + cost.get(CostCode.GAP_E);
			if(beg_end[seq])
				gap_cost *=  cost.get(CostCode.BEG_END_GAP_FACTOR);
			res += gap_cost;
			}
		
		if(normed)
			res = res / (float)nbSites;
		
		return res;
	}



	public CodingDnaSeq(String name, String seq, boolean fsAllowed, ElementaryCost cost)  {
		super(name,seq);
		this.fsAllowed = fsAllowed;
		this.ribosome = Ribosome.getRibosome();
		this.cost = cost;
		init();
	}

	public CodingDnaSeq(String name, String seq, boolean fsAllowed,Ribosome ribosome, ElementaryCost cost)  {
		super(name,seq);
		this.fsAllowed = fsAllowed;
		this.ribosome = ribosome;
		this.cost = cost;
		init();
	}

	public CodingDnaSeq(String seq, CodingDnaSeq modelSeq)  {
		super(modelSeq.name,seq);
		this.setNames(modelSeq.name, modelSeq.realFullName);
		this.fsAllowed = modelSeq.fsAllowed;
		this.ribosome = modelSeq.ribosome;
		this.cost = modelSeq.cost;
		
		init();
	}
	
	public static CodingDnaSeq putInPhase (CodingDnaSeq modelSeq, int readingFrame)  {
		String prefix="";
		if (readingFrame ==3) // 2 first nuc are ignored so that only one is missing
			prefix=prefix+NT_AAsymbols.NT_FS;
		if (readingFrame ==2)
			prefix=prefix+NT_AAsymbols.NT_FS+NT_AAsymbols.NT_FS;
		String newSeq=prefix+modelSeq.getSeq();
		while (newSeq.length()%3 !=0)
			newSeq = newSeq+NT_AAsymbols.NT_FS;
		CodingDnaSeq res = new CodingDnaSeq(modelSeq.realFullName,newSeq,modelSeq.isFsAllowed(),modelSeq.ribosome, modelSeq.cost);
		return res;
	}
	
	public String getAAtranslation(int readingFrame)
	{
		StringBuffer res = new StringBuffer("");
		int i;
		for( i=readingFrame+1;i<this.aaTranslation.length;i+=3)//frame 1 premier codon se fini a l'indice 2
			res.append(aaSymbolTab[this.aaTranslation[i]]);
		/*if( (i-3) != (this.aaTranslation.length -1) )
			res.append(AA_FS);*/
		return res.toString();
	}
	public String toString()
	{
		return this.getName()+"\t"+ribosome.getName()+"\n";
	}
	public boolean isFsAllowed() {
		return fsAllowed;
	}

	public final char getDNA(int i)
	{
		return get(i);
	}

	public final int getAA(int i)
	{
		return aaTranslation[i];
	}

	public static ArrayList<CodingDnaSeq> readFasta(String filePath, Hashtable<String, Ribosome>seq2Ribo,boolean removeGap, ElementaryCost cost) 
	{
		return readFasta(filePath, seq2Ribo, removeGap, cost,null);
	}
	public static ArrayList<CodingDnaSeq> readFasta(String filePath, Hashtable<String, Ribosome>seq2Ribo,boolean removeGap, ElementaryCost defaultCost,Hashtable<String, ElementaryCost> seq2cost)  {

		StringBuffer currentSeq=new StringBuffer("");
		String currentName=null;
		String currentLine;

		ArrayList<CodingDnaSeq> sequences = new ArrayList<CodingDnaSeq>();

		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(filePath));

			try {
				while ((currentLine = br.readLine()) != null) {
					

					if(currentLine.startsWith(">"))
					{
						// add the previous seq still the current one
						if(currentName != null)
						{
							Ribosome ribo = Ribosome.selectRibosome(seq2Ribo, currentName);
							System.out.println("\t"+currentName+"\t"+ ribo.getName());
							if(seq2cost==null || (!seq2cost.containsKey(currentName)))
								sequences.add(new CodingDnaSeq(currentName,currentSeq.toString(),true,ribo,defaultCost));
							else
								sequences.add(new CodingDnaSeq(currentName,currentSeq.toString(),true,ribo,seq2cost.get(currentName)));
						}
						currentName=currentLine.substring(1);// get new Name	
						currentSeq.delete(0, currentSeq.length());// clear current seq
					}
					else
					{
						currentLine = currentLine.replaceAll(" ", "");
						if(currentLine.equals(""))
							continue;
						
						if(removeGap)
						{
							currentLine=currentLine.replaceAll("-", "");
							currentLine=currentLine.replaceAll("!", "");
						}
						currentSeq.append(currentLine);
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.err.println("pb to open read: "+ filePath);
				System.exit(1);
			} 
			if(currentName != null)
			{
				Ribosome ribo = seq2Ribo.get(currentName);
				if(ribo==null)
					ribo=Ribosome.getRibosome();
				sequences.add(new CodingDnaSeq(currentName,currentSeq.toString(),true,ribo,defaultCost));
				System.out.println("\t"+currentName+"\t"+ ribo.getName());
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.err.println("fasta file:"+filePath+ " not found");
			e.printStackTrace();
			System.exit(1);
		}
		return sequences;
	}
	
	public static String alignUsingPattern(String pattern, CodingDnaSeq unalignedSeq)
	{
		int pos=0;
		StringBuffer sb1 = new StringBuffer();
		for(int cPos=0; cPos<pattern.length();cPos++)
		{
			if(pattern.charAt(cPos)=='N')
				sb1.append(unalignedSeq.getDNA(pos++));
			else
				sb1.append('-');
		}
		return CodingDnaSeq.correctFS(sb1.toString());
	}
	
	public String toAAfasta(int readingFrame)
	{
		return ">"+getRealFullName()+ "\n" + getAAtranslation(readingFrame);
	}

	public static void main(String[] args)  {
		String s11="CTAGAAAGGTTTAATAAAAAACTAAGTCAATTA---------ATAGATATT---GATGGGTTGACGAAGATTGCTAACCGT---CGTTGCTTTAATGTCCGCATTAAACAAGAGTGGCGGAGATTATCTAGGGCTCAAGTGCCC------------ATATCCTTAATCATGTTT---------GATATAGATTGCTTCAAAAATTACAATGACCGCTATGGCCATCCGGAGGGGGAT------------CTGTGCTTAATTAGGGTGGCCCAAGCAGCT---AGA---------AAA---GCTGCCAGTCGG---CCA------------GCGGACTTAGTGGCC------CGTTTTGGGGGGGAAGAATTT---GCGGTGCTTTTGCCC------GAAACCGAC------ACCAAGGGG---GCAATTATGGTTGCCGAGAAAATC---------ATTCAAGCCATCAGCCAATTGGCG------ATC---GCCCAT---GAAGCTTCCCCCATTAGTGGG---CAA------------ATCACAATCAGT---------------TTGGGCATC------------GGCACCCAGTTTCCCTCC---AAG---GAA------------CTC------ATCTCC---AGGACCCTTATCAAA---------------CAA---------GCGGATATGGCCCTG---TATGAAGCAAAACGCCGGGGCAGAAATCAGTATGTGGTCTGG";
		String s12="TGGCGACAAAAACTAGGGGAAACCGCAGAGTTA---------CTTAACCTC---GATAGCCTCACCCAGGTTTCCAACCGC---CGCCATTTTGACCTCCATCTGGCCCAACAATGGGAACGGGCCATGGATAGCCAAGAGGCG------------ATCGCCCTGATTTTATGT---------GATATCGACCATTTCAAACAATTCAACGACTTCTACGGCCACCTCAGTGGGGAT------------GACTGTTTGCGGCGCATTGCCAAAACCCTC---AGT---------GCC---ACCCTC---CGCAACCCG------------TTTGATCTTTTTGCT------CGCTACGGTGGTGAAGAATTT---GGTGTAATTTTGCCC------CAGGTGACCAGTGAAGCTGCCCAG---CAGATTGCTAAACGGATGCAGGCC---------TCTCTGACAATG------CTAGAA------ATT---CCCCAT---CACCATTCCCCCACTAGCGAA---TTC------------GTCACCATGTCC---------------TTTGGCATC------------GGTCGCCTGTATCCCCAG---CCG---GGA------------CAA------CTCCCC---CTAGACCTAATTGCC---------------CAA---------GCGGACGAAAATCTT---TACAAGGCGAAACGCCAGGGCCGGAACTGCATTTTCGGCCAT";
		Ribosome r = Ribosome.getRibosome("11");
		
		CodingDnaSeq cs1= new CodingDnaSeq("s11",s11,false,r,new ElementaryCost(-40,-4,-12,0,-120,(float) 0.95,1));
		CodingDnaSeq cs2= new CodingDnaSeq("s12",s12,false,r,new ElementaryCost(-40,-4,-12,0,-120,(float) 0.95,1));
		System.out.println(cs1.getAAtranslation(1));
		System.out.println(cs2.getAAtranslation(1));
		System.out.println(cs1.getNormCost(cs2, false));
		System.out.println(cs2.getNormCost(cs1, false));
		
		CodingDnaSeq cs = new CodingDnaSeq("test","TCTT!GGGG",true,new ElementaryCost(10,10,10,10,10,10, 10));
		//System.out.println(cs.toString()+ cs.getAAtranslation(1));
		
		//System.out.println("clean:"+CodingDnaSeq.removeUselessFS("TCT!!-GGGG"));

	}
}
