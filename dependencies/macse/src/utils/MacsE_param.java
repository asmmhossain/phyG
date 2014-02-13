package utils;

import java.io.PrintWriter;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;



import codesInterfaces.MacsEparamCode;


public class MacsE_param {
	
	
	
	
	protected Options macse_options ;
	protected CommandLine cmd = null;
	
	//private String default_genetic_code="The_Standard_Code";
	private String default_genetic_code="1";
	
	
	//* v2 http://www.ebi.ac.uk/help/matrix.html#BLOSSUM
	
	private float default_stop =-100;
	private float default_fs =-30;
	private String default_matSubst="BLOSUM62.txt";
	private float default_gapExt =-1;
	private float default_gapCreation =-7;
	private float default_beg_end_gap_factor = .95f; // .95 lead to some rouding pb
	private float opt_pesssimistic_factor = 1.0f;
	private String progName;
	private HelpDoc helpMsg;
	
	
	//private float opt_pesssimistic_factor = 0.6f;
	// patent http://www.freepatentsonline.com/y2004/0235104.html
	// parameter used in v07 ?
	/*
	private float default_stop =-120;
	private float default_fs =-40;
	private String default_matSubst="BLOSUM62.txt";
	private float default_gapCreation =-12;
	private float default_gapExt =-4;
	private float beg_end_gap_factor = .95f;
	private float opt_pesssimistic_factor = 1.0f;
	*/
	
	
	
	public float getOptimisticPesssimisticGap_factor()
	{
		return opt_pesssimistic_factor;
	}
	
	
	
	public MacsE_param(String [] myArgs) {
		
		macse_options = new Options();
		Option progFileOpt = new Option(MacsEparamCode.progName,true,"the program to be used among those proposed by MACSE toolkit");
		progFileOpt.setArgs(1);
		progFileOpt.setArgName(MacsEparamCode.progName_alSeq);
		progFileOpt.setRequired(true);
		macse_options.addOption(progFileOpt);
		
		 helpMsg = new HelpDoc();
		
		// first option have to be -prog followed by a valid progtype
		
		if(myArgs.length< 2 || (!myArgs[0].equals("-"+MacsEparamCode.progName)) || (!MacsEparamCode.validProgNames.contains(myArgs[1])))
		{
				
			System.out.println(helpMsg.getHeader("macse"));
			System.out.println("Please check your command line: ");
			HelpFormatter formatter = new HelpFormatter();		  
			formatter.printHelp(new PrintWriter(System.out, true), 150, 
							  "java -jar -Xmx600m testJar_macse.jar", "\n", macse_options, 5, 10, "", true);
			
			System.out.println("\nvalid program names are:");
			for (String validProg : MacsEparamCode.validProgNamesTab) {
				System.out.println("\t"+validProg);
			}
			System.out.println(helpMsg.getFooter("macse"));

			System.exit(1);
		}
			
		
		// depending on the progtype relevant options are loaded
		this.setProgName(myArgs[1]);
		String prog = this.getProgName();
		//System.out.println("prog : " + prog);
		
		
		macse_options = new Options();
		
		if(prog.equals(MacsEparamCode.progName_alSeq) || prog.equals(MacsEparamCode.progName_al2prof) || prog.equals(MacsEparamCode.progName_refineAlign)|| prog.equals(MacsEparamCode.progName_enrichAlignment) || prog.equals(MacsEparamCode.progName_trimSequences))
		{
			addOptions_Alignment();
		}
		
		if(prog.equals(MacsEparamCode.progName_alSeq))
		{			
			addOptions_stdAlignment();
		}
		if( prog.equals(MacsEparamCode.progName_al2prof) )
		{
			addOptions_profileAlignment();
		}
		if( prog.equals(MacsEparamCode.progName_refineAlign) )
		{
			addOptions_refineAlignment();
		}
		if( prog.equals(MacsEparamCode.progName_trNT2AA) )
		{
			addOptions_trNt2AA();
		}
		if( prog.equals(MacsEparamCode.progName_splitAlign) )
		{
			addOptions_splitAlignment();
		}
		if( prog.equals(MacsEparamCode.progName_removeSeqCausingGappySites) )
		{
			addOptions_removeSeqCausingGappySites();
		}
		if( prog.equals(MacsEparamCode.progName_enrichAlignment) )
		{
			addOptions_enrichAlignment();
		}
		if( prog.equals(MacsEparamCode.progName_exportAlignment) )
		{
			addOptions_exportAlignment();
		}
		if( prog.equals(MacsEparamCode.progName_reportGapsAA2NT) )
		{
			addOptions_reportGapsAA2NT();
		}
		if( prog.equals(MacsEparamCode.progName_trimSequences) )
		{
			addOptions_trimSequences();
		}
		
		
		// finally the argument line is checked
		String [] myArgsWithoutProgName = new String[myArgs.length-2];
		for (int i = 2; i < myArgs.length; i++) {
			myArgsWithoutProgName[i-2]= myArgs[i];
		}
		parse(myArgsWithoutProgName, prog);
		
	}
	
	//option to export alignment
	public String get_codonForXXX(String codonForXXX)
	{
		if (cmd.hasOption(codonForXXX))
			return cmd.getOptionValue(codonForXXX);
		else
			return null;
	}
	//
	
	/*public Boolean get_sequencesAlignedOrNot()
	{
		System.out.println(cmd.hasOption(MacsEparamCode.unalignedSequences));
		System.out.println("############");
		if (cmd.hasOption(MacsEparamCode.unalignedSequences))
			return false;
		else
			return true;
	}*/
	public Boolean get_guessOneReadingFrame()
	{
		
		if (cmd.hasOption(MacsEparamCode.guessOneReadingFrame))
			return true;
		else
			return false;
	}
	
	public Boolean get_incremental()
	{
		if(cmd.hasOption(MacsEparamCode.fixedAlignment))
			return false;
		else
			return true;
	}
	public Boolean get_keepLastStop()
	{
		
		if (cmd.hasOption(MacsEparamCode.keepLastStop))
			return true;
		else
			return false;
	}
	public Boolean get_ignoreGaps()
	{
		
		if (cmd.hasOption(MacsEparamCode.ignoreGaps))
			return true;
		else
			return false;
	}
	
	public String get_default_GC_code()
	{
		if (cmd.hasOption(MacsEparamCode.default_GC))
			return cmd.getOptionValue(MacsEparamCode.default_GC);
		else
			return default_genetic_code;
	}
	public String get_initial_alignment()
	{
		if (cmd.hasOption(MacsEparamCode.initial_alignment))
			return cmd.getOptionValue(MacsEparamCode.initial_alignment);
		else
			return null;
	}
	
	public String get_splitSpeciesList_file()
	{
		if(cmd.hasOption(MacsEparamCode.cladeToIsolate_F))
			return cmd.getOptionValue(MacsEparamCode.cladeToIsolate_F);
		else
			return null;
	}
		
	
	public String get_GC_file()
	{
		if (cmd.hasOption(MacsEparamCode.GC_F))
			return cmd.getOptionValue(MacsEparamCode.GC_F);
		else
			return null;
	}
	
	public String get_log_file()
	{
		if (cmd.hasOption(MacsEparamCode.logFile))
			return cmd.getOptionValue(MacsEparamCode.logFile);
		else
			return null;
	}
	
	public String getInputReliableFile()
	{
		return cmd.getOptionValue(MacsEparamCode.input_seq_F);	
	}
	
	public String getInputLessReliableFile()
	{
		if (cmd.hasOption(MacsEparamCode.input_less_reliable_seq_F))
			return cmd.getOptionValue(MacsEparamCode.input_less_reliable_seq_F);	
		else return null;
	}
	
	public String getStatFile()
	{
		if (cmd.hasOption(MacsEparamCode.statFile))
			return cmd.getOptionValue(MacsEparamCode.statFile);	
		else return null;
	}
	
	
	private String getInputPrefix()
	{
		String inputF="";
		
		if(cmd.hasOption(MacsEparamCode.input_seq_F))
				inputF = cmd.getOptionValue(MacsEparamCode.input_seq_F);
		if(cmd.hasOption(MacsEparamCode.initial_alignment))
			inputF = cmd.getOptionValue(MacsEparamCode.initial_alignment);
		inputF = inputF.substring(0,inputF.lastIndexOf('.'));
		
		return inputF+"_macse";
	}
	
	public String getTrimedSeqFileNT()
	{
		if(cmd.hasOption(MacsEparamCode.trimedSeqFileNT))
			return cmd.getOptionValue(MacsEparamCode.trimedSeqFileNT);
		else
			return null;
	}
	
	public String getAnnotatedSeqFileNT()
	{
		if(cmd.hasOption(MacsEparamCode.annotatedSeqFileNT))
			return cmd.getOptionValue(MacsEparamCode.annotatedSeqFileNT);
		else
			return null;
	}
	
	public String getTrimInfoFile()
	{
		if (cmd.hasOption(MacsEparamCode.trimInfoFile))
			return cmd.getOptionValue(MacsEparamCode.trimInfoFile);
		else
			return null;
	}

	public String getAlignStatLogFile()
	{
		String res = "";
		if (cmd.hasOption(MacsEparamCode.seqToAdd_logFile))
		{
			
			res = cmd.getOptionValue(MacsEparamCode.seqToAdd_logFile);
			
		}
		else
		{
			res= getInputPrefix()+"_alignStat.csv";
		}
		return res;
		
	}
	
	public String getNTOutputFile()
	{
		String res = "";
		if (cmd.hasOption(MacsEparamCode.outPut_NT))
		{
			res = cmd.getOptionValue(MacsEparamCode.outPut_NT);	
		}
		else
		{
			res= getInputPrefix()+"_NT.fasta";
		}
		System.out.println(cmd.hasOption(MacsEparamCode.outPut_NT));
		System.out.println(res);
		return res;
		
	}
	
	public String getAAOutputFile()
	{
		String res = "";
		if (cmd.hasOption(MacsEparamCode.outPut_AA))
		{
			res = cmd.getOptionValue(MacsEparamCode.outPut_AA);
		}
		else
		{
			res= getInputPrefix()+"_AA.fasta";
		}
		return res;
		
	}
	
	public String getSubsetOutputFile()
	{
		String res = "";
		if (cmd.hasOption(MacsEparamCode.outPut_subset))
		{
			res = cmd.getOptionValue(MacsEparamCode.outPut_subset);
		}
		else
		{
			res= getInputPrefix()+"_subset.fasta";
		}
		return res;
	}
	
	public String getOthersOutputFile()
	{
		String res = "";
		if (cmd.hasOption(MacsEparamCode.outPut_others))
		{
			res = cmd.getOptionValue(MacsEparamCode.outPut_others);
		}
		else
		{
			res= getInputPrefix()+"_others.fasta";
		}
		return res;
	}
	
	public String getInitialAlignmentFile()
	{
		return cmd.getOptionValue(MacsEparamCode.initial_alignment);	
	}
	
	public String getProfile1File()
	{
		return cmd.getOptionValue(MacsEparamCode.profile1_F);	
	}
	
	public String getProfile2File()
	{
		return cmd.getOptionValue(MacsEparamCode.profile2_F);	
	}
	
	
	
	public String getSubstMatrix()
	{
		if (cmd.hasOption(MacsEparamCode.substMatrix)) 
			return cmd.getOptionValue(MacsEparamCode.substMatrix); // divide between open and close	
		else
			return default_matSubst;
	}
	
	public float getBegEndGapFactor()
	{
		if (cmd.hasOption(MacsEparamCode.ExternVsInternGapCostRatio)) 
			return Float.parseFloat(cmd.getOptionValue(MacsEparamCode.ExternVsInternGapCostRatio));
		else
			return default_beg_end_gap_factor;
		
	}
	
	public float getGapOpExt()
	{
		if (cmd.hasOption(MacsEparamCode.gapExtensionCost)) 
			return Float.parseFloat(cmd.getOptionValue(MacsEparamCode.gapExtensionCost)); // divide between open and close	
		else
			return default_gapExt;
	}
	public float getGapOpCost()
	{
		if (cmd.hasOption(MacsEparamCode.gapOpenCost)) 
			return Float.parseFloat(cmd.getOptionValue(MacsEparamCode.gapOpenCost))/(float)2.0; // divide between open and close	
		else
			return default_gapCreation/(float)2.0;
	}
	public float getGapCloseCost()
	{
		return getGapOpCost();
	}
	public float getFsCost()
	{
		if (cmd.hasOption(MacsEparamCode.fsCost)) 
			return Float.parseFloat(cmd.getOptionValue(MacsEparamCode.fsCost));	
		else
			return default_fs;
	}
	
	public float getThresholdForGappyness()
	{
		return Float.parseFloat(cmd.getOptionValue(MacsEparamCode.thresholdForGappyness)); 	

	}
	
	public int getFirstSite()
	{
		if (cmd.hasOption(MacsEparamCode.firstSite)) 
			return Integer.parseInt(cmd.getOptionValue(MacsEparamCode.firstSite));
		else
			return -1;
	}
	
	public int getLastSite()
	{
		if (cmd.hasOption(MacsEparamCode.lastSite)) 
			return Integer.parseInt(cmd.getOptionValue(MacsEparamCode.lastSite));
		else
			return -1;
	}
	
	public int getOptimCode()
	{
		//if (cmd.hasOption(MacsEparamCode.optim)) 
		System.out.println(cmd.getOptionValue(MacsEparamCode.optim));
		return Integer.parseInt(cmd.getOptionValue(MacsEparamCode.optim));	
	}
	
	public int getMaxSTOP()
	{
		//System.out.println("stop =>" + cmd.getOptionValue(MacsEparamCode.maxSTOPinAddedSeq));
		if (cmd.hasOption(MacsEparamCode.maxSTOPinAddedSeq)) 
			return Integer.parseInt(cmd.getOptionValue(MacsEparamCode.maxSTOPinAddedSeq));
		else
			return -1;
	}
	
	public int getMaxFS()
	{
		if (cmd.hasOption(MacsEparamCode.maxFSinAddedSeq)) 
			return Integer.parseInt(cmd.getOptionValue(MacsEparamCode.maxFSinAddedSeq));
		else
			return -1;
	}
	
	public boolean getUseFixeAlignment()
	{
		if(cmd.hasOption(MacsEparamCode.fixedAlignment))
			return true;
		else
			return false;
	}
	
	public int getMaxINS()
	{
		if(cmd.hasOption(MacsEparamCode.fixedAlignment))
			return 0;
		if (cmd.hasOption(MacsEparamCode.maxINSinAddedSeq)) 
			return Integer.parseInt(cmd.getOptionValue(MacsEparamCode.maxINSinAddedSeq));
		else
			return -1;
	}
	
	public int getMaxTotalINS()
	{
		if (cmd.hasOption(MacsEparamCode.maxTotalINSinAddedSeq)) 
			return Integer.parseInt(cmd.getOptionValue(MacsEparamCode.maxTotalINSinAddedSeq));
		else
			return -1;
	}

	public int getMaxDEL()
	{
		if (cmd.hasOption(MacsEparamCode.maxDELinAddedSeq)) 
			return Integer.parseInt(cmd.getOptionValue(MacsEparamCode.maxDELinAddedSeq));
		else
			return -1;
	}
	public void setProgName(String progName)
	{
		this.progName = progName;
	}
	
	public String getProgName()
	{
		return progName;
	}
	
	public float getStopCost()
	{
		if (cmd.hasOption(MacsEparamCode.stopCodonCost)) 
			return Float.parseFloat(cmd.getOptionValue(MacsEparamCode.stopCodonCost));	
		else
			return default_stop;
	}
	
	public float getLessReliableFsCost()
	{
		if (cmd.hasOption(MacsEparamCode.fsCostLessReliable)) 
			return Float.parseFloat(cmd.getOptionValue(MacsEparamCode.fsCostLessReliable));	
		else
			return getFsCost();
	}
	
	public float getLessReliableStopCost()
	{
		if (cmd.hasOption(MacsEparamCode.stopCodonCostLessReliable)) 
			return Float.parseFloat(cmd.getOptionValue(MacsEparamCode.stopCodonCostLessReliable));	
		else
			return getStopCost();
	}
	
	public String getStatPerSiteNTFile()
	{
		if (cmd.hasOption(MacsEparamCode.statPerSiteNTFile)) 
			return cmd.getOptionValue(MacsEparamCode.statPerSiteNTFile);	
		else
			return null;
	}
	
	private CommandLine parse(String [] myArgs, String prog)
	{
		CommandLineParser parser = new BasicParser();
		
	try {
		  cmd = parser.parse(macse_options, myArgs,true);
		} catch (ParseException e) {
		System.out.println(helpMsg.getHeader(prog));
		  System.out.println("Please check your command line: " 
		   // + e.getClass() + ": " 
		    + e.getMessage());
		  
		  HelpFormatter formatter = new HelpFormatter();
		  //formatter.printHelp( 100,"parameters:", "header", macse_options,"footer" );
		 
		  formatter.printHelp(new PrintWriter(System.out, true), 150, 
				  "java -jar -Xmx600m testJar_macse.jar -prog "+this.getProgName(), "\n", macse_options, 5, 10, "", true);
		  System.out.println(helpMsg.getFooter(prog));
		  //System.out.println(HelpDoc.footer);
		   System.exit(-1) ;
		}

		return cmd;
	}
	
	private void addOptions_trimSequences()
	{
		Option initialAlignment = new Option(MacsEparamCode.initial_alignment,true,"initial NT alignment in fasta format. Sequences in seq and seq_lr file missing from this alignment will be sequentially added to it (without any refinement step, use refineAlignment on output if needed)");
		initialAlignment.setArgs(1);
		initialAlignment.setArgName("NT_alignment.fasta");
		initialAlignment.setRequired(true);
		macse_options.addOption(initialAlignment);
		
		Option inputFileOpt = new Option(MacsEparamCode.input_seq_F,true,"reliable sequence input file in fasta format");
		inputFileOpt.setArgs(1);
		inputFileOpt.setArgName("input.fasta");
		inputFileOpt.setRequired(true);
		macse_options.addOption(inputFileOpt);
		
		
		Option trimedSeqFileNTOpt = new Option(MacsEparamCode.trimedSeqFileNT,true,"output file containing the trimmed sequences");
		trimedSeqFileNTOpt.setArgs(1);
		trimedSeqFileNTOpt.setArgName("out_trimedNT.fasta");
		trimedSeqFileNTOpt.setRequired(true);
		macse_options.addOption(trimedSeqFileNTOpt);
		
		Option annotatedSeqFileNTOpt = new Option(MacsEparamCode.annotatedSeqFileNT,true,"output file containing the full sequences with trimmed fragment in lower cases");
		annotatedSeqFileNTOpt.setArgs(1);
		annotatedSeqFileNTOpt.setArgName("out_annotated.fasta");
		annotatedSeqFileNTOpt.setRequired(true);
		macse_options.addOption(annotatedSeqFileNTOpt);
		
		Option trimInfoFileOpt = new Option(MacsEparamCode.trimInfoFile,true,"csv file containing for each sequence: the number of nucleotides i) removed at the begining ii) kept iii) remove at the end of the sequence iv) and their sum\n");
		trimInfoFileOpt.setArgs(1);
		trimInfoFileOpt.setArgName("out_trim_info.csv");
		trimInfoFileOpt.setRequired(true);
		macse_options.addOption(trimInfoFileOpt);
		
		Option outputFileOptNT = new Option(MacsEparamCode.outPut_NT,true,"Name of the output file that will contain the output NT alignment of trimed sequences with no stop, no deletion, no insertion and no frameshift (others can be added using enrichAlignment sub program).");
		outputFileOptNT.setArgs(1);
		outputFileOptNT.setArgName("output_NT.fasta");
		outputFileOptNT.setRequired(false);
		macse_options.addOption(outputFileOptNT);
		
	}
	
	private void addOptions_enrichAlignment()
	{
		Option initialAlignment = new Option(MacsEparamCode.initial_alignment,true,"initial NT alignment in fasta format. Sequences in seq and seq_lr file missing from this alignment will be sequentially added to it (without any refinement step, use refineAlignment on output if needed)");
		initialAlignment.setArgs(1);
		initialAlignment.setArgName("NT_alignment.fasta");
		initialAlignment.setRequired(true);
		macse_options.addOption(initialAlignment);
		
		Option inputFileOpt = new Option(MacsEparamCode.input_seq_F,true,"reliable sequence input file in fasta format");
		inputFileOpt.setArgs(1);
		inputFileOpt.setArgName("input.fasta");
		inputFileOpt.setRequired(true);
		macse_options.addOption(inputFileOpt);
		
		Option maxSTOPOpt = new Option(MacsEparamCode.maxSTOPinAddedSeq,true,"if a sequence has more STOP it will not be added to the alignment (default no filtering)");
		maxSTOPOpt.setArgs(1);
		maxSTOPOpt.setArgName("1");
		maxSTOPOpt.setRequired(false);
		macse_options.addOption(maxSTOPOpt);
		
		Option maxFSOpt = new Option(MacsEparamCode.maxFSinAddedSeq,true,"if a sequence has more FS it will not be added to the alignment (default no filtering)");
		maxFSOpt.setArgs(1);
		maxFSOpt.setArgName("1");
		maxFSOpt.setRequired(false);
		macse_options.addOption(maxFSOpt);
		
		Option maxINSOpt = new Option(MacsEparamCode.maxINSinAddedSeq,true,"if a sequence has more AA internal insertions it will not be added to the alignment (default no filtering)");
		maxINSOpt.setArgs(1);
		maxINSOpt.setArgName("1");
		maxINSOpt.setRequired(false);
		macse_options.addOption(maxINSOpt);
		
		Option maxTotalINSOpt = new Option(MacsEparamCode.maxTotalINSinAddedSeq,true,"if a sequence has more AA total insertions it will not be added to the alignment (default no filtering)");
		maxTotalINSOpt.setArgs(1);
		maxTotalINSOpt.setArgName("1");
		maxTotalINSOpt.setRequired(false);
		macse_options.addOption(maxTotalINSOpt);
		
		
		Option maxDELOpt = new Option(MacsEparamCode.maxDELinAddedSeq,true,"if a sequence has more AA deletions it will not be added to the alignment (default no filtering)");
		maxDELOpt.setArgs(1);
		maxDELOpt.setArgName("1");
		maxDELOpt.setRequired(false);
		macse_options.addOption(maxDELOpt);
		
		Option alignStatFileOpt = new Option(MacsEparamCode.seqToAdd_logFile,true,"csv file containing information about the number of STOP, FS and INDEL events for each tested sequence (default is {input_file_prefixe}_alignStat.csv))");
		alignStatFileOpt.setArgs(1);
		alignStatFileOpt.setArgName("align_stat.csv");
		alignStatFileOpt.setRequired(false);
		macse_options.addOption(alignStatFileOpt);
		
		
		Option fixedAlignment = new Option(MacsEparamCode.fixedAlignment,true,"If this option is set all sequences are compared to the original alignment and their alignment are merged. This autommatically set "+ MacsEparamCode.maxINSinAddedSeq+" to 0, since resulting alignment is meaningless otherwise (option mainly useful for barcoding).");
		fixedAlignment.setArgs(0);
		fixedAlignment.setRequired(false);
		macse_options.addOption(fixedAlignment);
		
		
		addOptionOutputOptional();
		
	}

	private void addOptions_removeSeqCausingGappySites()
	{
		Option initialAlignment = new Option(MacsEparamCode.initial_alignment,true,"initial NT alignment in fasta format");
		initialAlignment.setArgs(1);
		initialAlignment.setArgName("NT_alignment.fasta");
		initialAlignment.setRequired(true);
		macse_options.addOption(initialAlignment);
		
		Option thresholdForGappyness = new Option(MacsEparamCode.thresholdForGappyness,true,"percentage of gap (between 0 and 1) above which a site is consider to be gappy)");
		thresholdForGappyness.setArgs(1);
		thresholdForGappyness.setArgName("1");
		thresholdForGappyness.setRequired(true);
		macse_options.addOption(thresholdForGappyness);
		
		Option defaultCodeOpt = new Option(MacsEparamCode.default_GC,true,"indicate the default genetic code");
		defaultCodeOpt.setArgs(1);
		defaultCodeOpt.setArgName("The_Standard_Code");
		defaultCodeOpt.setRequired(false);
		macse_options.addOption(defaultCodeOpt);

		Option codeFileOpt = new Option(MacsEparamCode.GC_F,true,"file containing the list of genetic code to use for each sequence");
		codeFileOpt.setArgs(1);
		codeFileOpt.setArgName("gc_file.txt");
		codeFileOpt.setRequired(false);
		macse_options.addOption(codeFileOpt);
		
		addOptionOutputOptional();
	}

	private void addOptions_splitAlignment()
	{
		Option initialAlignment = new Option(MacsEparamCode.initial_alignment,true,"initial NT alignment in fasta format");
		initialAlignment.setArgs(1);
		initialAlignment.setArgName("NT_alignment.fasta");
		initialAlignment.setRequired(true);
		macse_options.addOption(initialAlignment);
		
		Option outputFileOpt = new Option(MacsEparamCode.outPut_subset,true,"full name of the output file containing the alignment of the sequence subset. Default is (input_file_prefix)_subset_NT.fasta");
		outputFileOpt.setArgs(1);
		outputFileOpt.setRequired(false);
		macse_options.addOption(outputFileOpt);
		
		Option outputFileOpt2 = new Option(MacsEparamCode.outPut_others,true,"full name of the output file containing the alignment of the other sequences. Default is (input_file_prefix)_others_NT.fasta");
		outputFileOpt2.setArgs(1);
		outputFileOpt2.setRequired(false);
		macse_options.addOption(outputFileOpt2);
		
		Option cladeOpt_F = new Option(MacsEparamCode.cladeToIsolate_F,true,"file containing the list of sequences (one per line, optionnaly precede by a \">\") of the first of the two desired sub-alignments");
		cladeOpt_F.setArgs(1);
		cladeOpt_F.setArgName("seq2isolate");
		cladeOpt_F.setRequired(false);
		macse_options.addOption(cladeOpt_F);
		
		Option startSiteOpt = new Option(MacsEparamCode.firstSite,true,"keep only sites having an index greater or equal to this one (default first site index)");
		startSiteOpt.setArgs(1);
		startSiteOpt.setArgName("1");
		startSiteOpt.setRequired(false);
		macse_options.addOption(startSiteOpt);
		
		Option lastSiteOpt = new Option(MacsEparamCode.lastSite,true,"keep only sites having an index greater or equal to this one (default last site index)");
		lastSiteOpt.setArgs(1);
		lastSiteOpt.setArgName("1");
		lastSiteOpt.setRequired(false);
		macse_options.addOption(lastSiteOpt);
		
		
	}
	
	private void addOptions_trNt2AA()
	{
		Option initialAlignment = new Option(MacsEparamCode.input_seq_F,true,"initial fasta format containing NT sequences");
		initialAlignment.setArgs(1);
		initialAlignment.setArgName("seq.fasta");
		initialAlignment.setRequired(true);
		macse_options.addOption(initialAlignment);
		
	/*	Option outputFileOpt = new Option(MacsEparamCode.outPut_AA,true,"full name of the output file containing the AA translation of this fasta file (default is {input_file_prefixe}_AA.fasta)");
		outputFileOpt.setArgs(1);
		outputFileOpt.setRequired(false);
		macse_options.addOption(outputFileOpt);*/
		
		Option codeFileOpt = new Option(MacsEparamCode.GC_F,true,"file containing the list of genetic code to use for each sequence");
		codeFileOpt.setArgs(1);
		codeFileOpt.setArgName("gc_file.txt");
		codeFileOpt.setRequired(false);
		macse_options.addOption(codeFileOpt);
		
		Option defaultCodeOpt = new Option(MacsEparamCode.default_GC,true,"indicate the default genetic code");
		defaultCodeOpt.setArgs(1);
		defaultCodeOpt.setArgName("The_Standard_Code");
		defaultCodeOpt.setRequired(false);
		macse_options.addOption(defaultCodeOpt);
		
		Option ignoreGapsdOpt = new Option(MacsEparamCode.ignoreGaps,false,"use this option to remove gaps from sequences before transcription");
		ignoreGapsdOpt.setRequired(false);
		macse_options.addOption(ignoreGapsdOpt);
		
		Option keepLastStop = new Option(MacsEparamCode.keepLastStop,false,"Translate the final stop codons into * (default not translate) ");
		keepLastStop.setRequired(false);
		macse_options.addOption(keepLastStop);
		
		Option guessOneReadingFrameOpt = new Option(MacsEparamCode.guessOneReadingFrame,false,"If used, MACSE guess the best reading frame for the whole sequence (default no guess). The resulting nucleotic file will be stored according to out_NT option (which is otherwise ignored)");
		guessOneReadingFrameOpt.setArgs(0);
		guessOneReadingFrameOpt.setRequired(false);
		macse_options.addOption(guessOneReadingFrameOpt);
		
		addOptionOutputOptional();
	}
	
	private void addOptionOutputOptional()
	{

		Option outputFileOptNT = new Option(MacsEparamCode.outPut_NT,true,"Name of the output file that will contain the output NT alignment (default is {input_file_prefixe}_NT.fasta).");
		outputFileOptNT.setArgs(1);
		outputFileOptNT.setArgName("output_NT.fasta");
		outputFileOptNT.setRequired(false);
		macse_options.addOption(outputFileOptNT);
		
		Option outputFileOptAA = new Option(MacsEparamCode.outPut_AA,true,"Name of the output file that will contain the output AA alignment (default is {input_file_prefixe}_AA.fasta).");
		outputFileOptAA.setArgs(1);
		outputFileOptAA.setArgName("output_AA.fasta");
		outputFileOptAA.setRequired(false);
		macse_options.addOption(outputFileOptAA);
	}
	
	private void addOptions_reportGapsAA2NT()
	{
		Option initialAlignment = new Option(MacsEparamCode.initial_alignment,true,"initial AA alignment in fasta format");
		initialAlignment.setArgs(1);
		initialAlignment.setArgName("AA_alignment.fasta");
		initialAlignment.setRequired(true);
		macse_options.addOption(initialAlignment);
		
		Option inputFileOpt = new Option(MacsEparamCode.input_seq_F,true,"unaligned NT sequence file in fasta format");
		inputFileOpt.setArgs(1);
		inputFileOpt.setArgName("input.fasta");
		inputFileOpt.setRequired(true);
		macse_options.addOption(inputFileOpt);
		
		Option codeFileOpt = new Option(MacsEparamCode.GC_F,true,"file containing the list of genetic code to use for each sequence");
		codeFileOpt.setArgs(1);
		codeFileOpt.setArgName("gc_file.txt");
		codeFileOpt.setRequired(false);
		macse_options.addOption(codeFileOpt);
		
		Option defaultCodeOpt = new Option(MacsEparamCode.default_GC,true,"indicate the default genetic code");
		defaultCodeOpt.setArgs(1);
		defaultCodeOpt.setArgName("The_Standard_Code");
		defaultCodeOpt.setRequired(false);
		macse_options.addOption(defaultCodeOpt);
		
		Option outputFileOptNT = new Option(MacsEparamCode.outPut_NT,true,"Name of the output file that will contain the output NT alignment (default is {input_file_prefixe}_NT.fasta).");
		outputFileOptNT.setArgs(1);
		outputFileOptNT.setArgName("output_NT.fasta");
		outputFileOptNT.setRequired(false);
		macse_options.addOption(outputFileOptNT);
		
	
		
	}
	
	private void addOptions_exportAlignment()
	{
		Option initialAlignment = new Option(MacsEparamCode.initial_alignment,true,"initial NT alignment in fasta format");
		initialAlignment.setArgs(1);
		initialAlignment.setArgName("NT_alignment.fasta");
		initialAlignment.setRequired(true);
		macse_options.addOption(initialAlignment);
		
		Option codeFileOpt = new Option(MacsEparamCode.GC_F,true,"file containing the list of genetic code to use for each sequence");
		codeFileOpt.setArgs(1);
		codeFileOpt.setArgName("gc_file.txt");
		codeFileOpt.setRequired(false);
		macse_options.addOption(codeFileOpt);
		
		Option defaultCodeOpt = new Option(MacsEparamCode.default_GC,true,"indicate the default genetic code");
		defaultCodeOpt.setArgs(1);
		defaultCodeOpt.setArgName("The_Standard_Code");
		defaultCodeOpt.setRequired(false);
		macse_options.addOption(defaultCodeOpt);
		
		//replacing codons
		Option codonForExternalFSOpt = new Option(MacsEparamCode.codonForExternalFS,true,"Codon to use instead of external FS codon (default: keep FS in codon, but place the ! of the last codon at its end)");
		codonForExternalFSOpt.setArgs(1);
		codonForExternalFSOpt.setArgName("NNN");
		codonForExternalFSOpt.setRequired(false);
		macse_options.addOption(codonForExternalFSOpt);
		
		Option codonForInternalFSOpt = new Option(MacsEparamCode.codonForInternalFS,true,"Codon to use instead of internal FS codon (default: no modification)");
		codonForInternalFSOpt.setArgs(1);
		codonForInternalFSOpt.setArgName("NNN");
		codonForInternalFSOpt.setRequired(false);
		macse_options.addOption(codonForInternalFSOpt);
		
		Option codonForInternalStopOpt = new Option(MacsEparamCode.codonForInternalStop,true,"Codon to use instead of internal stop codon (default: no modification)");
		codonForInternalStopOpt.setArgs(1);
		codonForInternalStopOpt.setArgName("NNN");
		codonForInternalStopOpt.setRequired(false);
		macse_options.addOption(codonForInternalStopOpt);
		
		Option codonForFinalStopOpt = new Option(MacsEparamCode.codonForFinalStop,true,"Codon to use instead of internal stop codon (default: no modification)");
		codonForFinalStopOpt.setArgs(1);
		codonForFinalStopOpt.setArgName("NNN");
		codonForFinalStopOpt.setRequired(false);
		macse_options.addOption(codonForFinalStopOpt);
		
		Option charForRemainingFS = new Option(MacsEparamCode.charForRemainingFS,true,"The caracter to use instead of  the \"!\" that remains after codon replacement (default: no modification)");
		charForRemainingFS.setArgs(1);
		charForRemainingFS.setArgName("-");
		charForRemainingFS.setRequired(false);
		macse_options.addOption(charForRemainingFS);
		
		Option statFile = new Option(MacsEparamCode.statFile,true,"Name of the output file that will contain the number of insertion, deletion, stop etc. for each sequence (default no stat output).");
		statFile.setArgs(1);
		statFile.setArgName("output_stat.csv");
		statFile.setRequired(false);
		macse_options.addOption(statFile);
		
		Option out_NT_perSiteStatOpt = new Option(MacsEparamCode.statPerSiteNTFile,true,"Name of the csv output file that will contain for each site the frequence of each nucleotide (default no stat output).");
		out_NT_perSiteStatOpt.setArgs(1);
		out_NT_perSiteStatOpt.setArgName("null");
		out_NT_perSiteStatOpt.setRequired(false);
		macse_options.addOption(out_NT_perSiteStatOpt);
		
		
		
		addOptionOutputOptional();
		
	}
	
	private void addOptions_refineAlignment()
	{
		Option initialAlignment = new Option(MacsEparamCode.initial_alignment,true,"initial NT alignment in fasta format");
		initialAlignment.setArgs(1);
		initialAlignment.setArgName("NT_alignment.fasta");
		initialAlignment.setRequired(true);
		macse_options.addOption(initialAlignment);
		
		Option inputOptimLevel = new Option(MacsEparamCode.optim,true,"optimisation parameter, 0 (no optim) 1 (basic, leaf 2-cut) or 2 (standart 2-cut)");
		inputOptimLevel.setArgs(1);
		inputOptimLevel.setArgName("1");
		inputOptimLevel.setRequired(true);
		macse_options.addOption(inputOptimLevel);
		
		Option inputFileOpt = new Option(MacsEparamCode.input_seq_F,true,"reliable sequence input file in fasta format");
		inputFileOpt.setArgs(0);
		inputFileOpt.setArgName("seq.fasta");
		inputFileOpt.setRequired(false);
		macse_options.addOption(inputFileOpt);
		
		addOptionOutputOptional();
		
	}
	
	private void addOptions_profileAlignment()
	{
		Option inputP1Opt = new Option(MacsEparamCode.profile1_F,true,"first profile file in fasta format");
		inputP1Opt.setArgs(1);
		inputP1Opt.setArgName("inputP1.fasta");
		inputP1Opt.setRequired(true);
		macse_options.addOption(inputP1Opt);
		
		Option inputP2Opt = new Option(MacsEparamCode.profile2_F,true,"second profile file in fasta format");
		inputP2Opt.setArgs(1);
		inputP2Opt.setArgName("inputP2.fasta");
		inputP2Opt.setRequired(true);
		macse_options.addOption(inputP2Opt);
		
		Option inputFileOpt = new Option(MacsEparamCode.input_seq_F,true,"reliable sequence input file in fasta format");
		inputFileOpt.setArgs(1);
		inputFileOpt.setArgName("seq.fasta");
		inputFileOpt.setRequired(false);
		macse_options.addOption(inputFileOpt);
		

		Option outputFileOptNT = new Option(MacsEparamCode.outPut_NT,true,"Name of the output file that will contain the output NT alignment.");
		outputFileOptNT.setArgs(1);
		outputFileOptNT.setArgName("output_NT.fasta");
		outputFileOptNT.setRequired(true);
		macse_options.addOption(outputFileOptNT);
		
		Option outputFileOptAA = new Option(MacsEparamCode.outPut_AA,true,"Name of the output file that will contain the output AA alignment.");
		outputFileOptAA.setArgs(1);
		outputFileOptAA.setArgName("output_AA.fasta");
		outputFileOptAA.setRequired(true);
		macse_options.addOption(outputFileOptAA);
	}
	
	private void addOptions_stdAlignment()
	{
		
		Option inputFileOpt = new Option(MacsEparamCode.input_seq_F,true,"reliable sequence input file in fasta format");
		inputFileOpt.setArgs(1);
		inputFileOpt.setArgName("input.fasta");
		inputFileOpt.setRequired(true);
		macse_options.addOption(inputFileOpt);
		
		addOptionOutputOptional();
		
	}
	
	private void addOptions_Alignment() {
	
		
		
		Option codeFileOpt = new Option(MacsEparamCode.GC_F,true,"file containing the list of genetic code to use for each sequence");
		codeFileOpt.setArgs(1);
		codeFileOpt.setArgName("gc_file.txt");
		codeFileOpt.setRequired(false);
		macse_options.addOption(codeFileOpt);
		
		Option inputLessReliableFileOpt = new Option(MacsEparamCode.input_less_reliable_seq_F,true,"less reliable (pseudoGene, Reads) sequence input file in fasta format");
		inputLessReliableFileOpt.setArgs(1);
		inputLessReliableFileOpt.setArgName("input_lr.fasta");
		inputLessReliableFileOpt.setRequired(false);
		macse_options.addOption(inputLessReliableFileOpt);
		
		
		Option defaultCodeOpt = new Option(MacsEparamCode.default_GC,true,"indicate the default genetic code");
		defaultCodeOpt.setArgs(1);
		defaultCodeOpt.setArgName("The_Standard_Code");
		defaultCodeOpt.setRequired(false);
		macse_options.addOption(defaultCodeOpt);
		
		Option gapOpenCostOpt = new Option(MacsEparamCode.gapOpenCost,true,"cost of creating a gap");
		gapOpenCostOpt.setArgs(1);
		gapOpenCostOpt.setArgName("7");
		gapOpenCostOpt.setRequired(false);
		macse_options.addOption(gapOpenCostOpt);
		
		Option gapExtCostOpt = new Option(MacsEparamCode.gapExtensionCost,true,"cost of a gap extension");
		gapExtCostOpt.setArgs(1);
		gapExtCostOpt.setArgName("1");
		gapExtCostOpt.setRequired(false);
		macse_options.addOption(gapExtCostOpt);
		
		Option stopCostOpt = new Option(MacsEparamCode.stopCodonCost,true,"cost of a stop codon not at the end of the sequence");
		stopCostOpt.setArgs(1);
		stopCostOpt.setArgName("100");
		stopCostOpt.setRequired(false);
		macse_options.addOption(stopCostOpt);
		
		Option fsCostOpt = new Option(MacsEparamCode.fsCost,true,"cost of a frameshift within the sequence");
		fsCostOpt.setArgs(1);
		fsCostOpt.setArgName("30");
		fsCostOpt.setRequired(false);
		macse_options.addOption(fsCostOpt);
		
		Option stopCostLessReliableOpt = new Option(MacsEparamCode.stopCodonCostLessReliable,true,"cost of a stop codon not at the end of a less reliable sequence");
		stopCostLessReliableOpt.setArgs(1);
		stopCostLessReliableOpt.setArgName("60");
		stopCostLessReliableOpt.setRequired(false);
		macse_options.addOption(stopCostLessReliableOpt);
		
		Option fsCostLessReliableOpt = new Option(MacsEparamCode.fsCostLessReliable,true,"cost of a frameshift within a less reliable sequence");
		fsCostLessReliableOpt.setArgs(1);
		fsCostLessReliableOpt.setArgName("10");
		fsCostLessReliableOpt.setRequired(false);
		macse_options.addOption(fsCostLessReliableOpt);
		
		Option ExternVsInternGapCostRatioOpt = new Option(MacsEparamCode.ExternVsInternGapCostRatio,true,"the gap cost is multiply by this ratio (< 1) to define the cost of gaps at the begining/end of sequences.");
		ExternVsInternGapCostRatioOpt.setArgs(1);
		ExternVsInternGapCostRatioOpt.setArgName(".95");
		ExternVsInternGapCostRatioOpt.setRequired(false);
		macse_options.addOption(ExternVsInternGapCostRatioOpt);
		
		
		
		
	}
	
}
