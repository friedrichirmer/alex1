package networkUtils;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by laemmel on 27.04.17.
 */
public class Node {
    private final double x;
   	private final double y;
   	private final int id;

   	private final List<Link> outLinks = new LinkedList<Link>();
   	private final List<Link> inLinks = new LinkedList<Link>();

   	public Node(double x, double y, int id) {
   		this.x = x;
   		this.y = y;
   		this.id = id;
   	}

   	public void addOutLink(Link l) {
   		this.outLinks.add(l);
   	}

   	public void addInLink(Link l) {
   		this.inLinks.add(l);
   	}

   	public double getX() {
   		return this.x;
   	}

   	public double getY() {
   		return this.y;
   	}

	public int getId() {
		return id;
	}
}
