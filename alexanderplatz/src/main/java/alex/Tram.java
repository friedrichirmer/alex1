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
import network.Node;
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
	
	private double half_length = 4;
	private double half_width = 1;
	
	private double centerX;
	private double centerY;
	
	private double wishVelocity = 5;
	PVector v = new PVector(0,0);
	private final double tau = 1;
	private double bottomCenterX;
	private double bottomCenterY;
	
	private double startTime = Double.MAX_VALUE;
	
	/**
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 */
	
	public Tram(double x, double y, List<Link> route, double startTime) {
		this.centerX = x;
		this.centerY = y;
		this.route = route;
		this.routeIndex = 0;
		this.startTime = startTime;
		calcVelocityVector();
		setWalls();
	}

	
	public void update(List<Vehicle> vehicles, KDTree kdTree){
		
		calcVelocityVector();
    	reactToVehiclesInWay(kdTree);

	}
	
	private void calcVelocityVector(){
		Link currentLink = this.route.get(routeIndex);
		
		// Berechnung der Wunschrichtung vom Fahrzeugmittelpunkt aus
		double dx = currentLink.getTo().getX() - this.centerX;
    	double dy = currentLink.getTo().getY() - this.centerY;
    	
    	double dist = Math.sqrt(dx*dx+dy*dy);
    	dx /= dist;
    	dy /= dist;
    	
    	double resultForceX = (dx* this.wishVelocity);
    	double resultForceY = (dy* this.wishVelocity);
    	
    	this.v = new PVector( (float) ((resultForceX)) , (float) ((resultForceY)) )  ;
    	
	}
	
	private void reactToVehiclesInWay(KDTree kdtree){
		
//		where is the center going to be in 0.5 second ! (not dependent on time step)
		double projectedX =  this.centerX +  0.3 * this.v.x;
		double projectedY =  this.centerY +  0.3 * this.v.y;

		//rotation angle
		double alpha;
		if(this.v.x >= 0){
			if(this.v.y >= 0){
				alpha =  - Math.acos( this.v.y / v.mag() );
			}
			else{
				alpha =  - Math.acos( this.v.y / v.mag() );
			}
		}	else{
			if(this.v.y >= 0){
				alpha =  + Math.acos( this.v.y / v.mag() );
			} else{
				alpha = Math.acos( this.v.y / v.mag() );
			}
		}
		
		double leftTopX = projectedX - (half_width * Math.cos(alpha)) + (half_length * Math.sin(alpha));
		double leftTopY = projectedY - (half_width * Math.sin(alpha)) - (half_length * Math.cos(alpha));
			
		double rightBottomX = projectedX + (half_width * Math.cos(alpha)) - (half_length * Math.sin(alpha));
		double rightBottomY = projectedY + (half_width * Math.sin(alpha)) + (half_length * Math.cos(alpha));
		
		double rightTopX = projectedX + (half_width * Math.cos(alpha)) + (half_length * Math.sin(alpha));
		double rightTopY = projectedY + (half_width * Math.sin(alpha)) - (half_length * Math.cos(alpha));
		
		double leftBottomX = projectedX - (half_width * Math.cos(alpha)) - (half_length * Math.sin(alpha));
		double leftBottomY = projectedY - (half_width * Math.sin(alpha)) + (half_length * Math.cos(alpha));

		float bottomLineCenterX = (float) (projectedX - (half_length * Math.sin(alpha)));
		float bottomLineCenterY = (float) (projectedY + (half_length * Math.cos(alpha)));
		
		float topLineCenterX = (float) (projectedX + (half_length * Math.sin(alpha)));
		float topLineCenterY = (float) (projectedY - (half_length * Math.cos(alpha)));
		
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
		
		List<Vehicle> vehiclesAboutToCrash = 
				kdtree.getClosestNeighboursToPoint(topLineCenterX, topLineCenterY, 1, xMin, yMin, xMax, yMax);
		if(vehiclesAboutToCrash.size() == 0){
			System.out.println("+++no pedestrian in the way+++");
		}else{
//			Vehicle v = vehiclesAboutToCrash.get(0);
//			double distanceVehicleToFront = Math.sqrt( (Math.pow( (v.getX() - bottomLineCenterX), 2) ) + (Math.pow( (v.getY() - bottomLineCenterY), 2) ) );
//			
//			PVector vCopy = this.v.get();
//			vCopy.normalize();
//			vCopy.mult((float)(distanceVehicleToFront));
//			if(vCopy.mag() > this.v.mag()){
//				this.v.mult(0.001f);
//			}else{
//				this.v.sub(vCopy);
//			}
//			
			this.v.mult(0.0001f);
		}
	}
	
	public void move(){
//        this.x = this.x + Simulation.TIME_STEP * this.vX;
//        this.y = this.y + Simulation.TIME_STEP * this.vY;
        this.centerX = this.centerX + Simulation.TIME_STEP * this.v.x;
        this.centerY = this.centerY + Simulation.TIME_STEP * this.v.y;
        
        setWalls();
        
        Link currentLink = this.route.get(routeIndex);
        if (currentLink.hasVehicleReachedEndOfLink(this.centerX, this.centerY)) {
			moveVehicleToNextLinkOfRoute();
			System.out.println("----------------------------------MOVE TO NEXT LINK -------------------------------------------------");
		}
	}
	
	private void setWalls() {
		
		double alpha;
		if(this.v.x >= 0){
			if(this.v.y >= 0){
				alpha =  - Math.acos( this.v.y / v.mag() );
			}
			else{
				alpha =  - Math.acos( this.v.y / v.mag() );
			}
		}	else{
			if(this.v.y >= 0){
				alpha =  + Math.acos( this.v.y / v.mag() );
			} else{
				alpha = Math.acos( this.v.y / v.mag() );
			}
		}
		double rightBottomX = this.centerX + (half_width * Math.cos(alpha)) - (half_length * Math.sin(alpha));
		double rightBottomY = this.centerY + (half_width * Math.sin(alpha)) + (half_length * Math.cos(alpha));
		
		double rightTopX = this.centerX + (half_width * Math.cos(alpha)) + (half_length * Math.sin(alpha));
		double rightTopY = this.centerY + (half_width * Math.sin(alpha)) - (half_length * Math.cos(alpha));
		
		double leftBottomX = this.centerX - (half_width * Math.cos(alpha)) - (half_length * Math.sin(alpha));
		double leftBottomY = this.centerY - (half_width * Math.sin(alpha)) + (half_length * Math.cos(alpha));
		
		double leftTopX = this.centerX - (half_width * Math.cos(alpha)) + (half_length * Math.sin(alpha));
		double leftTopY = this.centerY - (half_width * Math.sin(alpha)) - (half_length * Math.cos(alpha));
		
		this.right   = new Wall(rightTopX, rightTopY, rightBottomX, rightBottomY);
		this.left   = new Wall(leftTopX, leftTopY, leftBottomX, leftBottomY);
		this.top    = new Wall(leftTopX, leftTopY, rightTopX, rightTopY);
		this.bottom = new Wall(leftBottomX, leftBottomY, rightBottomX, rightBottomY);
		
		bottomCenterX = this.centerX - (half_length * Math.sin(alpha));
		bottomCenterY = this.centerY + (half_length * Math.cos(alpha));
		
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
		 *		E			C			 I				E
		 *		F						 G				N
		 *		T						 H				G
		 *		'						 T				T
		 *		'						 '				H
		 *		'						 '				~
		 *		'						 '				~
		 *		'						 ' 				~
		 * 		----------BOTTOM----------				~
		 */
		
	
	}

    /**
     * checks whether the vehicle should enter the sim.
     * so basically it checks whether vehicle's start time is smaller than given time
     * @return
     */
    public boolean entersSimulation(double time){
    	return (this.startTime <= time);
    }

	private void moveVehicleToNextLinkOfRoute() {
		routeIndex++;
		if (this.route.size() == routeIndex) {
           this.finish  = true;
           System.out.println("----------------------FINISHED------------------------");
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
	
	public Wall getBottomWall() {
		return this.bottom;
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
		return this.centerX;
	}
	
	public double getY(){
		return this.centerY;
	}

	public float getLength() {
		// TODO Auto-generated method stub
		return (float) (this.half_length *2);
	}
	
	public float getWidth() {
		// TODO Auto-generated method stub
		return (float) (this.half_width *2);
	}

	public float getBottomY() {
		return (float)(this.bottomCenterY);
	}

	public float getBottomX() {
		return (float)(this.bottomCenterX);
	}

	public Node getDestinationNode(){
		return this.route.get(this.route.size()-1).getTo();
	}
}
