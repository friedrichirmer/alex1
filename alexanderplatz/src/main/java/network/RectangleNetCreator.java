package network;

public class RectangleNetCreator {

	private Network network;
	
	public RectangleNetCreator(){
		this.network = new Network();
	}
	
	public Network createNetwork(){
		Node n0 = this.network.createNode(5, 5, 1);
		Node n1 = this.network.createNode(15,5,2);
		Node n2 = this.network.createNode(15, 15, 3);
		Node n3 = this.network.createNode(5, 15, 4);
		
		this.network.createLink(n0, n1, 0);
		this.network.createLink(n1, n0, 0);
		this.network.createLink(n0, n3, 0);
		this.network.createLink(n3, n0, 0);
		this.network.createLink(n1, n2, 0);
		this.network.createLink(n2, n1, 0);
		this.network.createLink(n2, n3, 0);
		this.network.createLink(n3, n2, 0);
	
		
		return this.network;
	}
	
}
