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


import networkUtils.Link;
import networkUtils.Network;
import networkUtils.Node;
import networkUtils.RectangleNetCreator;

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
    private static final double MAX_TIME = 500;
    static final double TIME_STEP = 0.1;
    private static List<Integer> listOfNodesIds = new ArrayList<Integer>();
    private static final int NUMBER_OF_RANDOM_VEHICLES = 200;


    private final Vis vis;
    private List<Vehicle> allVehicles = new ArrayList<>();
    private List<Vehicle> vehiclesInSimulation = new ArrayList<>();
    
    public List<Vehicle> getVehicles() {
        return allVehicles;
    }

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
   	
//    Network network = new RectangleNetCreator().createNetwork();
    	
   	getListOfNodeIds(network);
    	
     Simulation simulation = new Simulation(network);
     addRandomVehicles(network, simulation, NUMBER_OF_RANDOM_VEHICLES);
    
//        Simulation simulation = makeTestScenario(network);

        simulation.run(network);
        
    }

	/**
	 * @param network
	 * @return
	 */
	private static Simulation makeTestScenario(Network network) {
		Simulation simulation = new Simulation(network);
        
        Node startNodeId = network.nodes.get(listOfNodesIds.get((int) (Math.random() * listOfNodesIds.size())));
        Node finishNodeId = network.nodes.get(listOfNodesIds.get((int) (Math.random() * listOfNodesIds.size())));
        
        Vehicle v1 = new Vehicle(network, startNodeId, finishNodeId, 1.0, "1");
        Vehicle v3 = new Vehicle(network, startNodeId, finishNodeId, 16.0, "3");
        Vehicle v5 = new Vehicle(network, startNodeId, finishNodeId, 31.0, "5");
        
        Vehicle v2 = new Vehicle(network, finishNodeId, startNodeId, 4.5, "2");
        Vehicle v4 = new Vehicle(network, finishNodeId, startNodeId, 19.5, "4");
        
        simulation.add(v1);
        simulation.add(v2);
        simulation.add(v3);
        simulation.add(v4);
        simulation.add(v5);
		return simulation;
	}

    private static void getListOfNodeIds(Network network) {
        Map nodesMap = network.getNodes();
        Iterator<Integer> nodeIdIterator = nodesMap.keySet().iterator();
        while ( nodeIdIterator.hasNext()){
            Integer nodeId = nodeIdIterator.next();
            listOfNodesIds.add(nodeId);
        }
    }

    private void run(Network network) {
        double time = 0;

        KDTree kdTree = new KDTree(this.vehiclesInSimulation);
        double visualRangeX = 5;
        double visualRangeY = 5;
        int nrOfNeighboursToConsider = 5;
        
        int oldNrOfVehInSim = vehiclesInSimulation.size();
        
        while (time < MAX_TIME) {
        	time *= 100;
        	time = Math.round(time);
        	time /= 100;
        	System.out.println("time = " + time);
        	
        	System.out.println("--check before clearing-- \n oldNrOfVehInSim=" + oldNrOfVehInSim + "\n vehiclesInSimulation.size()=" + vehiclesInSimulation.size());
        	/* Tilmann  6.7.:
        	 * 
        	 * Logik:
        	 * 
        	 * gehe durch Liste aller Vehicles
        	 * stelle fest wer sich in der simulation befindet
        	 * füge den in entsprechende liste
        	 * 	
        	 * operiere anschließend nur noch auf dieser liste,
        	 * wenn sie sich ändert, dann baue auch den kdTree neu
        	 * 
        	 */
        	
        	this.vehiclesInSimulation.clear();
        	System.out.println("--check after clearing before adding-- \n oldNrOfVehInSim=" + oldNrOfVehInSim + "\n vehiclesInSimulation.size()=" + vehiclesInSimulation.size());
        	boolean vehicleHasLeft = false;
            for (Iterator<Vehicle> vehicleIterator = this.allVehicles.iterator(); vehicleIterator.hasNext();) {
            	Vehicle vehicle = vehicleIterator.next();
            	if (vehicle.getFinish() == true) {
            		System.out.println("############Vehicle " + vehicle.getId() + " gets removed###########");
                  	vehicleIterator.remove();
                  	vehicleHasLeft = true;
                 }  else if(vehicle.entersSimulation(time)){
                	this.vehiclesInSimulation.add(vehicle); 
                 }
            }
        	System.out.println("--check after adding-- \n oldNrOfVehInSim=" + oldNrOfVehInSim + "\n vehiclesInSimulation.size()=" + vehiclesInSimulation.size());

            if(time % 1 == 0 || vehicleHasLeft || vehiclesInSimulation.size() != oldNrOfVehInSim){
            	System.out.println("###build new kdTree###");
            	List<Vehicle> vehInSimCopy = new ArrayList<Vehicle>();
            	vehInSimCopy.addAll(vehiclesInSimulation); 
        		kdTree = new KDTree(vehInSimCopy);
        		kdTree.buildKDTree();
        	}

//            if (time % 5 == 0 && time > 0){
//                recalculateWeightOfLinksBasedOnCurrentTravelTimes(network, time);
//            }

            List<VehicleInfo> vehicleInfoList = new ArrayList<>();
            
            for (Iterator<Vehicle> vehicleIterator = this.vehiclesInSimulation.iterator(); vehicleIterator.hasNext();) {
            	Vehicle vehicle = vehicleIterator.next();
            	List<Vehicle> neighboursToConsider = kdTree.getClosestNeighboursOfVehicle(vehicle, nrOfNeighboursToConsider, visualRangeX, visualRangeY);
                vehicle.update(neighboursToConsider, time);
                vehicle.move(time);
                VehicleInfo vehicleInfo = new VehicleInfo(vehicle.getX(), vehicle.getY(), vehicle.getPhi(), vehicle.getRadius(), vehicle.getColourR(), vehicle.getColourG(), vehicle.getColourB(),
                										vehicle.getForceTarget(), vehicle.getForceVehicles(), vehicle.getForceWalls());
                vehicleInfoList.add(vehicleInfo);
            }
            
            this.vis.update(time, vehicleInfoList);

            oldNrOfVehInSim = this.vehiclesInSimulation.size();
            
            time += TIME_STEP;

            try {
                Thread.sleep((long) (TIME_STEP * 1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
        }
        
    }

    private void recalculateWeightOfLinksBasedOnCurrentTravelTimes(Network network, double time) {
        Iterator<Link> linkIterator = network.getLinks().values().iterator();
        while (linkIterator.hasNext()){
            Link link= linkIterator.next();
            link.calculateRecentLinkWeights(allVehicles, time);
            System.out.println("The new weight of the link " + link.getId() + " was calculated");
        }
    }

    private static void addRandomVehicles(Network network, Simulation sim, int numberOfRandomVehicles) {
        System.out.println("Creating " + numberOfRandomVehicles + " random vehicles");
        for (int i = 0; i < numberOfRandomVehicles; i++){
            Integer startNodeId = listOfNodesIds.get((int) (Math.random() * listOfNodesIds.size()));
            Integer finishNodeId = listOfNodesIds.get((int) (Math.random() * listOfNodesIds.size()));
            System.out.println("trying to create the route from the node " + startNodeId + " to the node " + finishNodeId);
            if (!startNodeId.equals(finishNodeId)){
                createRandomDeparture(network, sim, startNodeId, finishNodeId);
            }


        }
    }

    private static void createRandomDeparture(Network network, Simulation simulation, Integer startNodeId, Integer finishNodeId) {
        double startTime = (Math.random() * (0.9*MAX_TIME));
        String vehicleId = "Vehicle_" + startNodeId + "_to_" + finishNodeId + "_at_" + startTime + "_" + (int) Math.random()*10;
        Node startNode = network.nodes.get(startNodeId);
        Node finishNode = network.nodes.get(finishNodeId);
        simulation.add(new Vehicle(network, startNode, finishNode, startTime, vehicleId));
        System.out.println("Random Vehicle " + vehicleId + " is created");
    }

    private void add(Vehicle vehicle) {
        this.allVehicles.add(vehicle);
    }
    
 
    
}
