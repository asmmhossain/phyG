package codesInterfaces;

public interface DimerCodes {
	public static int d00=0;// 0==a gap   1==not a gap
	public static int d01=1;
	public static int d10=2;
	public static int d11=3;
	public static int d1 =4; // non gap
	public static int d0 =5; // gap
	public static int d0E =6; // gap at seq extremities
	
//	public static int dX =6; //useless always equals to the number of seq (save memory)
	public static int nbDimerCode = 7;
}
