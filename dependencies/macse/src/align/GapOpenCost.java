package align;
import codesInterfaces.CostCode;
import codesInterfaces.DimerCodes;


// based on Aligning Alignments Kececioglu & zhang (1998)

public  class GapOpenCost implements DimerCodes{

	protected float fA[][];
	protected float fB[][];

	private Profile pA,pB;
	protected ElementaryCost cost;
	protected float sureGoCost; // the cost of a gap open
	protected float possGoCost; // the cost of a possible gap open
	protected boolean debug;

	protected void initCost() {
		this.sureGoCost= cost.get(CostCode.GAP_O);
		this.possGoCost= cost.get(CostCode.OPT_PESS_GAP_FACTOR)*cost.get(CostCode.GAP_O);
	}
	
	public GapOpenCost( ElementaryCost cost) {
		fA = new float[4][];
		fB = new float[4][];
		this.cost = cost;
		debug = false;
		initCost();
		
	}

	public void setDebug(boolean d)
	{
		this.debug = d;
	}
	public static int mvtCode (int di, int dj)
	{
			return 10*di+dj;
	}
	
	public void setSite1(Profile p1, int siteP1)
	{
		this.pA=p1;
		for (int d=0; d<4; d++)
			fA[d] = p1.getDimerFreq(siteP1 -d);
	}
	public void setSite2(Profile p2, int siteP2)
	{
		this.pB=p2;
		for (int d=0; d<4; d++)
			fB[d] = p2.getDimerFreq(siteP2 -d);
	}
	

	private void swapFAFB()
	{
		float[] ofA[] = fA;
		fA = fB;
		fB = ofA;
		
		Profile opA = pA; 
		pA = pB;
		pB = opA;
	}
	
	public float costD2M(int di, int dj)
	{
		float gopCost=0;
		switch (mvtCode(di, dj)) {
		case 33:
			gopCost +=possGoCost * fA[0][d0]*fB[0][d01];
			gopCost +=possGoCost * fA[0][d1]*fB[0][d00];
			gopCost +=sureGoCost * fA[0][d1]*fB[0][d10];
			break;
		case 32: case 31:
			gopCost +=possGoCost * fA[0][d0]*fB[dj][d0];
			break;
		case 23: case 13:
			gopCost +=possGoCost * fB[0][d00]*pA.nbSeq();
			gopCost +=sureGoCost * fB[0][d10]*pA.nbSeq();
			break;
		case 11: case 12: case 21: case 22:
			gopCost += 0;
			break;
		}
		return gopCost;
	}
	
	public float costM2M(int di, int dj)
	{
		float gopCost=0;
		switch (mvtCode(di, dj)) {
		case 33:
			gopCost +=possGoCost * fA[0][d00]*fB[0][d01];
			gopCost +=possGoCost * fA[0][d01]*fB[0][d00];
			gopCost +=sureGoCost * fA[0][d01]*fB[0][d10];
			gopCost +=sureGoCost * fA[0][d10]*fB[0][d1];
			gopCost +=sureGoCost * fA[0][d11]*fB[0][d10];
			break;
		case 23: case 13:
			gopCost +=possGoCost * fA[di][d0]*fB[0][d00];
			gopCost +=sureGoCost * pA.nbSeq()*fB[0][d10];
			break;
		case 31: case 32:
			gopCost +=possGoCost * fB[dj][d0]*fA[0][d00];
			gopCost +=sureGoCost * pB.nbSeq()*fA[0][d10];
			break;
		case 11: case 12: case 21: case 22:
			gopCost += 0;
			break;
		}
		return gopCost;
	}
	
	public float costD2D(int di, int dj)
	{
		float gopCost=0;
		switch (mvtCode(di, dj)) {
		case 03:
			gopCost +=possGoCost * pA.nbSeq()*fB[0][d01];
			break;
		case 02: case 01:
			gopCost +=possGoCost * pA.nbSeq()*fB[dj][d0];
			break;
		}
		if(debug)
			System.out.println("gop "+ gopCost);
		return gopCost;
	}
	
	public float costI2D(int di, int dj)
	{
		float gopCost=0;
		switch (mvtCode(di, dj)) {
		case 03:
			gopCost +=possGoCost * fA[0][d0]*fB[0][d1];
			gopCost +=sureGoCost * fA[0][d1]*fB[0][d1];
			break;
		case 02: case 01:
			gopCost +=possGoCost * fA[0][d0]*pB.nbSeq();
			gopCost +=sureGoCost * fA[0][d1]*pB.nbSeq();
			break;
		}
		return gopCost;
	}
	
	// FA ? -
	// FB ? ?
	public float costM2D(int di, int dj)
	{
		float gopCost=0;
		switch (mvtCode(di, dj)) {
		case 03:
			gopCost +=possGoCost * fA[0][d0]*fB[0][d01];
			gopCost +=sureGoCost * fA[0][d1]*fB[0][d1] ;
			break;
		case 02: case 01:
			gopCost +=possGoCost * fA[0][d0]*fB[dj][d0];
			gopCost +=sureGoCost * fA[0][d1]*pB.nbSeq();
			break;
		}
		return gopCost;
	}
	

	
	public float costI2M(int di, int dj)
	{
		swapFAFB();
		float gopCost = costD2M(dj,di);
		swapFAFB();
		return gopCost;
		
	}
	
	public float costI2I(int di, int dj)
	{
		swapFAFB();
		float gopCost = costD2D(dj, di);
		swapFAFB();
		return gopCost;
	}
		
	public float costD2I(int di, int dj)
	{
		swapFAFB();
		float gopCost = costI2D(dj, di);
		swapFAFB();
		return gopCost;
	}
	
	public float costM2I(int di, int dj)
	{
		swapFAFB();
		float gopCost = costM2D(dj, di);
		swapFAFB();
		return gopCost;
	}
	
}
