/**
 * 
 */
package network;

/**
 * @author Work
 *
 */
public class AlexanderplatzNetworkCreator {

	private Network network;

	
	
	public AlexanderplatzNetworkCreator(){
		this.network = new Network();
	}
	
	public Network createNetwork(){
		//EntryExitNodes			
		Node uSaturn = network.createNode(53.8,31.7,20);
        Node uPrimark = network.createNode(35.8,29.6,21);
        Node uMediaM = network.createNode(51.5,54,22);
        Node uTchibo = network.createNode(39.6,50.8,23);
        Node uKFC = network.createNode(30.5,56.5,24);
        Node uZentral = network.createNode(35.1,41.2,25);
        Node sBahn1 = network.createNode(20.8,45,26);
        Node sBahn2 = network.createNode(14.8,38.8,27);
        Node sBahn3 = network.createNode(8.9,32,28);
        Node primark = network.createNode(31.9,24.5,29);
		Node saturn = network.createNode(49.8,40.7,30);
		Node ampelLinks = network.createNode(0.9,23.4,31);
        Node ampelOben = network.createNode(23.9,1,32);
        Node ampelRechts = network.createNode(66.8,32.8,33);
		Node ampelMediaM = network.createNode(51.3,56.9,34);
		Node alexOase = network.createNode(42.3,32.4,35);
        Node doenerInn = network.createNode(19.2,13.9,36);
        Node sushi = network.createNode(9.7,18.9,37);
        Node reno = network.createNode(23.8,19.1,38);
        Node kaufhof = network.createNode(22.8,32.4,39);
        Node sanMarco = network.createNode(11.7,30.9,40);
        Node dm = network.createNode(52.3,36.2,41);
		Node newYorker = network.createNode(50.4,44.4,42);
		Node sparkasse = network.createNode(49.8,57,43);
        Node bruecke = network.createNode(28.4,54.6,44);
        Node ampelParkInn = network.createNode(39,11.5,45);
        Node cUndA = network.createNode(27.2,39.7,46);	
        
        	
		//Nodes of network
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
        Node node14 = network.createNode(52.4,26.8,14);
        
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
        network.createLink(node6,node8,1);
        network.createLink(node7,node2,1);
        network.createLink(node14,node13,1);
        network.createLink(node14,node8,1);
         
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
        network.createLink(node8,node6,1);
        network.createLink(node2,node7,1);
        network.createLink(node13,node14,1);
        network.createLink(node8,node14,1);
        
        //links von entryExitNodes ins Netz
        network.createLink(uSaturn,node9,1);
        network.createLink(uSaturn,node14,1);
        network.createLink(uPrimark,node10,1);
        network.createLink(uMediaM,node8,1);
        network.createLink(uMediaM,node2,1);
        network.createLink(uTchibo,node7,1);
        network.createLink(uTchibo,node8,1);
        network.createLink(uKFC,node3,1);
        network.createLink(uKFC,node4,1);
        network.createLink(uZentral,node7,1);
        network.createLink(uZentral,node8,1);
        network.createLink(sBahn1,node4,1);
        network.createLink(sBahn1,node5,1);
        network.createLink(sBahn2,node5,1);
        network.createLink(sBahn3,node5,1);
        network.createLink(sBahn3,node11,1);
        network.createLink(primark,node10,1);
        network.createLink(saturn,node8,1);
        network.createLink(ampelLinks,node11,1);
        network.createLink(ampelOben,node13,1);
        network.createLink(ampelRechts,node1,1);
        network.createLink(ampelMediaM,node2,1);
        network.createLink(alexOase,node8,1);
        network.createLink(alexOase,node7,1);
        network.createLink(doenerInn,node12,1);
        network.createLink(sushi,node11,1);
        network.createLink(sushi,node12,1);
        network.createLink(reno,node10,1);
        network.createLink(reno,node12,1);
        network.createLink(kaufhof,node6,1);
        network.createLink(sanMarco,node5,1);
        network.createLink(sanMarco,node11,1);
        network.createLink(dm,node8,1);
        network.createLink(dm,node9,1);
        network.createLink(newYorker,node8,1);
        network.createLink(newYorker,node2,1);
        network.createLink(sparkasse,node2,1);
        network.createLink(sparkasse,node3,1);
        network.createLink(bruecke,node4,1);
        network.createLink(ampelParkInn,node9,1);
        network.createLink(ampelParkInn,node13,1);
        network.createLink(cUndA,node6,1);
        network.createLink(cUndA,node7,1);;
        
        
        network.createLink(node9,uSaturn,1);
        network.createLink(node14,uSaturn,1);
        network.createLink(node10,uPrimark,1);
        network.createLink(node8,uMediaM,1);
        network.createLink(node2,uMediaM,1);
        network.createLink(node7,uTchibo,1);
        network.createLink(node8,uTchibo,1);
        network.createLink(node3,uKFC,1);
        network.createLink(node4,uKFC,1);
        network.createLink(node7,uZentral,1);
        network.createLink(node8,uZentral,1);
        network.createLink(node4,sBahn1,1);
        network.createLink(node5,sBahn1,1);
        network.createLink(node5,sBahn2,1);
        network.createLink(node5,sBahn3,1);
        network.createLink(node11,sBahn3,1);
        network.createLink(node10,primark,1);
        network.createLink(node8,saturn,1);
        network.createLink(node11,ampelLinks,1);
        network.createLink(node13,ampelOben,1);
        network.createLink(node1,ampelRechts,1);
        network.createLink(node2,ampelMediaM,1);
        network.createLink(node8,alexOase,1);
        network.createLink(node7,alexOase,1);
        network.createLink(node12,doenerInn,1);
        network.createLink(node11,sushi,1);
        network.createLink(node12,sushi,1);
        network.createLink(node10,reno,1);
        network.createLink(node12,reno,1);
        network.createLink(node6,kaufhof,1);
        network.createLink(node5,sanMarco,1);
        network.createLink(node11,sanMarco,1);
        network.createLink(node8,dm,1);
        network.createLink(node9,dm,1);
        network.createLink(node8,newYorker,1);
        network.createLink(node2,newYorker,1);
        network.createLink(node2,sparkasse,1);
        network.createLink(node3,sparkasse,1);
        network.createLink(node4,bruecke,1);
        network.createLink(node9,ampelParkInn,1);
        network.createLink(node13,ampelParkInn,1);
        network.createLink(node6,cUndA,1);
        network.createLink(node7,cUndA,1);
        
        //Abgrenzung
        network.createStaticWall(0,23.8,24.7,0);
        network.createStaticWall(24.7,0,67.7,33.2);
        network.createStaticWall(67.7,33.2,49.0,60.9);
        network.createStaticWall(49.0,60.9,39.6,68.3);
        network.createStaticWall(39.6,68.3,29.9,57.9);
        network.createStaticWall(29.9,57.9,23.8,48.7);
        network.createStaticWall(23.8,48.7,6.9,30.3);
        network.createStaticWall(6.9,30.3,0,23.8);
        //Building links
        network.createStaticWall(5.7,23.6,14.5,14.8);
        network.createStaticWall(14.5,14.8,27.2,27.1);
        network.createStaticWall(27.2,27.1,18.3,35.9);
        network.createStaticWall(18.3,35.9,5.7,23.6);
        //Building oben
        network.createStaticWall(19.3,15,24.5,7.7);
        network.createStaticWall(24.5,7.7,44.2,22.1);
        network.createStaticWall(44.2,22.1,39.1,29);
        network.createStaticWall(39.1,29,19.3,15);
        //AlexOase
        network.createStaticWall(39.8,29.6,45,22.9);
        network.createStaticWall(45,22.9,51.2,26.9);
        network.createStaticWall(51.2,26.9,45.4,33.9);
        network.createStaticWall(45.4,33.9,39.8,29.6);
        //Building rechts
        network.createStaticWall(49.8,39.9,61,30);
        network.createStaticWall(61,30,65.6,33.3);
        network.createStaticWall(65.6,33.3,55,49.2);
        network.createStaticWall(55,49.2,53.2,48.3);
        network.createStaticWall(53.2,48.3,49.8,39.9);
        //Building unten
        network.createStaticWall(32,52.6,35.3,50.4);
        network.createStaticWall(35.3,50.4,38,54.1);
        network.createStaticWall(38,54.1,48.6,52);
        network.createStaticWall(48.6,52,50.9,55.6);
        network.createStaticWall(50.9,55.6,48,57.4);
        network.createStaticWall(48,57.4,47.6,56.8);
        network.createStaticWall(47.6,56.8,42.3,60.3);
        network.createStaticWall(42.3,60.3,42.8,61);
        network.createStaticWall(42.8,61,39.4,62.8);
        network.createStaticWall(39.4,62.8,32,52.6);
        //Building links Mitte
        network.createStaticWall(20.5,38.1,23.3,36.2);
        network.createStaticWall(23.3,36.2,31,45.1);
        network.createStaticWall(31,45.1,28.3,47.5);
        network.createStaticWall(28.3,47.5,20.5,38.1);
        //Brunnen
        network.createStaticWall(28.4,32.3,28.8,31.5);
        network.createStaticWall(28.8,31.5,29.7,30.7);
        network.createStaticWall(29.7,30.7,31,30.9);
        network.createStaticWall(31,30.9,31.7,31.5);
        network.createStaticWall(31.7,31.5,32.1,32.5);
        network.createStaticWall(32.1,32.5,31.8,33.5);
        network.createStaticWall(31.8,33.5,30.8,34.2);
        network.createStaticWall(30.8,34.2,29.3,34.1);
        network.createStaticWall(29.3,34.1,28.6,33.2);
        network.createStaticWall(28.6,33.2,28.4,32.3);
        //U-Bahn Saturn
        network.createStaticWall(53.3,31.4,52,32.5);
        network.createStaticWall(52,32.5,52.7,33.1);
        network.createStaticWall(52.7,33.1,53.9,32.2);
        //U-Bahn Primark
        network.createStaticWall(35,28.6,36.9,30);
        network.createStaticWall(36.9,30,36.4,30.6);
        network.createStaticWall(36.4,30.6,34.5,29.1);
        //U-Bahn Media Markt
        network.createStaticWall(51.5,53.1,52.2,54.4);
        network.createStaticWall(52.2,54.4,51.8,54.7);
        network.createStaticWall(51.8,54.7,50.9,53.5);
        //U-Bahn Tchibo
        network.createStaticWall(39.5,51.8,39.2,50.1);
        network.createStaticWall(40,51.7,39.5,51.8);
        network.createStaticWall(39.8,49.9,40,51.7);
        //U-Bahn KFC
        network.createStaticWall(30,56.3,29.4,55.6);
        network.createStaticWall(29.4,55.6,29.8,55.4);
        network.createStaticWall(29.8,55.4,30.4,56.2);
        //U-Bahn Zentral
        network.createStaticWall(34.7,41.3,34.6,39.8);
        network.createStaticWall(34.6,39.8,35.6,39.8);
        network.createStaticWall(35.6,39.8,35.5,41.2);
       
		network.entryExitNodes.add(uSaturn);
		network.entryExitNodes.add(uPrimark);
		network.entryExitNodes.add(uMediaM);
		network.entryExitNodes.add(uTchibo);
		network.entryExitNodes.add(uKFC);
		network.entryExitNodes.add(uZentral);
		network.entryExitNodes.add(sBahn1);
		network.entryExitNodes.add(sBahn2);
		network.entryExitNodes.add(sBahn3);
		network.entryExitNodes.add(primark);
		network.entryExitNodes.add(saturn);
		network.entryExitNodes.add(ampelLinks);
		network.entryExitNodes.add(ampelOben);
		network.entryExitNodes.add(ampelRechts);
		network.entryExitNodes.add(ampelMediaM);
		network.entryExitNodes.add(alexOase);
		network.entryExitNodes.add(doenerInn);
		network.entryExitNodes.add(sushi);
		network.entryExitNodes.add(reno);
		network.entryExitNodes.add(kaufhof);
		network.entryExitNodes.add(sanMarco);
		network.entryExitNodes.add(dm);
		network.entryExitNodes.add(newYorker);
		network.entryExitNodes.add(sparkasse);
		network.entryExitNodes.add(bruecke);
		network.entryExitNodes.add(ampelParkInn);
		network.entryExitNodes.add(cUndA);
			
        return network;
	}

}
