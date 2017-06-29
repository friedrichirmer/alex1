package alex;
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


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import networkUtils.Link;
import networkUtils.Network;
import networkUtils.Node;
import networkUtils.Wall;
import processing.core.PVector;

/**
 */

public class Vehicle {


    private final List<Link> route;
    private double startTime;
    private double vtx = 0;
    private double vty = 0;
    private double vx = 0;
    private double vy = 0;
    private double forceX = 0;
    private double forceY = 0;
    private double length = 0.2;
    private double width = 0.2;
    private double speed = 1;
    private double maxSpeed = 3;
    private double tau = 0.5;
    private double rad;
    private double x;
    private double y;
    private double phi = 0;//radian!!
    private float colourR;
    private float colourG;
    private float colourB;
    double pushX = 0;
    double pushY = 0;

    private String id;

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
    private boolean isInTheSimulation = false;
	private double mass;
	private double walkParallelThreshold = Double.MAX_VALUE;
	

	
    public Vehicle(Network network, Node startNode, Node destinationNode, double startTime, String id) {
    	
    	this.x = startNode.getX() + random.nextDouble() * 1;
        this.y = startNode.getY() + random.nextDouble() * 1;
        this.net = network;
        this.route = Dijkstra.dijkstra(network, startNode, destinationNode);
    	System.out.println("route: " + route.toString());
        this.rad = 0.25 + random.nextDouble()*0.1;
        this.colourR = (float) (255*Math.random());
        this.colourG = (float) (255*Math.random());
        this.colourB = (float) (255*Math.random());
        this.maxSpeed = 2;
        this.vtx = 0;
        this.finish = false;     
        this.id = id;
        this.startTime = startTime;
        this.mass = 80;
    }

    public boolean isInTheSimulation() {

        return isInTheSimulation;
    }

    /**
     * !! Dieser Konstruktor ist nur zum einfacheren Testen von alex.KDTree gedacht und sollte i.A. nicht benutzt werden!!
     * @param x
     * @param y
     */
    public Vehicle(double x, double y, int id){
    	this.x = x;
        this.y = y;
        this.route = null;
        this.id = String.valueOf(id);
    }


    public void update(List<Vehicle> vehs, double time) {

        if ( startTime > time) {
            return;
        }
        isInTheSimulation = true;
    	
    	Link currentLink = this.route.get(routeIndex);
    	
    	//System.out.println("x: " + this.x + "   y: " + this.y);
        	
        pushX = 0;
        pushY = 0;
        
        pushWallX = 0;
    	pushWallY = 0;
        
    	/*
    	 * Die erste Schleife iteriert über alle per Parameter vehs übergebenen Fahrzeuge und summiert die abstoßenden Kräfte.
    	 * Hier haben Kräfte und Vektoren haben je eine x- und eine y-Komponente.
    	 */
    	   	
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
               
        /*
         * Die zweite Schleife iteriert über alle Wände und berechnet die abstoßenden Kräfte
         * Hier wird mit Vektoren gerechnet
         */
        
        for (int i =0;i<net.walls.size();i++ ) {

        	Wall w = net.walls.get(i);
        	
        		PVector wallA = new PVector ((float)w.getX1(),(float) w.getY1()); 	//Ende 1 der Wand
        		PVector wallB = new PVector ((float)w.getX2(),(float) w.getY2());	//Ende 2 der Wand
        		PVector vehic = new PVector ((float)this.x,(float) this.y); 		//Position des Fahrzeugs
        		
        
        		
        		PVector vecv = wallB.get();			// Vektor zeigt von Ende 1 auf Ende 2 der Wand
        		vecv.sub(wallA);		
        		PVector vecw = vehic.get();			// Vektor zeigt von Ende 1 auf Fahrzeug
        		vecw.sub(wallA);
        		PVector vecw2 = vehic.get();		// Vektor zeigt von Ende 2 auf Fahrzeug
        		vecw2.sub(wallB);  
        		
        		float c1 = 0;	// Hilfsgröße zur Bestimmung der Position relativ zur Wand
     			float c2 = 0; 	// Hilfsgröße zur Bestimmung der Position relativ zur Wand
     		
     			c1 = vecw.dot(vecv);
     			c2 = vecv.dot(vecv);
     			
     			double lot;		// Länge des Lots
     			double radlot = 1;	// Radius des Fahrzeugs minus Länge des Lots (meistens negativ)

   				PVector t = null; 	// tangetialer Richtungsvektor für die Kraft
        		PVector n = null;	// normaler Richtungsvektor für die Kraft
        		
        		/*
        		 * Fall 1:
        		 * Die Kraft zeigt von der einen Ecke der Wand
        		 */
     			
				if ( c1 <= 0 ) {
     			    lot = PVector.dist(vehic, wallA);
					radlot = this.getRadius() - lot;
					length = vecv.mag();
					n = vecw.get();
					n.normalize();
					t = n.get();
            	   
				}
				
				/*
				 * Fall 2:
				 * Die Kraft zeigt von der anderen Ecke der Wand
				 */
				
     			if ( c2 <= c1 ) {
     				lot = PVector.dist(vehic, wallB);
     				radlot = this.getRadius() - lot;
     				length = vecv.mag();
     				
     				n = vecw2.get();
     				n.normalize();
            	    t = n.get();
     			}
     			
     			/*
     			 * Fall 3:
     			 * Die Kraft zeigt vom Lot senkrecht auf das Fahrzeug
     			 */
     			
     			if ((c1 > 0) && (c1 < c2)) {
     				     				
     				float b = c1 / c2;
     				     				
     				PVector bv = PVector.mult(vecv, b);
     				PVector pb = PVector.add(wallA, bv); 	// Vektor pb ist der Lotfußpunkt auf der Wand
     				
     				PVector pbV = vehic.get();				// Vektor pbV zeigt vom Lotfußpunkt auf das Fahrzeug (Richtung der Kraft)
     				pbV.sub(pb);
     				     				
     				lot = PVector.dist(vehic, pb);
     				radlot = this.getRadius() - lot;
     				length = vecv.mag();
     				
     				n = pbV.get();
     			    n.normalize();
            	    t = n.get();
            	    
            	    }

        	    t.rotate((float)((Math.PI)/2));
        	    
        		double g;
        		if (radlot >= 0) {
        			g = 1;
        			
        		}
        		
        		else   	g=0;
        		 	
            	double vdifx = this.vtx * t.x;
            	double vdify = this.vty * t.y;

				pushWallX = pushWallX + 
        			(2000*Math.exp(radlot/0.08)+120000*g*radlot)*n.x +
        			240000*g*radlot*vdifx*t.x;
        	
            	pushWallY = pushWallY + 
        			(2000*Math.exp(radlot/0.08)+120000*g*radlot)*n.y + 
        			240000*g*radlot*vdify*t.y; 
           	
        		}

                
        forceWalls = new PVector((float)pushWallX, (float)pushWallY);

        
        
        // Berechnung der Wunschrichtung
        
    	
        double dx = currentLink.getTo().getX() - this.x;
    	double dy = currentLink.getTo().getY() - this.y;
    	
    	double dist = Math.sqrt(dx*dx+dy*dy);
    	dx /= dist;
    	dy /= dist;
        
        if(this.walkParallelThreshold  < Double.MAX_VALUE){
        	// wir müssen hier das Lot vom Vehicle auf den aktuellen Link fällen
        	// siehe http://geomalgorithms.com/a02-_lines.html
        	
        	PVector vFromNode = new PVector((float) currentLink.getFrom().getX(), (float) currentLink.getFrom().getY());
        	PVector vToNode = new PVector((float) currentLink.getTo().getX(), (float) currentLink.getTo().getY());
    		PVector vVehicle = new PVector ((float)this.x,(float) this.y); 		//Position des Fahrzeugs

        	
        	PVector vBackLink = vToNode.get();
        	vBackLink.sub(vToNode);
        	
        	PVector vehToFromNode = vVehicle.get();
        	vehToFromNode.sub(vFromNode);
        	
        	float c1 = PVector.dot(vehToFromNode, vBackLink);
        	
        	float c2 = PVector.dot(vBackLink, vBackLink);
        	
//        	if(c2 <= c1){ //Vehicle befindet sich schon über den toNode (bzw Zielline) hinaus => gleich zum nächsten Link übergehen?
//        		
//        	}
        	
        	if( !(c1 <= 0) && c2 > c1){
        		PVector pointOnLink = vBackLink.get();
        		pointOnLink.mult(c1/c2);
        		pointOnLink.add(vFromNode);
        		
        		PVector lot = vVehicle.get();
        		lot.sub(pointOnLink);
        		
        		if(lot.mag() <= walkParallelThreshold){
        			dx = vBackLink.x * -1 / vBackLink.mag();
        			dy = vBackLink.y * -1 / vBackLink.mag();
        			System.out.println("vehicle " + this.id + " wants to walk parallel to link " + currentLink.getId());
        		}
        		
        	}
        }
        
        
        
        
        /*
         * Alle Kräfte werden summiert.
         * Hier wieder mit getrennten x- und y-Komponenten.
         */
        
        
        
        //Hilfsvektor zum Zeichnen der Pfeile in der Visualisierung
        forceTarget = new PVector((float) (this.mass * (dx * this.speed - vtx) / this.tau), (float) (this.mass * (dy * this.speed - vty) / this.tau));

        
        //Berechnung der resultierenden Gesamtkraft gemaeß Social-Force-Model
        forceX = forceTarget.x + pushX + pushWallX ;
        forceY = forceTarget.y + + pushY + pushWallY ;
        
        
                
        vx = vtx + Simulation.TIME_STEP *(forceX/80);
        vy = vty + Simulation.TIME_STEP *(forceY/80);
        
        //	Begrenzung der Kräfte
        if (Math.sqrt((vx*vx)+(vy*vy)) > maxSpeed) {
        	double speed = Math.sqrt((vx*vx)+(vy*vy));
        	vx = Math.sqrt(speed) * vx / speed ;
        	vy = Math.sqrt(speed) * vy / speed ;
        }
        
        
        this.phi = Math.atan2(vy,vx);
        
    }
    
    public void move() {

        this.x = this.x + Simulation.TIME_STEP * this.vx;
        this.y = this.y + Simulation.TIME_STEP * this.vy;
        
        vtx = vx;
        vty = vy;
        
        Link currentLink = this.route.get(routeIndex);
        if (currentLink.hasVehicleReachedEndOfLink(this)) {
            routeIndex++; 
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
			return forceVehicles;
	}

	public PVector getForceWalls() {
		return forceWalls;
	}

	public String getId() {
		return id;
	}
	
	@Override
	public String toString(){
		return "" + id + " @ [" + this.x + ";" + this.y + "]";
	}

}
