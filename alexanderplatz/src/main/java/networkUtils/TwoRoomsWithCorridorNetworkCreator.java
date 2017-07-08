package networkUtils;

import java.util.List;

import alex.Vehicle;

public class TwoRoomsWithCorridorNetworkCreator {

	private Network network;
	
	public TwoRoomsWithCorridorNetworkCreator() {
		this.network = new Network();
	}
	
	public Network createNetwork(){
	        Node node1 = network.createNode(3.5,3.5,1);
	        Node node2 = network.createNode(13,3,2);
	        Node node3 = network.createNode(13,6,3);
	        Node node4 = network.createNode(13,9,4);
	        Node node5 = network.createNode(3,9,5);
	        Node node6 = network.createNode(19,6,6);
	        Node node7 = network.createNode(19,3,7);
	        Node node8 = network.createNode(29,3,8);
	        Node node9 = network.createNode(29,9,9);
	        Node node10 = network.createNode(19,9,10);
	        Node node11 = network.createNode(14,6,11);
	        Node node12 = network.createNode(18,6,12);

	        network.createLink(node1,node2,1);
	        network.createLink(node2,node3,1);
	        network.createLink(node3,node4,1);
	        network.createLink(node4,node5,1);
	        network.createLink(node5,node1,1);
	        network.createLink(node3, node11, 1);
	        network.createLink(node11,node12,2);
	        network.createLink(node12,node6,3);
	        network.createLink(node6,node7,3);
	        network.createLink(node7,node8,3);
	        network.createLink(node8,node9,3);
	        network.createLink(node9,node10,3);
	        network.createLink(node10,node6,3);
	        network.createLink(node2,node1,1);
	        network.createLink(node3,node2,1);
	        network.createLink(node3,node4,1);
	        network.createLink(node5,node4,1);
	        network.createLink(node1,node5,1);
	        network.createLink(node11,node3,1);
	        network.createLink(node12,node11,2);
	        network.createLink(node6,node12,3);
	        network.createLink(node7,node6,3);
	        network.createLink(node8,node7,3);
	        network.createLink(node9,node8,3);
	        network.createLink(node10,node9,3);
	        network.createLink(node6,node10,3);
	              
	        network.createWall(2,2,14,2,1);
	        network.createWall(6,2,9,5.5,1);
	        network.createWall(14,2,14,5,1);
	        network.createWall(14,7,13,11,1);
	        network.createWall(13,11,2,10,1);
	        network.createWall(2,10,2,2,1);
	        
	        network.createWall(14,5,18,5,2);
	        network.createWall(14,7,18,7,2);
	        
	        network.createWall(18,5,18,2,3);
	        network.createWall(18,2,30,2,3);
	        network.createWall(30,2,30,10,3);
	        network.createWall(30,10,18,10,3);
	        network.createWall(18,10,18,7,3);
	        
	        return this.network;
	}
}