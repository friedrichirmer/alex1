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


import processing.core.PApplet;

import javax.swing.*;


import kn.uni.voronoitreemap.datastructure.OpenList;
import kn.uni.voronoitreemap.diagram.PowerDiagram;
import kn.uni.voronoitreemap.j2d.PolygonSimple;
import kn.uni.voronoitreemap.j2d.Site;
import network.Network;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashSet;
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

    private static final int WIDTH = 1200;
    private static final int HEIGHT = 800;

    private int x = 0;
    private int y = 0;

    private double phi = 0;
    private Network net;

    private int densityWindowX1;
	private int densityWindowY1;
	private int densityWindowX2;
	private int densityWindowY2;

    
    
    OpenList sites = new OpenList();
	Random rand = new Random(100);


	private int originX;
	private int originY;

	private int rootWidth;
	private int rootHeight;

	private double densityInRoot =  0;
	private double numberInRoot = 0;
	private double areasInRoot = 0;

	private Set<PolygonSimple> polygonsInRoot =  new HashSet<PolygonSimple>();


    public static double xOffset = 0;
    public static double yOffset = 0;
    public static double scale = 10;


    public Vis(Network net) {
        this.net = net;

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
        
    
        //panel.addMouseListener(new MouseClick());

        size(WIDTH, HEIGHT);
        background(255);
        
        

    }
    
    /**
     * Mit der rechten(!) Maustaste kann ein Fenster f�r die Dichtemessung aufgezogen werden.
     */
    
    public void mousePressed( MouseEvent e ) {
    	
    	if (e.getButton() == 1) {
    		// Bei einem Mausklick wird die Messung automatisch zur�ckgesetzt
    		//System.out.println("gepresst: [" + e.getX() + "] [" + e.getY() + "]");
    		densityWindowX1 = e.getX() ;
    		densityWindowY1 = e.getY();
    		
    		densityInRoot =  0;
    		numberInRoot = 0;
    		areasInRoot = 0;
    		
    	}
	}
    
    public void mouseReleased( MouseEvent e ) {
    	if (e.getButton() == 1) {
    		//System.out.println("losgelassen: [" + e.getX() + "] [" + e.getY() + "]");
    		densityWindowX2 = e.getX();
    		densityWindowY2 = e.getY();
    		
    		PolygonSimple rootPolygon = new PolygonSimple();
    		PolygonSimple outerPolygon = new PolygonSimple();
    		PowerDiagram diagram = new PowerDiagram();
    		
    		polygonsInRoot.clear();
    		
    		rootPolygon.add(densityWindowX1, densityWindowY1);
    		rootPolygon.add(densityWindowX1, densityWindowY2);
    		rootPolygon.add(densityWindowX2, densityWindowY2);
    		rootPolygon.add(densityWindowX2, densityWindowY1);
    		
    		if ((densityWindowX1 < densityWindowX2) && (densityWindowY1 < densityWindowY2)) {
    			outerPolygon.add(densityWindowX1 - 100, densityWindowY1 - 100);
    			outerPolygon.add(densityWindowX2 + 100, densityWindowY1 - 100);
    			outerPolygon.add(densityWindowX2 + 100, densityWindowY2 + 100);
    			outerPolygon.add(densityWindowX1 - 100, densityWindowY2 + 100);
    			originX = densityWindowX1;
    			originY = densityWindowY1;
    			rootWidth = densityWindowX2 - densityWindowX1;
    			rootHeight = densityWindowY2 - densityWindowY1;
    		}
    		
    		if ((densityWindowX1 >= densityWindowX2) && (densityWindowY1 < densityWindowY2)) {
    			outerPolygon.add(densityWindowX2 - 100, densityWindowY1 - 100);
    			outerPolygon.add(densityWindowX1 + 100, densityWindowY1 - 100);
    			outerPolygon.add(densityWindowX1 + 100, densityWindowY2 + 100);
    			outerPolygon.add(densityWindowX2 - 100, densityWindowY2 + 100);
    			originX = densityWindowX2;
    			originY = densityWindowY1;
    			rootWidth = densityWindowX1 - densityWindowX2;
    			rootHeight = densityWindowY2 - densityWindowY1;
    		}
    		
    		if ((densityWindowX1 < densityWindowX2) && (densityWindowY1 >= densityWindowY2)) {
    			outerPolygon.add(densityWindowX1 - 100, densityWindowY2 - 100);
    			outerPolygon.add(densityWindowX2 + 100, densityWindowY2 - 100);
    			outerPolygon.add(densityWindowX2 + 100, densityWindowY1 + 100);
    			outerPolygon.add(densityWindowX1 - 100, densityWindowY1 + 100);
    			originX = densityWindowX1;
    			originY = densityWindowY2;
    			rootWidth = densityWindowX2 - densityWindowX1;
    			rootHeight = densityWindowY1 - densityWindowY2;
    		}
    		
    		if ((densityWindowX1 >= densityWindowX2) && (densityWindowY1 >= densityWindowY2)) {
    			outerPolygon.add(densityWindowX2 - 100, densityWindowY2 - 100);
    			outerPolygon.add(densityWindowX1 + 100, densityWindowY2 - 100);
    			outerPolygon.add(densityWindowX1 + 100, densityWindowY1 + 100);
    			outerPolygon.add(densityWindowX2 - 100, densityWindowY1 + 100);
    			originX = densityWindowX2;
    			originY = densityWindowY2;
    			rootWidth = densityWindowX1 - densityWindowX2;
    			rootHeight = densityWindowY1 - densityWindowY2;
    			
    		}
    		
    		
    		
    		 
    		//double[] testpunkte = {100,200,300,400,500,600};	  		
    		//edges = voronoi.generateVoronoi(testpunkte, testpunkte, densityWindowX1, densityWindowX2, densityWindowY1, densityWindowY2);
    		
    		for (int i = 0; i < 100; i++) {
			 	Site site = new Site(rand.nextInt(1000), rand.nextInt(1000));
			 	// we could also set a different weighting to some sites
			 	// site.setWeight(30)
			 	sites.add(site);
			 	}
    		
    		diagram.setSites(sites);
    		diagram.setClipPoly(outerPolygon);
    		diagram.computeDiagram();
    		
    		for (Site s : sites) {
    			if (rootPolygon.contains(s.x, s.y) && (s.getPolygon() != null)) {
    				polygonsInRoot.add(s.getPolygon());
    				System.out.println("xxxxxxxxxxxxxx");
    				numberInRoot++;
    				areasInRoot += s.getPolygon().getArea();
    			}		
    		}
    		
			System.out.println(numberInRoot +  " und " + areasInRoot);
    		if (areasInRoot > 0) densityInRoot = (numberInRoot / areasInRoot);
    		System.out.println(" New Density " + (numberInRoot / areasInRoot));
    	}

	}


 


 

    @Override
    public void draw() {
        background(255); // eraser

        pushMatrix();

        if (keyPressed) {
            if (key == CODED) {
                if (keyCode == UP) {
                    yOffset += 10;
                } else if (keyCode == DOWN) {
                    yOffset -= 10;
                }
                if (keyCode == RIGHT) {
                    xOffset -= 10;
                } else if (keyCode == LEFT) {
                    xOffset += 10;
                }
            }


            else {

                if (key == '+') {
                    scale += 0.5;
                    yOffset -= 15;
                    xOffset -= 15;
                } else if (key == '-') {
                    scale -= 0.5;
                    yOffset += 15;
                    xOffset += 15;
                }
            }
        }
        
            for (Site s : sites) {
            	 this.point((float) s.x, (float) s.y);
            	 PolygonSimple polygon = s.getPolygon();
            	 if(polygonsInRoot.contains(polygon)){
            		 double[] polyx = polygon.getXPoints();
            		 double[] polyy = polygon.getYPoints();
           		 
           		 for (int i=0;i<(polygon.getNumPoints()-1);i++) {
            			 this.line( (float) polyx[i], (float) polyy[i], (float) polyx [i+1], (float) polyy[i+1]);
            		 }
            		 this.line( (float) polyx[polygon.getNumPoints()-1], (float) polyy[polygon.getNumPoints()-1], (float) polyx [0], (float) polyy[0]);
 
            	 }
            	 
            }
            
            this.noFill();
            this.rect(originX, originY, rootWidth, rootHeight, 3);
            this.fill(0);
    		this.textSize(15);
    		this.fill(0);
    		this.text("Voronoi-Dichte: " + densityInRoot + " [Anzahl Personen / m^2]", 500, 500);
    		
        translate((float) xOffset, (float) yOffset);
        net.draw(this);

        synchronized (this.vehs) {
            for (VehicleInfo v : this.vehs) {
                v.draw(this);
            }
        }
        synchronized (this.trams) {
            for (TramInfo t : this.trams) {
                t.draw(this);
            }
        }
        
        popMatrix();
        
    } 


	public void update(double time, List<VehicleInfo> vehs, List<TramInfo> trams) {
        synchronized (this.vehs) {
            this.vehs = new ArrayList<VehicleInfo>(vehs);
        }
        synchronized (this.trams) {
            this.trams = new ArrayList<TramInfo>(trams);
        }
        
    }

}
