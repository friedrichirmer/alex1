package alex;/* *********************************************************************** *
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


import network.Node;
import processing.core.PApplet;

import javax.swing.*;


import kn.uni.voronoitreemap.datastructure.OpenList;
import kn.uni.voronoitreemap.diagram.PowerDiagram;
import kn.uni.voronoitreemap.j2d.PolygonSimple;
import kn.uni.voronoitreemap.j2d.Site;
import network.AlexanderplatzNetworkCreator;
import network.Network;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * Created by laemmel on 17/04/16.
 */

public class Vis extends PApplet implements MouseListener {

	
	private List<VehicleInfo> vehs = new ArrayList<VehicleInfo>();
	private List<TramInfo> trams = new ArrayList<TramInfo>();

    private static final int WIDTH = 840;
    private static final int HEIGHT = 840;

    private Network pedestrianNet;
    private Network tramNet;

    private float densityWindowX1;
	private float densityWindowY1;
	private float densityWindowX2;
	private float densityWindowY2;
	
	PolygonSimple rootPolygon = new PolygonSimple();
	PolygonSimple outerPolygon = new PolygonSimple();

    PowerDiagram diagram = new PowerDiagram();
    
    OpenList sites = new OpenList();
	Random rand = new Random(100);


	private float originX;
	private float originY;

	private float rootWidth;
	private float rootHeight;

	private double densityInRoot =  0;
	private double numberInRoot = 0;
	private double areasInRoot = 0;
	
	private Set<PolygonSimple> polygonsInRoot =  new HashSet<PolygonSimple>();
	private PolygonSimple testPolygon;
	private List<Vehicle> vehiclesInRoot =  new ArrayList<Vehicle>();
	private double avgSpeed;
	private List<Double> speedList = new ArrayList<Double>();

    public static float xOffset = 20;
    public static float yOffset = 20;
    public static float scale = 3;
    
    public static double xScaleAndOffset = xOffset * scale;
    public static double yScaleAndOffset= yOffset * scale;
	static boolean alarmActivated = false;
	static boolean simPaused = false;
	private static double currentTime;

	int buttonX = 660;
	int buttonY = 60;
	int buttonWidth = 135;
	int buttonHeight = 60;
	int pauseButtonX = 660;
	int pauseButtonY = 140;
	int pauseButtonWidth = 135;
	int pauseButtonHeight = 60;
	
	private float pavillonCenterX;
	private float pavillonCenterY;
	private float pavillonAngleY;
	private float pavillonAngleX;
	private float pavillonDragX;
	private float pavillonDragY;
	private boolean pavillonDragger = false;
	

	public Vis(Network pedestrianNet, Network tramNet) {
        this.pedestrianNet = pedestrianNet;
        this.tramNet = tramNet;

        JFrame fr = new JFrame();
        fr.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        fr.setSize(WIDTH, HEIGHT);
        JPanel panel = new JPanel();
        panel.setLayout(new OverlayLayout(panel));

        fr.add(panel, BorderLayout.CENTER);

        panel.add(this);
        panel.setEnabled(true);
        panel.setVisible(true);

        this.init();
        frameRate(90);
        fr.setVisible(true);
        
        panel.addMouseListener(this);
        panel.addMouseMotionListener(this);
      
        size(WIDTH, HEIGHT);
        background(255);
        
        

    }
    
    /**
     * Mit der linken (!) Maustaste kann ein Fenster f�r die Dichtemessung aufgezogen werden.
     */
    
    public void mousePressed( MouseEvent e ) {
    	
    	if (e.getButton() == 1) {
    		
    		densityWindowX1 = (float) (e.getX() / scale - xOffset);
    		densityWindowY1 = (float) (e.getY() / scale - yOffset );
    		
    		densityInRoot =  0;
    		numberInRoot = 0;
    		areasInRoot = 0;
    		
    	}
    	
    	if (e.getButton() == 2) {
    		pavillonCenterX = (float) (e.getX() / scale - xOffset);
    		pavillonCenterY = (float) (e.getY() / scale - yOffset);
    		pavillonDragger  = true;
    	}
	}
    
    public void mouseDragged (MouseEvent e) {

    	pavillonDragX = (float) (e.getX() / scale - xOffset);
		pavillonDragY = (float) (e.getY() / scale - yOffset);
    	
    }
    
    public void mouseReleased( MouseEvent e ) {
    	if (e.getButton() == 1) {
    		//System.out.println("losgelassen: [" + e.getX() + "] [" + e.getY() + "]");
    		densityWindowX2 = (float) (e.getX() / scale - xOffset);
    		densityWindowY2 = (float) (e.getY() / scale - yOffset);
    	}

    	if (e.getButton() == 2){
    		pavillonAngleX = (float) (e.getX() / scale - xOffset);
    		pavillonAngleY = (float) (e.getY() / scale - yOffset);
    		double angle = Math.atan((pavillonAngleX-pavillonCenterX)/(pavillonAngleY-pavillonCenterY));
    		if(!Double.isNaN(angle)) pedestrianNet.createPavilion(pavillonCenterX,pavillonCenterY, angle);
    		pavillonDragger  = false;
    		
		}

		if (e.getButton() == 3){
			if ( checkIfOverAlarmButton(e)){
				alarmActivated = true;
			} else if( checkIfOverPauseButton(e)){
				simPaused = !(simPaused);
			}
		}
	}

	private boolean checkIfOverAlarmButton(MouseEvent e) {
		if (mouseX >= buttonX && mouseX <= buttonX + buttonWidth &&
				mouseY >= buttonY && mouseY <= buttonY + buttonHeight){
    		return true;
		} else return false;
	}

	private boolean checkIfOverPauseButton(MouseEvent e) {
		if (mouseX >= pauseButtonX && mouseX <= pauseButtonX + pauseButtonWidth &&
				mouseY >= pauseButtonY && mouseY <= pauseButtonY + pauseButtonHeight){
    		return true;
		} else return false;
	}
	
	@Override
    public void draw() {
        background(255); // eraser

		drawAlarmButton();
		drawPauseButton();
        pushMatrix();
		drawEvacuationPoints();

		if (keyPressed) {
            if (key == CODED) {
                if (keyCode == UP) {
                    yOffset += 5;
                } else if (keyCode == DOWN) {
                    yOffset -= 5;
                }
                if (keyCode == RIGHT) {
                    xOffset -= 5;
                } else if (keyCode == LEFT) {
                    xOffset += 5;
                }
            }

            else {
            	
                if (key == '+') {
                	
                    scale += 0.2;
                    yOffset -= 15/scale;
                    xOffset -= 15/scale;
                    
                } else if (key == '-') {
                	
                    scale -= 0.2;
                    yOffset += 15/scale;
                    xOffset += 15/scale;
                    
                }
                
            }
            
        }
        
        translate((float) xOffset*scale, (float) yOffset*scale);
          
        updateDensityWindow();
        
        Iterator<PolygonSimple> polygonIterator = polygonsInRoot.iterator();
        while(polygonIterator.hasNext()){
        	PolygonSimple p = polygonIterator.next();
        	double[] polyx = p.getXPoints();
        	double[] polyy = p.getYPoints();
   		          		 
        	for (int i=0;i<(p.getNumPoints()-1);i++) {
        		this.line( (float) (polyx[i]*scale), (float) (polyy[i]*scale), (float) (polyx [i+1]*scale), (float) (polyy[i+1]*scale));
        	}
   		 	this.line( (float) (polyx[p.getNumPoints()-1]*scale), (float) (polyy[p.getNumPoints()-1]*scale), (float) (polyx [0]*scale), (float) (polyy[0]*scale));
        }
        

		if (areasInRoot > 0) densityInRoot = (numberInRoot / areasInRoot);

        //translate((float) xOffset, (float) yOffset);
        
        pedestrianNet.draw(this,false);
        tramNet.draw(this,true);

		this.noFill();
	    this.rect(originX * scale, originY * scale, rootWidth * scale, rootHeight * scale, 3);
	    this.fill(0);
	    this.textSize(15);
	    this.fill(0);
	    
	    if (pavillonDragger) {
			System.out.println("Drag");
			this.line(pavillonCenterX * scale, pavillonCenterY * scale, pavillonDragX * scale, pavillonDragY * scale);
	    }
	    
        synchronized (this.trams) {
        	for (TramInfo t : this.trams) {
        		t.draw(this);
        	}
        }
        
        synchronized (this.vehs) {
            for (VehicleInfo v : this.vehs) {
                v.draw(this);
            }
        }
        

        
        popMatrix();
        
		String dichte = String.format("%.4f", densityInRoot);
		String speed = String.format("%.4f", avgSpeed);
		String flow = String.format("%.4f", (avgSpeed * densityInRoot));
		
        this.fill(0);
        this.textSize(10);
        
        this.text("Voronoi-Dichte: ", 10, 15);
        this.text("Mittlere Geschwindikkeit: ", 10, 30);
        this.text("Mittlerer Fluss: ", 10, 45);
        this.text("Zeit: ", 10, 60);
        
        this.text(dichte  + " [Anzahl Personen / m^2]", 150, 15);
        this.text(speed + " [m/s]", 150, 30);
        this.text(flow +"[Anzahl Personen / (m*s)]", 150, 45);
        this.text(Double.toString(currentTime), 150, 60);
        this.text("[s]",178,60);
        
    }

	private void drawEvacuationPoints() {
		if (alarmActivated){
            for (Node node : pedestrianNet.evacuationNodes){
                double evacuationMarkingRadius = (0.1 + (currentTime % 1)) * 20;
                stroke(255,0,0);
                fill(255,0,0,80);
                ellipse((float) (node.getX() + xOffset) * scale,(float) (node.getY() + yOffset) * scale,
                        (float) evacuationMarkingRadius,(float) evacuationMarkingRadius);
            }
        }
		stroke(0);
	}

	private void drawAlarmButton() {
		if (!alarmActivated){
			stroke(0);
			fill(0);
			rect(buttonX, buttonY, buttonWidth, buttonHeight);
			textSize(32);
			fill(255);
			text("ALARM", 670, 100);
		} else {
			stroke(0);
			fill(255,0,0);
			rect(buttonX, buttonY, buttonWidth, buttonHeight);
			textSize(24);
			fill(255);
			text("  ALARM  ", 670, 80);
			text("activated", 670, 110);
		}
		textSize(10);
	}
	
	private void drawPauseButton() {
		if (!simPaused){
			stroke(0);
			fill(0,200,50);
			rect(pauseButtonX, pauseButtonY, pauseButtonWidth, pauseButtonHeight);
			textSize(32);
			fill(255);
			text("PAUSE", 670, 170);
		} else {
			stroke(0);
			fill(200,50,20);
			rect(pauseButtonX, pauseButtonY, pauseButtonWidth, pauseButtonHeight);
			textSize(24);
			fill(255);
			text("PAUSED", 670, 170);
		}
		textSize(10);
	}


	public void update(double time, List<VehicleInfo> vehs, List<TramInfo> trams) {
					
        synchronized (this.vehs) {
            this.vehs = new ArrayList<VehicleInfo>(vehs);
        }
        synchronized (this.trams) {
            this.trams = new ArrayList<TramInfo>(trams);
        }
       
        
    }
	
	public void updateDensityWindow(){
		rootPolygon = new PolygonSimple();
		outerPolygon = new PolygonSimple();
		testPolygon = new PolygonSimple();

		rootPolygon.add(densityWindowX1, densityWindowY1);
		rootPolygon.add(densityWindowX1, densityWindowY2);
		rootPolygon.add(densityWindowX2, densityWindowY2);
		rootPolygon.add(densityWindowX2, densityWindowY1);
		
		if ((densityWindowX1 < densityWindowX2) && (densityWindowY1 < densityWindowY2)) {
			outerPolygon.add(densityWindowX1 - 10, densityWindowY1 - 10);
			outerPolygon.add(densityWindowX2 + 10, densityWindowY1 - 10);
			outerPolygon.add(densityWindowX2 + 10, densityWindowY2 + 10);
			outerPolygon.add(densityWindowX1 - 10, densityWindowY2 + 10);
			testPolygon.add(densityWindowX1 - 9, densityWindowY1 - 9);
			testPolygon.add(densityWindowX2 + 9, densityWindowY1 - 9);
			testPolygon.add(densityWindowX2 + 9, densityWindowY2 + 9);
			testPolygon.add(densityWindowX1 - 9, densityWindowY2 + 9);
			originX = densityWindowX1;
			originY = densityWindowY1;
			rootWidth = densityWindowX2 - densityWindowX1;
			rootHeight = densityWindowY2 - densityWindowY1;
		}
		
		if ((densityWindowX1 >= densityWindowX2) && (densityWindowY1 < densityWindowY2)) {
			outerPolygon.add(densityWindowX2 - 10, densityWindowY1 - 10);
			outerPolygon.add(densityWindowX1 + 10, densityWindowY1 - 10);
			outerPolygon.add(densityWindowX1 + 10, densityWindowY2 + 10);
			outerPolygon.add(densityWindowX2 - 10, densityWindowY2 + 10);
			testPolygon.add(densityWindowX2 - 9, densityWindowY1 - 9);
			testPolygon.add(densityWindowX1 + 9, densityWindowY1 - 9);
			testPolygon.add(densityWindowX1 + 9, densityWindowY2 + 9);
			testPolygon.add(densityWindowX2 - 9, densityWindowY2 + 9);
			originX = densityWindowX2;
			originY = densityWindowY1;
			rootWidth = densityWindowX1 - densityWindowX2;
			rootHeight = densityWindowY2 - densityWindowY1;
		}
		
		if ((densityWindowX1 < densityWindowX2) && (densityWindowY1 >= densityWindowY2)) {
			outerPolygon.add(densityWindowX1 - 10, densityWindowY2 - 10);
			outerPolygon.add(densityWindowX2 + 10, densityWindowY2 - 10);
			outerPolygon.add(densityWindowX2 + 10, densityWindowY1 + 10);
			outerPolygon.add(densityWindowX1 - 10, densityWindowY1 + 10);
			testPolygon.add(densityWindowX1 - 9, densityWindowY2 - 9);
			testPolygon.add(densityWindowX2 + 9, densityWindowY2 - 9);
			testPolygon.add(densityWindowX2 + 9, densityWindowY1 + 9);
			testPolygon.add(densityWindowX1 - 9, densityWindowY1 + 9);
			originX = densityWindowX1;
			originY = densityWindowY2;
			rootWidth = densityWindowX2 - densityWindowX1;
			rootHeight = densityWindowY1 - densityWindowY2;
		}
		
		if ((densityWindowX1 >= densityWindowX2) && (densityWindowY1 >= densityWindowY2)) {
			outerPolygon.add(densityWindowX2 - 10, densityWindowY2 - 10);
			outerPolygon.add(densityWindowX1 + 10, densityWindowY2 - 10);
			outerPolygon.add(densityWindowX1 + 10, densityWindowY1 + 10);
			outerPolygon.add(densityWindowX2 - 10, densityWindowY1 + 10);
			testPolygon.add(densityWindowX2 - 9, densityWindowY2 - 9);
			testPolygon.add(densityWindowX1 + 9, densityWindowY2 - 9);
			testPolygon.add(densityWindowX1 + 9, densityWindowY1 + 9);
			testPolygon.add(densityWindowX2 - 9, densityWindowY1 + 9);
			originX = densityWindowX2;
			originY = densityWindowY2;
			rootWidth = densityWindowX1 - densityWindowX2;
			rootHeight = densityWindowY1 - densityWindowY2;
			
		}
		
	diagram.setClipPoly(outerPolygon);
	
	}
	
	public void updateVoronoi(List<Vehicle> vehiclesInSimulation){
				
	    sites.clear();
        polygonsInRoot.clear();
        vehiclesInRoot.clear();
        avgSpeed = 0;
        
		for (Vehicle v : vehiclesInSimulation) {
		 	Site site = new Site(v.getX(), v.getY());
		 	sites.add(site);
		 	if (rootPolygon.contains(site.x, site.y)) {
		 		vehiclesInRoot.add(v);

		 	}
		 }
		
		

		diagram.setSites(sites);

		diagram.computeDiagram();

		numberInRoot = 0;
		areasInRoot = 0;
		
		for (Site s : sites) {
			if (rootPolygon.contains(s.x, s.y) && (s.getPolygon() != null)) {
				PolygonSimple sPoly = s.getPolygon();
				
				if (testPolygon.contains(sPoly.getBounds2D())){
					polygonsInRoot.add(s.getPolygon());
					numberInRoot++;
					areasInRoot += s.getPolygon().getArea();
					
					for (Vehicle v : vehiclesInRoot) {
						if (v.getX() == s.x && v.getY() == s.y) {
							System.out.println("Speed: " + v.getSpeed());
							speedList.add(v.getSpeed());
						}
					}
				}		 		
				
			}		
	 
        }
		
		for (int i=0; i<speedList.size(); i++ ) {
			Double s = speedList.get(i);
			avgSpeed = ((avgSpeed * i) + s ) / (i+1);
		}
	}

	public static void drawTime(double time) {
		 
        currentTime = time;
	}

}


