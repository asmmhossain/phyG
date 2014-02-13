package utils;

import java.text.DecimalFormat;
import java.util.ArrayList;


import bioObject.Sequence;

/**
 * Static Object used to display informations
 * @author HARISPE SEBASTIEN (ISEM work placement)
 *
 */
public  class Out {

	/**
	 * To display an error during application execution
	 * Stop execution show USAGE and error
	 * @param error
	 */
	public static void ERROR_MSA(String error) {
		System.err.println(error);
		//USAGE_MSA();
	}


}
