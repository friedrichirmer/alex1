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


import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import network.AlexanderplatzNetworkCreator;
import network.Link;
import network.Network;
import network.NetworkUtils;

import network.Node;
import network.TramNetworkCreator;
import network.Wall;

/**
 * Created by laemmel on 24/04/16.
 */
public class Simulation {

    private static final double MAX_TIME = 500;
    static final double TIME_STEP = 0.02;
    private static List<Integer> listOfNodesIds = new ArrayList<Integer>();
    private static final int NUMBER_OF_RANDOM_VEHICLES = 2000;

    public double visualRangeX = 5;
    public double visualRangeY = 5;
    public int nrOfNeighboursToConsider = 100;

    private final Vis vis;
    private List<Vehicle> allVehicles = new ArrayList<>();
    private List<Vehicle> vehiclesInSimulation = new ArrayList<>();
	private List<Tram> tramsInSimulation = new ArrayList<Tram>();
	
	private Network pedestrianNetwork;
	private Network tramNetwork;
	private TramNetworkCreator tramFactory = new TramNetworkCreator();

	public List<Vehicle> getVehicles() {
        return allVehicles;
    }

    public Simulation(Network pedNetwork) {
    	this.pedestrianNetwork = pedNetwork;
    	this.tramNetwork = tramFactory.createNetwork();
        this.vis = new Vis(pedNetwork, tramNetwork);
    }

    public static void main(String[] args) {

    	//Network network = new RectangleNetCreator().createNetwork();
    	//Network network = new TwoRoomsWithCorridorNetworkCreator().createNetwork();
    	Network pedestrianNetwork = new AlexanderplatzNetworkCreator().createNetwork();
        getListOfNodeIds(pedestrianNetwork);
        
    	Simulation simulation = new Simulation(pedestrianNetwork);
    	NetworkUtils.createListOfNodeIds(pedestrianNetwork, simulation);
    	simulation.addRandomVehicles(pedestrianNetwork, simulation, NUMBER_OF_RANDOM_VEHICLES);
//      Simulation simulation = makeTestScenario(network);
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
        KDTree currentKDTree = new KDTree(this.vehiclesInSimulation);
//        createTram(network);
        int oldNrOfVehInSim = vehiclesInSimulation.size();

//        createTram(tramNetwork);
        createTestTramOnPedNetwork();

        while (time < MAX_TIME) {
            time = roundAndPrintTime(time);

            boolean vehicleHasLeft = updateListAndCheckIfVehicleHasLeft(time);
            currentKDTree = getNewKdTree(time, currentKDTree, oldNrOfVehInSim, vehicleHasLeft);
            updateLinkWeights(time);

            Set<Wall> allStaticWallsInSimulation = new HashSet<Wall>();
            Set<Wall> allTramWallsInSimulation = new HashSet<Wall>();

            allStaticWallsInSimulation.addAll(this.pedestrianNetwork.staticWalls);
            List<TramInfo> tramInfoList = new ArrayList<TramInfo>();
            updateTramPositions(currentKDTree, allStaticWallsInSimulation, tramInfoList);

            List<VehicleInfo> vehicleInfoList = new ArrayList<VehicleInfo>();
            updateVehiclePositions(time, currentKDTree, allStaticWallsInSimulation, allTramWallsInSimulation, vehicleInfoList);

            this.vis.update(time, vehicleInfoList,tramInfoList);

            if (Double.toString(time).endsWith("0")) this.vis.updateVoronoi(vehiclesInSimulation);

            oldNrOfVehInSim = this.vehiclesInSimulation.size();

            time += TIME_STEP;

            try {
                Thread.sleep((long) (TIME_STEP * 1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }

    private double roundAndPrintTime(double time) {
        time *= 100;
        time = Math.round(time);
        time /= 100;
        System.out.println("time = " + time);
        return time;
    }
/*
    boolean overRect(int x, int y, int width, int height)  {
        if (mouseX >= x && mouseX <= x+width &&
                mouseY >= y && mouseY <= y+height) {
            return true;
        } else {
            return false;
        }
    }
*/
    private void updateVehiclePositions(double time, KDTree currentKDTree, Set<Wall> allStaticWallsInSimulation, Set<Wall> allTramWallsInSimulation, List<VehicleInfo> vehicleInfoList) {
        for (Iterator<Vehicle> vehicleIterator = this.vehiclesInSimulation.iterator(); vehicleIterator.hasNext();) {
            Vehicle vehicle = vehicleIterator.next();

            //mit kdTree
            List<Vehicle> neighboursToConsider = currentKDTree.getClosestNeighboursOfVehicle(vehicle, nrOfNeighboursToConsider, visualRangeX, visualRangeY);
            vehicle.update(neighboursToConsider, time, allStaticWallsInSimulation, allTramWallsInSimulation);

            //ohne kdTree
//            	vehicle.update(vehiclesInSimulation, time, allWallsInSimulation);
            vehicle.move(time);
            VehicleInfo vehicleInfo = new VehicleInfo(vehicle.getX(), vehicle.getY(), vehicle.getPhi(), vehicle.getRadius(),
                                                    vehicle.getForceTarget(), vehicle.getForceVehicles(), vehicle.getForceWalls(), vehicle.momentSpeed);
            vehicleInfoList.add(vehicleInfo);
        }
    }

    private void updateTramPositions(KDTree currentKDTree, Set<Wall> allTramWallsInSimulation, List<TramInfo> tramInfoList) {
        for(Iterator<Tram> tramIterator = this.tramsInSimulation.iterator(); tramIterator.hasNext();) {
            Tram tram = tramIterator.next();
            tram.update(vehiclesInSimulation, currentKDTree);
            tram.move();

            if(!tram.isFinished()){
                allTramWallsInSimulation.addAll(tram.getWalls());
                TramInfo tramInfo = new TramInfo(tram);
                tramInfoList.add(tramInfo);
            }
            else{
                tramIterator.remove();
            }
        }
    }

    private void updateLinkWeights(double time) {
        if (time % 5 == 0 && time > 0){
            recalculateWeightOfLinksBasedOnCurrentTravelTimes(this.pedestrianNetwork, time);
        }
    }

    private boolean updateListAndCheckIfVehicleHasLeft(double time) {
        this.vehiclesInSimulation.clear();
//        	System.out.println("--check after clearing before adding-- \n oldNrOfVehInSim=" + oldNrOfVehInSim + "\n vehiclesInSimulation.size()=" + vehiclesInSimulation.size());

        boolean vehicleHasLeft = false;
        for (Iterator<Vehicle> vehicleIterator = this.allVehicles.iterator(); vehicleIterator.hasNext();) {
            Vehicle vehicle = vehicleIterator.next();
            if (vehicle.getFinished() == true) {
                System.out.println("############Vehicle " + vehicle.getId() + " gets removed###########");
                  vehicleIterator.remove();
                  vehicleHasLeft = true;
             }  else if(vehicle.entersSimulation(time)){
                this.vehiclesInSimulation.add(vehicle);
             }
        }
        return vehicleHasLeft;
    }

    private KDTree getNewKdTree(double time, KDTree kdTree, int oldNrOfVehInSim, boolean vehicleHasLeft) {
        if(time % 1 == 0 || vehicleHasLeft || vehiclesInSimulation.size() != oldNrOfVehInSim){
            System.out.println("###build new kdTree###");
            List<Vehicle> vehInSimCopy = new ArrayList<Vehicle>();
            vehInSimCopy.addAll(vehiclesInSimulation);
            kdTree = new KDTree(vehInSimCopy);
            kdTree.buildKDTree();
        }
        return kdTree;
    }

    private void recalculateWeightOfLinksBasedOnCurrentTravelTimes(Network network, double time) {
        Iterator<Link> linkIterator = network.getLinks().values().iterator();
        while (linkIterator.hasNext()){
            Link link= linkIterator.next();
            link.calculateRecentLinkWeights(allVehicles, time);
        }
    }

    private void addRandomVehicles(Network network, Simulation sim, int numberOfRandomVehicles) {
        System.out.println("Creating " + numberOfRandomVehicles + " random vehicles");
        
        DijkstraV2 router = new DijkstraV2(network);
        
        int nrRoutesNull = 0;
        for (int i = 0; i < numberOfRandomVehicles; i++){

            Integer startNodeId =  network.getEntryExitNodes().get(((int) (Math.random() * network.getEntryExitNodes().size()))).getId();
            Integer finishNodeId =  network.getEntryExitNodes().get(((int) (Math.random() * network.getEntryExitNodes().size()))).getId();
            if (!startNodeId.equals(finishNodeId)){
            	List<Link> route = router.calculateRoute(network.getNodes().get(startNodeId), network.getNodes().get(finishNodeId));
                if (!(route == null)) {
                    this.createRandomDeparture(network, sim, startNodeId, finishNodeId, route);
                }
                else{
                	nrRoutesNull ++;
                }
            }
            
        }
        
        System.out.println("created all " + numberOfRandomVehicles + " vehicles");
        System.out.println(" number of routes that were null = " + nrRoutesNull);
        System.out.println(" simulation allVehicles.size() = " + this.allVehicles.size());
    }
    
    private void createTram(Network tramNetwork){
    	DijkstraV2 router = new DijkstraV2(tramNetwork);
    	Node from = this.tramFactory.getrandomEntryNode();
    	Node to = this.tramFactory.getTramExitNode(from);
    	List<Link> route = router.calculateRoute(from, to);
    	Tram tram = new Tram(from.getX(),from.getY() , route);
    	if(route == null || route.size() == 0){
    		System.out.println("couldnt create tram route");
    	}
    	this.tramsInSimulation.add(tram);
    }

    private void createTestTramOnPedNetwork(){
    	DijkstraV2 router = new DijkstraV2(pedestrianNetwork);
    	Node from = pedestrianNetwork.getNodes().get(12);
    	Node to = pedestrianNetwork.getNodes().get(39);
    	List<Link> route = router.calculateRoute(from, to);
    	Tram tram = new Tram(from.getX(),from.getY() , route);

    	this.tramsInSimulation.add(tram);
    }

    private void createRandomDeparture(Network network, Simulation simulation, Integer startNodeId, Integer finishNodeId, List<Link> route) {
    	
        double startTime = (Math.random() * (0.1*MAX_TIME));
        String vehicleId = "Vehicle_" + startNodeId + "_to_" + finishNodeId + "_at_" + startTime + "_" + (int) Math.random()*10;
        Node startNode = network.nodes.get(startNodeId);
        Node finishNode = network.nodes.get(finishNodeId);
        Vehicle v = new Vehicle(network, startNode, finishNode, startTime, vehicleId, route);
        simulation.addVehicle(v);
        System.out.println("Random Vehicle " + vehicleId + " is created");
        v.placeVehicleSomwhereOnCurrentLink();
    }

    public void addVehicle(Vehicle vehicle) {
        this.allVehicles.add(vehicle);
    }

	public List<Integer> getListOfNodesIds() {
		return this.listOfNodesIds;
	}
    
}
