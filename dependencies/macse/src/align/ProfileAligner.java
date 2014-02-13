package align;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;

import bioObject.CodingDnaSeq;
import bioObject.Ribosome;

import codesInterfaces.CostCode;
import codesInterfaces.DimerCodes;
import codesInterfaces.MatName;
import codesInterfaces.NT_AAsymbols;


class MatrixPosition
{
	int mat;
	int i;
	int j;
	double score;
	

	public MatrixPosition() {	
	}

	@Override
	public String toString() {
		return "mat: "+MatName.names[mat]+" i: "+i+" j:"+j+" score:"+score;
	}
	public void update(int mat, int i,int j,double score)
	{
		this.mat= mat;
		this.i=i;
		this.j=j;
		this.score = score;
	}
}


public class ProfileAligner {

	short bestMvt[][];
	boolean swapProfiles;
	double scores[][][];
	ElementaryCost cost;
	Profile p1,p2;
	String templateP1, templateP2;
	MatrixPosition bestMove;
	GapOpenCost gapOpen;
	public static double duree=0;
	public static double duree_match=0;
	public static double duree_ins=0;
	
	// encode mvt using MMJJ MMII MMIIJJ
	private short encode(MatrixPosition mvt, int currentMat)
	{
		int bestMvt=0;
		bestMvt = (short)bestMove.mat; // source matrix 0 1 or 2 (2 bits)
		bestMvt <<= 2;
		
		switch (currentMat) {
		case MatName.M:
			bestMvt |= bestMove.i; // di 0 1 2 or 3 (2 bits)
			bestMvt <<= 2;
			bestMvt |= bestMove.j; // dj 0 1 2 or 3 (2bits)
			break;
		case MatName.I:
			bestMvt |= bestMove.i; // dj 0 1 2 or 3 (2bits)
			bestMvt<<=6; // 6 first bits are for the M matrix
			break;
		case MatName.D:
			bestMvt |= bestMove.j; // di 0 1 2 or 3 (2 bits)
			bestMvt <<=10;
			break;
		}
		return (short)bestMvt;
	}
	// 1 2 || 4 8 || 16 32||  
	private void decode(short mvt, int i, int j, int currentMat)
	{
		int di=0,dj=0;
		int m = -1;
		int mvtTmp;
		
		switch (currentMat) {
		case MatName.M:
			dj = mvt & 3; // 3 gives the 11 mask needed to get dj (2^0 + 2^1)
			di = (mvt & 12)>>2; // 3 gives the 1100 mask needed to get dj (2^2 + 2^3)
			m = (mvt & 48)>>4; // 3 gives the 110000 mask needed to get dj (2^4 + 2^5)
			break;
		case MatName.I:
			mvtTmp = (mvt>>6); // shift to get information on lower bits
			di = mvtTmp & 3; 
			m = (mvtTmp & 12)>>2;
			break;
		case MatName.D:
			mvtTmp = (mvt>>10); // shift to get information on lower bits
			dj = mvtTmp & 3; 
			m = (mvtTmp & 12)>>2;
			break;
		}
		
		bestMove.i = i - di;
		bestMove.j = j - dj;
		bestMove.mat = m;
		
	}
	
	public ProfileAligner(int lgProf, ElementaryCost cost) {
		
		scores = new double [4][lgProf][3];
		bestMvt = new short [lgProf][lgProf];
		this.cost = cost; 
		bestMove = new MatrixPosition();
		gapOpen = new GapOpenCost(cost);
	
	}

	public void afficheAllMat(boolean FS, int max)
	{
		System.out.println("\n\n"+MatName.names[0]);
		
		afficheMat(0,FS,max);
		System.out.println("\n\n"+MatName.names[1]);
		afficheMat(1,FS,max);
		System.out.println("\n\n"+MatName.names[2]);
		afficheMat(2,FS,max);
	}
	
	public void afficheMat(int mat, boolean FS, int max)
	{
		int di,dj;
		if(FS)
		{
			di=1; dj=1;
		}
		else
		{
			di=3; dj=3;
		}
		
		for (int i = 0; i < 4; i+=di) {
			System.out.print("i:"+i+"\t");
			for (int j = 0; j < Math.min(max,dim2()); j+=dj) {
				System.out.print(getScore(mat, i, j)+"\t");
			}
			System.out.println();
		}
	}
	
	
	public ArrayList<String> alignProfiles_2profile(Profile p1, Profile p2)
	{
		ArrayList<String> templates = alignProfiles(p1, p2);
		return templates;
		//return new Profile(UPGMA.normalizedClusterLabel(p1.getName(), p2.getName()), alignSet);
	}
	
	public ArrayList<String> alignProfiles(Profile p1_param, Profile p2_param)
	{
		//System.out.println("\n"+ duree +"\t"+duree_match);
		double t1 = System.currentTimeMillis();
		if(p1_param.nbSites()>= p2_param.nbSites())
		{
			this.p1 = p1_param;
			this.p2 = p2_param;
			swapProfiles = false;
		}
		else
		{
			this.p1=p2_param;
			this.p2=p1_param;
			swapProfiles = true;
		}

		adjustScoreMatrix();
		updateScores();
		
		
		ArrayList<String> res = backTrackTemplate();
	//	Profile p = new Profile(UPGMA.normalizedClusterLabel(p1.getName(), p2.getName()), res);
		//p.affiche();
		//System.out.println("fin backtrack\n\n");
		double t2 = System.currentTimeMillis();
		ProfileAligner.duree += (t2-t1);
		//System.out.println("\n"+ duree +"\t"+duree_match + "\t"+duree_ins);
		return res;
	}
	

	public  double scoreM(int siteP1,  int siteP2)
	{
		double score = this.p1.getCharFreq(siteP1).computeIndepCost(this.p2.getCharFreq(siteP2));
		
		//handle gap extension
		score += p1.dimerFreq[siteP1][DimerCodes.d1] * p2.getGapCost(siteP2);
		score += p2.dimerFreq[siteP2][DimerCodes.d1] * p1.getGapCost(siteP1);
		
		// handle ! et *  
		score += p1.getInternalCost(siteP1) * (p1.nbSeq() + p2.nbSeq() -1);
		score += p2.getInternalCost(siteP2) * (p1.nbSeq() + p2.nbSeq() -1);
		
		return score;
	}
	
	private final double getScore(int m, int di, int j)
	{
		if(j<0)
			return pseudoNegInf(); 
		else
			return scores[di][j][m];
	}

	
	private final double pseudoNegInf()
	{
		return Double.NEGATIVE_INFINITY;// - p1.length() * p2.length()*3* cost.getMinCost(); // to avoid overflow when adding extra cost (fs or stop)
	}
	
	// find the best of the 9 possible moves toward the I matrix

	private short findBestMove2I(int i, int j)
	{
		return findBestMove2I(i, j, false);
	}
	
	private short findBestMove2I(int i, int j, boolean debug)
	{
		double possScore;
		bestMove.score=pseudoNegInf();
		int dj=0;
	
		for(int mat=0;mat<MatName.names.length; mat++)
		{
			for( int di=1; di<=3; di++)
			{
				possScore = getScore(mat,di,j-dj);
				if(possScore <= bestMove.score) // from now score can only decrease
					continue;
				switch (di) {
				case 1: case 2: // ! -
					possScore += p1.getInsertFSCost(i, di)* (p1.nbSeq() + p2.nbSeq() -1) + p2.getInsertGapCost(j)*p1.nbSeq(); 
					break;
				case 3: // X -		
					possScore += p2.getInsertGapCost(j) *p1.dimerFreq[i][DimerCodes.d1];
					possScore += p1.getInternalCost(i)* (p1.nbSeq() + p2.nbSeq() -1);				
					break;
				default:
					break;
				}
				if(possScore <= bestMove.score)
					continue;
				double gapOpenCost=0;
				
				switch (mat) {
				case MatName.D:
					gapOpenCost += gapOpen.costD2I(di,dj);
					break;
				case MatName.I:		
					gapOpenCost += gapOpen.costI2I(di,dj);
					break;
				case MatName.M:
					gapOpenCost += gapOpen.costM2I(di,dj);
					break;
				}
				
				//if(i==3 && j==0)
				//	System.out.println("mat "+mat+" gop"+gapOpenCost+" possScore"+possScore);
				//if(j<=3 || j>= (dim2()-1)-3)
				if(j<=2 || j>= (dim2()-1)-2)
					gapOpenCost *= cost.get(CostCode.BEG_END_GAP_FACTOR); 
				//if(i==3 && j==0)
				//	System.out.println("mat "+mat+" gop"+gapOpenCost);
				//System.out.println(gapOpenCost);
				
				possScore += gapOpenCost;
				if(possScore >= bestMove.score)
				{
					//if(i==3&& j==0)
					//	System.out.println("best move "+mat+" di:"+di+" dj:"+dj+" "+possScore);
					bestMove.update(mat,di,dj,possScore);
				}

			}
		}
		//System.out.println(bestMove);
		//pBinShort("encoded as", encode (bestMove, MatName.I));
		//System.out.println();
		return encode (bestMove, MatName.I);
	}

	private short findBestMove2D(int i, int j)
	{
		return findBestMove2D(i, j, false);
	}
			
	// find the best of the 9 possible moves toward the D matrix
	private  short findBestMove2D(int i, int j, boolean debug)
	{
		double t1 = System.currentTimeMillis();
		double possScore;
		bestMove.score=pseudoNegInf();
		int di=0;
		for(int mat=0;mat<MatName.names.length; mat++)
		{
			for( int dj=1; dj<=3; dj++)
			{
				possScore = getScore(mat,di,j-dj);
				if(possScore <= bestMove.score)
					continue;
				switch (dj) {
				case 1: case 2: // - !
					possScore += p2.getInsertFSCost(j, dj)*(p1.nbSeq() + p2.nbSeq() -1) + p1.getInsertGapCost(i)*p2.nbSeq(); 
					break;
				case 3: // - X
					possScore += p1.getInsertGapCost(i) *p2.dimerFreq[j][DimerCodes.d1];
					possScore += p2.getInternalCost(j)* (p1.nbSeq() + p2.nbSeq() -1);	
					break;
				default:
					break;
				}
				if(possScore <= bestMove.score)
					continue;
				double gapOpenCost=0;
				switch (mat) {
				case MatName.D:
					gapOpenCost += gapOpen.costD2D(di,dj);
					break;
				case MatName.I:
					gapOpenCost += gapOpen.costI2D(di,dj);
					break;
				case MatName.M:
					gapOpenCost += gapOpen.costM2D(di,dj);
					break;
				}
				//bug > dim
				//if(i<=3 || i<=(dim1()-1)-3)
				if(i<=2 || i>=(dim1()-1)-2)
					gapOpenCost *= cost.get(CostCode.BEG_END_GAP_FACTOR); 	
				
				possScore += gapOpenCost ;
				
				if(possScore >= bestMove.score)
				{	
					bestMove.update(mat,di,dj,possScore);
				}

			}
		}
		//System.out.println(bestMove);
		//pBinShort("encoded as", encode (bestMove, MatName.D));
		//System.out.println();
		double t2=System.currentTimeMillis();
		duree_ins += (t2-t1);
		return encode(bestMove, MatName.D);

	}

	// find the best of the 27 possible moves toward the M matrix
	
	
	
	
// find the best of the 27 possible moves toward the M matrix
	
	private short findBestMove2M(int i, int j)
	{
		double t1 = System.currentTimeMillis();
		double possScore;
		bestMove.score=pseudoNegInf();
		double matchScore = this.scoreM(i, j);
		boolean FS_FS =false;
		double t2 = System.currentTimeMillis();
		duree_match += (t2-t1);
		
		for(int mat=0;mat<MatName.names.length; mat++)
		{			
			for( int di=1; di<=3; di++)
			{
				for( int dj=1; dj<=3; dj++)
				{
					possScore = getScore(mat,di,j-dj);
					FS_FS =false;
					int mvt = GapOpenCost.mvtCode(di, dj);
					if(possScore <= bestMove.score && mvt !=33)
						continue;
				
					switch (mvt) {
					case 12: case 11: case 21: case 22:  // ! ! 
						possScore += (p1.getInsertFSCost(i, di) + p2.getInsertFSCost(j, dj))*(p1.nbSeq() + p2.nbSeq() -1);
						FS_FS =true;
						break;
					case 13: case 23: // ! X
						possScore += (p1.getInsertFSCost(i, di) + (p2.getInternalCost(j))) * (p1.nbSeq() + p2.nbSeq() -1)+p2.getGapCost(j)*p1.nbSeq();
						break;
					case 31: case 32: // X !
						possScore += (p2.getInsertFSCost(j, dj) + (p1.getInternalCost(i))) * (p1.nbSeq() + p2.nbSeq() -1)+ p1.getGapCost(i)*p2.nbSeq();
						break;
					case 33: // X X
						possScore += matchScore;
						break;
					}
					if(possScore <= bestMove.score)
						continue;
				
					double gapOpenCost=0f;
					if(! FS_FS)// for ! ! gapOpen is always equal to 0
					{
						switch (mat) {
						case MatName.D:
							gapOpenCost += gapOpen.costD2M(di,dj);
							break;
						case MatName.I:
							gapOpenCost += gapOpen.costI2M(di,dj);
							break;
						case MatName.M:
							gapOpenCost += gapOpen.costM2M(di,dj);
							break;
						}

						//bug >= dim
						//if(i<=(dim1()-1)-3 || j<= (dim2()-1)-3 || i<=3 || j<=3)// || i==dim1()-1)
						if(i>=(dim1()-1)-3 || j>= (dim2()-1)-3 || i<=3 || j<=3)// || i==dim1()-1)
							gapOpenCost *= cost.get(CostCode.BEG_END_GAP_FACTOR); 
						possScore += gapOpenCost;
					}
					
					if(possScore >= bestMove.score)
					{			
						bestMove.update(mat,di,dj,possScore);
					}

				}
			}
			
		}
		//System.out.println(bestMove);
		//pBinShort("encoded as", encode (bestMove, MatName.M));
		//System.out.println();
		
		return encode (bestMove, MatName.M);
		
		
	}

	public void updateScores()
	{
		for (int i = 0; i < scores.length; i++) {
			for (int j = 0;  j< scores[i].length; j++) {
				for (int m = 0;  m< scores[i][j].length; m++) {
					scores[i][j][m]=pseudoNegInf();
				}
			}
		}
		
		scores[0][0][MatName.M]=0; // seul match chaine vide X chaineVide a un sens le reste s'initalise ensuite correctement en fonction des dimers
		
		
		short bestMvt;
		
		for (int i = 0; i < dim1(); i++) {
			if(i>0)
				swapScoresRow();
			gapOpen.setSite1(p1, i);
			for (int j = 0; j < dim2(); j++) {
				if(i==0 && j==0)
					continue;
				else
				{	
					//System.out.println("update score for "+ i + " "+j);
					bestMvt = 0;
					gapOpen.setSite2(p2, j);
					bestMvt = findBestMove2M(i,j);
					scores[0][j][MatName.M]=bestMove.score;
					bestMvt = (short) (bestMvt | findBestMove2D(i,j));
					scores[0][j][MatName.D]=bestMove.score;
					bestMvt = (short) (bestMvt | findBestMove2I(i,j));
					scores[0][j][MatName.I]=bestMove.score; 	
				}
				//TestFasterProfileAligner.pBinShort("bestMvt "+i+" "+j+" :", bestMvt);
				this.bestMvt[i][j]= bestMvt;
				
			}
		}
		
		
	}

	static void pBinShort(String s, short i) {
	    System.out.println(
	      s + ", int: " + i + ", binary: ");
	    System.out.print("   ");
	    for(int j = 13; j >=0; j--)
	      if(((1 << j) &  i) != 0)
	        System.out.print("1");
	      else
	        System.out.print("0");
	    System.out.println();
	  }
	
	private void swapScoresRow() {
		double[][]tmp= scores[3];
		scores[3]= scores[2];
		scores[2]= scores[1];
		scores[1]= scores[0];
		scores[0] = tmp; // all values will be erased but it have to be a different vector than scores[1] !
		
	}
	
	public ArrayList<String> backTrackTemplate()
	{
		StringBuffer template1 = new StringBuffer("");
		StringBuffer template2 = new StringBuffer("");
		String codonTemplate[] = new String[4];
		codonTemplate[0]="---";
		codonTemplate[1]="N!!";
		codonTemplate[2]="NN!";
		codonTemplate[3]="NNN";
		
		
		MatrixPosition current = new MatrixPosition();
		current.update(0, -1, -1, pseudoNegInf());
		for(int mat =0; mat<3;mat++)
		{
			if(getScore(mat, 0, dim2()-1)>=current.score)
				current.update(mat, dim1()-1, dim2()-1, getScore(mat, 0, dim2()-1));
		}
		int di, dj;
		while(current.i!=0 || current.j!=0)
		{
			short mvt = bestMvt[current.i][current.j];
			decode(mvt, current.i, current.j, current.mat);
			di = current.i - bestMove.i;
			dj = current.j - bestMove.j;
			template1.append(codonTemplate[di]);
			template2.append(codonTemplate[dj]);
			current.update(bestMove.mat, bestMove.i, bestMove.j, bestMove.score);
		}
		ArrayList<String> res = new ArrayList<String>();
		templateP1 = template1.reverse().toString();
		templateP2 = template2.reverse().toString();
		if(swapProfiles)
		{
			res.add(templateP2);
			res.add(templateP1);	
		}
		else
		{
			res.add(templateP1);
			res.add(templateP2);			
		}
	
		return res;
	}
	
	
	/*
	public ArrayList<CodingDnaSeq> backTrackOld()
	{
		//Profile p = new Profile();
		ArrayList<StringBuffer> newStringsP1 = new ArrayList<StringBuffer>(p1.nbSeq());
		ArrayList<StringBuffer> newStringsP2 = new ArrayList<StringBuffer>(p2.nbSeq());

		//p1.affiche();
		//p2.affiche();
		for (int i = 0; i < p1.nbSeq(); i++) {
			newStringsP1.add(new StringBuffer());
		}
		
		for (int j = 0; j < p2.nbSeq(); j++) {
			newStringsP2.add(new StringBuffer());
		}

		MatrixPosition current = new MatrixPosition();
		current.update(0, -1, -1, pseudoNegInf());
		for(int mat =0; mat<3;mat++)
		{
			if(getScore(mat, 0, dim2()-1)>=current.score)
				current.update(mat, dim1()-1, dim2()-1, getScore(mat, 0, dim2()-1));
		}

		while(current.i!=0 || current.j!=0)
		{
			//gapOpen.setSite1(p1, current.i);
			//gapOpen.setSite2(p2, current.j);
			//System.out.println(current.i+" "+current.j+" "+MatName.names[current.mat]);
			short mvt = bestMvt[current.i][current.j];
			
			decode(mvt, current.i, current.j, current.mat);
			int nbCharInCodon =0;
			for (int site=current.i; site>bestMove.i;site--)
			{
				nbCharInCodon++;
				for (int seq = 0; seq < newStringsP1.size(); seq++) {
					newStringsP1.get(seq).append(p1.getChar(seq, site));
				}
			}	
			completeCodonWithFS(newStringsP1, nbCharInCodon);

			nbCharInCodon =0;
			for (int site=current.j; site>bestMove.j;site--)
			{
				nbCharInCodon++;
				for (int seq = 0; seq < newStringsP2.size(); seq++) {
					newStringsP2.get(seq).append(p2.getChar(seq, site));
				}
			}
			completeCodonWithFS(newStringsP2, nbCharInCodon);
			current.update(bestMove.mat, bestMove.i, bestMove.j, bestMove.score);
		}

		ArrayList<CodingDnaSeq> sequences= new ArrayList<CodingDnaSeq>();
		
		try {
			int seqNum=0;
			for (StringBuffer stringBuffer : newStringsP1) {
				String seq1=stringBuffer.reverse().toString();
				//seq1 =CodingDnaSeq.removeUselessFS(seq1);
				seq1 = CodingDnaSeq.correctFS(seq1);
				sequences.add(new CodingDnaSeq(seq1,p1.getSeq(seqNum++)));	
			}
			seqNum=0;
			for (StringBuffer stringBuffer : newStringsP2) {
				String seq2=stringBuffer.reverse().toString();
				//seq2 =CodingDnaSeq.removeUselessFS(seq2);
				seq2 = CodingDnaSeq.correctFS(seq2);
				sequences.add( new CodingDnaSeq( seq2,p2.getSeq(seqNum++)));	
			}
		} catch (Exception e) {
			System.out.println("exception in backtrack");
			e.printStackTrace();
			System.exit(1);
		}
		
		for (CodingDnaSeq codingDnaSeq : sequences) {
			//System.out.println(codingDnaSeq.getSeq());
		}
		//System.out.println();
		//System.out.println("nbSeq"+sequences.size());
		return sequences;
		//return  new Profile(UPGMA.normalizedClusterLabel(p1.getName(), p2.getName()),sequences);
	}
*/
	public ArrayList<CodingDnaSeq> backTrack()
	{
	
		//Profile p = new Profile();
		ArrayList<CodingDnaSeq> sequences= new ArrayList<CodingDnaSeq>();
		
		try {
			int seqNum;
			for ( seqNum=0 ; seqNum<p1.nbSeq(); seqNum++) {
				CodingDnaSeq s1 = p1.getSeq(seqNum);
				int pos=0;
				StringBuffer sb1 = new StringBuffer();
				for(int cPos=0; cPos<templateP1.length();cPos++)
				{
					if(templateP1.charAt(cPos)=='N')
						sb1.append(s1.getDNA(pos++));
					else
						sb1.append('-');
				}
				String seq1 = CodingDnaSeq.correctFS(sb1.toString());
				sequences.add(new CodingDnaSeq(seq1,p1.getSeq(seqNum)));	
			}
			
			for ( seqNum=0 ; seqNum<p2.nbSeq(); seqNum++) {
				CodingDnaSeq s2 = p2.getSeq(seqNum);
				int pos=0;
				StringBuffer sb2 = new StringBuffer();
				for(int cPos=0; cPos<templateP2.length();cPos++)
				{
					if(templateP2.charAt(cPos)=='N')
						sb2.append(s2.getDNA(pos++));
					else
						sb2.append('-');
				}
				String seq2 = CodingDnaSeq.correctFS(sb2.toString());
				sequences.add(new CodingDnaSeq(seq2,p2.getSeq(seqNum)));	
			}
			
			
			
		} catch (Exception e) {
			System.out.println("exception in backtrack");
			e.printStackTrace();
			System.exit(1);
		}
		
		return sequences;
		//return  new Profile(UPGMA.normalizedClusterLabel(p1.getName(), p2.getName()),sequences);
	}

	
	private final int dim1()
	{
		return p1.nbSites();
	}
	private final int dim2()
	{
		return p2.nbSites();
	}

	
	private void adjustScoreMatrix()
	{
		boolean change = dim1()>bestMvt.length || dim2()>bestMvt[0].length;
		if (change)
		{
			bestMvt = new short [dim1()][Math.max(dim2(), scores[0].length )];	
			scores = new double [4][Math.max(dim2(), scores[0].length )][3];
		}
		
		if(change || !change)
		{
			//System.out.println("adjuste matrix " + bestMvt.length +" "+bestMvt[0].length);
		}
		
		
	}
	
	public static void main(String[] args) {
		short[][] mvt= new short [2100][2100];
		while(true)
		{
		for (int i = 0; i < mvt.length; i++) {
			for (int j = 0; j < mvt[i].length; j++) {
				mvt[i][j]=(short) ((i+j)%10);
			}
		}
		}
	}
	
	

}










