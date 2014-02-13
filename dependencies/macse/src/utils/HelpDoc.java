package utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Hashtable;

import bioObject.Sequence;

public class HelpDoc {

	private Hashtable<String, String> headers;
	private Hashtable<String, String> footers;

	public String getFooter(String prog)
	{
		String res="";
		if (footers.containsKey(prog))
			res= footers.get(prog);
		return res;
	}
	
	public String getHeader(String prog)
	{
		String res="";
		//System.out.println("search header for "+ prog);
		if (headers.containsKey(prog))
			res= headers.get(prog);
		return res;
	}
	
	public HelpDoc() {
		// TODO Auto-generated constructor stub
		headers = new Hashtable<String, String>();
		footers = new Hashtable<String, String>();
		Sequence r = new Sequence("toto","AA");
		InputStream ips = r.getClass().getClassLoader().getResourceAsStream("utils/help.txt");
		try {
			String line;
			StringBuffer help_msg=null;
			String prog=null;
			String type=null;
			String [] infos;
			BufferedReader br=new BufferedReader(new InputStreamReader(ips,"utf-16"));
			while ((line=br.readLine())!=null ){
				if (line.startsWith("###"))
				{	

					if(help_msg != null)
					{
						if(type.equals("header"))
							headers.put(prog, help_msg.toString());
						else 
							footers.put(prog, help_msg.toString());
					
					}
					infos = line.split("@");
					prog = infos[2];
					type = infos[1];
					help_msg=new StringBuffer();
				}
				else
				{
					help_msg.append("\n"+line);
				}
			}

			if(help_msg != null)
			{
				if(type.equals("header"))
					headers.put(prog, help_msg.toString());
				else 
					footers.put(prog, help_msg.toString());
			}
			

		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}



}


