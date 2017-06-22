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

    
    public static final double TIME_STEP = 0.02;

    private final Vis vis;
    private List<Vehicle> vehicles = new ArrayList<>();

    public Simulation(Network network) {
        this.vis = new Vis(network);
    }

    public static void main(String[] args) {

        Network network = new Network();
        Node node1 = network.createNode(3,3,1);
        Node node2 = network.createNode(13,3,2);
        Node node3 = network.createNode(13,6,3);
        Node node4 = network.createNode(13,9,4);
        Node node5 = network.createNode(3,9,5);
        Node node6 = network.createNode(19,6,6);
        Node node7 = network.createNode(19,3,7);
        Node node8 = network.createNode(29,3,8);
        Node node9 = network.createNode(29,9,9);
        Node node10 = network.createNode(19,9,10);
        Node node11 = network.createNode(14,6,11);
        Node node12 = network.createNode(18,6,12);
        
        network.createLink(node1,node2,1);
        network.createLink(node2,node3,1);
        network.createLink(node3,node4,1);
        network.createLink(node4,node5,1);
        network.createLink(node5,node1,1);
        network.createLink(node3, node11, 1);
        network.createLink(node11,node12,2);
        network.createLink(node12,node6,3);
        network.createLink(node6,node7,3);
        network.createLink(node7,node8,3);
        network.createLink(node8,node9,3);
        network.createLink(node9,node10,3);
        network.createLink(node10,node6,3);
        network.createLink(node2,node1,1);
        network.createLink(node3,node2,1);
        network.createLink(node3,node4,1);
        network.createLink(node5,node4,1);
        network.createLink(node1,node5,1);
        network.createLink(node11,node3,1);
        network.createLink(node12,node11,2);
        network.createLink(node6,node12,3);
        network.createLink(node7,node6,3);
        network.createLink(node8,node7,3);
        network.createLink(node9,node8,3);
        network.createLink(node10,node9,3);
        network.createLink(node6,node10,3);
              
        network.createWall(2,2,14,2,1);
        //network.createWall(6,2,9,5.5,1);
        network.createWall(14,2,14,5,1);
        network.createWall(14,7,14,10,1);
        network.createWall(14,10,2,10,1);
        network.createWall(2,10,2,2,1);
        
        network.createWall(14,5,18,5,2);
        network.createWall(14,7,18,7,2);
        
        network.createWall(18,5,18,2,3);
        network.createWall(18,2,30,2,3);
        network.createWall(30,2,30,10,3);
        network.createWall(30,10,18,10,3);
        network.createWall(18,10,18,7,3);
            
       
        Simulation simulation = new Simulation(network);
        
        Vehicle v1 = new Vehicle(node2, node8, network, 1);
        Vehicle v2 = new Vehicle(node5, node8, network, 2);
        Vehicle v3 = new Vehicle(node9, node5, network, 3);
        Vehicle v4 = new Vehicle(node3, node6, network, 4);
        Vehicle v5 = new Vehicle(node1, node4, network, 5);
        simulation.add(v1);
        simulation.add(v2);
        simulation.add(v3);
        simulation.add(v4);
        simulation.add(v5);
        simulation.run();
        
    }

    private void run() {
        double time = 0;

        double maxTime = 1000;
        while (time < maxTime) {
        	
            for (Vehicle vehicle : this.vehicles) {
            	if (vehicle.getFinish() == true) {
                  	this.vehicles.remove(vehicle);
                  	break;
                  }
            	vehicle.update(this.vehicles);
            }
            
            for (Vehicle vehicle : this.vehicles) {
                vehicle.move();
            }
                        
            List<VehicleInfo> vehicleInfoList = new ArrayList<>();
            for (Vehicle vehicle : this.vehicles) {
                VehicleInfo vehicleInfo = new VehicleInfo(vehicle.getX(), vehicle.getY(), vehicle.getPhi(), vehicle.getRadius(), vehicle.getColourR(), vehicle.getColourG(), vehicle.getColourB(),
                		vehicle.getForceTarget(), vehicle.getForceVehicles(), vehicle.getForceWalls());
                vehicleInfoList.add(vehicleInfo);
            }
            
            this.vis.update(time, vehicleInfoList);
            
            time += TIME_STEP;

            try {
                Thread.sleep((long) (TIME_STEP * 1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
        }
        
    }

    private void add(Vehicle vehicle) {
        this.vehicles.add(vehicle);
    }
    
}
