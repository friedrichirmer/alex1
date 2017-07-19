package network;

import alex.Vis;
import processing.core.PApplet;
import java.util.*;
import static java.lang.Math.sqrt;

public class Network {
    public final Map<Integer, Node> nodes = new HashMap<Integer,Node>();

	public Map<Integer, Link> getLinks() {
		return links;
	}

	public final Map<Integer,Link> links = new HashMap<Integer,Link>();
	public Set<Wall> staticWalls = new HashSet<Wall>();
   	private final List<LinkInfo> linkInfos = new ArrayList<>();
   	private int counterOfLinks = 1;
	private int counterW = 1;
	public List<Node> entryExitNodes = new ArrayList<Node>();
	public List<Node> tramEntryNodes = new ArrayList<Node>();
	public List<Node> tramExitNodes = new ArrayList<Node>();
	public List<Node> tramExitAlexanderstr = new ArrayList<Node>();
	public List<Node> tramExitSBahn = new ArrayList<Node>();
	public List<Node> tramExitKlStr = new ArrayList<Node>();
	private double proportion = 3.051;
	public List<Node> evacuationNodes = new ArrayList<Node>();
	
	public List<Node> getEntryExitNodes() {
		return entryExitNodes;
	}

	public Node createNode(double x, double y, int id) {
   		Node n = new Node(x*proportion,y*proportion,id);
   		this.nodes.put(id,n);
   		return n;
   	}

	public Map<Integer, Node> getNodes() {
		return nodes;
	}

	public Link createLink(Node fromNode, Node toNode, int roomNumber) {
   		int idLink = counterOfLinks;
   		Link link = new Link(fromNode, toNode, idLink, roomNumber);
   		this.links.put(idLink,link);
   		fromNode.addOutLink(link);
   		toNode.addInLink(link);
        LinkInfo linkInfo = new LinkInfo();
        linkInfo.x0 = (float)(fromNode.getX());
        linkInfo.y0 = (float)(fromNode.getY());
        linkInfo.x1 = (float)(toNode.getX());
        linkInfo.y1 = (float)(toNode.getY());
        linkInfos.add(linkInfo);
        counterOfLinks++;
   		return link;
   	}

   	public Wall createStaticWall(double x1, double y1, double x2, double y2) {
   		Wall w = new Wall(x1*proportion,y1*proportion,x2*proportion,y2*proportion);
   		staticWalls.add(w);
   		counterW++;
     	return w;
   	}

   	public void createPavilion(double centerX, double centerY, double phi) {
   		phi = phi + Math.PI/4;
   		double sinA = Math.sin(phi) * 3;
   		double cosA = Math.cos(phi) * 3;
		Wall wall1 = new Wall(centerX - sinA, centerY - cosA, centerX - cosA, centerY + sinA);
		Wall wall2 = new Wall(centerX + cosA, centerY - sinA, centerX + sinA, centerY + cosA);
		Wall wall3 = new Wall(centerX + sinA, centerY + cosA, centerX - cosA, centerY + sinA);
		Wall wall4 = new Wall(centerX + cosA, centerY - sinA, centerX - sinA, centerY - cosA);
		staticWalls.add(wall1);
		counterW++;
		staticWalls.add(wall2);
		counterW++;
		staticWalls.add(wall3);
		counterW++;
		staticWalls.add(wall4);
		counterW++;
	}

	public void draw(PApplet p, boolean isTramNetwork) {
		if(isTramNetwork) drawLinks(p, isTramNetwork);
		drawWalls(p);
		drawNodes(p);
	}

	private void drawWalls(PApplet p) {
		Iterator<Wall> it = staticWalls.iterator();
		while(it.hasNext()){
			Wall wa = it.next();
   	    	float wx1 = (float)(wa.getX1()* Vis.scale) ;
   	    	float wy1 = (float)(wa.getY1()* Vis.scale) ;
   	    	float wx2 = (float)(wa.getX2()* Vis.scale) ;
   	    	float wy2 = (float)(wa.getY2()* Vis.scale) ;
   	    	p.strokeWeight(2);
   	    	p.line(wx1,wy1,wx2,wy2);
   	    	p.strokeWeight(1);
   	    }
	}

	private void drawLinks(PApplet p, boolean isTramNetwork) {
		for (LinkInfo linkInfo : this.linkInfos) {
			if(isTramNetwork){
				p.fill(210,180,140);
				p.stroke(210,180,140);
			} else {
				p.fill(0);
				p.stroke(0);
				p.strokeWeight(0.2f);
			}
			
            p.line((float) (linkInfo.x0 * Vis.scale),
					(float) (linkInfo.y0 * Vis.scale),
					(float) (linkInfo.x1 * Vis.scale),
					(float) (linkInfo.y1 * Vis.scale));
     	}
		p.fill(0);
		p.stroke(0);
		p.strokeWeight(1);
	}
	
	private void drawNodes(PApplet p) {
		for(Integer n: this.nodes.keySet()){
			Node node = this.nodes.get(n);
			float  xx = (float) (Vis.scale * node.getX());
			float yy = (float) (Vis.scale* node.getY());
			if(this.entryExitNodes.contains(node)){
				
				p.pushMatrix();
//				p.translate(xx, yy);
				p.fill(0,50,200);
				p.ellipse((float)xx, (float)yy, 5, 5);
				p.popMatrix();
				p.fill(0);
			}
			p.text(n, xx, yy);
		}
	}

	public Node findNearestNode(double x, double y) {
   		Double minimalDistance = Double.POSITIVE_INFINITY;
   		Integer currentClosest = 0;
		currentClosest = iterateAllNodesAndCalculateMinimalDistance(x, y, minimalDistance, currentClosest);
		return nodes.get(currentClosest);
	}

	private Integer iterateAllNodesAndCalculateMinimalDistance(
			double x, double y, Double minimalDistance, Integer currentClosest) {
		Iterator<Map.Entry<Integer, Node>> iterator = nodes.entrySet().iterator();
		while (iterator.hasNext()){
            Node node = iterator.next().getValue();
            Double distance = sqrt((x - node.getX()) * (x - node.getX()) +
                 (y - node.getY()) * (y - node.getY()));
            if (distance < minimalDistance){
                currentClosest = node.getId();
        	}
     	}
		return currentClosest;
	}

	private static final class LinkInfo {
   	    float x0;
   	    float y0;
   	    float x1;
   	    float y1;
    }
}
