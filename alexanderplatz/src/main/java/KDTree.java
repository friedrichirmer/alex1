import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class KDTree {

	private List<Vehicle> origin;
	private KDTree leftChild;
	private KDTree rightChild;
	
	public KDTree(List<Vehicle> list){
		this.origin = list;
	}
	
	public KDTree buildKDTree (){
		return this.buildKDTree(0);
	}
	
	private KDTree buildKDTree (int depth){
		
		if(this.origin.size() != 1){
			if(depth % 2 == 0){
				Collections.sort(this.origin, new XComparator());
			}
			else{
				Collections.sort(this.origin, new YComparator());
			}
			Double midIndex = (double) (this.origin.size()/2);
			leftChild = new KDTree(this.origin.subList(0, midIndex.intValue()) );
			leftChild.buildKDTree(depth+1);
			rightChild = new KDTree(this.origin.subList(midIndex.intValue() + 1, this.origin.size()));
			rightChild.buildKDTree(depth+1);
		}
		return this;
	}
	
	class XComparator implements Comparator<Vehicle>{

		@Override
		public int compare(Vehicle v1, Vehicle v2) {
			double x1 = v1.getX();
			double x2 = v2.getX();
			return (x1 > x2) ? 1 : ( (x2 > x1) ? -1 : 0 );
		}
		
	}
	
	class YComparator implements Comparator<Vehicle>{

		@Override
		public int compare(Vehicle v1, Vehicle v2) {
			double y1 = v1.getY();
			double y2 = v2.getY();
			return (y1 > y2) ? 1 : ( (y2 > y1) ? -1 : 0 );
		}
		
	}
}
