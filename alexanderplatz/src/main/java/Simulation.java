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
import java.util.List;

/**
 * Created by laemmel on 24/04/16.
 */
public class Simulation {

    public static final double SCALE = 25;

    
    public static final double H = 0.02;

    private final Vis vis;
    private List<Vehicle> vehs = new ArrayList<>();

    public Simulation(Network net) {
        this.vis = new Vis(net);
    }

    public static void main(String[] args) {

        Network net = new Network();
        Node n1 = net.createNode(3,3,1);
        Node n2 = net.createNode(13,3,2);
        Node n3 = net.createNode(13,6,3);
        Node n4 = net.createNode(13,9,4);
        Node n5 = net.createNode(3,9,5);
        Node n6 = net.createNode(19,6,6);
        Node n7 = net.createNode(19,3,7);
        Node n8 = net.createNode(29,3,8);
        Node n9 = net.createNode(29,9,9);
        Node n10 = net.createNode(19,9,10);
        Node n11 = net.createNode(14,6,11);
        Node n12 = net.createNode(18,6,12);
        
        net.createLink(n1,n2,1);
        net.createLink(n2,n3,1);
        net.createLink(n3,n4,1);
        net.createLink(n4,n5,1);
        net.createLink(n5,n1,1);
        net.createLink(n3, n11, 1);
        net.createLink(n11,n12,2);
        net.createLink(n12,n6,3);
        net.createLink(n6,n7,3);
        net.createLink(n7,n8,3);
        net.createLink(n8,n9,3);
        net.createLink(n9,n10,3);
        net.createLink(n10,n6,3);
        net.createLink(n2,n1,1);
        net.createLink(n3,n2,1);
        net.createLink(n3,n4,1);
        net.createLink(n5,n4,1);
        net.createLink(n1,n5,1);
        net.createLink(n11,n3,1);
        net.createLink(n12,n11,2);
        net.createLink(n6,n12,3);
        net.createLink(n7,n6,3);
        net.createLink(n8,n7,3);
        net.createLink(n9,n8,3);
        net.createLink(n10,n9,3);
        net.createLink(n6,n10,3);
              
        net.createWall(2,2,14,2,1);
        //net.createWall(6,2,9,5.5,1);
        net.createWall(14,2,14,5,1);
        net.createWall(14,7,14,10,1);
        net.createWall(14,10,2,10,1);
        net.createWall(2,10,2,2,1);
        
        net.createWall(14,5,18,5,2);
        net.createWall(14,7,18,7,2);
        
        net.createWall(18,5,18,2,3);
        net.createWall(18,2,30,2,3);
        net.createWall(30,2,30,10,3);
        net.createWall(30,10,18,10,3);
        net.createWall(18,10,18,7,3);
            
       
        Simulation sim = new Simulation(net);
        
        Vehicle v1 = new Vehicle(n2, n8, net);
        Vehicle v2 = new Vehicle(n5, n8, net);
        Vehicle v3 = new Vehicle(n9, n5, net);
        Vehicle v4 = new Vehicle(n3, n6, net);
        Vehicle v5 = new Vehicle(n1, n4, net);
        sim.add(v1);
        sim.add(v2);
        sim.add(v3);
        sim.add(v4);
        sim.add(v5);
        sim.run();
        
    }

    private void run() {
        double time = 0;

        double maxTime = 1000;
        while (time < maxTime) {
        	
            for (Vehicle v : this.vehs) {
            	if (v.getFinish() == true) {
                  	this.vehs.remove(v);
                  	break;
                  }
            	v.update(this.vehs);
            }
            
            for (Vehicle v : this.vehs) {
                v.move();
            }
                        
            List<VehicleInfo> vInfos = new ArrayList<>();
            for (Vehicle v : this.vehs) {
                VehicleInfo vi = new VehicleInfo(v.getX(), v.getY(), v.getPhi(), v.getRadius(), v.getColourR(), v.getColourG(), v.getColourB(),
                		v.getForceTarget(), v.getForceVehicles(), v.getForceWalls());
                vInfos.add(vi);
            }
            
            this.vis.update(time, vInfos);
            
            time += H;

            try {
                Thread.sleep((long) (H * 1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
        }
        
    }

    private void add(Vehicle v) {
        this.vehs.add(v);
    }
    
}
