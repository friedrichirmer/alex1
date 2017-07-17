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
	
	private double half_length = 2;
	private double half_width = 1;
	
	private double x;
	private double y;
	private double phi;
	
	private int wishVelocity = 1;
	PVector v = new PVector(0,0);
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
    	
   
    	this.v.add( new PVector( (float) (Simulation.TIME_STEP *(resultForceX)) , (float) (Simulation.TIME_STEP *(resultForceY)) ) ) ;
//        vX = vX + Simulation.TIME_STEP *(resultForceX);
//        vY = vY + Simulation.TIME_STEP *(resultForceY);
        
        this.phi = Math.atan(vX/vY);

	}
	
	
	public void move(){
//        this.x = this.x + Simulation.TIME_STEP * this.vX;
//        this.y = this.y + Simulation.TIME_STEP * this.vY;
        this.x = this.x + Simulation.TIME_STEP * this.v.x;
        this.y = this.y + Simulation.TIME_STEP * this.v.y;
        
        setWalls();
        
        Link currentLink = this.route.get(routeIndex);
        if (currentLink.hasVehicleReachedEndOfLink(this.x, this.y)) {
			moveVehicleToNextLinkOfRoute();
		}
	}
	
	private void setWalls() {
		
		double alpha = Math.acos( this.v.y / v.mag());
		
		
//		PVector middle = new PVector((float) this.x, (float) this.y);
//		
//		
//		PVector vNormalized = v.get();
//		vNormalized.normalize();
//		
//		
//		PVector vTimesLength = vNormalized.get();
//		vTimesLength.mult( (float) length/2 );
//		
//		PVector vTimesLengthToTop = vTimesLength.get();
//		vTimesLengthToTop.mult(-1);
//		
//		PVector fromBottomToRightPoint = new PVector( (float)(Math.cos(alpha)*width/2), - (float)(Math.sin(alpha)*width/2));
//		PVector fromBottomToLeftPoint = new PVector( - (float)(Math.cos(alpha)*width/2), (float)(Math.sin(alpha)*width/2));
//		
//		PVector rightBottomCorner = middle.get();
//		rightBottomCorner.add(vTimesLength);
//		rightBottomCorner.add(fromBottomToRightPoint);
//
//		
//		PVector rightTopCorner = middle.get();
//		rightTopCorner.add(vTimesLengthToTop);
//		rightTopCorner.add(fromBottomToRightPoint);
//
//		PVector leftBottomCorner = middle.get();
//		leftBottomCorner.add(vTimesLength);
//		leftBottomCorner.add(fromBottomToLeftPoint);
//
//		
//		PVector leftTopCorner = middle.get();
//		leftTopCorner.add(vTimesLengthToTop);
//		leftTopCorner.add(fromBottomToLeftPoint);
		

//		this.right  = new Wall(rightTopCorner.x,rightTopCorner.y, rightBottomCorner.x, rightBottomCorner.y);
//		this.left   = new Wall(leftTopCorner.x,leftTopCorner.y, leftBottomCorner.x, leftBottomCorner.y);
//		this.top    = new Wall(leftTopCorner.x,leftTopCorner.y,rightTopCorner.x,rightTopCorner.y);
//		this.bottom = new Wall(leftBottomCorner.x,leftBottomCorner.y,rightBottomCorner.x,rightBottomCorner.y);
		
		double rightBottomX = this.x + (half_width * Math.cos(alpha)) - (half_length * Math.sin(alpha));
		double rightBottomY = this.y + (half_width * Math.sin(alpha)) + (half_length * Math.cos(alpha));
		
		double rightTopX = this.x + (half_width * Math.cos(alpha)) + (half_length * Math.sin(alpha));
		double rightTopY = this.y + (half_width * Math.sin(alpha)) - (half_length * Math.cos(alpha));
		
		double leftBottomX = this.x - (half_width * Math.cos(alpha)) - (half_length * Math.sin(alpha));
		double leftBottomY = this.y - (half_width * Math.sin(alpha)) + (half_length * Math.cos(alpha));
		
		double leftTopX = this.x - (half_width * Math.cos(alpha)) + (half_length * Math.sin(alpha));
		double leftTopY = this.y - (half_width * Math.sin(alpha)) - (half_length * Math.cos(alpha));
		
		this.right   = new Wall(rightTopX, rightTopY, rightBottomX, rightBottomY);
		this.left   = new Wall(leftTopX, leftTopY, leftBottomX, leftBottomY);
		this.top    = new Wall(leftTopX, leftTopY, rightTopX, rightTopY);
		this.bottom = new Wall(leftBottomX, leftBottomY, rightBottomX, rightBottomY);
		
		
//		this.right   = new Wall(this.x + half_width/2, this.y -half_length/2, this.x + half_width/2, this.y + half_length/2);
//		this.left   = new Wall(this.x - half_width/2, this.y -half_length/2, this.x -half_width/2, this.y + half_length/2);
//		this.top    = new Wall(this.x-half_width/2, this.y - half_length/2, this.x+ half_width/2, this.y - half_length/2);
//		this.bottom = new Wall(this.x-half_width/2, this.y + half_length/2, this.x+half_width/2, this.y + half_length/2);
		
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
	
	public boolean isFinished(){
		return this.finish;
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
