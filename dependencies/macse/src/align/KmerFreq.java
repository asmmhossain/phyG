package align;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;

import bioObject.CodingDnaSeq;

import codesInterfaces.MatName;
import codesInterfaces.NT_AAsymbols;

//import com.sun.tools.javac.util.Bits;

//import sun.security.util.BitArray;



public class KmerFreq extends HashMap<String,Integer> {
	
	private int nbKword;
	private int nbPossKword;
	
	public int getNbPossKword() {
		return nbPossKword;
	}

	public static float[][]dist(ArrayList<CodingDnaSeq> sequences)
	{
		return dist(sequences, 12);
	}
	
	public static float[][]dist(ArrayList<CodingDnaSeq> sequences, int k)
	{
		// test if k is not to big
		int lgMin = sequences.get(0).length();
		for (CodingDnaSeq seq : sequences) {
			lgMin = Math.min(lgMin, seq.length());
		}
		
		k=  (int)Math.min(k, Math.floor((double)lgMin/2.) );
		float[][] res= new float[sequences.size()][sequences.size()];
		ArrayList<KmerFreq> kmerPerSeq = new ArrayList<KmerFreq>(sequences.size());
		for (CodingDnaSeq seq : sequences) {
			kmerPerSeq.add(new KmerFreq(k,seq));
		}
		for (int i = 0; i < res.length; i++) {
			for (int j = i; j < res.length; j++) {
				res[i][j]=res[j][i]= kmerPerSeq.get(i).distance(kmerPerSeq.get(j));
			}
		}	
		
		
		return res;
	}
	
	public KmerFreq(int k,CodingDnaSeq s1) {
		super();
		nbKword=0;
		String kword;
		String dnaSeq= s1.getSeq();
		for (int i = 0; i+k <= dnaSeq.length(); i++) {
			kword = s1.getSeq().substring(i, i+k);
			nbPossKword++;
			if(kword.indexOf('N')==-1) // true kword
				incKwordLetter(kword);
			
		}
	}
	
	public KmerFreq(int k,CodingDnaSeq s1, int readingFrame) {
		super();
		nbKword=0;
		String kword;
		String aaSeq= s1.getAAtranslation(readingFrame);
		for (int i = 0; i+k <= aaSeq.length(); i++) {
			kword = aaSeq.substring(i, i+k);
			nbPossKword++;
			if(kword.indexOf('?')==-1 && kword.indexOf('*')==-1 && kword.indexOf('!')==-1 && kword.indexOf('X')==-1) // true kword
				incKwordLetter(kword);
		}
	}
	public KmerFreq(int k,ArrayList<CodingDnaSeq> seqs) {
		super();
		nbKword=0;
		String kword;
		for (int si = 0; si < seqs.size(); si++) {
			for(int readingFrame=1; readingFrame<=3; readingFrame++)
			{
			String aaSeq= seqs.get(si).getAAtranslation(readingFrame);
			String noStop = aaSeq.replaceAll("\\"+NT_AAsymbols.AA_STOP,"");
			if(aaSeq.length() - noStop.length()>2)
				continue;
			for (int i = 0; i+k <= aaSeq.length(); i++) {
				kword = aaSeq.substring(i, i+k);
				nbPossKword++;
				if(kword.indexOf('?')==-1 && kword.indexOf('*')==-1 && kword.indexOf('!')==-1 && kword.indexOf('X')==-1) // true kword
					{
					incKwordLetter(kword);
					}
			}
			}
		}
		
	}
	
	public float distance(KmerFreq kmerFreq2)
	{
		float F=0;
		int f1,f2;
		HashSet <String> kwords = new HashSet<String>(this.keySet());
		kwords.addAll(kmerFreq2.keySet());
		for (String kword : kwords) {
			f1 = this.getKwordFreq(kword);
			f2 = kmerFreq2.getKwordFreq(kword);
			F+=  (float) Math.min(f1, f2);
		}
		F = F/((float)(Math.min(this.getNbPossKword(),kmerFreq2.getNbPossKword())));
		return (float)(-1*Math.log(0.1+F));
	}
	
	public void incKwordLetter(String kword) {
		setKwordFreq(kword,getKwordFreq(kword)+1);
		nbKword++;
	}
	
	private void setKwordFreq(String kword, int freqKword) {
		this.put(kword, new Integer(freqKword));
	}

	private boolean containsKword(String kword) {
		return this.containsKey(kword);
	}
	
	public int getKwordFreq(String kword) {
		if (containsKword(kword))
			return this.get(kword).intValue();
		else
			return 0;
	}
		
}
