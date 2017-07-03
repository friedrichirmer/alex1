package alex;

import networkUtils.Link;
import networkUtils.Network;
import networkUtils.Node;

import java.util.*;

public class Dijkstra {
	
	private static Map<Node,Double> nodeWeights = new HashMap<Node,Double>();
	private static Map<Node,Node> predecessor = new HashMap<Node,Node>();

	private static Network network;
	private static Node source;
	

	public static List<Link> returnRoute(Network network, Node originNode, Node destinationNode) {

		List<Node> qu = new ArrayList<Node>();
		Dijkstra.network = network;

		initializeQuListOfNodes(qu);

		nodeWeights.replace(originNode, (double)0);
		source = originNode;
		Node currentNode = originNode;

		expandAllNodesInTheQuList(destinationNode, qu, currentNode);

		List<Link> path = reconstructPath(destinationNode);
		return path;
	}

	


	private static void expandAllNodesInTheQuList(Node destinationNode, List<Node> qu, Node currentNode) {
		while (!currentNode.equals(destinationNode)) {
			qu.remove(currentNode);
			expandNode(currentNode);
			Node next = qu.get(0);
			for (int i = 0; i < qu.size(); i++) {
				Node node = qu.get(i);
				if (nodeWeights.get(node) < nodeWeights.get(next)) {
					next = node;
				}
			}
			currentNode = next;
		}
	}

	private static void initializeQuListOfNodes(List<Node> qu) {
		for (int i = 1; i<= Dijkstra.network.nodes.size(); i++) {
			Node node = Dijkstra.network.nodes.get(i);
			nodeWeights.put(node, Double.POSITIVE_INFINITY);
			predecessor.put(node, null);
			qu.add(node);
		}
	}

	private static void expandNode(Node currentNode) {
		for (int i=1;i<network.links.size();i++) {
			Link link =  network.links.get(i);
			if (link.getFrom() == currentNode) {
				
				Node vnew = link.getTo();
				double c;
				c = nodeWeights.get(currentNode) + link.getWeight();
				if(c < nodeWeights.get(vnew)) {
					nodeWeights.put(vnew, c);
					predecessor.put(vnew, currentNode);
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
