import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.crypto.NodeSetData;

public class Dijkstra {
	
	private static Map<Node,Double> nodeWeights = new HashMap<Node,Double>();
	private static Map<Node,Node> predecessor = new HashMap<Node,Node>();

	private static Network network;
	private static Node source;
	

	public static List<Link> dijkstra(Network net, Node src, Node dstn) {
		
		List<Node> qu = new ArrayList<Node>();
		network = net;

		for (int i=1; i<=network.nodes.size();i++) {
			Node no = network.nodes.get(i);
			nodeWeights.put(no, Double.POSITIVE_INFINITY);
			predecessor.put(no, null);
			qu.add(no);
		}

		nodeWeights.replace(src, (double)0);
		source = src;

		Node vcurr = src;
		
		while (!vcurr.equals(dstn)) {
			qu.remove(vcurr);
			expandGraph(vcurr);
			Node next = qu.get(0);
			for (int i=0;i<qu.size();i++) {
				Node no = qu.get(i);
				if (nodeWeights.get(no) < nodeWeights.get(next)) {
					next = no;					
				}		
			}
			
			vcurr = next;
		}
				
		List<Link> path = reconstructPath(dstn);
		
		return path;

		
	}
	



	private static void expandGraph(Node vcurr) {
		for (int i=1;i<network.links.size();i++) {
			Link lin =  network.links.get(i);
			if (lin.getFrom() == vcurr) {
				
				Node vnew = lin.getTo();
				double c;
				c = nodeWeights.get(vcurr) + lin.getWeight();
				if(c < nodeWeights.get(vnew)) {
					nodeWeights.put(vnew, c);
					predecessor.put(vnew, vcurr);
					
				}
			}
		}

	}
	
	private static List<Link> reconstructPath(Node dstn) {
		
		List<Link> l = new ArrayList<Link>();
		List<Link> lback = new ArrayList<Link>();

		Node ncurr = dstn;
		
		while (ncurr != null) {
			
			Node next = predecessor.get(ncurr);
			for (int i=1;i<network.links.size();i++) {
				Link li =  network.links.get(i);
				if (li.getFrom().equals(next) && li.getTo().equals(ncurr)) {
					l.add(li);
				}
			}
			ncurr = next;
		}		
		
		int j = l.size() - 1;		
		
		for (int i=1;i<=l.size(); i++) {
			lback.add(l.get(j));
			j--;
		}
		
		return lback;
		
	}
	

	
}
