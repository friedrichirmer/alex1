package network;



import java.util.ArrayList;
import java.util.List;

/**
 * @author Work
 *
 */
public class TramNetworkCreator {

	private Network network;

	
	
	public TramNetworkCreator(){
		this.network = new Network();
	}
	
	public Network createNetwork(){
		//EntryNodes			
		Node alexanderstr = network.createNode(56.4,23.2,101);
        Node klStr = network.createNode(1.1,22.4,102);
        Node sBahn = network.createNode(26.6,53.6,103);
		network.tramEntryNodes.add(alexanderstr);
		network.tramEntryNodes.add(klStr);
		network.tramEntryNodes.add(sBahn);

		//ExitNodes
        Node alexanderstr2 = network.createNode(56.9,23.7,104);
        Node klStr2 = network.createNode(1.6,21.9,105);
        Node sBahn2 = network.createNode(26.1,53.1,106);
        Node stop = network.createNode(24.9,47,107);
        
        network.tramExitSBahn.add(alexanderstr2);
        network.tramExitAlexanderstr.add(klStr2);
        network.tramExitAlexanderstr.add(sBahn2);
        network.tramExitKlStr.add(stop);
		
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
             
       
       
		network.tramEntryNodes.add(alexanderstr);
		network.tramEntryNodes.add(klStr);
		network.tramEntryNodes.add(sBahn);

		
				
        return network;
	}

}
