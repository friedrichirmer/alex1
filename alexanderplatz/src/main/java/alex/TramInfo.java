package alex;

import java.util.Iterator;
import java.util.Set;

import network.Wall;
import processing.core.PApplet;

public class TramInfo {

	private Set<Wall> walls;
    
    private final double phi;

	private Wall bottom;

	private float width;

	private float length;
	
	private float xCenter;
	private float yCenter;
	private float xBottom;
	private float yBottom;
    
	public TramInfo(Tram tram) {
		this.walls = tram.getWalls();
		this.phi = tram.getPhi();
		this.bottom = tram.getBottomWall();
		this.width = tram.getWidth();
		this.length = tram.getLength();
		this.xCenter = (float) tram.getX();
		this.yCenter = (float) tram.getY();
		this.xBottom = tram.getBottomX();
		this.yBottom = tram.getBottomY();
	}

	public void draw(PApplet p){
		p.strokeWeight(2);
		p.stroke(255,0,0);
//		p.rect((float)left.getX1(), (float)left.getY1(), width, length);
		p.fill(255,0,0);
//		p.ellipse(xCenter * Vis.scale, yCenter* Vis.scale, 5, 5);
		p.ellipse(xBottom * Vis.scale, yBottom * Vis.scale, 5, 5);
		drawWalls(p);
	}
	
	private void drawWalls(PApplet p) {
		Iterator<Wall> it = walls.iterator();
		p.strokeWeight(1);
		p.stroke(0);
		while(it.hasNext()){
			Wall wa = it.next();
   	    	float wx1 = (float)(wa.getX1()* Vis.scale) ;
   	    	float wy1 = (float)(wa.getY1()* Vis.scale) ;
   	    	float wx2 = (float)(wa.getX2()* Vis.scale) ;
   	    	float wy2 = (float)(wa.getY2()* Vis.scale) ;
   	    	
   	    	if(wa.equals(bottom)){
   	    		p.strokeWeight(2);
   	    		p.stroke(255,0,0);
   	    	}
   	    	p.line(wx1,wy1,wx2,wy2);
   	    	p.strokeWeight(1);
   	    	p.stroke(0);
   	    }
		
		
	}
	
}
