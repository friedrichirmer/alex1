package network;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import alex.Simulation;
import alex.Vehicle;

public class NetworkUtils {

	
	/**
	 * @param network
	 * @return

	public static Simulation makeTestScenario(Network network) {
		Simulation simulation = new Simulation(network);
        createListOfNodeIds(network, simulation);
        List<Integer> listOfNodesIds = simulation.getListOfNodesIds();
        Node startNodeId = network.nodes.get(listOfNodesIds.get((int) (Math.random() * listOfNodesIds.size())));
        Node finishNodeId = network.nodes.get(listOfNodesIds.get((int) (Math.random() * listOfNodesIds.size())));
        
        Vehicle v1 = new Vehicle(network, startNodeId, finishNodeId, 1.0, "1");
        Vehicle v3 = new Vehicle(network, startNodeId, finishNodeId, 16.0, "3");
        Vehicle v5 = new Vehicle(network, startNodeId, finishNodeId, 31.0, "5");
        
        Vehicle v2 = new Vehicle(network, finishNodeId, startNodeId, 4.5, "2");
        Vehicle v4 = new Vehicle(network, finishNodeId, startNodeId, 19.5, "4");
        
        simulation.addVehicle(v1);
        simulation.addVehicle(v2);
        simulation.addVehicle(v3);
        simulation.addVehicle(v4);
        simulation.addVehicle(v5);
		return simulation;
	}
*/

    public static void createListOfNodeIds(Network network, Simulation sim) {
        Map nodesMap = network.getNodes();
        Iterator<Integer> nodeIdIterator = nodesMap.keySet().iterator();
        while ( nodeIdIterator.hasNext()){
            Integer nodeId = nodeIdIterator.next();
            sim.getListOfNodesIds().add(nodeId);
        }
    }
	
}
