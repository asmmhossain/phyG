package codesInterfaces;

import java.util.Arrays;
import java.util.HashSet;

public interface MacsEparamCode {
	public static String progName="prog";
	public static String progName_alSeq="alignSequences";
	public static String progName_al2prof="alignTwoProfiles";
	public static String progName_trNT2AA="translateNT2AA";
	public static String progName_reportGapsAA2NT="reportGapsAA2NT";
	public static String progName_correctContigs="correctContigs";
	public static String progName_refineAlign="refineAlignment";
	public static String progName_splitAlign="splitAlignment";
	public static String progName_enrichAlignment="enrichAlignment";
	public static String progName_exportAlignment="exportAlignment";
	public static String progName_removeSeqCausingGappySites="removeSeqCausingGappySites";
	public static String progName_trimSequences="trimSequences";
	
	public static String [] validProgNamesTab= new String[] {progName_alSeq,progName_al2prof,progName_enrichAlignment,progName_refineAlign,progName_exportAlignment, progName_trNT2AA, progName_reportGapsAA2NT, progName_splitAlign,progName_removeSeqCausingGappySites,progName_trimSequences};
	public static HashSet<String> validProgNames = new HashSet<String>(Arrays.asList(validProgNamesTab));
	
	public static String input_seq_F="seq";
	public static String outPut_NT="out_NT";
	public static String outPut_AA="out_AA";
	
	public static String outPut_subset="out_subset";
	public static String outPut_others="out_others";
	
	public static String input_less_reliable_seq_F="seq_lr";
	public static String GC_F="gc_file";
	//public static String refSeq_F="r";
	public static String default_GC="gc_def";
	public static String initial_alignment="align";
	
	public static String substMatrix="mat";
	public static String gapOpenCost="gap_op";
	public static String gapExtensionCost="gap_ext";
	public static String stopCodonCost="stop";
	public static String fsCost="fs";
	public static String ExternVsInternGapCostRatio="ext_gap_ratio";
	
	public static String stopCodonCostLessReliable="stop_lr";
	public static String fsCostLessReliable="fs_lr";
	
	public static String logFile="log";
	
	public static String DNA_only_output_file="NT_only";
	
	public static String profile1_F="p1";
	public static String profile2_F="p2";
	
	public static String optim="optim";
	
	public static String cladeToIsolate_F="subset";
	public static String firstSite="firstSite";
	public static String lastSite="lastSite";
	
	public static String codonForInternalFS="codonForInternalFS";
	public static String codonForExternalFS="codonForExternalFS";
	public static String codonForInternalStop="codonForInternalStop";
	public static String codonForFinalStop="codonForFinalStop";
	public static String charForRemainingFS="charForRemainingFS";
	public static String statFile="statFile";
	
	public static String maxFSinAddedSeq="maxFS_inSeq";
	public static String maxSTOPinAddedSeq="maxSTOP_inSeq";
	public static String maxDELinAddedSeq="maxDEL_inSeq";
	public static String maxINSinAddedSeq="maxINS_inSeq";
	public static String maxTotalINSinAddedSeq="maxTotalINS_inSeq";
	public static String seqToAdd_logFile="seqToAdd_logFile";
	
	public static String ignoreGaps="ignoreGaps";
	public static String keepLastStop="keepLastStop";
	public static String guessOneReadingFrame="guessOneReadingFrame";
	
	public static String trimedSeqFileNT="out_NT_trimed";
	public static String annotatedSeqFileNT="out_NT_annotated";
	public static String fixedAlignment="fixedRefAlignment";
	public static String trimInfoFile="out_trim_stat";
	
	public static String statPerSiteNTFile = "out_NT_perSiteStat";
//	public static String statPerSiteAAFile = "out_AA_perSiteStat";
	
	public static String thresholdForGappyness="thresholdForGappyness";
	
	
	
	
	
}
