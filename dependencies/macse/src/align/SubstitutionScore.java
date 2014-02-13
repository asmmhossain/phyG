package align;



import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;


/**
 * To create an object {@link SubstitutionScore} able to load a substitution matrix from a specific file
 *
 *@author HARISPE SEBASTIEN (ISEM work placement)
 *
 */
public class SubstitutionScore {

	float [][] matrix;//the substitution matrix
	public static String defaultMatrix = "BLOSUM62.txt"; 
	public static SubstitutionScore factory(String matrixName)
	{
		return new SubstitutionScore(matrixName);
	}
	public static SubstitutionScore factory()
	{	
		return SubstitutionScore.factory(defaultMatrix);
	}
	
	public HashMap<Character, Integer> indexAA;
	/*
	 * Each amino acid is associate to an index
	 * Then matrix[index1][index2] correspond to the substitution score between
	 * the amino acid define by index1 and the amino acid define by index 2
	 */
	
	public SubstitutionScore(String matrixPath){

		indexAA = new HashMap<Character, Integer>();
	
		InputStream ips = this.getClass().getClassLoader().getResourceAsStream("subsMatrixAA/"+matrixPath);
		BufferedReader br=new BufferedReader(new InputStreamReader(ips));
		
		int currentRow = 0;
		boolean firstLine = true;
		String currentLine ="";
		String[] result = null;

		try {
			while ((currentLine=br.readLine())!=null && notEnd(currentLine)){
				// this statement reads the line from the file and print it to
				
				if (testLine(currentLine)){
					if(firstLine){//Amino Acid order depends 
						result = currentLine.split(" ");
						
						matrix = new float[result.length][result.length];
						firstLine = false;
					}
					else{
						result = currentLine.split("\\s+");
						if(result.length > 0)
						{
						indexAA.put(new Character(result[0].charAt(0)), currentRow);
						for (int i = 1; i < result.length; i++) {
							matrix[i-1][currentRow] = Float.parseFloat(result[i]);
						}
						currentRow++;
						}
					}
				}
			}
			br.close();
			checkCoherence();
			float [][] matrix2 = new float[26][26];
			String alphabet="ABCDEFGHIJKLMNOPQRSTUVWXYZ";
			for (int i = 0; i < alphabet.length(); i++) {
				for (int j = i; j < alphabet.length(); j++) {
					char c1 = alphabet.charAt(i);
					char c2 = alphabet.charAt(j);
					if(indexAA.containsKey(c1)&& indexAA.containsKey(c2))
					{
						float val =  matrix[indexAA.get(c1)][indexAA.get(c2)];
						matrix2[(int)c1-(int)'A'][(int)c2-(int)'A']= val;
						matrix2[(int)c2-(int)'A'][(int)c1-(int)'A']= val;
					}
				}
				
			}
			matrix = matrix2;

		}catch (Exception e) {
			System.err.println("- Error during matrix loading (working on "+matrixPath+")");
			e.printStackTrace();
		}
		
	}
	
	
	
	/**
	 * To test if the end of the matrix is reached
	 * @param currLine
	 * @return
	 */
	private boolean notEnd(String currLine) {

		if(currLine.equals("#END")){
			return false;
		}
		else{
			return true;
		}
	}

	/**
	 * To test if a line is a not comment line 
	 * @param currentLine
	 * @return
	 */
	private boolean testLine(String currentLine) {
		if(currentLine.contains("#"))
			return false;
		else
			return true;
	}

	/**
	 * To obtain the matrix value corresponding to the couple of given Amino Acids during profile alignment
	 * @param A an amino acid (Char)
	 * @param B an amino acid (Char)
	 * @return value corresponding to the probability of a substitution considering A & B
	 * @throws MatrixLderException 
	 * @throws Exception 
	 */
	public float probAtoB_MA(Character A,Character B){// throws MatrixLderException{
		float result = -1;

			try{
				result = matrix[(int)A-(int)'A'][(int)B-(int)'A'];
				
			}
			catch(Exception e){
				System.err.println("No correspondance for Amino acid "+A+" & "+B+", please check matrix file.");
				System.exit(-1);
			}
			return result;
	}

	
	/**
	 * To check if the specified substitution is symmetric
	 * @throws MatrixLderException 
	 */
	public void checkCoherence() throws Exception{
		boolean coherence = true;
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[0].length; j++) {

				if(matrix[i][j] != matrix[i+(0-i)+j][j+(0-j)+i]){
					
					coherence = false;
					break;
				}
			}
		}
		if(!coherence){
			System.out.println(matrix[0].length);
			System.out.println(matrix.length);
			throw new Exception("Error : the specified Matrix is not symetric");
			
		}
	}
	

	
}
