package align;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.tree.DefaultMutableTreeNode;

import bioObject.CodingDnaSeq;

import utils.Named;

class Test implements Named
{
	String name;
	Test(String name)
	{
		this.name=name;
	}
	public String getName() {
		return name;
	}
	
}

class Id_val implements Comparable <Id_val>
{
	int id;
	double val;
	public double getVal()
	{
		return val;
	}
	public int getId()
	{
		return this.id;
	}
	public Id_val(int id_1, double val_1)
	{
		this.id = id_1;
		this.val=val_1;
	}
	
	public int compareTo(Id_val v2) {
		// TODO Auto-generated method stub
		return Double.compare(this.getVal(), v2.getVal());
	}
	
}

public class UPGMA {

	public static Vector<Id_val> getClosestNeighbors(float [][] dist, int [] remainingId, int curId, int nbMax)
	{
		Vector<Id_val> neigbors = new Vector<Id_val>();
		for (int i = 0; i < remainingId.length; i++) {			
				neigbors.add(new Id_val(i,dist[remainingId[curId]][remainingId[i]]));
		}
		Collections.sort(neigbors, Collections.reverseOrder());
		neigbors.setSize(Math.min(neigbors.size(), nbMax));
		return neigbors;
	}

	public static float sumDist(Vector<Id_val> neigbors)
	{
		float res=0;
		for (int i = 0; i < neigbors.size(); i++) {
			res += neigbors.get(i).getVal();
		}
		return res;
	}
	
	public static Vector<ArrayList<CodingDnaSeq>> getUPGMAmax (float [][] dist, ArrayList<? extends Named> elementToBeClustered, int maxTreeSize)
	{
		Vector<ArrayList<CodingDnaSeq>> subProblems = new Vector<ArrayList<CodingDnaSeq>>();
		Hashtable<String , Integer> availableElt = new Hashtable<String , Integer> ();
		int id=0;
		for (Named elt : elementToBeClustered) {
			availableElt.put(elt.getName(), id);
			id++;
		}
		while ( availableElt.isEmpty() == false)
		{
			int [] remainingId = new int[availableElt.size()];
			id=0;
			for (Integer idI : availableElt.values()) {
				remainingId[id++]= idI;
			}
			// find central sequence
			int centralId=-1;
			double minSum = Double.MAX_VALUE;
			//System.out.println("remainingId:"+ remainingId.length);
			for (int i = 0; i < remainingId.length; i++) {
				double sum = sumDist(getClosestNeighbors(dist, remainingId, i,maxTreeSize));
				//System.out.println("test "+i+" sum:"+sum);
				if(sum < minSum)
					centralId = i;
			}
			// extract maxSubPbSize closest neighbors
			Vector<Id_val> neigbors = getClosestNeighbors(dist, remainingId, centralId,maxTreeSize);
			//System.out.println(neigbors.size() +" "+availableElt.size());
			// build corresponding subProblems
			ArrayList<CodingDnaSeq> subSeq = new ArrayList<CodingDnaSeq>();
			for (Id_val neigId_val : neigbors) {
				subSeq.add((CodingDnaSeq)elementToBeClustered.get(remainingId[neigId_val.getId()]));
			}
			//System.out.("add subPb");
			subProblems.add(subSeq);
			// remove used elt
			for (Id_val neigId_val : neigbors) {
			availableElt.remove(elementToBeClustered.get(remainingId[neigId_val.getId()]).getName());	
			}
		}
		return subProblems;
	}
	float [][] dist;
	ArrayList<? extends Named> elementToBeClustered;
	int [] order;
	int nbUsed;
	DefaultMutableTreeNode nodes [];
	int minI,minJ;
	
	public static String normalizedClusterLabel(String lab1, String lab2)
	{
		StringBuffer b=new StringBuffer("(");
		if (lab1.compareTo(lab2)<0)
		{
			b.append(lab1);
			b.append(',');
			b.append(lab2);
		}
		else
		{
			b.append(lab2);
			b.append(',');
			b.append(lab1);
		}
		b.append(')');
		return b.toString();
	}
	public UPGMA(float[][] dist, ArrayList<? extends Named>elt) {
		// TODO Auto-generated constructor stub
		this.dist = dist;
		this.elementToBeClustered = elt;
		nodes = new DefaultMutableTreeNode[elt.size()];
		order = new int[elt.size()];
		for (int i = 0; i < elt.size(); i++) {
			nodes[i]= new DefaultMutableTreeNode(elt.get(i).getName());
			order[i]=i;
		}
		nbUsed = elt.size();
	}
	
	private void findMin()
	{
		this.minI=0; this.minJ=1;
		double valMin= dist[minI][minJ];
		for (int i = 0; i < nbUsed; i++) {
			for (int j = i+1; j < nbUsed; j++) {
				if(dist[order[i]][order[j]]<valMin)
				{
					minI=i;
					minJ=j;
					valMin=dist[order[i]][order[j]];
				}
			}
		}
		if(minI>minJ)
		{
			int tmpI=minI;
			minI=minJ;
			minJ=tmpI;
		}
	}
	
	protected float computeDist(float d1, float d2)
	{
		return (d1 + d2)/2.f;
	}
	
	private void createNextCluster()
	{
		
		this.findMin();
		//create the new Node with canonical string
		String newLabel=UPGMA.normalizedClusterLabel(nodes[minI].getUserObject().toString(), nodes[minJ].getUserObject().toString());
		//System.out.println("create clust "+newLabel);
		DefaultMutableTreeNode n = new DefaultMutableTreeNode(newLabel);
		n.add(nodes[minI]);
		n.add(nodes[minJ]);
		nodes[minI] = n;
		// compute New Distance
		for (int k = 0; k < nbUsed; k++) {
			if( k!=minI && k != minJ)
			{
				dist[order[k]][order[minI]]= dist[order[minI]][order[k]]= computeDist(dist[order[k]][order[minI]],dist[order[k]][order[minJ]]);	
			}
			
		}
		// update vectors
		order[minJ]= order[nbUsed-1];
		nodes[minJ]=nodes[nbUsed-1];
		nbUsed --;
	}
	
	public DefaultMutableTreeNode buildTree()
	{
		while(nbUsed >1)
		{
			createNextCluster();
		}
		//System.out.println(nodes[0].getUserObject().toString());
		return nodes[0];
	}
	
	public static void main(String[] args) {
		// test from http://www.nmsr.org/upgma.htm
		// mais ils ont une erreur dans leurs moyennes :-(
		float [][]dist = {
				{0,19,27,8,33,18,13},
				{19,0,31,18,36,1,13},
				{27,31,0,26,41,32,29},
				{8,18,26,0,31,17,14},
				{33,36,41,31,0,35,28},
				{18,1,32,17,35,0,12},
				{13,13,29,14,28,12,0}};
				
		String [] seq = {"A","B","C","D","E","F","G"};
		ArrayList<Test> sequences = new ArrayList<Test>();
		for (int i = 0; i < seq.length; i++) {
			sequences.add(new Test(seq[i]));
		}
		UPGMA dm = new UPGMA(dist,sequences);
		dm.buildTree();
		
	}

	public static Vector<String> getSubtrees(DefaultMutableTreeNode node) {
		Vector<String> subTrees= new Vector<String>();
		Enumeration<DefaultMutableTreeNode> nodes = node.postorderEnumeration();
		while(nodes.hasMoreElements())
		{
			DefaultMutableTreeNode n = nodes.nextElement();
			subTrees.add(n.getUserObject().toString());
		}
		return subTrees;
	}
	
	
	
	public static Vector<String> getSubClades(DefaultMutableTreeNode node) {
		Vector<String> subTrees= getSubtrees(node);
		Vector<String> subClades = new Vector<String>();
		
		String tree = node.getUserObject().toString();
		tree= tree.replaceAll("\\(", "");
		tree= tree.replaceAll("\\)", "");
		String[]taxa = tree.split(",");
		
		for (int i = 0; i < subTrees.size(); i++) {
			subClades.add(subTree2clade(subTrees.get(i),taxa));
		}
		return subClades;
	}
	
	public static String subTree2clade(String subtree, String[] all_taxa)
	{
		String subtree1= subtree.replaceAll("\\(", "");
		subtree1= subtree1.replaceAll("\\)", "");
		String[]seqProf1 = subtree1.split(",");
		Vector<String> taxa = new Vector<String>(Arrays.asList(seqProf1));
		
		if(seqProf1.length > all_taxa.length-seqProf1.length)
		{
			HashSet<String> taxaProf1 = new HashSet<String>(Arrays.asList(seqProf1));
			Vector<String>seqProf1_bis = new Vector<String>(all_taxa.length-seqProf1.length);
			
			for (String taxon : all_taxa) {
				if(! taxaProf1.contains(taxon))
					seqProf1_bis.add(taxon);
				
			}
			taxa = seqProf1_bis;
		}
		
		
		Collections.sort(taxa);
		
		
		StringBuffer res = new StringBuffer();
		res.append("(");
		for (int i = 0; i < taxa.size(); i++) {
			if(i>0)
				res.append(",");
			res.append(taxa.get(i));
			//System.out.println(taxa.get(i));
		}
		res.append(")");
		return res.toString();
	}
}
		
		
		
