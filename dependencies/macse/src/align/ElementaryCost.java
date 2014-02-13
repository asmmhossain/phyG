package align;
import codesInterfaces.NT_AAsymbols;
import codesInterfaces.CostCode;


public class ElementaryCost {	
	
	private float [] cost;
	private float [][] substitutionCost;
	private SubstitutionScore a2aScore;
	
	public ElementaryCost(float fs, float gap_e, float gap_o, float gap_c, float stop, float beg_end_gap_factor, float opt_pess_gap_factor) {
		this.cost = new float[CostCode.nbCosts];
		this.cost[CostCode.FS]= fs;
		this.cost[CostCode.GAP_E]= gap_e;
		this.cost[CostCode.GAP_O]= gap_o;
		this.cost[CostCode.GAP_C]= gap_c;
		this.cost[CostCode.STOP]= stop;
		this.cost[CostCode.BEG_END_GAP_FACTOR]= beg_end_gap_factor;
		this.cost[CostCode.OPT_PESS_GAP_FACTOR]= opt_pess_gap_factor;
		
		
		a2aScore = SubstitutionScore.factory();
		substitutionCost = new float [NT_AAsymbols.aaSymbolTab.length][NT_AAsymbols.aaSymbolTab.length];
		for (int i=0; i<NT_AAsymbols.aaSymbolTab.length;i++)
		{
			for (int j=i; j<NT_AAsymbols.aaSymbolTab.length;j++)
			{	
				substitutionCost[i][j]=substitutionCost[j][i]= computeScore(i,j, this,this);
				
			}
		}
		
		
	}
	
	
	public final float get(int type)
	{
		return cost[type];
	}
	
	public final float getSubstScore(int aa1, int aa2)
	{
		if(aa1<NT_AAsymbols.NB_AA_cost_indep && aa2<NT_AAsymbols.NB_AA_cost_indep)
			return substitutionCost[aa1][aa2];
		else{
			System.err.println("bug getSubstScore is called withouth precising elementary cost with "+aa1+" "+aa2);
			System.exit(1);
		}
		return 0;
	}
	
	public final float getSubstScore(int aa1, int aa2, ElementaryCost cost2)
	{
		if(aa2<NT_AAsymbols.NB_AA_cost_indep || cost2==this)
			return substitutionCost[aa1][aa2];
		else if (aa1<NT_AAsymbols.NB_AA_cost_indep)
			return cost2.getSubstScore(aa2, aa1, this);
		else
			return computeScore(aa1, aa2, this, cost2);
	}
	
	private  static float computeScore(int aa1, int aa2, ElementaryCost cost1, ElementaryCost cost2)
	{
		float score=0;
		int nbAA=0;
		
		switch (aa1) {
		case NT_AAsymbols.AA_STOP_int: 
			score += cost1.get(CostCode.STOP);	
			break;
		case NT_AAsymbols.AA_STOPE_int: 
			score += 0;	
			break;
		case NT_AAsymbols.AA_FS_int: 
			score += cost1.get(CostCode.FS);	
			break;
		case NT_AAsymbols.AA_FSE_int: 
			score += cost1.get(CostCode.FS) * CostCode.FSE_FACTOR;	
			break;
		case NT_AAsymbols.AA_GAP_int: 
			if(NT_AAsymbols.aaSymbolTab[aa2] != NT_AAsymbols.AA_GAP && aa2 != NT_AAsymbols.AA_UKN_int )
				score += cost1.get(CostCode.GAP_E);	
			break;
		case NT_AAsymbols.AA_GAPE_int: 
			if(NT_AAsymbols.aaSymbolTab[aa2] != NT_AAsymbols.AA_GAP && aa2 != NT_AAsymbols.AA_UKN_int )
				score += cost1.get(CostCode.GAP_E)*cost1.get(CostCode.BEG_END_GAP_FACTOR);	
			break;
		case NT_AAsymbols.AA_UKN_int:
			break;
		default:
			nbAA++;
			break;
		}
		
		switch (aa2) {
		case NT_AAsymbols.AA_STOP_int: 
			score += cost2.get(CostCode.STOP);	
			break;
		case NT_AAsymbols.AA_STOPE_int: 
			score += 0;	
			break;
		case NT_AAsymbols.AA_FS_int: 
			score += cost2.get(CostCode.FS);	
			break;
		case NT_AAsymbols.AA_FSE_int: 
			score += cost1.get(CostCode.FS) * CostCode.FSE_FACTOR;	
			break;
		case NT_AAsymbols.AA_GAP_int: 
			if(NT_AAsymbols.aaSymbolTab[aa1] != NT_AAsymbols.AA_GAP && aa1 != NT_AAsymbols.AA_UKN_int )
				score += cost2.get(CostCode.GAP_E);	
			break;
		case NT_AAsymbols.AA_GAPE_int: 
			if(NT_AAsymbols.aaSymbolTab[aa1] != NT_AAsymbols.AA_GAP && aa1 != NT_AAsymbols.AA_UKN_int )
				score += cost2.get(CostCode.GAP_E)*cost2.get(CostCode.BEG_END_GAP_FACTOR);	
			break;
		case NT_AAsymbols.AA_UKN_int:
			break;
		default:
			nbAA++;
			break;
		}
			
		if(nbAA==2)
			score = cost1.a2aScore.probAtoB_MA(NT_AAsymbols.aaSymbolTab[aa1],NT_AAsymbols.aaSymbolTab[aa2] );	
		
		return score;
	}
	
	
	
}
