package codesInterfaces;

public interface NT_AAsymbols {
	
	static char[] aaSymbolTab = {'A','R','N','D','C','Q','E','G','H','I','L','K','M','F','P','S','T','V','W','Y','?','-','-','*','*','!','!'};// all permitted AA symbols (two type ofGap) inside or outside seq
	static char[] realAASymbolTab = {'A','R','N','D','C','Q','E','G','H','I','L','K','M','F','P','S','T','V','W','Y'};// all real permitted amino acids
	
	
	static int AA_UKN_int=20;
	static int AA_GAP_int=21;
	static int AA_GAPE_int=22; // externalGap
	static int AA_STOPE_int=23;// stop at the end of the sequence
	static int NB_AA_cost_indep = 21; // Nb of symbol  for which substitution are independent of the sequence and site position
	
	static int AA_STOP_int=24; // 2 cost that are sequence dependent
	static int AA_FS_int=25;
	static int AA_FSE_int=26;
	
	
	
	
	static char AA_STOP='*';
	static char AA_UKN='?';
	static char AA_GAP='-';
	static char AA_FS='!';
	
	static char NT_GAP='-';
	static char NT_FS='!';
	static char NT_UKN='N';
}
