/**
 * 
 */
package alex;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import network.Link;
import network.Wall;
import processing.core.PVector;

/**
 * @author Work
 *
 */
public class Tram {

	private List<Link> route;
	private int routeIndex;
	private boolean finish = false;

	private Wall left;
	private Wall right;
	private Wall top;
	private Wall bottom;
	
	private double length = 4;
	private double width = 2;
	
	private double x;
	private double y;
	private double phi;
	
	private int wishVelocity = 1;
	private double vX;
	private double vY;
	private final double tau = 0.5;
	
	/**
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 */
	
	public Tram(double x, double y, List<Link> route) {
		this.x = x;
		this.y = y;
		this.route = route;
		this.routeIndex = 0;
		
		setWalls();
	}

	
	public void update(){
		
		Link currentLink = this.route.get(routeIndex);
		
    // Berechnung der Wunschrichtung
    	
        double dx = currentLink.getTo().getX() - this.x;
    	double dy = currentLink.getTo().getY() - this.y;
    	
    	double dist = Math.sqrt(dx*dx+dy*dy);
    	dx /= dist;
    	dy /= dist;
		
    	double resultForceX = (dx* this.wishVelocity - vX) / this.tau;
    	double resultForceY = (dy* this.wishVelocity - vY) / this.tau;
    	
    	
        vX = vX + Simulation.TIME_STEP *(resultForceX);
        vY = vY + Simulation.TIME_STEP *(resultForceY);
        
        this.phi = Math.atan2(vY,vX);

	}
	
	
	public void move(){
        this.x = this.x + Simulation.TIME_STEP * this.vX;
        this.y = this.y + Simulation.TIME_STEP * this.vY;
        
        setWalls();
        
        Link currentLink = this.route.get(routeIndex);
        if (currentLink.hasVehicleReachedEndOfLink(this.x, this.y)) {
			moveVehicleToNextLinkOfRoute();
		}
	}
	
	private void setWalls() {
		this.left   = new Wall(this.x-(this.width/2), this.y - (this.length/2), this.x-(this.width/2), this.y + (this.length/2));
		this.right  = new Wall(this.x+(this.width/2), this.y - (this.length/2), this.x+(this.width/2), this.y + (this.length/2));
		this.top    = new Wall(this.x-(this.width/2), this.y - (this.length/2), this.x+(this.width/2), this.y - (this.length/2));
		this.bottom = new Wall(this.x-(this.width/2), this.y + (this.length/2), this.x+(this.width/2), this.y + (this.length/2));
		
		/*
		 * 		~~~~~~~~~~~WIDTH~~~~~~~~~~
		 * 
		 * 		-----------TOP----------- 				~
		 *		'						 '				~
		 *		'						 '				~
		 *		'						 '				~
		 *		'						 '				~
		 *		'						 '				~
		 *		L						 R				L
		 *		E						 I				E
		 *		F			X			 G				N
		 *		T						 H				G
		 *		'						 T				T
		 *		'						 '				H
		 *		'						 '				~
		 *		'						 '				~
		 *		'						 ' 				~
		 * 		----------BOTTOM----------				~
		 */
		
	
	}


	private void moveVehicleToNextLinkOfRoute() {
		routeIndex++;
		if (this.route.size() == routeIndex) {
           this.finish  = true;
        }
	}
	
	public Set<Wall> getWalls(){
		Set<Wall> walls = new HashSet<Wall>();
		walls.add(left);
		walls.add(right);
		walls.add(top);
		walls.add(bottom);
		return walls;
	}
	
	public double getX(){
		return this.x;
	}
	
	public double getY(){
		return this.y;
	}

	public double getPhi() {
		return this.phi;
	}
}
