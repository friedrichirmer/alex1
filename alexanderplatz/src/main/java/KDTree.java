import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class KDTree {

	private List<Vehicle> origin;
	private KDTree leftChild;
	private KDTree rightChild;
	private double xMin, xMax, yMin, yMax;
	
	public KDTree(List<Vehicle> list){
		this.origin = list;
		xMin = Double.MAX_VALUE;
		xMax = Double.MAX_VALUE;
		yMin = Double.MAX_VALUE;
		yMax = Double.MAX_VALUE;
	}
	
	KDTree(List<Vehicle> subList, KDTree kdTree) {
		this.origin = subList;
		this.xMin = kdTree.xMin;
		this.xMax = kdTree.xMax;
		this.yMin = kdTree.yMin;
		this.yMax = kdTree.yMax;
	}

	public KDTree buildKDTree (){
		return this.buildKDTree(0);
	}
	
	private KDTree buildKDTree (int depth){
		
		if(this.origin.size() != 1){
			if(depth % 2 == 0){
				if(depth == 0){
					Collections.sort(this.origin, new YComparator());
					this.yMin = origin.get(0).getY();
					this.yMax = origin.get(origin.size()-1).getY();
				}
				Collections.sort(this.origin, new XComparator());
				this.xMin = origin.get(0).getX();
				this.xMax = origin.get(origin.size()-1).getX();
			}
			else{
				Collections.sort(this.origin, new YComparator());
				this.yMin = origin.get(0).getY();
				this.yMax = origin.get(origin.size()-1).getY();
			}
			Double midIndex = (double) (this.origin.size()/2);
			leftChild = new KDTree(this.origin.subList(0, midIndex.intValue()), this );
			rightChild = new KDTree(this.origin.subList(midIndex.intValue() + 1, this.origin.size()), this);
			
			if(depth % 2 == 0){
				leftChild.xMax = this.origin.get(midIndex.intValue()).getX();
				rightChild.xMin = this.origin.get(midIndex.intValue() +1 ).getX();
			}
			else{
				leftChild.yMax = this.origin.get(midIndex.intValue()).getY();
				rightChild.yMin = this.origin.get(midIndex.intValue() +1 ).getY();
			}
			
			leftChild.buildKDTree(depth+1);
			rightChild.buildKDTree(depth+1);
		}
		else{
			xMax = xMin = this.origin.get(0).getX();
			yMax = yMin = this.origin.get(0).getY();
		}
		return this;
	}
	
	public List<Vehicle> searchKDTree(double xMin, double yMin, double xMax, double yMax){
		
		if(this.isFullyContainedInRange(xMin, yMin, xMax, yMax)){
			return this.origin;
		}
		else{
			return this.processSearch(xMin, yMin, xMax, yMax);
		}
		
	}
	
	private List<Vehicle> processSearch(double xMin, double yMin, double xMax, double yMax){
		List<Vehicle> report = new ArrayList<Vehicle>();
		if(this.origin.size() != 1){		
			//linken Baum abfragen
			if(this.leftChild.isFullyContainedInRange(xMin, yMin, xMax, yMax)){
				report.addAll(leftChild.origin);
			}
			else if(this.leftChild.intersectsRange(xMin, yMin, xMax, yMax)){
				report.addAll(leftChild.processSearch(xMin, yMin, xMax, yMax));
			}
			//rechten Baum abfragen
			if(this.rightChild.isFullyContainedInRange(xMin, yMin, xMax, yMax)){
				report.addAll(rightChild.origin);
			}
			else if(this.rightChild.intersectsRange(xMin, yMin, xMax, yMax)){
				report.addAll(rightChild.processSearch(xMin, yMin, xMax, yMax));
			}
		}
		else{
			if(isFullyContainedInRange(xMin, yMin, xMax, yMax)) report = this.origin;
		}
		return report;
	}
	
	private boolean intersectsRange(double left, double bottom, double right, double top) {
		
		//case 0: Partition is definitely outside of range
		if(this.xMax < left || this.xMin > right || this.yMax < bottom || this.yMin > top) return false;
		
		//case 1: Range is fully contained in this partition
		if(this.xMin < left && this.yMin < bottom && this.xMax > right && this.yMax > top)	return true;
		
		if( (left < this.xMax && this.xMax < right) || (left < this.xMin && this.xMin < right) )
			return( (this.yMin > bottom && this.yMin < top) || (this.yMax < top && this.yMax > bottom));
		
		return false;
	}

	private boolean isFullyContainedInRange(double xMin, double yMin, double xMax, double yMax){
		return(this.xMax < xMax && this.xMin > xMin && this.yMax < yMax && this.yMin > yMin);
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
