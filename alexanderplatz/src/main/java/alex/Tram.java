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
	
	private double half_length = 4;
	private double half_width = 2;
	
	private double centerX;
	private double centerY;
	private double phi;
	
	private double wishVelocity = 1000;
	PVector v = new PVector(0,0);
	PVector vFront = new PVector(0,0);
	private double vX;
	private double vY;
	private final double tau = 1;
	private double bottomCenterX;
	private double bottomCenterY;
	
	/**
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 */
	
	public Tram(double x, double y, List<Link> route) {
		this.centerX = x;
		this.centerY = y;
		this.route = route;
		this.routeIndex = 0;
		setWalls();
	}

	
	public void update(List<Vehicle> vehicles){
		
		Link currentLink = this.route.get(routeIndex);
		
		// Berechnung der Wunschrichtung vom Fahrzeugmittelpunkt aus
		double dx = currentLink.getTo().getX() - this.centerX;
    	double dy = currentLink.getTo().getY() - this.centerY;

    	vFront = new PVector((float) (currentLink.getTo().getX()- this.bottomCenterX) , (float) (currentLink.getTo().getY() - this.bottomCenterY) );
    	vFront.normalize();
    	vFront.mult((float) wishVelocity);
		// Berechnung der Wunschrichtung von der Mitte der Fahrzeugfront aus
//        double dx = currentLink.getTo().getX() - bottomCenterX;
//    	double dy = currentLink.getTo().getY() - bottomCenterY;
    	
    	double dist = Math.sqrt(dx*dx+dy*dy);
    	dx /= dist;
    	dy /= dist;
		
//    	double resultForceX = (dx* this.wishVelocity - vX) / this.tau;
//    	double resultForceY = (dy* this.wishVelocity - vY) / this.tau;
    	
    	double resultForceX = (dx* this.wishVelocity);
    	double resultForceY = (dy* this.wishVelocity);
    	
   
//    	this.v.add( new PVector( (float) (Simulation.TIME_STEP *(resultForceX)) , (float) (Simulation.TIME_STEP *(resultForceY)) ) ) ;

    	//von der Mitte aus
    	this.v = new PVector( (float) (Simulation.TIME_STEP *(resultForceX)) , (float) (Simulation.TIME_STEP *(resultForceY)) )  ;
    	
    	//von der Front aus
//    	this.v = vFront.get();
    	
//        vX = vX + Simulation.TIME_STEP *(resultForceX);
//        vY = vY + Simulation.TIME_STEP *(resultForceY);
        
    	isSomethingInTheWay(vehicles, null);
    	
        this.phi = Math.atan(vX/vY);

	}
	
	
	private void isSomethingInTheWay(List<Vehicle> vehicles, List<Tram> cars){
		
//		where is the center going to be
		double projectedX =  this.centerX + Simulation.TIME_STEP * this.v.x;
		double projectedY =  this.centerY + Simulation.TIME_STEP * this.v.y;
		//rotation angle
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
		
		float leftLineCenterX = (float) (projectedX - (half_width * Math.cos(alpha))) ;
		float leftLineCenterY = (float) (projectedY - (half_width * Math.sin(alpha)));

		float rightLineCenterX = (float) (projectedX + (half_width * Math.cos(alpha))) ;
		float rightLineCenterY = (float) (projectedY + (half_width * Math.sin(alpha))) ;
				
		
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
			
				//calculate if vehicles is within future width range
				PVector leftLineCenter = new PVector(leftLineCenterX, leftLineCenterY); 
				
				PVector lineThroughTramHorizontal = new PVector(rightLineCenterX, rightLineCenterY);
				lineThroughTramHorizontal.sub( leftLineCenter );
				
				PVector vehicle = new PVector ((float) v.getX(), (float) v.getY());
				
				PVector leftLineToVehicle = vehicle.get();
				leftLineToVehicle.sub(leftLineCenter);
				
				float cH1 = PVector.dot(leftLineToVehicle, lineThroughTramHorizontal);
				float cH2 = PVector.dot(lineThroughTramHorizontal, lineThroughTramHorizontal);
				
				
				if(cH1 >= 0 && cH2 >= cH1){	//vehicle is in future width range
//					PVector horizontalLot = lineThroughTramHorizontal.get();
//					horizontalLot.mult(cH1/cH2);
//					horizontalLot.add(leftLineCenter);
//					System.out.println("vehicle is in future width range");
//					if(horizontalLot.mag() <= this.half_length * 2){
//						System.out.println("vehicle is in future width Range and closer than the length");
						if(v.getX() >= xMin && v.getX() <= xMax && v.getY() >= yMin && v.getY() <= yMax){
							System.out.println("vehicle is in future width range AND in EXPANDED FUTURE RANGE");
							PVector topLineCenter = new PVector(topLineCenterX, topLineCenterY); 
							
							PVector lineThroughTram = new PVector(bottomLineCenterX, bottomLineCenterY);
							lineThroughTram.sub( topLineCenter );
							
							PVector topLineToVehicle = vehicle.get();
							topLineToVehicle.sub(topLineCenter);
							
							float c1 = PVector.dot(topLineToVehicle, lineThroughTram);
							float c2 = PVector.dot(lineThroughTram, lineThroughTram);
							
							if(c1 >= 0 && c2 >= c1){
								System.out.println("A vehicle is in the way of a tram");
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
//				}
			}
			if(fractionToSubFromVector != Float.MAX_VALUE){
				this.v.mult(fractionToSubFromVector);
				System.out.println("v angepasst");
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
		}
	}
	
	private void setWalls() {
		
		double alpha = Math.acos( this.v.y / v.mag());
		
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


	public float getBottomY() {
		return (float)(this.bottomCenterY);
	}

	public float getBottomX() {
		return (float)(this.bottomCenterX);
	}

}
