package align;


import codesInterfaces.NT_AAsymbols;


public class CharFreqManager {
	private int []myAA;
	private float []myFreq;
	private float [] costInFrontOfAA;
	private int siteNum;
	
	
	
	public int getSiteNum() {
		return siteNum;
	}
	
	public String toString() {
		String res="charFreq=";
		for (int i = 0; i < myAA.length; i++) {
			res+="\t"+NT_AAsymbols.aaSymbolTab[myAA[i]]+":"+myFreq[i];
		}
		return res;
	}

	public float computeIndepCost(CharFreqManager freq2)
	{
		
		float score=0;
		if(myAA.length > freq2.myAA.length)
			score=freq2.computeIndepCost(this);
		else
		{
			double t1 = System.currentTimeMillis();
			for (int i = 0; i < myAA.length; i++) {
				score += this.myFreq[i] * freq2.costInFrontOfAA[this.myAA[i]];
			}
		double t2 = System.currentTimeMillis();
		//ProfileAligner.time_min += (t2-t1);
		}
		/*System.out.println(this);
		System.out.println(freq2);
		System.out.println("score : "+ score+"\n");*/
		
		return score;
	}
	
	
	
	CharFreqManager (int [] usedAA, float[]freqAA, int nbUsedAA, int siteNum, ElementaryCost cost)
	{
		this.siteNum=siteNum ;
		myAA = new int[nbUsedAA];
		myFreq = new float [nbUsedAA];
		
		costInFrontOfAA = new float[NT_AAsymbols.NB_AA_cost_indep];
		for (int aaInFront = 0; aaInFront < NT_AAsymbols.NB_AA_cost_indep; aaInFront++) {
			for (int i=0; i<nbUsedAA;i++) {
				costInFrontOfAA[aaInFront] += cost.getSubstScore(aaInFront,usedAA[i])*freqAA[usedAA[i]];
				myAA[i]=usedAA[i];
				myFreq[i]=freqAA[usedAA[i]];
			}
		}
		
		
	}

	
}
