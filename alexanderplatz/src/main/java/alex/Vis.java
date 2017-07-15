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

import network.Network;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by laemmel on 17/04/16.
 */

public class Vis extends PApplet implements MouseListener {

	
	private List<VehicleInfo> vehs = new ArrayList<>();

    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;

    private int x = 0;
    private int y = 0;

    private double phi = 0;
    private final Network net;

    private int densityWindowX1;
	private int densityWindowY1;
	private int densityWindowX2;
	private int densityWindowY2;

	private Voronoi voronoi;
    private double xOffset;
    private double yOffset;
    private double scale;

    public Vis(Network net) {
        this.net = net;
		this.voronoi = new Voronoi();
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
    		voronoi.reset(); // Bei einem Mausklick wird die Messung automatisch zur�ckgesetzt
    		//System.out.println("gepresst: [" + e.getX() + "] [" + e.getY() + "]");
    		voronoi.corner1(e.getX() , e.getY());
    	}
	}
    
    public void mouseReleased( MouseEvent e ) {
    	if (e.getButton() == 1) {
    		//System.out.println("losgelassen: [" + e.getX() + "] [" + e.getY() + "]");
    		voronoi.corner2(e.getX(), e.getY());
    	}

	}
    

    @Override
    public void draw() {
        background(255); // eraser

        pushMatrix();
        translate((float) xOffset, (float) yOffset);

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
            } else {
                if (key == '+') {
                    scale += 0.1;
                    yOffset -= 15;
                    xOffset -= 15;
                } else if (key == '-') {
                    scale -= 0.1;
                    yOffset += 15;
                    xOffset += 15;
                }
            }

            net.draw(this);

            synchronized (this.vehs) {
                for (VehicleInfo v : this.vehs) {
                    v.draw(this);
                }
            }

            voronoi.draw(this);
        }
    
    } 


	public void update(double time, List<VehicleInfo> vehs) {
        synchronized (this.vehs) {
            this.vehs = new ArrayList<VehicleInfo>(vehs);
        }
        
    }

}
