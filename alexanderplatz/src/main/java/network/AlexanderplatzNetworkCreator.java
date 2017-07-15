/**
 * 
 */
package network;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Work
 *
 */
public class AlexanderplatzNetworkCreator {

	private Network network;
	private List<Node> entryExitNodes = new ArrayList<Node>();
	
	
	public AlexanderplatzNetworkCreator(){
		this.network = new Network();
	}
	
	public Network createNetwork(){
		//Nodes die Personen auswerfen			
		Node uSaturn = network.createNode(52,33.2,20);
        Node uPrimark = network.createNode(35.8,29.6,21);
        Node uMediaM = network.createNode(51.5,54,22);
        Node uTchibo = network.createNode(39.6,50.8,23);
        Node uKFC = network.createNode(29.8,55.8,24);
        Node uZentral = network.createNode(35.1,44.2,25);
        Node sBahn1 = network.createNode(20.6,45.2,26);
        Node sBahn2 = network.createNode(14.6,38.8,27);
        Node sBahn3 = network.createNode(8.5,32,28);
        Node primark = network.createNode(31.9,24.5,29);
		Node saturn = network.createNode(49.8,40.7,30);
		Node ampelLinks = network.createNode(0.9,23.4,31);
        Node ampelOben = network.createNode(23.9,1,32);
        Node ampelRechts = network.createNode(66.8,32.8,33);
		Node ampelMediaM = network.createNode(51.3,56.9,34);

		
		
        Node node1 = network.createNode(66.7,33.1,1);
        Node node2 = network.createNode(54.1,52.4,2);
        Node node3 = network.createNode(40.6,66.7,3);
        Node node4 = network.createNode(29.7,52,4);
        Node node5 = network.createNode(17.7,39,5);
        Node node6 = network.createNode(24.1,33.3,6);
        Node node7 = network.createNode(34.7,44.8,7);
        Node node8 = network.createNode(47,40.6,8);
        Node node9 = network.createNode(60.8,28.6,9);
        Node node10 = network.createNode(30.4,26.7,10);
        Node node11 = network.createNode(2.9,23.1,11);
        Node node12 = network.createNode(14,12.2,12);
        Node node13 = network.createNode(23.8,3.3,13);
        
        network.createLink(node1,node2,1);
        network.createLink(node2,node3,1);
        network.createLink(node3,node4,1);
        network.createLink(node4,node5,1);
        network.createLink(node5,node6,1);
        network.createLink(node6,node7,1);
        network.createLink(node7,node8,1);
        network.createLink(node8,node9,1);
        network.createLink(node9,node1,1);
        network.createLink(node10,node8,1);
        network.createLink(node10,node6,1);
        network.createLink(node8,node2,1);
        network.createLink(node7,node4,1);
        network.createLink(node11,node12,1);
        network.createLink(node12,node13,1);
        network.createLink(node13,node9,1);
        network.createLink(node5,node11,1);
        network.createLink(node10,node12,1);
        
        network.createLink(node2,node1,1);
        network.createLink(node3,node2,1);
        network.createLink(node4,node3,1);
        network.createLink(node5,node4,1);
        network.createLink(node6,node5,1);
        network.createLink(node7,node6,1);
        network.createLink(node8,node7,1);
        network.createLink(node9,node8,1);
        network.createLink(node1,node9,1);
        network.createLink(node8,node10,1);
        network.createLink(node6,node10,1);
        network.createLink(node2,node8,1);
        network.createLink(node4,node7,1);
        network.createLink(node12,node11,1);
        network.createLink(node13,node12,1);
        network.createLink(node9,node13,1);
        network.createLink(node11,node5,1);
        network.createLink(node12,node10,1);
        //links von Nodes die Personen auswerfen ins Netz
        network.createLink(uSaturn,node9,1);
        network.createLink(uPrimark,node10,1);
        network.createLink(uMediaM,node8,1);
        network.createLink(uTchibo,node7,1);
        network.createLink(uKFC,node3,1);
        network.createLink(uZentral,node7,1);
        network.createLink(sBahn1,node4,1);
        network.createLink(sBahn2,node5,1);
        network.createLink(sBahn3,node5,1);
        network.createLink(primark,node10,1);
        network.createLink(saturn,node8,1);
        network.createLink(ampelLinks,node11,1);
        network.createLink(ampelOben,node13,1);
        network.createLink(ampelRechts,node1,1);
        network.createLink(ampelMediaM,node2,1);
        
        network.createLink(node9,uSaturn,1);
        network.createLink(node10,uPrimark,1);
        network.createLink(node8,uMediaM,1);
        network.createLink(node7,uTchibo,1);
        network.createLink(node3,uKFC,1);
        network.createLink(node7,uZentral,1);
        network.createLink(node4,sBahn1,1);
        network.createLink(node5,sBahn2,1);
        network.createLink(node5,sBahn3,1);
        network.createLink(node10,primark,1);
        network.createLink(node8,saturn,1);
        network.createLink(node11,ampelLinks,1);
        network.createLink(node13,ampelOben,1);
        network.createLink(node1,ampelRechts,1);
        network.createLink(node2,ampelMediaM,1);

        //Abgrenzung
        network.createWall(0,23.8,23.7,0);
        network.createWall(23.7,0,67.7,33.2);
        network.createWall(67.7,33.2,49.0,60.9);
        network.createWall(49.0,60.9,39.6,68.3);
        network.createWall(39.6,68.3,29.9,57.9);
        network.createWall(29.9,57.9,23.8,48.7);
        network.createWall(23.8,48.7,6.9,30.3);
        network.createWall(6.9,30.3,0,23.8);
        //Building links
        network.createWall(5.7,23.6,14.5,14.8);
        network.createWall(14.5,14.8,27.2,27.1);
        network.createWall(27.2,27.1,18.3,35.9);
        network.createWall(18.3,35.9,5.7,23.6);
        //Building oben
        network.createWall(19.3,15,24.5,7.7);
        network.createWall(24.5,7.7,44.2,22.1);
        network.createWall(44.2,22.1,39.1,29);
        network.createWall(39.1,29,19.3,15);
        //AlexOase
        network.createWall(39.8,29.6,45,22.9);
        network.createWall(45,22.9,51.2,26.9);
        network.createWall(51.2,26.9,45.4,33.9);
        network.createWall(45.4,33.9,39.8,29.6);
        //Building rechts
        network.createWall(49.8,39.9,61,30);
        network.createWall(61,30,65.6,33.3);
        network.createWall(65.6,33.3,55,49.2);
        network.createWall(55,49.2,53.2,48.3);
        network.createWall(53.2,48.3,49.8,39.9);
        //Building unten
        network.createWall(32,52.6,35.3,50.4);
        network.createWall(35.3,50.4,38,54.1);
        network.createWall(38,54.1,48.6,52);
        network.createWall(48.6,52,50.9,55.6);
        network.createWall(50.9,55.6,48,57.4);
        network.createWall(48,57.4,47.6,56.8);
        network.createWall(47.6,56.8,42.3,60.3);
        network.createWall(42.3,60.3,42.8,61);
        network.createWall(42.8,61,39.4,62.8);
        network.createWall(39.4,62.8,32,52.6);
        //Building links Mitte
        network.createWall(20.5,38.1,23.3,36.2);
        network.createWall(23.3,36.2,31,45.1);
        network.createWall(31,45.1,28.3,47.5);
        network.createWall(28.3,47.5,20.5,38.1);
        //Brunnen
        network.createWall(28.4,32.3,28.8,31.5);
        network.createWall(28.8,31.5,29.7,30.7);
        network.createWall(29.7,30.7,31,30.9);
        network.createWall(31,30.9,31.7,31.5);
        network.createWall(31.7,31.5,32.1,32.5);
        network.createWall(32.1,32.5,31.8,33.5);
        network.createWall(31.8,33.5,30.8,34.2);
        network.createWall(30.8,34.2,29.3,34.1);
        network.createWall(29.3,34.1,28.6,33.2);
        network.createWall(28.6,33.2,28.4,32.3);
        //U-Bahn Saturn
        network.createWall(53.3,31.4,50.7,33.7);
        network.createWall(50.7,33.7,51.1,34.3);
        network.createWall(51.1,34.3,53.9,32.2);       
        //U-Bahn Primark
        network.createWall(35,28.6,36.9,30);
        network.createWall(36.9,30,36.4,30.6);
        network.createWall(36.4,30.6,34.5,29.1);
        //U-Bahn Media Markt
        network.createWall(51.5,53.1,52.2,54.4);
        network.createWall(52.2,54.4,51.8,54.7);
        network.createWall(51.8,54.7,50.9,53.5);
        //U-Bahn Tchibo
        network.createWall(39.5,51.8,39.2,50.1);
        network.createWall(39.2,50.1,39.8,49.9);
        network.createWall(39.8,49.9,40,51.7);
        //U-Bahn KFC
        network.createWall(30,56.3,29.4,55.6);
        network.createWall(29.4,55.6,29.8,55.4);
        network.createWall(29.8,55.4,30.4,56.2);
        //U-Bahn Zentral
        network.createWall(34.7,41.3,34.6,39.8);
        network.createWall(34.6,39.8,35.6,39.8);
        network.createWall(35.6,39.8,35.5,41.2);
       
		entryExitNodes.add(uSaturn);
		entryExitNodes.add(uPrimark);
		entryExitNodes.add(uMediaM);
		entryExitNodes.add(uTchibo);
		entryExitNodes.add(uKFC);
		entryExitNodes.add(uZentral);
		entryExitNodes.add(sBahn1);
		entryExitNodes.add(sBahn2);
		entryExitNodes.add(sBahn3);
		entryExitNodes.add(primark);
		entryExitNodes.add(saturn);
		entryExitNodes.add(ampelLinks);
		entryExitNodes.add(ampelOben);
		entryExitNodes.add(ampelRechts);
		entryExitNodes.add(ampelMediaM);
				
        return network;
	}
	
	public List<Node> getEntryExitNodes(){
		return this.entryExitNodes;
	}
	
	
}
