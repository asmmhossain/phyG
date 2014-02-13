package utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Vector;



import bioObject.CodingDnaSeq;
import bioObject.Ribosome;
import bioObject.Sequence;
import align.BasicProfile;
import align.CodingMSA;
import align.KmerFreq;
import align.Profile;
import align.ProfileAligner;
import codesInterfaces.MacsEparamCode;
import codesInterfaces.NT_AAsymbols;

//-prog translateNT2AA
//-prog translateNT2AA
//-prog splitAlignment
//-prog refineAlignment
//-prog reportGapsAA2NT
//-prog align2profiles
//-prog correctContigs
//-prog alignSequences

public class MacseMain {
	
	
	 static BasicProfile reportNTGap2AA(ArrayList<CodingDnaSeq> coding_dna_seq, String aa_alignment_file) throws IOException {
		Hashtable<String, String> aa_aligned_seqs = Sequence.collectSequences(aa_alignment_file,false);
		Hashtable<String, CodingDnaSeq> dna_seqs = new Hashtable<String, CodingDnaSeq>();
		for (CodingDnaSeq codingDnaSeq : coding_dna_seq) {
			dna_seqs.put(codingDnaSeq.getRealFullName(), codingDnaSeq);
		}
		
		//check seq ids are the same or print a warning
		HashSet<String> subset= new HashSet<String>(dna_seqs.keySet());
		subset.removeAll(aa_aligned_seqs.keySet());
		if(dna_seqs.keySet().size() != aa_aligned_seqs.keySet().size() || subset.isEmpty()==false)
		{
			System.out.println("\n\n WARNING : the two files do not contain the same species, only common species will be aligned at the NT level please");
		}
		
		ArrayList<CodingDnaSeq> alignedNTseq = new ArrayList<CodingDnaSeq>();
		
		for (String seq_id : aa_aligned_seqs.keySet()) {
			String aaSeq = aa_aligned_seqs.get(seq_id);
			String ntSeq = dna_seqs.get(seq_id).getSeq();
			StringBuffer seqRes = new StringBuffer();
			int idDNA=0;
			for (int i = 0; i < aaSeq.length(); i++) {
				if(aaSeq.charAt(i)=='-'){
	                seqRes.append("---");	       
	            }else{
	                try {
						seqRes.append(ntSeq.substring(idDNA, idDNA+3));
						idDNA+=3;
					} catch (Exception e) {
						// TODO Auto-generated catch block
						System.err.println("pb for sequence "+dna_seqs.get(seq_id).getRealFullName());
						System.out.println(aaSeq);
						System.out.println(ntSeq);
						System.out.println(seqRes);
						System.exit(1);
					}
	            }
			}
			
	        	//Sequence lengths should correspond appart from incomplete codon and final stop.
	        	 if (idDNA +3 < ntSeq.length() || (idDNA +3 == ntSeq.length() && dna_seqs.get(seq_id).getAA(idDNA+2)!=NT_AAsymbols.AA_STOPE_int))
	        	 {
	        		 System.out.println("idDNA: " + idDNA);
	        		 System.out.println("ntSeq.lg: "+ ntSeq.length());
	        		System.out.println("pb incompatible DNA and AA length\n"+seq_id+"\n"+ntSeq+"\n"+aaSeq);
	        		//System.out.println("last codon code "+dna_seqs.get(seq_id).getAA(idDNA));
	        		//System.out.println(ntSeq.substring(idDNA, idDNA+3));
	        		System.exit(1);
	        	}
	        alignedNTseq.add(new CodingDnaSeq(seqRes.toString(), dna_seqs.get(seq_id)));
			
		}
		return new BasicProfile(alignedNTseq);
	}
	
	public static void saveAAAlignment(BasicProfile currentAlign, String outputFile, boolean keepFinalStop) throws IOException
	{
		
			BufferedWriter bwAA = new BufferedWriter(new FileWriter(outputFile));
			for (CodingDnaSeq seq : currentAlign.getSequences()) {
				String seq1 = seq.toAAfasta(1);
				if (seq1.charAt(seq1.lastIndexOf(seq1)) == NT_AAsymbols.AA_STOP)
					seq1 = seq1.substring(0, seq1.lastIndexOf(seq1));
				bwAA.append(seq1+"\n");
			}
			bwAA.close();
	}
	public static void saveAASequences(ArrayList<CodingDnaSeq> sequences, String outputFile) throws IOException
	{
		
			BufferedWriter bwAA = new BufferedWriter(new FileWriter(outputFile));
			for (CodingDnaSeq seq : sequences) {
				bwAA.append(seq.toAAfasta(1)+"\n");
			}
			bwAA.close();
	}
	
	public static void saveNTSequences(ArrayList<CodingDnaSeq> sequences, String outputFile) throws IOException
	{
		
			BufferedWriter bwNT = new BufferedWriter(new FileWriter(outputFile));
			for (CodingDnaSeq seq : sequences) {
				bwNT.append(seq.toFasta()+"\n");
			}
			bwNT.close();
	}
	
	public static void saveNTAlignment(BasicProfile currentAlign, String outputFile) throws IOException
	{
		
			BufferedWriter bwNT = new BufferedWriter(new FileWriter(outputFile));
			for (CodingDnaSeq seq : currentAlign.getSequences()) {
				bwNT.append(seq.toFasta()+"\n");
			}
			bwNT.close();
	}
	
	
	private static String subsetAsTree(String subsetFile, Hashtable<String, String>true2shortNames) throws IOException
	{
		InputStreamReader ipsr=new InputStreamReader(new FileInputStream(subsetFile));
		BufferedReader br=new BufferedReader(ipsr);
		StringBuffer subtree = new StringBuffer("(");
		String line;
		boolean first = true;
		while ((line=br.readLine())!=null ){
			if( ! line.isEmpty())
				{
				if(line.startsWith(">"))
					line = line.substring(1);
				if (first)
					subtree.append(true2shortNames.get(line));
				else
					subtree.append(","+true2shortNames.get(line));
				first=false;
				}
		}
		subtree.append(")");
		ipsr.close();
		return subtree.toString();
	}
	
	private static boolean sequenceCannotBeAdded(CodingDnaSeq seqToAdd, CodingDnaSeq seqTemplateProfile, MacsE_param macseOption, BufferedWriter bw) throws IOException
	{
		int [] nb_FS_stop_gapIE_inSeq2Add = seqToAdd.nb_internal_FS_STOP_Gap(1);
		int [] nb_FS_stop_gapIE_inAlignment = seqTemplateProfile.nb_internal_FS_STOP_Gap(1);
		
		//check limits ATTENTION DEL INS NT ou AA
		boolean pb = false;
		if(macseOption.getMaxFS()>=0 && nb_FS_stop_gapIE_inSeq2Add[0] > macseOption.getMaxFS())
			pb=true;
		if(macseOption.getMaxSTOP()>=0 && nb_FS_stop_gapIE_inSeq2Add[1] > macseOption.getMaxSTOP())
			pb=true;
		if(macseOption.getMaxDEL()>=0 && nb_FS_stop_gapIE_inSeq2Add[2] > macseOption.getMaxDEL())
			pb=true;
		if(macseOption.getMaxINS()>=0 && nb_FS_stop_gapIE_inAlignment[2] > macseOption.getMaxINS())
			pb=true;
		if(macseOption.getMaxTotalINS()>=0 && (nb_FS_stop_gapIE_inAlignment[2] + nb_FS_stop_gapIE_inAlignment[3])> macseOption.getMaxTotalINS())
			pb=true;
		
		String keptMsg ="yes";
		if(pb == true)
			keptMsg="no";
		bw.append(">"+seqToAdd.getRealFullName()+";added="+keptMsg+";FS="+nb_FS_stop_gapIE_inSeq2Add[0]+";stop="+nb_FS_stop_gapIE_inSeq2Add[1]+";DEL="+nb_FS_stop_gapIE_inSeq2Add[2]+";INS_internal="+nb_FS_stop_gapIE_inAlignment[2]+";INS_total="+(nb_FS_stop_gapIE_inAlignment[2]+nb_FS_stop_gapIE_inAlignment[3])+";\n");
		
		return pb;
	}
	
	private static boolean sequenceWithPb(CodingDnaSeq seqToAdd, CodingDnaSeq seqTemplateProfile) throws IOException
	{
		int [] nb_FS_stop_gapIE_inSeq2Add = seqToAdd.nb_internal_FS_STOP_Gap(1);
		int [] nb_FS_stop_gapIE_inAlignment = seqTemplateProfile.nb_internal_FS_STOP_Gap(1);
		
		//check limits ATTENTION DEL INS NT ou AA
		boolean pb = false;
		if( nb_FS_stop_gapIE_inSeq2Add[0] > 0)
			pb=true;
		if( nb_FS_stop_gapIE_inSeq2Add[1] > 0)
			pb=true;
		//DEL in seq
		if(nb_FS_stop_gapIE_inSeq2Add[2] >0)
			pb=true;
		//INS in Seq
		if(nb_FS_stop_gapIE_inAlignment[2]>0)
			pb=true;
		
		String keptMsg ="yes";
		if(pb == true)
			keptMsg="no";
		//System.out.println(">"+seqToAdd.getRealFullName()+";added="+keptMsg+";FS="+nb_FS_stop_gapIE_inSeq2Add[0]+";stop="+nb_FS_stop_gapIE_inSeq2Add[1]+";DEL="+nb_FS_stop_gapIE_inSeq2Add[2]+";INS_internal="+nb_FS_stop_gapIE_inAlignment[2]+";INS_total="+(nb_FS_stop_gapIE_inAlignment[2]+nb_FS_stop_gapIE_inAlignment[3])+";\n");
		
		return pb;
	}
	
	private static void enrichAlignment(MacsE_param macseOption, AlignmentParameterWrappers parameters) throws IOException
	{
		ArrayList<CodingDnaSeq> alignedSeq=CodingDnaSeq.readFasta(macseOption.get_initial_alignment(), parameters.getSeq2Ribo(), false, parameters.getCost(), parameters.getSeq2cost());
		ArrayList<CodingDnaSeq> resAlignedSeq=CodingDnaSeq.readFasta(macseOption.get_initial_alignment(), parameters.getSeq2Ribo(), false, parameters.getCost(), parameters.getSeq2cost());

		
		Hashtable<String, CodingDnaSeq> fullName2seq = new Hashtable<String, CodingDnaSeq>();
		for (CodingDnaSeq seq : alignedSeq) {
			fullName2seq.put(seq.getRealFullName(), seq);
		}
		
		ArrayList<CodingDnaSeq> allSeq = parameters.getAllSequences();
		ArrayList<CodingDnaSeq> seq2add = new ArrayList<CodingDnaSeq>();
		System.out.println(alignedSeq.size() +"sequences added");
		for (CodingDnaSeq seq : allSeq) {
			if(fullName2seq.containsKey(seq.getRealFullName()))
			{
				fullName2seq.get(seq.getRealFullName()).setNames(seq.getName(), seq.getRealFullName());
			}
			else
				seq2add.add(seq);
		}
		BufferedWriter bw = new BufferedWriter(new FileWriter(macseOption.getAlignStatLogFile()));
		fullName2seq.clear();//free memory
		System.out.println(seq2add.size()+" sequences to add");
		Profile currentAlign = new Profile("alignment",alignedSeq);
		ProfileAligner profileAligner = new ProfileAligner(currentAlign.nbSites(),parameters.getCost());
		ArrayList<CodingDnaSeq> seq1 = new ArrayList<CodingDnaSeq>();
		//int seqNum=1;
		for (CodingDnaSeq seq : seq2add) {
			
			//System.out.println(seq.getRealFullName());
			seq1.clear();
			seq1.add(seq);
			Profile p1 = new Profile("p1", seq1);
			profileAligner.alignProfiles(p1, currentAlign);
			ArrayList<String> pattern = profileAligner.backTrackTemplate();
			String patternSeq1=pattern.get(0);
			
			String seq_aligned = CodingDnaSeq.alignUsingPattern(patternSeq1, seq);
			
			CodingDnaSeq seq_toAdd = new CodingDnaSeq(seq_aligned,seq);
			//profile
			CodingDnaSeq seqTemplateProfile = new CodingDnaSeq(pattern.get(1),seq_toAdd);
			
			Boolean pb = sequenceCannotBeAdded(seq_toAdd, seqTemplateProfile, macseOption, bw);
			
			if( pb == false )
			{
				if(macseOption.get_incremental())					
					currentAlign = new Profile("fusion",profileAligner.backTrack());
				else
				{
					resAlignedSeq.add(seq_toAdd);
				}
					
			}
			System.out.print(".");//System.out.print(seq.getRealFullName());
		}
		
		if(!macseOption.get_incremental())
			currentAlign = new Profile("fusion",resAlignedSeq);
		currentAlign.exportToFasta(macseOption.getNTOutputFile(), macseOption.getAAOutputFile());
		bw.close();
	}
	
	private static void trimSequences(MacsE_param macseOption, AlignmentParameterWrappers parameters) throws IOException
	{
		ArrayList<CodingDnaSeq> alignedSeq=CodingDnaSeq.readFasta(macseOption.get_initial_alignment(), parameters.getSeq2Ribo(), false, parameters.getCost(), parameters.getSeq2cost());
		ArrayList<CodingDnaSeq> allSeq = parameters.getAllSequences();
		
		BufferedWriter bwAnnot = new BufferedWriter(new FileWriter(macseOption.getAnnotatedSeqFileNT()));
		BufferedWriter bwTrimed = new BufferedWriter(new FileWriter(macseOption.getTrimedSeqFileNT()));
		BufferedWriter bwCoding = new BufferedWriter(new FileWriter(macseOption.getTrimInfoFile()));
		BufferedWriter bwNTAlignment = new BufferedWriter(new FileWriter(macseOption.getNTOutputFile()));
		
		
		Profile currentAlign = new Profile("alignment",alignedSeq);
		ProfileAligner profileAligner = new ProfileAligner(currentAlign.nbSites(),parameters.getCost());
		ArrayList<CodingDnaSeq> seq1 = new ArrayList<CodingDnaSeq>();
	
		for (CodingDnaSeq seq : allSeq) {
			
			//System.out.println(seq.getRealFullName());
			seq1.clear();
			seq1.add(seq);
			Profile p1 = new Profile("p1", seq1);
			profileAligner.alignProfiles(p1, currentAlign);
			ArrayList<String> pattern = profileAligner.backTrackTemplate();
			
			// seq added to the profile
			String patternSeq1=pattern.get(0);
			String seq_aligned = CodingDnaSeq.alignUsingPattern(patternSeq1, seq);
			
			CodingDnaSeq seq_al = new CodingDnaSeq(seq_aligned,seq);
	
			//profile
			CodingDnaSeq seq_profile = new CodingDnaSeq(pattern.get(1),seq_al);		
	
				
				StringBuffer alSeq = new StringBuffer("");
				StringBuffer prefix = new StringBuffer("");
				for(int i=0; i<seq_profile.getFirstNonGap(); i++)
				{
					prefix.append(seq_aligned.charAt(i));
				}
				
				for(int i=seq_profile.getFirstNonGap(); i<=seq_profile.getLastNonGap(); i++)
				{
					alSeq.append(seq_aligned.charAt(i));
				}
				CodingDnaSeq alSeqCodingSeq = new CodingDnaSeq(alSeq.toString(), seq_al);
				
				StringBuffer suffix= new StringBuffer("");
				
				for(int i=seq_profile.getLastNonGap(); i<seq_profile.length(); i++)
				{
					suffix.append(seq_aligned.charAt(i));
				}
				
				String prefixOK = Sequence.removeFS(Sequence.removeGap(prefix.toString())).toLowerCase();
				String mainOK = Sequence.removeFS(Sequence.removeGap(alSeq.toString())).toUpperCase();
				String suffixOK = Sequence.removeFS(Sequence.removeGap(suffix.toString())).toLowerCase();
				String fullAnnotSeq = prefixOK+mainOK+suffixOK;
				
				bwAnnot.append(">"+seq.getRealFullName()+"\n"+fullAnnotSeq+"\n");
				bwTrimed.append(">"+seq.getRealFullName()+"\n"+mainOK+"\n");
				bwCoding.append(">"+seq.getRealFullName()+"\t"+prefixOK.length()+"\t"+mainOK.length()+"\t"+suffixOK.length()+"\t"+fullAnnotSeq.length()+"\n");
			
				Boolean pb=sequenceWithPb(alSeqCodingSeq, seq_profile);
				if (pb==false)
					bwNTAlignment.append(">"+seq.getRealFullName()+"\n"+alSeq.toString()+"\n");
				
			
				//currentAlign = new Profile("fusion",profileAligner.backTrack());
			System.out.print(".");//System.out.print(seq.getRealFullName());
		}
		//currentAlign= new Profile("res",alignedSeq);
		//currentAlign.exportToFasta(macseOption.getNTOutputFile(), macseOption.getAAOutputFile());
		
		bwTrimed.close();
		bwAnnot.close();
		bwCoding.close();
		bwNTAlignment.close();
	}
	
	public static void main(String[] args) throws Exception {
		
		MacsE_param macseOption = new MacsE_param(args);
		
		AlignmentParameterWrappers parameters = new AlignmentParameterWrappers(macseOption);
		
		BasicProfile res_alignment=null;
		if (macseOption.getProgName().equals(MacsEparamCode.progName_alSeq))
		{
			res_alignment = CodingMSA.run(parameters);
			res_alignment.exportToFasta(macseOption.getNTOutputFile(), macseOption.getAAOutputFile());
		}
		
		if (macseOption.getProgName().equals(MacsEparamCode.progName_trimSequences))
		{
			trimSequences(macseOption, parameters);
		}
		
		if (macseOption.getProgName().equals(MacsEparamCode.progName_enrichAlignment))
		{
			enrichAlignment(macseOption, parameters);
		}
		
		if (macseOption.getProgName().equals(MacsEparamCode.progName_reportGapsAA2NT))
		{
			//ICII ne pas enlever les ! avant le report sinon le guessReadingFrame perd son interet
			ArrayList<CodingDnaSeq>  nt_seq= CodingDnaSeq.readFasta(macseOption.getInputReliableFile(),parameters.seq2Ribo,false,parameters.getCost());
			res_alignment = reportNTGap2AA(nt_seq,macseOption.get_initial_alignment());
			saveNTAlignment(res_alignment,macseOption.getNTOutputFile());
			
		}
		
		if (macseOption.getProgName().equals(MacsEparamCode.progName_al2prof))
		{
			System.out.println(macseOption.getNTOutputFile());
			System.out.println(macseOption.getAAOutputFile());
			//System.exit(1);
			// less reliable cost are lost !
			Profile p1 = new Profile("p1",CodingDnaSeq.readFasta(macseOption.getProfile1File(), parameters.getSeq2Ribo(), false, parameters.getCost(), parameters.getSeq2cost()));
			Profile p2 = new Profile("p2",CodingDnaSeq.readFasta(macseOption.getProfile2File(), parameters.getSeq2Ribo(), false, parameters.getCost(), parameters.getSeq2cost()));
			
			ProfileAligner profileAligner = new ProfileAligner(Math.max(p1.nbSites(), p2.nbSites()),parameters.getCost());
			profileAligner.alignProfiles(p1, p2);
			res_alignment = new BasicProfile("fusion",profileAligner.backTrack());
			

			res_alignment.exportToFasta(macseOption.getNTOutputFile(), macseOption.getAAOutputFile());
		}
		
		if (macseOption.getProgName().equals(MacsEparamCode.progName_refineAlign))
		{
			
			// less reliable cost are lost !
			BasicProfile currentAlign = new BasicProfile("alignment",CodingDnaSeq.readFasta(macseOption.get_initial_alignment(), parameters.getSeq2Ribo(), false, parameters.getCost(), parameters.getSeq2cost()));
			currentAlign.standartizationOfSeqNames("seq");
			System.out.println("initial alignment read from file "+macseOption.get_initial_alignment()+"\n refined ...");
			
			CodingMSA msa =new CodingMSA(currentAlign.getSequences(),parameters.getCost());
			if (macseOption.getOptimCode() ==0 )
				System.out.println(msa.evalProfileScore(currentAlign));
			if (macseOption.getOptimCode() ==1 )
				res_alignment = new Profile(msa.refine2cutLeavesOnly(new BasicProfile(currentAlign.getSequences()), 500));
			if (macseOption.getOptimCode() ==2 )
				res_alignment = new Profile(msa.refine2cut(new BasicProfile(currentAlign.getSequences()), 500));
			
			if (macseOption.getOptimCode() > 0 )
			res_alignment.exportToFasta(macseOption.getNTOutputFile(), macseOption.getAAOutputFile());
		}
		
		if (macseOption.getProgName().equals(MacsEparamCode.progName_trNT2AA))
		{
			
			BasicProfile currentAlign = new BasicProfile("alignment",CodingDnaSeq.readFasta(macseOption.getInputReliableFile(), parameters.getSeq2Ribo(), macseOption.get_ignoreGaps(), parameters.getCost(), parameters.getSeq2cost()));
			if (macseOption.get_guessOneReadingFrame()==false)
			{
				saveAAAlignment(currentAlign, macseOption.getAAOutputFile(), macseOption.get_keepLastStop());
			}
			else
			{
			ArrayList<CodingDnaSeq> seqInPhase = new ArrayList<CodingDnaSeq>();
			//experimental
			{
				int k=6;
				KmerFreq kAll = new KmerFreq(k,currentAlign.getSequences());
				for (int i = 0; i < currentAlign.nbSeq(); i++) {
					float distMin = Float.MAX_VALUE;
					int bestRF=-1;
					for(int readingFrame=1;readingFrame<=3; readingFrame++)
					{
						
						String aaSeq= currentAlign.getSeq(i).getAAtranslation(readingFrame);
						String noStop = aaSeq.replaceAll("\\"+NT_AAsymbols.AA_STOP,"");
						KmerFreq ktmp = new KmerFreq(k,currentAlign.getSeq(i), readingFrame);
						float d = (float)(aaSeq.length() - noStop.length());// + kAll.distance(ktmp);
						if(d<distMin)
						{
							distMin=d;
							bestRF=readingFrame;
						}
					}
				//	System.out.println(currentAlign.getSeq(i).getRealFullName()+"bestRF\t"+bestRF+"bestDist\t"+distMin);
					seqInPhase.add(CodingDnaSeq.putInPhase( currentAlign.getSeq(i), bestRF));
				}
			}
			currentAlign = new BasicProfile(seqInPhase);
			currentAlign.exportToFasta(macseOption.getNTOutputFile(), macseOption.getAAOutputFile());
			
			
			}
		}
		
		if (macseOption.getProgName().equals(MacsEparamCode.progName_exportAlignment))
		{
			
			BasicProfile currentAlign = new BasicProfile("alignment",CodingDnaSeq.readFasta(macseOption.get_initial_alignment(), new Hashtable<String, Ribosome>(), false, parameters.getCost(), parameters.getSeq2cost()));
			String FSint= macseOption.get_codonForXXX(MacsEparamCode.codonForInternalFS);
			String FSext= macseOption.get_codonForXXX(MacsEparamCode.codonForExternalFS);
			String STOPint= macseOption.get_codonForXXX(MacsEparamCode.codonForInternalStop);
			String STOPfinal= macseOption.get_codonForXXX(MacsEparamCode.codonForFinalStop);
			String charForRemainingFS= macseOption.get_codonForXXX(MacsEparamCode.charForRemainingFS);
			
			for (CodingDnaSeq seq : currentAlign.getSequences()) {
				seq.replaceNonStandartCodon(FSint,STOPfinal, STOPint,FSext, charForRemainingFS);
			}
			currentAlign.exportToFasta(macseOption.getNTOutputFile(), macseOption.getAAOutputFile());
			if(macseOption.getStatFile()!=null)
			{
				currentAlign.exportStat(macseOption.getStatFile());
			}
			if(macseOption.getStatPerSiteNTFile()!=null)
			{
				currentAlign.exportperSiteNTStat(macseOption.getStatPerSiteNTFile());
			}
		}
		

		if (macseOption.getProgName().equals(MacsEparamCode.progName_removeSeqCausingGappySites))
		{
			BasicProfile currentAlign = new BasicProfile("alignment",CodingDnaSeq.readFasta(macseOption.get_initial_alignment(), new Hashtable<String, Ribosome>(), false, parameters.getCost(), parameters.getSeq2cost()));
			boolean change;
			BasicProfile filteredProfile;
			do
			{
				filteredProfile = currentAlign.extractSubProfile(macseOption.getThresholdForGappyness());
				change = (currentAlign.nbSeq() != filteredProfile.nbSeq());
				System.out.println("=>"+currentAlign.nbSeq() + " "+ filteredProfile.nbSeq() + change);
				currentAlign=filteredProfile;
			}while (change);
			
			filteredProfile.exportToFasta(macseOption.getNTOutputFile(), macseOption.getAAOutputFile());
			
		}
		
		if (macseOption.getProgName().equals(MacsEparamCode.progName_splitAlign))
		{
			// pass ribosome even thought useless for NT splitting of the alignment
			BasicProfile currentAlign = new BasicProfile("alignment",CodingDnaSeq.readFasta(macseOption.get_initial_alignment(), new Hashtable<String, Ribosome>(), false, parameters.getCost(), parameters.getSeq2cost()));
			
			
			if(macseOption.getFirstSite()>0 || macseOption.getLastSite()>0)
				currentAlign = currentAlign.extractSubProfile(macseOption.getFirstSite(), macseOption.getLastSite());
			
			/* reflexion sur virer site -! ou juste -- et !! */
			if(macseOption.get_splitSpeciesList_file()!=null)
			{
				Hashtable<String, String> true2shortNames = currentAlign.standartizationOfSeqNames("seq");
				String subtree= subsetAsTree(macseOption.get_splitSpeciesList_file(),true2shortNames);
				System.out.println(subtree);
				Vector<BasicProfile> splittedAlign = currentAlign.splitProfile(subtree.toString());
				if(splittedAlign != null)
				{
				saveNTAlignment(splittedAlign.get(0), macseOption.getSubsetOutputFile());
				saveNTAlignment(splittedAlign.get(1), macseOption.getOthersOutputFile());
				}
				else
					if(subtree.equals("()"))
						saveNTAlignment(currentAlign, macseOption.getOthersOutputFile());
					else
						saveNTAlignment(currentAlign, macseOption.getSubsetOutputFile());						
			}
			else
			{
				saveNTAlignment(currentAlign, macseOption.getSubsetOutputFile());
			}
		}
		
	}
	
}
/*
translateNT2AA
splitAlignment
refineAlignment
reportGapsAA2NT
align2profiles
correctContigs
alignSequences
*/