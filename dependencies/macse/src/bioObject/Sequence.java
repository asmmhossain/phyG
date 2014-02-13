package bioObject;



import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;

import codesInterfaces.NT_AAsymbols;

import utils.Named;


public class Sequence implements Named{
	protected String seq;
	protected String name;
	protected String realFullName;
	public Sequence(String name,String seq) {
		this.name = name;
		this.seq = seq.toUpperCase();
		this.realFullName=name;
	}
	
	public static String removeGap(String seq)
	{
		return seq.replaceAll(""+NT_AAsymbols.NT_GAP, "");
	}
	public static String removeFS(String seq)
	{
		return seq.replaceAll(""+NT_AAsymbols.NT_FS, "");
	}
	
	public static Hashtable<String, String> collectSequences(String fastaFile, boolean removeGap) throws IOException {
	    String line;
	    StringBuffer seq = new StringBuffer("");
	    String seqId = "";

	    boolean first = true;
	    BufferedReader br = new BufferedReader(new FileReader(fastaFile));

	    Hashtable <String,String>seqInfo = new Hashtable<String,String>();

	     while (((line = br.readLine()) != null)) {
	        if (!line.isEmpty()) {
	            if (line.charAt(0) == '>') {
	                    line = line.substring(1); // remove ">"
	                
	                if(first == false)
	                    {
	                	if (removeGap)
	                		seqInfo.put(seqId, seq.toString().replaceAll("-", ""));
	                	else
	                		seqInfo.put(seqId, seq.toString());
	                    seqId = "";
	                    seq = new StringBuffer("");
	                    seqId = line;
	                    }
	                else{
	                    seqId = line;
	                    first=false;
	                    }
	                }
	            else{
	                seq.append(line);
	            }
	        }
	     }
	    if(!seq.equals("")) 
	    {
	    	if (removeGap)
	    		seqInfo.put(seqId, seq.toString().replaceAll("-", ""));
	    	else
	    		seqInfo.put(seqId, seq.toString());
	    }
	    br.close();
	    return seqInfo;
	    }

	
	
	public String toFasta()
	{
		return ">"+getRealFullName()+"\n"+getSeq();
	}
	
	public void setNames(String practicalName, String realFullName)
	{
		this.name=practicalName;
		this.realFullName = realFullName;
	}
	public String getSeq()
	{
		return seq;
	}
	
	public String getRealFullName()
	{
		return realFullName;
	}
	
	public String getName()
	{
		return name;
	}
	public final int length()
	{
		return seq.length();
	}
	public final char get(int i)
	{
		return seq.charAt(i);
	}

	
}
