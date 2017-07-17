/**
 * 
 */
package alex;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Generated;

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
	private double half_width = 0.5;
	
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

	
	public void update(List<Vehicle> vehicles){
		
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
        
    	isSomethingInTheWay(vehicles, null);
    	
        this.phi = Math.atan(vX/vY);

	}
	
	
	private void isSomethingInTheWay(List<Vehicle> vehicles, List<Tram> cars){
		
//		where is the center going to be
		double projectedX =  this.x + Simulation.TIME_STEP * this.v.x;
		double projectedY =  this.y + Simulation.TIME_STEP * this.v.y;
		
		double alpha = Math.acos( this.v.y / v.mag());
		
		double leftTopX = projectedX - (half_width * Math.cos(alpha)) + (half_length * Math.sin(alpha));
		double leftTopY = projectedY - (half_width * Math.sin(alpha)) - (half_length * Math.cos(alpha));
			
		double rightBottomX = projectedX + (half_width * Math.cos(alpha)) - (half_length * Math.sin(alpha));
		double rightBottomY = projectedY + (half_width * Math.sin(alpha)) + (half_length * Math.cos(alpha));
		
		double rightTopX = projectedX + (half_width * Math.cos(alpha)) + (half_length * Math.sin(alpha));
		double rightTopY = projectedY + (half_width * Math.sin(alpha)) - (half_length * Math.cos(alpha));
		
		double leftBottomX = projectedX - (half_width * Math.cos(alpha)) - (half_length * Math.sin(alpha));
		double leftBottomY = projectedY - (half_width * Math.sin(alpha)) + (half_length * Math.cos(alpha));

		float topLineCenterX = (float) (projectedX + (half_length * Math.sin(alpha)));
		float topLineCenterY = (float) (projectedY - (half_length * Math.cos(alpha)));

		float bottomLineCenterX = (float) (projectedX - (half_length * Math.sin(alpha)));
		float bottomLineCenterY = (float) (projectedY + (half_length * Math.cos(alpha)));
		
		double xMin = Double.MAX_VALUE;
		xMin = Math.min(xMin, leftTopX);
		xMin = Math.min(xMin, rightTopX);
		xMin = Math.min(xMin, rightBottomX);
		xMin = Math.min(xMin, leftBottomX);
		
		double xMax = Double.MIN_VALUE;
		xMax = Math.max(xMax, leftTopX);
		xMax = Math.max(xMax, rightTopX);
		xMax = Math.max(xMax, rightBottomX);
		xMax = Math.max(xMax, leftBottomX);

		double yMin = Double.MAX_VALUE;
		yMin = Math.min(yMin, leftTopY);
		yMin = Math.min(yMin, rightTopY);
		yMin = Math.min(yMin, rightBottomY);
		yMin = Math.min(yMin, leftBottomY);
		
		double yMax = Double.MIN_VALUE;
		yMax = Math.max(yMax, leftTopY);
		yMax = Math.max(yMax, rightTopY);
		yMax = Math.max(yMax, rightBottomY);
		yMax = Math.max(yMax, leftBottomY);

		float fractionToSubFromVector = Float.MAX_VALUE;
		double closestDistance = Double.MAX_VALUE;
		
		for (Vehicle v: vehicles){
			if(v.getX() >= xMin && v.getX() <= xMax && v.getY() >= yMin && v.getY() <= yMax){
				
				PVector topLineCenter = new PVector(topLineCenterX, topLineCenterY); 
				
				PVector lineThroughTram = new PVector(bottomLineCenterX, bottomLineCenterY);
				lineThroughTram.sub( topLineCenter );
				
				PVector vehicle = new PVector ((float) v.getX(), (float) v.getY());
				
				PVector topLineToVehicle = vehicle.get();
				topLineToVehicle.sub(topLineCenter);
				
				float c1 = PVector.dot(topLineToVehicle, lineThroughTram);
				float c2 = PVector.dot(lineThroughTram, lineThroughTram);
				
				if(c1 < 0) System.out.println("this should currently not happen ------------------------------------------");
				
				else if(c2 < c1) System.out.println("this should currently not happen ------------------------------------------");
				
				else{
					double distance = PVector.dist(vehicle, topLineCenter);
					if(distance < closestDistance){
						fractionToSubFromVector = (1-c1/c2);
					}
					else if(distance == closestDistance && (1-(c1/c2)) > fractionToSubFromVector){
						fractionToSubFromVector = (1-c1/c2);
					}
				}
			}
		}
		if(fractionToSubFromVector != Float.MAX_VALUE){
			this.v.mult(fractionToSubFromVector);
			System.out.println("A vehicle is in the way of a tram");
		}
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
	
	public Wall getRightWall(){
		return this.right;
	}
	
	public Wall getLeftWall(){
		return this.left;
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

	public float getLength() {
		// TODO Auto-generated method stub
		return (float) (this.half_length *2);
	}
	
	public float getWidth() {
		// TODO Auto-generated method stub
		return (float) (this.half_width *2);
	}
}
