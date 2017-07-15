package alex;

import java.util.Iterator;
import java.util.Set;

import network.Wall;
import processing.core.PApplet;

public class TramInfo {

	private Set<Wall> walls;
    
    private final double phi;
    
	public TramInfo(Set<Wall> walls, double phi) {
		this.walls = walls;
		this.phi = phi;
	}

	public void draw(PApplet p){
		drawWalls(p);
	}
	
	private void drawWalls(PApplet p) {
		Iterator<Wall> it = walls.iterator();
		while(it.hasNext()){
			Wall wa = it.next();
   	    	float wx1 = (float)(wa.getX1()* Vis.scale) ;
   	    	float wy1 = (float)(wa.getY1()* Vis.scale) ;
   	    	float wx2 = (float)(wa.getX2()* Vis.scale) ;
   	    	float wy2 = (float)(wa.getY2()* Vis.scale) ;
   	    	

   	    	p.strokeWeight(1);
   	    	p.line(wx1,wy1,wx2,wy2);
   	    	p.strokeWeight(1);
   	    }
	}
	
}
