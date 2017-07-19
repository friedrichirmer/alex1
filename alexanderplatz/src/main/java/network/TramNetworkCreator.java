package network;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author Work
 *
 */
public class TramNetworkCreator {

	private Network network;
	
	public List<Node> tramEntryNodes = new ArrayList<Node>();
	public List<Node> tramExitNodes = new ArrayList<Node>();

	private Map<Node,Node> correspondingExitNodes = new HashMap<Node,Node>();
	
	public TramNetworkCreator(){
		this.network = new Network();
	}
	
	public Network createNetwork(){
		//EntryNodes			
		Node alexanderstr = network.createNode(56.4,23.2,101);
        Node klStr = network.createNode(1.1,22.4,102);
        Node sBahn = network.createNode(26.6,53.6,103);
        
		tramEntryNodes.add(alexanderstr);
		tramEntryNodes.add(klStr);
		tramEntryNodes.add(sBahn);

		//ExitNodes
        Node alexanderstr2 = network.createNode(56.9,23.7,104);
        Node klStr2 = network.createNode(1.6,21.9,105);
        Node sBahn2 = network.createNode(26.1,53.1,106);
        Node stop = network.createNode(24.9,47,107);
		
        this.correspondingExitNodes.put(klStr, stop);
        this.correspondingExitNodes.put(alexanderstr, sBahn2);
        this.correspondingExitNodes.put(sBahn, alexanderstr2);
        
        this.tramExitNodes.add(stop);
        this.tramExitNodes.add(sBahn2);
        this.tramExitNodes.add(alexanderstr2);
        
		//Nodes of network
        Node southDir = network.createNode(45.7,37.1,8);
        Node northDir = network.createNode(46.2,37.7,9);
        Node splitter = network.createNode(30,50.1,10);
        
        network.createLink(alexanderstr,southDir,1);
        network.createLink(southDir,splitter,1);
        network.createLink(splitter,klStr2,1);
        network.createLink(splitter,sBahn2,1);
              
        network.createLink(klStr,stop,1);
       
        network.createLink(sBahn,northDir,1);
        network.createLink(northDir,alexanderstr2,1);
				
        return network;
	}
	
	public List<Node> getTramEntryNodes(){
		return this.tramEntryNodes;
	}
	
	public Node getTramExitNode (Node node){
		return this.correspondingExitNodes.get(node);
	}

	public Node getrandomEntryNode() {
		int index = (int) (this.tramEntryNodes.size() * Math.random());
		return this.tramEntryNodes.get(index);
	}
		
	public Node getCorrespondingEntryNode(Node exit){
		for(Entry<Node, Node> e : this.correspondingExitNodes.entrySet()){
			if(e.getValue().equals(exit)) return e.getKey();
		}
		System.out.println("could not find corresponding entry node of tramExit: " + exit.getId());
		for(Entry<Node, Node> e : this.correspondingExitNodes.entrySet()){
			if(e.getKey().equals(exit)) return e.getValue();
		}
		return null;
	}

	public List<Node> getTramExitNodes() {
		return this.tramExitNodes;
	}
}
