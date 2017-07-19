/*
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import alex.KDTree;
import alex.Vehicle;
import org.junit.Test;



/**
 * @author tilmann
 *
 */
/*
public class KDTreeTest {
	
	Logger log = Logger.getLogger("KDTreeTest");
	
	@Test
	public void testScenario2(){
		int in = 5;
		Double d = (double) in/2;
		log.info("d = " + d +" intvalue= " + d.intValue());
		log.info("Starting test scenario 2");
		log.info("------------------------");
		
		
		List<Vehicle> vehicles = new ArrayList<Vehicle>();
		for(int i = 0; i < 10; i++){
			Vehicle v = new Vehicle(i*10,i*10, i);
			vehicles.add(v);
		}
		
		KDTree tree = new KDTree(vehicles);
		tree.buildKDTree();
		
		log.info("printing kdtree \n \n \n");
		log.info(tree.toString());
		
		
		double xTolerance = 30;
		double yTolerance = 30;
		
		for(Vehicle v : vehicles){
			log.info("looking at vehicle: " + v.toString());
			
				String str = "\t";
			for(Vehicle vvv: tree.searchKDTree(v.getX()-xTolerance, v.getY()-yTolerance, v.getX()+xTolerance, v.getY()+yTolerance)){
				str += vvv.toString() + "\n \t";
			}
			log.info("  these are the neighbours: \n" + str);
		}
		
	}

}

*/