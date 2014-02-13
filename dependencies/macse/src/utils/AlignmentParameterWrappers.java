package utils;


import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;

import codesInterfaces.MacsEparamCode;

import bioObject.CodingDnaSeq;
import bioObject.Ribosome;
import align.ElementaryCost;
import align.SubstitutionScore;

/**
 * 
 * @author vranwez
 * This class is used to extract and gather alignment related parameters from the command line
 *
 */
public class AlignmentParameterWrappers {
	private ElementaryCost cost;
	private ElementaryCost costLR;
	private ArrayList<CodingDnaSeq> sequences;
	private ArrayList<CodingDnaSeq> lessReliableSequences;
	private boolean saveOnlyDNAFile;
	Hashtable<String, Ribosome> seq2Ribo;
	Hashtable<String, ElementaryCost> seq2cost;
	
	public Hashtable<String, ElementaryCost> getSeq2cost() {
		return seq2cost;
	}

	public void setSeq2cost(Hashtable<String, ElementaryCost> seq2cost) {
		this.seq2cost = seq2cost;
	}

	public Hashtable<String, Ribosome> getSeq2Ribo() {
		return seq2Ribo;
	}

	public void setSeq2Ribo(Hashtable<String, Ribosome> seq2Ribo) {
		this.seq2Ribo = seq2Ribo;
	}

	public ArrayList<CodingDnaSeq> getAllSequences()
	{
		ArrayList<CodingDnaSeq> allSeq = new ArrayList<CodingDnaSeq>();
		allSeq.addAll(sequences);
		allSeq.addAll(lessReliableSequences);
		return allSeq;
	}
	
	public boolean hasLessReliableSequences()
	{
		return ! lessReliableSequences.isEmpty();
	}
	
	public ElementaryCost getCost() {
		return cost;
	}


	public void setCost(ElementaryCost cost) {
		this.cost = cost;
	}


	public ElementaryCost getCostLR() {
		return costLR;
	}


	public void setCostLR(ElementaryCost costLR) {
		this.costLR = costLR;
	}


	public ArrayList<CodingDnaSeq> getSequences() {
		return sequences;
	}


	public void setSequences(ArrayList<CodingDnaSeq> sequences) {
		this.sequences = sequences;
	}


	public ArrayList<CodingDnaSeq> getLessReliableSequences() {
		return lessReliableSequences;
	}


	public void setLessReliableSequences(
			ArrayList<CodingDnaSeq> lessReliableSequences) {
		this.lessReliableSequences = lessReliableSequences;
	}


	public boolean isSaveOnlyDNAFile() {
		return saveOnlyDNAFile;
	}


	public void setSaveOnlyDNAFile(boolean saveOnlyDNAFile) {
		this.saveOnlyDNAFile = saveOnlyDNAFile;
	}


	public AlignmentParameterWrappers(ElementaryCost cost, ElementaryCost costLR, ArrayList<CodingDnaSeq> sequences,ArrayList<CodingDnaSeq> lessReliableSequences, String geneticCode )
	{
		Ribosome.defaultCode= geneticCode;
		SubstitutionScore.defaultMatrix = "BLOSUM62.txt";
		
		this.cost = cost;
		this.costLR = costLR;
		this.sequences = sequences;
		this.lessReliableSequences = lessReliableSequences;
		//change seqNames for more compact clade representations
		seq2cost = new Hashtable<String, ElementaryCost>();
		int nb=1;
		for (CodingDnaSeq s : lessReliableSequences) {
			s.setNames("seq_lr_"+nb, s.getRealFullName());
			seq2cost.put(s.getRealFullName(), costLR);
			nb++;
		}
		nb=1;
		for (CodingDnaSeq s : sequences) {
			s.setNames("seq_"+nb, s.getRealFullName());
			seq2cost.put(s.getRealFullName(), cost);
			nb++;
		}
	}

	
	/**
	 * 
	 * @param macseOption command line option passed to the program
	 * @throws Exception 
	 * 
	 */
	public AlignmentParameterWrappers(MacsE_param macseOption) {
		
		// cost should be negative values  
		float fs = -Math.abs(macseOption.getFsCost());
		float gapE = -Math.abs(macseOption.getGapOpExt());
		float gapO=-Math.abs(macseOption.getGapOpCost());
		float gapC=-Math.abs(macseOption.getGapCloseCost());
		float stop = -Math.abs(macseOption.getStopCost());
		float begEndGapFact = Math.abs(macseOption.getBegEndGapFactor());
		float optPessFact = Math.abs(macseOption.getOptimisticPesssimisticGap_factor());
		float fsLR = -Math.abs(macseOption.getLessReliableFsCost());
		float stopLR = -Math.abs(macseOption.getLessReliableStopCost());
		
		Ribosome.defaultCode= macseOption.get_default_GC_code();
		SubstitutionScore.defaultMatrix = macseOption.getSubstMatrix();
		
		// Open and close gaps are no longer distinguished
		gapO += gapC;
		gapC =0;
		
		
	
		
		cost = new ElementaryCost(fs,gapE,gapO,gapC,stop,begEndGapFact,optPessFact);
		costLR = new ElementaryCost(fsLR,gapE,gapO,gapC,stopLR,begEndGapFact,optPessFact);
		
		
		Ribosome.defaultCode= macseOption.get_default_GC_code();
		
		// this can no longer be modified via command line option, this would require to rescale other cost
		SubstitutionScore.defaultMatrix = macseOption.getSubstMatrix(); 
		
		seq2Ribo = new Hashtable<String, Ribosome>();
		if(macseOption.get_GC_file() !=null) 
			seq2Ribo= Ribosome.parseGCfile(macseOption.get_GC_file());

		sequences = new ArrayList<CodingDnaSeq>();
		if( macseOption.getInputReliableFile() !=null )
		{
			sequences= CodingDnaSeq.readFasta(macseOption.getInputReliableFile(),seq2Ribo,true,cost);
		}
		
		lessReliableSequences = new ArrayList<CodingDnaSeq>();		
		if( macseOption.getInputLessReliableFile() !=null )
		{
			lessReliableSequences= CodingDnaSeq.readFasta(macseOption.getInputLessReliableFile(),seq2Ribo,true,costLR);
		}
		
		
		//change seqNames for more compact clade representations
		seq2cost = new Hashtable<String, ElementaryCost>();
		int nb=1;
		for (CodingDnaSeq s : lessReliableSequences) {
			s.setNames("seq_lr_"+nb, s.getRealFullName());
			seq2cost.put(s.getRealFullName(), costLR);
			nb++;
		}
		nb=1;
		for (CodingDnaSeq s : sequences) {
			s.setNames("seq_"+nb, s.getRealFullName());
			seq2cost.put(s.getRealFullName(), cost);
			nb++;
		}

	}

}

//-prog alignSequences -i /Users/vranwez/MesDocuments/data_save_CD/biologicalDataSets/alignmentBenchmarks/MACSE_benchmark/Jaculus_454_Plos/Jaculus_454_Plos_coding.fas -i_lr /Users/vranwez/MesDocuments/data_save_CD/biologicalDataSets/alignmentBenchmarks/MACSE_benchmark/Jaculus_454_Plos/Jaculus_454_Plos_reads.fas -o /Users/vranwez/MesDocuments/data_save_CD/biologicalDataSets/alignmentBenchmarks/MACSE_benchmark/macse_v0.9_4_results/test_tmp_Jaculus -s_lr -60 -f_lr -10
