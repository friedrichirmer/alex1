import processing.core.PApplet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by laemmel on 27.04.17.
 */



public class Network {
    public final Map<Integer,Node> nodes = new HashMap<Integer,Node>();
   	public final Map<Integer,Link> links = new HashMap<Integer,Link>();
	public List<Wall> walls = new ArrayList<>();

   	private final List<LinkInfo> linkInfos = new ArrayList<>();
      	
   	private int counterL = 1;
	private int counterW = 1;

   	public Node createNode(double x, double y, int id) {
   		Node n = new Node(x,y,id);
   		this.nodes.put(id,n);
   		return n;
   	}

   	
   	
   	public Link createLink(Node from, Node to, int room) {
   		int id = counterL;
   		Link l = new Link(from,to,id, room);
   		this.links.put(id,l);
   		from.addOutLink(l);
   		to.addInLink(l);
        LinkInfo li = new LinkInfo();
        li.x0 = (float)(from.getX()*Simulation.SCALE);
        li.y0 = (float)(from.getY()*Simulation.SCALE);
        li.x1 = (float)(to.getX()*Simulation.SCALE);
        li.y1 = (float)(to.getY()*Simulation.SCALE);
        linkInfos.add(li);
        counterL++;
   		return l;
   	}
   	
   	public Wall createWall(double x1, double y1, double x2, double y2, int room) {
   		int id = counterW ;
   		Wall w = new Wall(x1,y1,x2,y2,room,id);
   		walls.add(w);
   		counterW++;
     	return w;
   	}


	public void draw(PApplet p) {

   	    for (LinkInfo linkInfo : this.linkInfos) {
   	        p.line(linkInfo.x0,linkInfo.y0,linkInfo.x1,linkInfo.y1);
        }
   	    
   	    for (int i = 0; i<walls.size(); i++) {
   	    	Wall wa = walls.get(i);
   	    	float wx1 = (float)(wa.getX1()*Simulation.SCALE) ;
   	    	float wy1 = (float)(wa.getY1()*Simulation.SCALE) ;
   	    	float wx2 = (float)(wa.getX2()*Simulation.SCALE) ;
   	    	float wy2 = (float)(wa.getY2()*Simulation.SCALE) ;
   	    	p.strokeWeight(2);
   	    	p.line(wx1,wy1,wx2,wy2);
   	    	p.strokeWeight(1);
   	    }
   	    
	}

	private static final class LinkInfo {
   	    float x0;
   	    float y0;
   	    float x1;
   	    float y1;
    }

	
	
}
