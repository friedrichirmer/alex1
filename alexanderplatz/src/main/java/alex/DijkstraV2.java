package alex;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import network.Link;
import network.Network;
import network.Node;

public class DijkstraV2 {
	private final Network network;
	private Map<Node, DijkstraNode> correspondingNodes = new HashMap<Node, DijkstraNode>();
	private List<DijkstraNode> remainingNodes = new ArrayList<DijkstraNode>();


	public DijkstraV2(Network network) {
		this.network = network;
	}

	public List<Link> calculateRoute(Node start, Node destination) {
		
		this.correspondingNodes.clear();
		this.remainingNodes.clear();
		
		initiate(start);

		DijkstraNode current = this.remainingNodes.remove(0);

		while (current.getNode() != destination) {
			System.out.println("remaining nodes size = " + remainingNodes.size());
			expandDijkstraNode(current);
			current = this.remainingNodes.remove(0);
		}
		List<Link> route = getRoute(destination); 
		if (!(route.size() == 0)) {

			return route;
		} else return null;
	}



	    private void expandDijkstraNode(DijkstraNode current) {
	    	for(Link link : current.getNode().getOutLinks()){
	    		double weight = link.getWeight();
	    		double newCost = current.getCost() + weight;
	    		Node toNode = link.getTo();
	    		DijkstraNode toDijk = this.correspondingNodes.get(toNode);
	    		if(newCost < toDijk.getCost()){
	    			toDijk.setCost(newCost);
	    			toDijk.setPredecessor(current.getNode());
	    			if(!this.remainingNodes.contains(toDijk)) this.remainingNodes.add(toDijk);
	    		}
	    	}
	    	Collections.sort(this.remainingNodes);
		}

		private void initiate(Node start) {
//			System.out.println("network.nodes.size = " + network.nodes.size());
			for (Node node : network.nodes.values()){
	            
	    		double nodeCost;
	            if(node.getId() == start.getId()){
	            	nodeCost = 0.;
	            }
	            else{
	            	nodeCost = Double.MAX_VALUE;
	            }
	            DijkstraNode dijk = new DijkstraNode(node, nodeCost);
	            this.remainingNodes.add(dijk);
	            this.correspondingNodes.put(node, dijk);
	        }
	    	Collections.sort(this.remainingNodes);
//	    	System.out.println("remainingNodes.size = " + remainingNodes.size());
	    }

		private List<Link> getRoute(Node end){
			DijkstraNode current = this.correspondingNodes.get(end);
			List<Link> route = new ArrayList<Link>();
			Node predecessor = current.getPredecessor();
			
			while(predecessor != null){
				for(Link l : predecessor.getOutLinks()){
					if(l.getTo().equals(current.getNode())){
						route.add(0, l);
						break;
					}
				}
				
				current = this.correspondingNodes.get(current.getPredecessor());
				predecessor = current.getPredecessor();
				
			}
			return route;
		}
	   
	    class DijkstraNode implements Comparable{
	    	private Node node;
	    	private Node predecessor;
	    	private double cost;
	    	
	    	public DijkstraNode(Node node, double cost) {	
	    		this.node = node;
	    		this.cost = cost;
	    		this.predecessor = null;
			}
	    	
	    	void setCost(double cost){
	    		this.cost = cost;
	    	}
	    	
	    	void setPredecessor(Node predecessor){
	    		this.predecessor = predecessor;
	    	}
	    	
	    	Node getPredecessor(){
	    		return this.predecessor;
	    	}
	    	
	    	double getCost(){
	    		return this.cost;
	    	}
	    	
	    	Node getNode(){
	    		return this.node;
	    	}

			@Override
			public int compareTo(Object o) {
				if (!(o instanceof DijkstraNode)) throw new RuntimeException("cannot compare a DijkstraNode with an object of another type");
				
				double otherCost = ((DijkstraNode) o).getCost();
				return (cost < otherCost) ? -1 : ( (cost == otherCost) ? 0 : 1 )  ;
			}
	    	
	    }
	    
}
