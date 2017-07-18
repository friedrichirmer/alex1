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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import network.Link;
import network.Network;
import network.Node;
import network.Wall;
import processing.core.PVector;

public class Vehicle {
    private List<Link> route;
    private double startTime;
    private double vtx = 0;
    private double vty = 0;
    private double vx = 0;
    private double vy = 0;
    private double forceX = 0;
    private double forceY = 0;
    private double length = 0.2;
    private double width = 0.2;
    double desiredSpeed = 1.34;
    private double maxSpeed;
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
	double momentSpeed;

    private String id;

    private int routeIndex = 0;
	private Network network;
	private boolean finished;
	private int counterV = 1;
	private Node destinationNode;
	private static Random random = new Random(41);
	
	public final Map<Vehicle,PVector> forces = new HashMap<Vehicle,PVector>();
	
	private PVector forceWalls;
	private PVector forceVehicles;
	private PVector forceTarget;
	double pushWallX, pushWallY;
    private boolean isInTheSimulation = false;
	private double mass;
	private final double walkParallelThreshold = 1.0;	//set to Double.MAX_Value to disable parallel walking
	private Map<Integer, Double[]> mapOfEnterLeaveTimes = new HashMap<Integer, Double[]>();
	private double constantA = 2000;
	private double constantB = 0.08;
	private double constantK = 120000;
	private double constantKSmall = 240000;

	public List<Link> getRoute() {
		return route;
	}

	public Vehicle(Network network, Node startNode, Node destinationNode, double startTime, String id) {
    	
    	this.x = startNode.getX() + random.nextDouble() * 1;
        this.y = startNode.getY() + random.nextDouble() * 1;
        this.network = network;
        this.route = Dijkstra.returnRoute(network, startNode, destinationNode);
        this.rad = 0.25 + random.nextDouble()*0.1;
        this.maxSpeed = 2;
        this.vtx = 0;
        this.finished = false;
        this.id = id;
        this.startTime = startTime;
        this.mass = 80;
		this.mapOfEnterLeaveTimes.put(route.get(0).getId(), new Double[]{startTime, null});
        this.destinationNode = destinationNode;
    }

	public Vehicle(Network network, Node startNode, Node destinationNode, double startTime, String id, List<Link> route){
		this.x = startNode.getX() + random.nextDouble() * 1;
        this.y = startNode.getY() + random.nextDouble() * 1;
        this.network = network;
        this.route = route;
    	System.out.println("route: " + route.toString());
        this.rad = 0.25 + random.nextDouble()*0.1;
        this.maxSpeed = 2;
        this.vtx = 0;
        this.finished = false;
        this.id = id;
        this.startTime = startTime;
        this.mass = 60 + random.nextInt(40);
		this.mapOfEnterLeaveTimes.put(route.get(0).getId(), new Double[]{startTime, null});
        this.destinationNode = destinationNode;
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

    /**
     * checks whether the vehicle should enter the sim.
     * so basically it checks whether vehicle's start time is smaller than given time
     * @return
     */
    public boolean entersSimulation(double time){
//    	if(this.startTime <= time){
//    		this.isInTheSimulation = true;
//    		return true;
//    	}
//    	return false;
    	return (this.startTime <= time);
    }
    

    public void update(List<Vehicle> vehs, double time, Set<Wall> wallSet) {

//        if ( startTime > time) {
//            return;
//        }
//        isInTheSimulation = true;
    	
    	Link currentLink = this.route.get(routeIndex);
    	
    	//System.out.println("x: " + this.x + "   y: " + this.y);
        	
        pushX = 0;
        pushY = 0;
        pushWallX = 0;
    	pushWallY = 0;
        
    	/*
    	 * Die erste Schleife iteriert �ber alle per Parameter vehs �bergebenen Fahrzeuge und summiert die absto�enden Kr�fte.
    	 * Hier haben Kr�fte und Vektoren haben je eine x- und eine y-Komponente.
    	 */
    	   	
        for (Vehicle v : vehs) {
        	
        	if (this == v) continue;  

        	double vehx = v.x;
        	double vehy = v.y;
        	double radS = (rad + v.getRadius());
        	double distMX = (vehx - this.x);
        	double distMY = (vehy - this.y);
        	double distM = Math.sqrt(distMX*distMX + distMY*distMY); // Abstand der Mittelpunkte
        	double distR = radS - distM; // Abstand der Radien

			double g;
			if (distR >= 0) g = 1;
        	else g=0;

        	double dirNX = (-distMX) / distM; 		//richtungsvektor n_x
        	double dirTX = (distMY) / distM;		//richtungsvektor t_x
        	double dirNY = (-distMY) / distM;		//richtungsvektor n_y
        	double dirTY = (-distMX) / distM;		//richtungsvektor t_y
        	double vDiff = (v.vtx - this.vtx) * dirTX + (v.vty - this.vty) * dirTY;

        	pushX = pushX + 
        			(constantA * Math.exp(distR / constantB) + constantK * g * distR) * dirNX +
        			constantKSmall*g*distR*vDiff*dirTX;
        	pushY = pushY + 
        			(constantA * Math.exp(distR / constantB) + constantK * g * distR) * dirNY +
        			constantKSmall * g * distR * vDiff * dirTY;
        }
        
        forceVehicles = new PVector((float)pushX, (float)pushY);
               
        /*
         * Die zweite Schleife iteriert �ber alle W�nde und berechnet die absto�enden Kr�fte
         * Hier wird mit Vektoren gerechnet
         */
      
        Iterator<Wall> it = wallSet.iterator();
        while(it.hasNext()){
        	Wall wall = it.next();
        	calcWallForce(wall);
        }
        
//        for (int i = 0; i< network.walls.size(); i++ ) {
//        	Wall wall = network.walls.get(i);
//       		calcWallForce(wall);
//        }

        forceWalls = new PVector((float)pushWallX, (float)pushWallY);
        
        // Berechnung der Wunschrichtung
    	
        double dx = currentLink.getTo().getX() - this.x;
    	double dy = currentLink.getTo().getY() - this.y;
    	
    	double dist = Math.sqrt(dx*dx+dy*dy);
    	dx /= dist;
    	dy /= dist;
        
        if(this.walkParallelThreshold  < Double.MAX_VALUE){
        	// wir muessen hier das Lot vom Vehicle auf den aktuellen Link faellen
        	// siehe http://geomalgorithms.com/a02-_lines.html
        	
        	PVector vFromNode = new PVector((float) currentLink.getFrom().getX(), (float) currentLink.getFrom().getY());
        	PVector vToNode = new PVector((float) currentLink.getTo().getX(), (float) currentLink.getTo().getY());
    		PVector vVehicle = new PVector ((float)this.x,(float) this.y); 		//Position des Fahrzeugs

        	
        	PVector vLink = vToNode.get();
        	vLink.sub(vFromNode);
        	
        	PVector fromNodeToVeh = vVehicle.get();
        	fromNodeToVeh.sub(vFromNode);
        	
        	float c1 = PVector.dot(fromNodeToVeh, vLink);
        	float c2 = PVector.dot(vLink, vLink);
        	
//        	if(c2 <= c1){ //Vehicle befindet sich schon ueber den toNode (bzw Zielline) hinaus => gleich zum naechsten Link uebergehen?
//        		
//        	}
        	
        	if( !(c1 <= 0) && c2 > c1){
        		PVector lotFootpointOnLink = vLink.get();
        		lotFootpointOnLink.mult(c1/c2);
        		lotFootpointOnLink.add(vFromNode);
        		
        		PVector lot = vVehicle.get();
        		lot.sub(lotFootpointOnLink);
        		
        		if(lot.mag() <= walkParallelThreshold){
        			vLink.normalize();
        			dx = vLink.x;
        			dy = vLink.y;
        		}
        		
        	}
        }

        /*
         * Alle Kraefte werden summiert.
         * Hier wieder mit getrennten x- und y-Komponenten.
         */

        //Hilfsvektor zum Zeichnen der Pfeile in der Visualisierung
        forceTarget = new PVector((float) (this.mass * (dx * this.desiredSpeed - vtx) / this.tau), (float) (this.mass * (dy * this.desiredSpeed - vty) / this.tau));

        
        //Berechnung der resultierenden Gesamtkraft gemae� Social-Force-Model
        forceX = forceTarget.x + pushX + pushWallX ;
        forceY = forceTarget.y + + pushY + pushWallY ;
        
        
                
        vx = vtx + Simulation.TIME_STEP *(forceX/80);
        vy = vty + Simulation.TIME_STEP *(forceY/80);
        momentSpeed = Math.sqrt((vx*vx)+(vy*vy));
        //	Begrenzung der Kraefte
        if (momentSpeed > maxSpeed) {
			vx = (vx / momentSpeed) * maxSpeed;
			vy = (vy / momentSpeed) * maxSpeed;
        }
        this.momentSpeed = Math.sqrt((vx*vx)+(vy*vy));
        this.phi = Math.atan2(vy,vx);
        
    }

    public boolean placeVehicleSomwhereOnCurrentLink(){
    	if(this.routeIndex == 0){
    		double rnd = Math.random();
    		Link l = this.route.get(routeIndex);
    		double dx = l.getTo().getX()-l.getFrom().getX();
    		double dy = l.getTo().getY()-l.getFrom().getY();
    		this.x += dx*rnd;
    		this.y += dy*rnd;
    		return true;
    	}
    	else{
    		return false;
    	}
    }
    
	/**
	 * @param wall
	 */
	private void calcWallForce(Wall wall) {
		PVector wallA = new PVector ((float)wall.getX1(),(float) wall.getY1()); 	//Ende 1 der Wand
		PVector wallB = new PVector ((float)wall.getX2(),(float) wall.getY2());	//Ende 2 der Wand
		PVector vehic = new PVector ((float)this.x,(float) this.y); 		//Position des Fahrzeugs

		PVector vecv = wallB.get();			// Vektor zeigt von Ende 1 auf Ende 2 der Wand
		vecv.sub(wallA);		
		PVector vecw = vehic.get();			// Vektor zeigt von Ende 1 auf Fahrzeug
		vecw.sub(wallA);
		PVector vecw2 = vehic.get();		// Vektor zeigt von Ende 2 auf Fahrzeug
		vecw2.sub(wallB);  
		
		float c1 = 0;	// Hilfsgr��e zur Bestimmung der Position relativ zur Wand
		float c2 = 0; 	// Hilfsgr��e zur Bestimmung der Position relativ zur Wand
  		
		c1 = vecw.dot(vecv);
		c2 = vecv.dot(vecv);
		
		double lot;		// L�nge des Lots
		double radlot = 1;	// Radius des Fahrzeugs minus L�nge des Lots (meistens negativ)

		PVector t = null; 	// tangetialer Richtungsvektor f�r die Kraft
		PVector n = null;	// normaler Richtungsvektor f�r die Kraft
		
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
			PVector pb = PVector.add(wallA, bv); 	// Vektor pb ist der Lotfu�punkt auf der Wand
			
			PVector pbV = vehic.get();				// Vektor pbV zeigt vom Lotfu�punkt auf das Fahrzeug (Richtung der Kraft)
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
			(constantA * Math.exp(radlot / constantB) + constantK * g * radlot) * n.x +
			constantKSmall * g * radlot * vdifx * t.x;
     	
		pushWallY = pushWallY + 
			(constantA * Math.exp(radlot / constantB) + constantK * g * radlot) * n.y +
			constantKSmall * g * radlot * vdify * t.y;
	}
    
    public void move(double time) {

        this.x = this.x + Simulation.TIME_STEP * this.vx;
        this.y = this.y + Simulation.TIME_STEP * this.vy;
        vtx = vx;
        vty = vy;
        
        Link currentLink = this.route.get(routeIndex);
		Double timeWhenEnteredLink = this.mapOfEnterLeaveTimes.get(currentLink.getId())[0];
        if (currentLink.hasVehicleReachedEndOfLink(this.x, this.y)) {
			recordTravelTimeOnTheLastLink(time, currentLink, timeWhenEnteredLink);
			moveVehicleToNextLinkOfRoute(time);
        } else if (time - timeWhenEnteredLink > this.getRoute().get(routeIndex).getWeight() + 15 && !(this.finished = true)){
			rerouteVehicleIfStucked(time);
		}
    }

	private void recordTravelTimeOnTheLastLink(double time, Link currentLink, Double timeWhenEnteredLink) {
		this.mapOfEnterLeaveTimes.put(currentLink.getId(), new Double[]{timeWhenEnteredLink, time});
	}

	private void moveVehicleToNextLinkOfRoute(double time) {
		routeIndex++;
		if (this.route.size() == routeIndex) {
           this.finished = true;
        } else {
            Link newCurrentLink = this.route.get(routeIndex);
            this.mapOfEnterLeaveTimes.put(newCurrentLink.getId(), new Double[]{time, null});
        }
	}

	private void rerouteVehicleIfStucked(double time) {
		if (time % 5 == 0 && new Random().nextDouble() < 0.1){
            Node newStartNode = network.findNearestNode(this.x, this.y);
            DijkstraV2 router = new DijkstraV2(network);
            this.route = router.calculateRoute(network.getNodes().get(newStartNode.getId()), network.getNodes().get(destinationNode.getId()));
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
	
	public boolean getFinished() {
		return finished;
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

	public Map<Integer, Double[]> getMapOfEnterLeaveTimes() {
		return mapOfEnterLeaveTimes;
	}
}
