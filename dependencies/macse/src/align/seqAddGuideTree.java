package align;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.tree.DefaultMutableTreeNode;

import utils.Named;

public class seqAddGuideTree {

	class namedEldWithDist implements Comparable<namedEldWithDist> {
		Named elt;
		float value;
		int matId;

		public namedEldWithDist(Named elt, float val) {
			// TODO Auto-generated constructor stub
			this.elt = elt;
			this.value = val;
		}
		public namedEldWithDist(Named elt, float val, int matId) {
			// TODO Auto-generated constructor stub
			this.elt = elt;
			this.value = val;
			this.matId = matId;
		}
		public float getValue() {
			return value;
		}
		
		public void setValue(float v) {
			this.value=v;
		}

		public Named getNamedElt() {
			return elt;
		}

		public int compareTo(namedEldWithDist o) {
			// TODO Auto-generated method stub
			return Double.compare(value, o.getValue());
		}

	}

	float dist[][];
	ArrayList<? extends Named> allElt;
	ArrayList<? extends Named> initialElt;

	public seqAddGuideTree(float[][] dist, ArrayList<? extends Named> allElt,
			ArrayList<? extends Named> initialElt) {
		this.dist = dist;
		this.allElt = allElt;
		this.initialElt = initialElt;

	}
	
	private DefaultMutableTreeNode buildInitialEltTree()
	{
		
		HashSet<String> initial_names = new HashSet<String>();
		ArrayList<Named> initial_namesV = new ArrayList<Named>();
		for (Named namedElt : initialElt) {
			initial_names.add(namedElt.getName());
			initial_namesV.add(namedElt);
		}
		
		Hashtable<String, Integer> initialId = new Hashtable<String, Integer>();
		int initId=0;
		for (Named namedElt : allElt) {
				if (initial_names.contains(namedElt.getName())) {
				initialId.put(namedElt.getName(), new Integer(initId));
				initId++;
				}
		}
		
		float distInit[][]= new float[initial_names.size()][initial_names.size()];
		for (int i = 0; i < initial_namesV.size(); i++) {
			for (int j = 0; j < initial_namesV.size(); j++) {
				distInit[i][j]= dist[initialId.get(initial_namesV.get(i).getName())][initialId.get(initial_namesV.get(j).getName())];
			}
		}
		UPGMA upgma = new UPGMA(distInit,initial_namesV);
		return upgma.buildTree();
	}

	public DefaultMutableTreeNode buildTree(boolean expandInitialSubtree) {
		HashSet<String> initial_names = new HashSet<String>();
		for (Named namedElt : initialElt) {
			initial_names.add(namedElt.getName());
		}

		
		float distToInitial;
		int i = 0;
		Vector<namedEldWithDist> additionalElts = new Vector<namedEldWithDist>();

		for (Named namedElt : allElt) {
			if (!initial_names.contains(namedElt.getName())) {
				int j = 0;
				distToInitial = 0;
				for (Named namedElt2 : allElt) {
					if (initial_names.contains(namedElt2.getName())) {
						distToInitial += dist[i][j];
					}
					j++;
				}
				additionalElts
						.add(new namedEldWithDist(namedElt, distToInitial));
				i++;
			}
		}
		Collections.sort(additionalElts);

		DefaultMutableTreeNode current_tree = buildInitialEltTree();
		if (! expandInitialSubtree)
			current_tree = new DefaultMutableTreeNode(current_tree.getUserObject().toString());
		
		for (namedEldWithDist namedEldWithDist : additionalElts) {
			String newLabel = UPGMA.normalizedClusterLabel(current_tree
					.getUserObject().toString(), namedEldWithDist.getNamedElt()
					.getName());
			// System.out.println("create clust "+newLabel);
			DefaultMutableTreeNode n = new DefaultMutableTreeNode(newLabel);
			DefaultMutableTreeNode n_son = new DefaultMutableTreeNode(namedEldWithDist.getNamedElt().getName());
			n.add(current_tree);
			n.add(n_son);
			current_tree=n;
		}
		return current_tree;
	}
	
	
	public DefaultMutableTreeNode buildTreeUsingAdded(boolean expandInitialSubtree, String initialTreeLabel) {
		HashSet<String> initial_names = new HashSet<String>();
		for (Named namedElt : initialElt) {
			initial_names.add(namedElt.getName());
		}

		
		float distToInitial;
		int i = 0;
		HashSet<namedEldWithDist> additionalElts = new HashSet<namedEldWithDist>();

		namedEldWithDist closer = null;
		namedEldWithDist tmp;
		for (Named namedElt : allElt) {
			if (!initial_names.contains(namedElt.getName())) {
				int j = 0;
				distToInitial = 0;
				for (Named namedElt2 : allElt) {
					if (initial_names.contains(namedElt2.getName())) {
						distToInitial += dist[i][j];
					}
					j++;
				}
				tmp = new namedEldWithDist(namedElt, distToInitial,i);
				additionalElts.add(tmp);
				if(closer == null || distToInitial<closer.getValue())
					closer = tmp;
				i++;
			}
		}
		
		DefaultMutableTreeNode current_tree = buildInitialEltTree();
		if (! expandInitialSubtree)
		{
			if(initialTreeLabel.length()<1)
				current_tree = new DefaultMutableTreeNode(current_tree.getUserObject().toString());
			else
				current_tree = new DefaultMutableTreeNode(initialTreeLabel);
		}
		
		while(! additionalElts.isEmpty())
		{
			String newLabel = UPGMA.normalizedClusterLabel(current_tree
					.getUserObject().toString(), closer.getNamedElt()
					.getName());
			DefaultMutableTreeNode n = new DefaultMutableTreeNode(newLabel);
			DefaultMutableTreeNode n_son = new DefaultMutableTreeNode(closer.getNamedElt().getName());
			n.add(current_tree);
			n.add(n_son);
			current_tree=n;
			additionalElts.remove(closer);
			tmp =null;
			for (namedEldWithDist elt : additionalElts) {
				float newDist= elt.getValue() + dist[elt.matId][closer.matId];
				elt.setValue(newDist);
				if(tmp == null || newDist<tmp.getValue())
					tmp = elt;
			}
			closer=tmp;
		}
		for (namedEldWithDist namedEldWithDist : additionalElts) {
			
			// System.out.println("create clust "+newLabel);
			
		}
		return current_tree;
	}

}
