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


import java.text.DecimalFormat;
import java.util.*;

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
    private static final int NUMBER_OF_RANDOM_VEHICLES = 500;

    public double visualRangeX = 5;
    public double visualRangeY = 5;
    public int nrOfNeighboursToConsider = 100;

    private final Vis vis;
    private List<Vehicle> allVehicles = new ArrayList<>();
    private List<Vehicle> vehiclesInSimulation = new ArrayList<>();
    private List<Tram> allTrams = new ArrayList<Tram>();
	private List<Tram> tramsInSimulation = new ArrayList<Tram>();
	
	private Network pedestrianNetwork;
	private Network tramNetwork;
	private TramNetworkCreator tramFactory = new TramNetworkCreator();
	private boolean evacuationReroutingHappened = false;

	public List<Vehicle> getVehicles() {
        return allVehicles;
    }

    public Simulation(Network pedNetwork) {
    	this.pedestrianNetwork = pedNetwork;
    	this.tramNetwork = tramFactory.createNetwork();
        this.vis = new Vis(pedNetwork, tramNetwork);
    }

    public static void main(String[] args) {

    	Network pedestrianNetwork = new AlexanderplatzNetworkCreator().createNetwork();
        getListOfNodeIds(pedestrianNetwork);
        
    	Simulation simulation = new Simulation(pedestrianNetwork);
    	NetworkUtils.createListOfNodeIds(pedestrianNetwork, simulation);
    	simulation.addRandomVehicles(pedestrianNetwork, simulation, NUMBER_OF_RANDOM_VEHICLES);
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
        int oldNrOfVehInSim = vehiclesInSimulation.size();

        for(Node node: this.tramFactory.getTramExitNodes()){
        	createTram(tramNetwork, 3*Math.random(), node);
        }
        while (time < MAX_TIME) {
            time = roundAndPrintTime(time);
            Vis.drawTime(time);
            if (Vis.alarmActivated){
                handleAlarm(time);
            }
            
            if(!Vis.simPaused){
            	boolean vehicleHasLeft = updateVehicleListAndCheckIfVehicleHasLeft(time);
            	currentKDTree = getNewKdTree(time, currentKDTree, oldNrOfVehInSim, vehicleHasLeft);
            	updateLinkWeights(time);
            	
            	Set<Wall> allStaticWallsInSimulation = new HashSet<Wall>();
            	
            	allStaticWallsInSimulation.addAll(this.pedestrianNetwork.staticWalls);
            	List<TramInfo> tramInfoList = new ArrayList<TramInfo>();
            	updateTramListAndTramPositions(currentKDTree, tramInfoList, time);
            	
            	List<VehicleInfo> vehicleInfoList = new ArrayList<VehicleInfo>();
            	updateVehiclePositions(time, currentKDTree, allStaticWallsInSimulation, vehicleInfoList);
            	
            	this.vis.update(time, vehicleInfoList,tramInfoList);
            	
            	if (Double.toString(time).endsWith("0")) this.vis.updateVoronoi(vehiclesInSimulation);
            	
            	oldNrOfVehInSim = this.vehiclesInSimulation.size();
            }


            time += TIME_STEP;

            try {
                Thread.sleep((long) (TIME_STEP * 1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }

	/**
	 * @param time
	 */
	private void handleAlarm(double time) {
		if (!evacuationReroutingHappened){
		    Iterator<Vehicle> vehicleIterator = this.allVehicles.iterator();
		    while (vehicleIterator.hasNext()){
		        Vehicle vehicleToEvacuate = vehicleIterator.next();
		        if (!(pedestrianNetwork.evacuationNodes.contains(vehicleToEvacuate.destinationNode))){
		            Node currentStartNode = pedestrianNetwork.findNearestNode(vehicleToEvacuate.getX(),
		                    vehicleToEvacuate.getY());
		            DijkstraV2 router = new DijkstraV2(pedestrianNetwork);
		            Random random = new Random();
		            Node newDestination = pedestrianNetwork.evacuationNodes.get(0);
		            vehicleToEvacuate.route = router.calculateRoute(currentStartNode, newDestination);
		            vehicleToEvacuate.currentLink = vehicleToEvacuate.route.get(0);
		            vehicleToEvacuate.mapOfEnterLeaveTimes.clear();
		            vehicleToEvacuate.mapOfEnterLeaveTimes.put(vehicleToEvacuate.currentLink.getId(), new Double[]{time, null});
		            System.out.println("The vehicle " + vehicleToEvacuate.getId() + " will be evacuated to the point " + newDestination.getId());
		        }
		    }
		    evacuationReroutingHappened = true;
		}
	}

    private double roundAndPrintTime(double time) {
        time *= 100;
        time = Math.round(time);
        time /= 100;
        System.out.println("time = " + time);
        return time;
    }

    private void updateVehiclePositions(double time, KDTree currentKDTree, Set<Wall> allStaticWallsInSimulation, List<VehicleInfo> vehicleInfoList) {
        for (Iterator<Vehicle> vehicleIterator = this.vehiclesInSimulation.iterator(); vehicleIterator.hasNext();) {
            Vehicle vehicle = vehicleIterator.next();

            List<Vehicle> neighboursToConsider = currentKDTree.getClosestNeighboursOfVehicle(vehicle, nrOfNeighboursToConsider, visualRangeX, visualRangeY);
            vehicle.update(neighboursToConsider, time, allStaticWallsInSimulation, tramsInSimulation);

            vehicle.move(time);
            VehicleInfo vehicleInfo = new VehicleInfo(vehicle.getX(), vehicle.getY(), vehicle.getPhi(), vehicle.getRadius(),
                                                    vehicle.getForceTarget(), vehicle.getForceVehicles(), vehicle.getForceWalls(), vehicle.momentSpeed);
            vehicleInfoList.add(vehicleInfo);
        }
    }

    private void updateTramListAndTramPositions(KDTree currentKDTree, List<TramInfo> tramInfoList, double time) {
    	List<Node> passedExitNodes = new ArrayList<Node>();
        this.tramsInSimulation.clear();
        for(Iterator<Tram> tramIterator = this.allTrams.iterator(); tramIterator.hasNext();) {
            Tram tram = tramIterator.next();
            
            if(!tram.isFinished() && tram.entersSimulation(time)){
            	this.tramsInSimulation.add(tram);
            } else if(tram.isFinished()){
            	passedExitNodes.add(tram.getDestinationNode());
                tramIterator.remove();
            }
        }
        for(Iterator<Tram> tramIterator = this.tramsInSimulation.iterator(); tramIterator.hasNext();) {
        	Tram tram = tramIterator.next();
        	tram.update(vehiclesInSimulation, currentKDTree);
            tram.move();
            TramInfo tramInfo = new TramInfo(tram);
            tramInfoList.add(tramInfo);
        }
    	for(Node exit : passedExitNodes){
    		createTram(tramNetwork, time + Math.random() * 4, exit);
    	}
    }

    private void updateLinkWeights(double time) {
        if (time % 5 == 0 && time > 0){
            recalculateWeightOfLinksBasedOnCurrentTravelTimes(this.pedestrianNetwork, time);
        }
    }

    private boolean updateVehicleListAndCheckIfVehicleHasLeft(double time) {
        this.vehiclesInSimulation.clear();
//        	System.out.println("--check after clearing before adding-- \n oldNrOfVehInSim=" + oldNrOfVehInSim + "\n vehiclesInSimulation.size()=" + vehiclesInSimulation.size());

        boolean vehicleHasLeft = false;
        for (Iterator<Vehicle> vehicleIterator = this.allVehicles.iterator(); vehicleIterator.hasNext();) {
            Vehicle vehicle = vehicleIterator.next();
            if (vehicle.getFinished() == true) {
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
            link.calculateCurrentLinkWeights(allVehicles, time);
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
    
    private void createTram(Network tramNetwork, double time, Node exitNode){
    	DijkstraV2 router = new DijkstraV2(tramNetwork);
    	Node entryNode = this.tramFactory.getCorrespondingEntryNode(exitNode);
    	List<Link> route = router.calculateRoute(entryNode, exitNode);
    	Tram tram = new Tram(entryNode.getX(),entryNode.getY() , route, time);
    	if(route == null || route.size() == 0){
    		System.out.println("couldnt create tram route");
    	}
    	this.allTrams.add(tram);
    }

    private void createRandomDeparture(Network network, Simulation simulation, Integer startNodeId, Integer finishNodeId, List<Link> route) {
        Double startTime = (Math.random() * (0.0001*MAX_TIME));
        String doubleDigitStartTime = new DecimalFormat("#.##").format(startTime);
        String vehicleId = "Vehicle_" + startNodeId + "_to_" + finishNodeId + "_at_" + doubleDigitStartTime + "_" + (int) Math.random()*10;
        Node startNode = network.nodes.get(startNodeId);
        Node finishNode = network.nodes.get(finishNodeId);
        Vehicle v = new Vehicle(network, startNode, finishNode, startTime, vehicleId, route);
        simulation.addVehicle(v);
//        System.out.println("Random Vehicle " + vehicleId + " is created");
        v.placeVehicleSomwhereOnCurrentLink();
    }

    public void addVehicle(Vehicle vehicle) {
        this.allVehicles.add(vehicle);
    }

	public List<Integer> getListOfNodesIds() {
		return this.listOfNodesIds;
	}
    
}
