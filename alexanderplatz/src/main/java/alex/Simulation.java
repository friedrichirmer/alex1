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


import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import network.Link;
import network.Network;
import network.NetworkUtils;
import network.Node;
import network.RectangleNetCreator;
import network.TwoRoomsWithCorridorNetworkCreator;

/**
 * Created by laemmel on 24/04/16.
 */
public class Simulation {

    public static final double SCALE = 25;
    private static final double MAX_TIME = 500;
    static final double TIME_STEP = 0.1;
    private static List<Integer> listOfNodesIds = new ArrayList<Integer>();
    private static final int NUMBER_OF_RANDOM_VEHICLES = 500;

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

        Node node1 = network.createNode(49.5,58.2,1);
//        Node node2 = network.createNode(49.7,40.8,2);
//        Node node3 = network.createNode(43.1,33.3,3);
//        Node node4 = network.createNode(19.4,43,4);
//        Node node5 = network.createNode(12.2,35.6,5);
//        Node node6 = network.createNode(23.8,21.9,6);
//        Node node7 = network.createNode(24.1,0.9,7);
//        Node node8 = network.createNode(38.9,12,8);
//        Node node9 = network.createNode(66.5,33,9);
//        Node node10 = network.createNode(25.5,30.6,10);
        Node node11 = network.createNode(66.7,33.1,11);
        Node node12 = network.createNode(54.1,52.4,12);
        Node node13 = network.createNode(40.6,66.7,13);
        Node node14 = network.createNode(29.7,52,14);
        Node node15 = network.createNode(17.7,39,15);
        Node node16 = network.createNode(24.1,33.3,16);
        Node node17 = network.createNode(34.7,44.8,17);
        Node node18 = network.createNode(47,40.6,18);
        Node node19 = network.createNode(60.8,28.6,19);
        Node node20 = network.createNode(30.4,26.7,20);
        Node node21 = network.createNode(2.9,23.1,21);
        Node node22 = network.createNode(14,12.2,22);
        Node node23 = network.createNode(23.8,3.3,23);

        getListOfNodeIds(network);

        network.createLink(node11,node12,1);
        network.createLink(node12,node13,1);
        network.createLink(node13,node14,1);
        network.createLink(node14,node15,1);
        network.createLink(node15,node16,1);
        network.createLink(node16, node17, 1);
        network.createLink(node17,node18,1);
        network.createLink(node18,node19,1);
        network.createLink(node19,node11,1);
        network.createLink(node20,node18,1);
        network.createLink(node20,node16,1);
        network.createLink(node18,node12,1);
        network.createLink(node17,node14,1);
        network.createLink(node21,node22,1);
        network.createLink(node22,node23,1);
        network.createLink(node23,node19,1);
        network.createLink(node15,node21,1);
        network.createLink(node20,node22,1);
//        network.createLink(node11,node3,1);
//        network.createLink(node12,node11,2);
//        network.createLink(node6,node12,3);
//        network.createLink(node7,node6,3);
//        network.createLink(node8,node7,3);
//        network.createLink(node9,node8,3);
//        network.createLink(node10,node9,3);
//        network.createLink(node6,node10,3);


        //Abgrenzung
        network.createWall(0,23.8,23.7,0);
        network.createWall(23.7,0,67.7,33.2);
        network.createWall(67.7,33.2,49.0,60.9);
        network.createWall(49.0,60.9,40.9,67.6);
        network.createWall(40.9,67.6,0,23.8);
        //Building links
        network.createWall(5.7,23.6,14.5,14.8);
        network.createWall(14.5,14.8,27.2,27.1);
        network.createWall(27.2,27.1,18.3,35.9);
        network.createWall(18.3,35.9,5.7,23.6);
        //Building oben
        network.createWall(19.3,15,24.5,7.7);
        network.createWall(24.5,7.7,44.2,22.1);
        network.createWall(44.2,22.1,39.1,29);
        network.createWall(39.1,29,19.3,15);
        //AlexOase
        network.createWall(39.8,29.6,45,22.9);
        network.createWall(45,22.9,51.2,26.9);
        network.createWall(51.2,26.9,45.4,33.9);
        network.createWall(45.4,33.9,39.8,29.6);
        //Building rechts
        network.createWall(49.8,39.9,61,30);
        network.createWall(61,30,65.6,33.3);
        network.createWall(65.6,33.3,55,49.2);
        network.createWall(55,49.2,53.2,48.3);
        network.createWall(53.2,48.3,49.8,39.9);
        //Building unten
        network.createWall(32,52.6,35.3,50.4);
        network.createWall(35.3,50.4,38,54.1);
        network.createWall(38,54.1,48.6,52);
        network.createWall(48.6,52,50.9,55.6);
        network.createWall(50.9,55.6,48,57.4);
        network.createWall(48,57.4,47.6,56.8);
        network.createWall(47.6,56.8,42.3,60.3);
        network.createWall(42.3,60.3,42.8,61);
        network.createWall(42.8,61,39.4,62.8);
        network.createWall(39.4,62.8,32,52.6);
        //Building links Mitte
        network.createWall(20.5,38.1,23.3,36.2);
        network.createWall(23.3,36.2,31,45.1);
        network.createWall(31,45.1,28.3,47.5);
        network.createWall(28.3,47.5,20.5,38.1);
        //Brunnen
        network.createWall(28.4,32.3,28.8,31.5);
        network.createWall(28.8,31.5,29.7,30.7);
        network.createWall(29.7,30.7,31,30.9);
        network.createWall(31,30.9,31.7,31.5);
        network.createWall(31.7,31.5,32.1,32.5);
        network.createWall(32.1,32.5,31.8,33.5);
        network.createWall(31.8,33.5,30.8,34.2);
        network.createWall(30.8,34.2,29.3,34.1);
        network.createWall(29.3,34.1,28.6,33.2);
        network.createWall(28.6,33.2,28.4,32.3);

//  	Network network = new RectangleNetCreator().createNetwork();
    	//Network network = new TwoRoomsWithCorridorNetworkCreator().createNetwork();
    	
    	Simulation simulation = new Simulation(network);
    	NetworkUtils.createListOfNodeIds(network, simulation);
    	addRandomVehicles(network, simulation, NUMBER_OF_RANDOM_VEHICLES);
    
//      Simulation simulation = makeTestScenario(network);

        simulation.run(network);
        
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
        int nrOfNeighboursToConsider = 10;
        
        int oldNrOfVehInSim = vehiclesInSimulation.size();
        
        while (time < MAX_TIME) {
        	time *= 100;
        	time = Math.round(time);
        	time /= 100;
        	System.out.println("time = " + time);
        	
//        	System.out.println("--check before clearing-- \n oldNrOfVehInSim=" + oldNrOfVehInSim + "\n vehiclesInSimulation.size()=" + vehiclesInSimulation.size());
        	/* Tilmann  6.7.:
        	 * 
        	 * Logik:
        	 * 
        	 * gehe durch Liste aller Vehicles
        	 * stelle fest wer sich in der simulation befindet
        	 * f�ge den in entsprechende liste
        	 * 	
        	 * operiere anschlie�end nur noch auf dieser liste,
        	 * wenn sie sich �ndert, dann baue auch den kdTree neu
        	 * 
        	 */
        	
        	this.vehiclesInSimulation.clear();
//        	System.out.println("--check after clearing before adding-- \n oldNrOfVehInSim=" + oldNrOfVehInSim + "\n vehiclesInSimulation.size()=" + vehiclesInSimulation.size());
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
//        	System.out.println("--check after adding-- \n oldNrOfVehInSim=" + oldNrOfVehInSim + "\n vehiclesInSimulation.size()=" + vehiclesInSimulation.size());

            if(time % 1 == 0 || vehicleHasLeft || vehiclesInSimulation.size() != oldNrOfVehInSim){
            	System.out.println("###build new kdTree###");
            	List<Vehicle> vehInSimCopy = new ArrayList<Vehicle>();
            	vehInSimCopy.addAll(vehiclesInSimulation); 
        		kdTree = new KDTree(vehInSimCopy);
        		kdTree.buildKDTree();
        	}

            if (time % 5 == 0 && time > 0){
                recalculateWeightOfLinksBasedOnCurrentTravelTimes(network, time);
            }

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
        simulation.addVehicle(new Vehicle(network, startNode, finishNode, startTime, vehicleId));
        System.out.println("Random Vehicle " + vehicleId + " is created");
    }

    public void addVehicle(Vehicle vehicle) {
        this.allVehicles.add(vehicle);
    }

	public List<Integer> getListOfNodesIds() {
		return this.listOfNodesIds;
	}
    
 
    
}
