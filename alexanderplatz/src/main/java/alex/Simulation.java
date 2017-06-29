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


import networkUtils.Network;
import networkUtils.Node;

import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by laemmel on 24/04/16.
 */
public class Simulation {

    public static final double SCALE = 25;
    private static final double MAX_TIME = 1000;
    static final double TIME_STEP = 0.02;
    private static List<Integer> listOfNodesIds = new ArrayList<Integer>();
    private static final int NUMBER_OF_RANDOM_VEHICLES = 23;


    private final Vis vis;
    private List<Vehicle> vehicles = new ArrayList<>();

    public Simulation(Network network) {
        this.vis = new Vis(network);
    }

    public static void main(String[] args) {

        Network network = new Network();
        Node node1 = network.createNode(3.5,3.5,1);
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

        getListOfNodeIds(network);

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
        network.createWall(6,2,9,5.5,1);
        network.createWall(14,2,14,5,1);
        network.createWall(14,7,13,11,1);
        network.createWall(13,11,2,10,1);
        network.createWall(2,10,2,2,1);
        
        network.createWall(14,5,18,5,2);
        network.createWall(14,7,18,7,2);
        
        network.createWall(18,5,18,2,3);
        network.createWall(18,2,30,2,3);
        network.createWall(30,2,30,10,3);
        network.createWall(30,10,18,10,3);
        network.createWall(18,10,18,7,3);
            
       
        Simulation simulation = new Simulation(network);
        addRandomVehicles(network, simulation, NUMBER_OF_RANDOM_VEHICLES);
        simulation.run();
        
    }

    private static void getListOfNodeIds(Network network) {
        Map nodesMap = network.getNodes();
        Iterator<Integer> nodeIdIterator = nodesMap.keySet().iterator();
        while ( nodeIdIterator.hasNext()){
            Integer nodeId = nodeIdIterator.next();
            listOfNodesIds.add(nodeId);
        }
    }

    private void run() {
        double time = 0;

        KDTree kdTree = new KDTree(this.vehicles);
        double visualRangeX = 3;
        double visualRangeY = 3;
        int nrOfNeighboursToConsider = 5;
        
        while (time < MAX_TIME) {
        	
        	/* Tilmann 22.6::
        	 * bisher wurde unnötig of über vehicles iteriert, müsste auch mit nur 2 mal gehen:
        	 * 										(1.: Für alle Vehicles: checken vehicle.getFinish)
        	 * 										(2.: kdTree bauen)
        	 * 										(3.: Für alle Vehicles: vehicle.update(neighboursToConsider
        	 * 												dann veh.move()
        	 * 												dann vehicleInfo() )
        	 * 
        	 * Entscheidung:  ENTWEDER____________wir müssen jedes mal wenn wir feststellen dass mind. ein vehicle gefinished ist den kdTree neu bauen
        	 * 											=> teuer
        	 * 				  ODER________________wir ignorieren dass ein veh vllt schon draußen ist und lassen die socialforces zu/von ihm trotzdem
        	 * 									  bis zum nächsten Bau von kdTree berechnen
        	 * 											=> billig aber ungenau
        	 *
        	 * momentan umgesetzt ist erstere option, aber gerade wenn viele agenten unterwegs sind wird das wohl dazu führen,
        	 * dass wir jeden Zeitschritt den kdTree neu bauen 
        	 */
        	
        	boolean vehicleListhasChanged = false;
            for (Iterator<Vehicle> it = this.vehicles.iterator(); it.hasNext();) {
            	Vehicle vehicle = it.next();
            	if (vehicle.getFinish() == true) {
                  	it.remove();
                  	vehicleListhasChanged = true;
                  }
            }

            if(time % 1 == 0 || vehicleListhasChanged){
        		kdTree = new KDTree(this.vehicles);
        		kdTree.buildKDTree();
        	}
        	
            List<VehicleInfo> vehicleInfoList = new ArrayList<>();
            
            for (Vehicle vehicle : this.vehicles) {
            	List<Vehicle> neighboursToConsider = kdTree.getClosestNeighboursOfVehicle(vehicle, nrOfNeighboursToConsider, visualRangeX, visualRangeY);
            	vehicle.update(neighboursToConsider, time);
            
                vehicle.move();
                        
                VehicleInfo vehicleInfo = new VehicleInfo(vehicle.getX(), vehicle.getY(), vehicle.getPhi(), vehicle.getRadius(), vehicle.getColourR(), vehicle.getColourG(), vehicle.getColourB(),
                		vehicle.getForceTarget(), vehicle.getForceVehicles(), vehicle.getForceWalls(), vehicle.isInTheSimulation());
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

    private static void addRandomVehicles(Network network, Simulation sim, int numberOfRandomVehicles) {
        System.out.println("Creating " + numberOfRandomVehicles + " random vehicles");
        for (int i = 0; i < numberOfRandomVehicles; i++){
            Integer startNodeId = listOfNodesIds.get((int) (Math.random() * listOfNodesIds.size()));
            Integer finishNodeId = listOfNodesIds.get((int) (Math.random() * listOfNodesIds.size()));
            System.out.println("trying to create the route from the node " + startNodeId + " to the node " + finishNodeId);
            if (!startNodeId.equals(finishNodeId)){
                createRandomDeparture(network, sim, startNodeId, finishNodeId, i * (int) (Math.random()*20000000));
            }


        }
    }

    private static void createRandomDeparture(Network network, Simulation sim, Integer startNodeId, Integer finishNodeId, int i) {
        double startTime = (Math.random() * (MAX_TIME - 980));
        String vehicleId = "Vehicle_" + startNodeId + "_to_" + finishNodeId + "_at_" + startTime + "_" + (int) Math.random()*10;
        Node startNode = network.nodes.get(startNodeId);
        Node finishNode = network.nodes.get(finishNodeId);
        sim.add(new Vehicle(network, startNode, finishNode, startTime, vehicleId));
        System.out.println("Random Vehicle " + vehicleId + " is created");
    }

    private void add(Vehicle vehicle) {
        this.vehicles.add(vehicle);
    }
    
 
    
}
