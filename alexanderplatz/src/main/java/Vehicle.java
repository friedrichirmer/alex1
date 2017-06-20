/* *********************************************************************** *
 * project: simsocsys
 *
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 * copyright       : (C) 2016 by the members listed in the COPYING,        *
 *                   LICENSE and WARRANTY file.                            *
 * email           : gregor dot laemmel at gmail dot org                                *
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *   See also COPYING, LICENSE and WARRANTY file                           *
 *                                                                         *
 * *********************************************************************** */


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import processing.core.PVector;

/**
 * Created by laemmel on 18/04/16.
 */

public class Vehicle {


    private final List<Link> route;
    private double vtx = 0;
    private double vty = 0;
    private double vx = 0;
    private double vy = 0;
    private double forceX = 0;
    private double forceY = 0;
    private double length = 0.2;
    private double width = 0.2;
    private double speed = 1;
    private double maxSpeed = 2;
    private double rad;
    private double x;
    private double y;
    private double phi = 0;//radian!!
    private float colourR;
    private float colourG;
    private float colourB;
    double pushX = 0;
    double pushY = 0;


    private int routeIndex = 0;
	private Network net;
	private boolean finish;
	private int counterV = 1;
	private static Random random = new Random(41);
	
	public final Map<Vehicle,PVector> forces = new HashMap<Vehicle,PVector>();
	
	private PVector forceWalls;
	private PVector forceVehicles;
	private PVector forceTarget;
	double pushWallX;
	double pushWallY;
	

	
    public Vehicle(Node src, Node dstn, Network net) {
    	
    	this.x = src.getX();
        this.y = src.getY();
        this.net = net;
        this.route = Dijkstra.dijkstra(net, src, dstn);
    	System.out.println("route: " + route.toString());
        this.rad = 0.25 + random.nextDouble()*0.1;
        this.colourR = (float) (255*Math.random());
        this.colourG = (float) (255*Math.random());
        this.colourB = (float) (255*Math.random());
        this.maxSpeed = 2;
        this.vtx = 0;
        this.finish = false;     
        
    }
    
    /**
     * !! Dieser Konstruktor ist nur zum einfacheren Testen von KDTree gedacht und sollte i.A. nicht benutzt werden!!
     * @param x
     * @param y
     */
    public Vehicle(double x, double y){
    	this.x = x;
        this.y = y;
        this.route = null;
    }

    public void update(List<Vehicle> vehs) {
    	
    	Link currentLink = this.route.get(routeIndex);
    	
    	//System.out.println("x: " + this.x + "   y: " + this.y);
        	
        pushX = 0;
        pushY = 0;
        
        pushWallX = 0;
    	pushWallY = 0;
        
        for (int i=0; i<vehs.size(); i++) {
        	
        	Vehicle v;
        	
           	v = vehs.get(i);
        	if (this == v) continue;  
        	
        	double vehx;
        	double vehy;
        	double radS;
        	double distMX;
        	double distMY;
        	double distM;	// Abstand der Mittelpunkte
        	double distR;	// Abstand der Radien
       
        	double g;
 
        	vehx = v.x;
        	vehy = v.y;
        	
        	radS = (rad + v.getRadius());
        	distMX = (vehx - this.x);
        	distMY = (vehy - this.y);
        	distM = Math.sqrt(distMX*distMX + distMY*distMY);
        	distR = radS - distM;
   
           	if (distR >= 0) g = 1;
        	else g=0;
           	
           	
        	
        	double dirNX = (-distMX) / distM; 		//richtungsvektor n_x
        	double dirTX = (distMY) / distM;		//richtungsvektor t_x
        	double dirNY = (-distMY) / distM;		//richtungsvektor n_y
        	double dirTY = (-distMX) / distM;		//richtungsvektor t_y
        	double vDiff = (v.vtx - this.vtx) * dirTX + (v.vty - this.vty) * dirTY;
        	
        	
        	pushX = pushX + 
        			(2000*Math.exp(distR/0.08)+120000*g*distR)*dirNX +
        			240000*g*distR*vDiff*dirTX;
        	
        	pushY = pushY + 
        			(2000*Math.exp(distR/0.08)+120000*g*distR)*dirNY + 
        			240000*g*distR*vDiff*dirTY; 
       
        }
        
        forceVehicles = new PVector((float)pushX, (float)pushY);
               
        
        for (int i =0;i<net.walls.size();i++ ) {
        	//System.out.println(net.walls.size());
        	Wall w = net.walls.get(i);
        	
        	//if (w.getRoom() == currentLink.getRoom()) {
        		
        	      			    		   
        			    		   
        		PVector wallA = new PVector ((float)w.getX1(),(float) w.getY1());
        		PVector wallB = new PVector ((float)w.getX2(),(float) w.getY2());
        		PVector vehic = new PVector ((float)this.x,(float) this.y); 
        		
        		PVector vecv = wallB.get();
        		vecv.sub(wallA);
        		PVector vecw = vehic.get();
        		vehic.sub(wallA);
        		PVector vecw2 = vehic.get();
        		vehic.sub(wallB);  
        		
        		float c1 = 0;
     			float c2 = 0; 
     		
     			c1 = vecw.dot(vecv);
     			c2 = vecv.dot(vecv);
     			
     			double lot;
     			double radlot = 1;
     			double length = 1;
   				PVector t = null; 
        		PVector n = null;
        		
        		
     			
				if ( c1 <= 0 ) {
     			    lot = PVector.dist(vehic, wallA);
					radlot = this.getRadius() - lot;
					length = vecv.mag();
					n = vecw.get();
					n.normalize();
					t = n.get();
            	   
				}
				
				
     			if ( c2 <= c1 ) {
     				lot = PVector.dist(vehic, wallB);
     				radlot = this.getRadius() - lot;
     				length = vecv.mag();
     				
     				n = vecw2.normalize(n);
            	    t = n.get();
     			}
     			
     			if ((c1 > 0) && (c1 < c2)) {
     				     				
     				float b = c1 / c2;
     				
     				PVector bv = PVector.mult(vecv, b);
     				PVector Pb = PVector.add(wallA, bv);
     				     				
     				lot = PVector.dist(vehic, Pb);
     				radlot = this.getRadius() - lot;
     				length = vecv.mag();
     			
            		n = Pb.normalize(n);
            	    t = n.get();
            	    
            	    if (i == 4) {
         				System.out.println(vehs.indexOf(this)+ " : " + c1 + "  -  " + c2);
         				System.out.println(Pb);
         			}
            	
     			}
     			
     			
     			    		   
     			//System.out.println("vor  "+t);
        	    //n.rotate((float)((Math.PI)));
        	    t.rotate((float)((Math.PI)/2));
        	    //System.out.println("nach  "+t);
        	    
        		double g;
        		if (radlot >= 0) {
        			g = 1;
        			System.out.println("g=1");
        		}
        		
        		else   	g=0;
        		 	
//            	double dirTX = (w.getX2()-w.getX1()) / length; 		
//            	double dirNX = (w.getX2()-w.getX1()) / length;		
//            	double dirNY = (w.getY2()-w.getY1()) / length;		
//            	double dirTY = -(w.getY2()-w.getY1()) / length;		

            	double vdifx = this.vtx * t.x;
            	double vdify = this.vty * t.y;

//				if ((w.getId() == 2)) {
//					System.out.println("person " + vehs.indexOf(this) + "  " + radlot);
//					// System.out.println("t: " + t);
//					System.out.println("n:   " + n.heading());
//				}

				pushWallX = pushWallX + 
        			(2000*Math.exp(radlot/0.08)+120000*g*radlot)*n.x +
        			240000*g*radlot*vdifx*t.x;
        	
            	pushWallY = pushWallY + 
        			(2000*Math.exp(radlot/0.08)+120000*g*radlot)*n.y + 
        			240000*g*radlot*vdify*t.y; 
           	
        		}

                
        forceWalls = new PVector((float)pushWallX, (float)pushWallY);

        double dx = currentLink.getTo().getX() - this.x;
        double dy = currentLink.getTo().getY() - this.y;

        double dist = Math.sqrt(dx*dx+dy*dy);
        dx /= dist;
        dy /= dist;
        
        forceTarget = new PVector((float) (85 * (dx * this.speed - vtx) / 0.5), (float) (85 * (dy * this.speed - vty) / 0.5 ));
        //forceTarget.normalize();
                 
        forceX = (85 * (dx * this.speed - vtx) / 0.5) + pushX + pushWallX ;
        forceY = (85 * (dy * this.speed - vty) / 0.5) + pushY + pushWallY ;
        
        
                
        vx = vtx + Simulation.H*(forceX/80);
        vy = vty + Simulation.H*(forceY/80);
        
        //	Begrenzung der Kräfte
        if (Math.sqrt((vx*vx)+(vy*vy)) > 3) {
        	double speed = Math.sqrt((vx*vx)+(vy*vy));
        	vx = Math.sqrt(speed) * vx / speed ;
        	vy = Math.sqrt(speed) * vy / speed ;
        }
        
        
        this.phi = Math.atan2(vy,vx);
        
    }
    
    public void move() {

        this.x = this.x + Simulation.H * this.vx;
        this.y = this.y + Simulation.H * this.vy;
        
        vtx = vx;
        vty = vy;
        
        Link currentLink = this.route.get(routeIndex);
        if (currentLink.hasVehicleReachedEndOfLink(this)) {
            routeIndex++; 
            System.out.println("neue route: " + this.routeIndex);
        	if (this.route.size() == routeIndex) {
        	   this.finish = true;
        	}
        }

    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getPhi() {
        return phi;
    }

    public double getWidth() {
        return width;
    }

    public double getLength() {
        return length;
    }

	public double getRadius() {
		return rad;
	}
	
	public double getColourR() {
		return colourR;
	}
	
	public double getColourG() {
		return colourG;
	}
	
	public double getColourB() {
		return colourB;
	}
	
	public boolean getFinish() {
		return finish;
	}

	public PVector getForceTarget() {
		return forceTarget;
	}

	public PVector getForceVehicles() {
		// TODO Auto-generated method stub
		return forceVehicles;
	}

	public PVector getForceWalls() {
		// TODO Auto-generated method stub
		return forceWalls;
	}

	//public int getId() {
	//	return id;
	//}

}
