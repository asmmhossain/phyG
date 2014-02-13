package bioObject;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Hashtable;

import codesInterfaces.NT_AAsymbols;

import utils.Named;


/**
 * Ribosome is an object able to realize the link between nucleic and protein sequence 
 * by loading a genetic code.
 * Object used to translate protein or to check sequence coherence considering amino 
 * and nucleic symbols and genetic codes.
 * 
 */
public class Ribosome implements Named{

	private String codeUsed;// genetic code used to translate the sequence default standard.
	Hashtable<String, String> codon2AA;
	Hashtable<String, Integer> codon2intAA;
	Hashtable<String, String> AA2codon;
	public static String defaultCode="1";
	
	

	public static Hashtable<String, Ribosome> existingRibosome=null;

	public static Ribosome selectRibosome(Hashtable<String, Ribosome> seq2Ribo, String seqName)
	{
		Ribosome ribo = seq2Ribo.get(seqName);
		if(ribo==null)
			ribo=Ribosome.getRibosome();
		return ribo;
	}

	private static void loadGC() {
		existingRibosome = new Hashtable<String, Ribosome> ();
		
		/*
		existingRibosome.put("The_Standard_Code", new Ribosome("The_Standard_Code"));
		*/
		
		Sequence r = new Sequence("toto","AA");
		InputStream ips = r.getClass().getClassLoader().getResourceAsStream("genetic_code/genetic_code_list");
		String codeName;
		
		try {
			BufferedReader br=new BufferedReader(new InputStreamReader(ips));
			
			while ((codeName=br.readLine())!=null ){
				String [] infos =codeName.split(" ");
				codeName = infos[1];
				//System.out.println(codeName);
				existingRibosome.put(infos[0], new Ribosome(codeName));
				//System.out.println("put "+ infos[0]+ " codeName"+ codeName);
			}
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	/**
	 * GCname1 : seq1 seq2 seq3
	 * GCname2 : seq4 etc.
	 * @param GCfile
	 * @return
	 */
	public static Hashtable<String, Ribosome> parseGCfile(String GCfile)
	{

		InputStream ips;
		Ribosome currentRibo=null;
		Hashtable<String, Ribosome> seq2Ribo= new Hashtable<String, Ribosome>();

		// To count the number of associations
		try {
			ips = new FileInputStream(GCfile);
			InputStreamReader ipsr=new InputStreamReader(ips);
			BufferedReader br=new BufferedReader(ipsr);
			String line;

			while ((line=br.readLine())!=null && !line.equals("")){
				String currentAsso[] = line.split("\\t");
				String gcName = currentAsso[1];
				String seqName = currentAsso[0];
				currentRibo = getRibosome(gcName);
				if(currentRibo==null)
					System.err.println("genetic code:"+gcName+" is unknown");
				if(seq2Ribo.containsKey(seqName))
				{
					System.err.println("several genetic code are associated to "+ seqName);
				}
				seq2Ribo.put(seqName, currentRibo);
				
				}
			br.close(); 
		} 
		catch(IOException e){
			System.err.println("Error while parsing file associating seqName to geneticCode, file unreachable... please check "+GCfile);
			System.exit(1);
		}
		return seq2Ribo;
	}

	public static Ribosome getRibosome(String GCName)
	{
		if(existingRibosome == null)
		{
			loadGC();
		}
		return existingRibosome.get(GCName);
	}


	public static Ribosome getRibosome()
	{
		return getRibosome(Ribosome.defaultCode);
	}







	/**
	 * To create a Ribosome object considering a specific genetic code
	 * @param GCname is the name of the genetic code to load
	 */
	private Ribosome(String GCname){
	
		codon2AA = new Hashtable<String, String>();
		codon2intAA = new Hashtable<String, Integer>();
		AA2codon = new Hashtable<String, String>();
		codeUsed = GCname;
		try{
			loadGeneticCode();
		}
		catch (Exception e) {
			System.err.println("Specific Ribosome creation failed ...\nDetails : "+GCname+" "+e.getMessage());
			System.exit(1);
		}

	}



	/**
	 * To load a genetic code considering a file
	 * @param filePath The file to load
	 * The genetic code file has to contain all translations for all codons
	 *  format: 
	 *  codon1;AA1
	 *  codon2;AA2
	 * @throws RiboException 
	 */
	private void loadGeneticCode() throws Exception {

		String path = "genetic_code/"+codeUsed;
		//System.out.println(path);
		InputStream ips = this.getClass().getClassLoader().getResourceAsStream(path);
		// To count the number of associations
		Hashtable<String, Integer> aa2int = new Hashtable<String, Integer>();
		for(int i =0; i<NT_AAsymbols.aaSymbolTab.length;i++)
		{
			aa2int.put(NT_AAsymbols.aaSymbolTab[i]+"", new Integer(i));
		//	System.out.println("association: "+NT_AAsymbols.aaSymbolTab[i]+" "+i);
		}
		
		try {
			InputStreamReader ipsr=new InputStreamReader(ips);
			BufferedReader br=new BufferedReader(ipsr);
			String line;
			int i=0;

			while ((line=br.readLine())!=null && !line.equals("")){
				String CurrentAsso[] = line.split(";");
				codon2intAA.put(CurrentAsso[0], aa2int.get(CurrentAsso[1]));
				codon2AA.put(CurrentAsso[0], CurrentAsso[1] );
				AA2codon.put(CurrentAsso[1], CurrentAsso[0] );
			}
			br.close(); 
		} 
		catch(IOException e){
//			throw new RiboException("Error during genetic code construction, file unreachable... please check "+genticCodePath);
		}
		// Test if genetic code is complete
		if(codon2AA.size() != 64 ){
	//		throw new RiboException("Error during genetic code construction, missing data ("+codon2AA.size()+" codons) ... please check "+genticCodePath);
		}
		codon2AA.put("---","-");
		AA2codon.put("-", "---");
		codon2intAA.put("---",aa2int.get("-"));
	}


	/**
	 * To translate a given CODON into its Amino Acid
	 * @param aCodon
	 * @return the corresponding amino acid
	 * @throws RiboException 
	 */
	
	public char translate(String aCodon) throws Exception{
		char anAA ='@';
		if(codon2AA.containsKey(aCodon)){	// include --- => -
			anAA = codon2AA.get(aCodon).charAt(0);
		}
		else if(aCodon.equals("!!!"))// ajout FS au mauvais endroit puis rectification
		{
			anAA='-';
		}
		else if(aCodon.contains("!") || aCodon.length()<3 ||aCodon.contains("-")){// Frame shifted nucleotide triplet
			anAA = '!';
		}
		else if(aCodon.contains("?")||aCodon.contains("N") || aCodon.contains("R") || aCodon.contains("Y")|| aCodon.contains("M") 
				|| aCodon.contains("K")|| aCodon.contains("S")|| aCodon.contains("W")
				|| aCodon.contains("H")|| aCodon.contains("B")|| aCodon.contains("V")
				|| aCodon.contains("D")){// Unknown nucleotide so ? amino acid
			anAA = '?';
		}
		else{
			throw new Exception("Error during translation of CODON "+aCodon+" using Genetic code "+codeUsed+"\n" +
					"Please replace resources folder with the original resource folder or check file " +
					System.getProperty("user.dir")+"/resources/genetic_code/"+codeUsed);
		}

		return anAA;
	}

	/**
	 * To translate a given CODON into its Amino Acid
	 * @param aCodon
	 * @return the corresponding amino acid
	 * @throws RiboException 
	 */
	
	public int translateInt(String aCodon) throws Exception{
		int anAA =-1;
		if(codon2intAA.containsKey(aCodon)){	// include --- => -
			anAA = codon2intAA.get(aCodon).intValue();
		}
		else if(aCodon.equals("!!!"))// ajout FS au mauvais endroit puis rectification
		{
			anAA=NT_AAsymbols.AA_GAP_int;
		}
		else if(aCodon.contains("!") || aCodon.length()<3 ||aCodon.contains("-")){// Frame shifted nucleotide triplet
			anAA = NT_AAsymbols.AA_FS_int;
		}
		else if(aCodon.contains("?")||aCodon.contains("N") || aCodon.contains("R") || aCodon.contains("Y")|| aCodon.contains("M") 
				|| aCodon.contains("K")|| aCodon.contains("S")|| aCodon.contains("W")
				|| aCodon.contains("H")|| aCodon.contains("B")|| aCodon.contains("V")
				|| aCodon.contains("D")){// Unknown nucleotide so ? amino acid
			anAA = NT_AAsymbols.AA_UKN_int;
		}
		else{
			throw new Exception("Error during translation of CODON "+aCodon+" using Genetic code "+codeUsed+"\n" +
					"Please replace resources folder with the original resource folder or check file " +
					System.getProperty("user.dir")+"/resources/genetic_code/"+codeUsed);
		}

		return anAA;
	}


	/**
	 * To retro-translate a given Amino Acid into one of these corresponding codons
	 * @param aCodon the codon to retro translate
	 * @return the corresponding amino acid
	 * @throws RiboException 
	 */
	public String retroTranslate(char anAA) throws Exception{

		if(AA2codon.containsKey(anAA+""))
		{
			return AA2codon.get(anAA+"");
		}
		else
			throw new Exception("Error during retro-translation of amino acid : "+anAA+" using Genetic code "+codeUsed+"\n" +
					"Please replace resources folder with the original resource folder or check file " +
					System.getProperty("user.dir")+"/resources/genetic_code/"+codeUsed);
	}


	public String getName() {
		return codeUsed;
	}

	public static void main(String[] args) throws Exception {
		Ribosome r = getRibosome("21");
		System.out.println("AAA " +r.translate("AAA"));
	}


}

