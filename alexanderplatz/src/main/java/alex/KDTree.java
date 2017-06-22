package alex;

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
	
	private KDTree(List<Vehicle> subList, KDTree kdTree) {
		this.origin = subList;
		this.xMin = kdTree.xMin;
		this.xMax = kdTree.xMax;
		this.yMin = kdTree.yMin;
		this.yMax = kdTree.yMax;
	}

	public KDTree buildKDTree (){
		return this.buildKDTree(1);
	}
	
	private KDTree buildKDTree (int depth){
		
		
		if(this.origin.size() != 1){
			
			//sortieren und r�nder merken
			if(depth % 2 == 0){
				Collections.sort(this.origin, new YComparator());
				this.yMin = origin.get(0).getY();
				this.yMax = origin.get(origin.size()-1).getY();
			}
			else{
				if(depth == 1){
					Collections.sort(this.origin, new YComparator());
					this.yMin = origin.get(0).getY();
					this.yMax = origin.get(origin.size()-1).getY();
				}
				Collections.sort(this.origin, new XComparator());
				this.xMin = origin.get(0).getX();
				this.xMax = origin.get(origin.size()-1).getX();
			}
			
			//teilen
			int midIndex;
			if(this.origin.size() % 2 == 0){
				midIndex = this.origin.size()/2;
			}
			else{
				midIndex = (int) this.origin.size()/2;
			}
			
			if(this.origin.size() == 2){
				List<Vehicle> l = new ArrayList<Vehicle>();
				l.add(this.origin.get(0));
				List<Vehicle> r = new ArrayList<Vehicle>();
				r.add(this.origin.get(1));
				
				leftChild = new KDTree(l, this );
				rightChild = new KDTree(r, this);
			}
			else{
				//achtung bei ser subList-Methode ist die Obergrenze exklusiv!!
				leftChild = new KDTree(this.origin.subList(0, midIndex), this );
				rightChild = new KDTree(this.origin.subList(midIndex, this.origin.size()), this);
			}
			
			if(depth % 2 == 0){
				leftChild.yMax = this.origin.get(midIndex-1).getY();
				rightChild.yMin = this.origin.get(midIndex).getY();
			}
			else{
				leftChild.xMax = this.origin.get(midIndex-1).getX();
				rightChild.xMin = this.origin.get(midIndex).getX();
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
		if(this.xMin <= left && this.yMin <= bottom && this.xMax >= right && this.yMax >= top)	return true;
		
		if( (left <= this.xMax && this.xMax <= right) || (left <= this.xMin && this.xMin <= right) )
			return( (this.yMin >= bottom && this.yMin <= top) || (this.yMax <= top && this.yMax >= bottom));
		
		return false;
	}

	private boolean isFullyContainedInRange(double xMin, double yMin, double xMax, double yMax){
		return(this.xMax <= xMax && this.xMin >= xMin && this.yMax <= yMax && this.yMin >= yMin);
	}

	private String printVehicleList(){
		String str = "{ ";
		for(Vehicle v : this.origin){
			str += v.toString() + " ; ";
		}
		str += "}";
		return str;
	}
	
	@Override
	public String toString(){
		String str = "ROOT : \t " + this.printVehicleList();
		if(leftChild != null)		str+= "LEFTCHILD: \t " + this.leftChild.printVehicleList();
		if(rightChild!= null) str+= "RIGHTCHILD: \t " + this.rightChild.printVehicleList();
		
		str+= "\n minY = " + this.yMin + " maxY = " + this.yMax;
		str+= "\n"
			+ "  �\n"
			+ " �\n";
				
		if(this.leftChild != null)		str+= this.leftChild.toString();
		
		str+= "\n"
				+ "      `\n"
				+ "       `\n";

		if(this.rightChild != null)		str+= rightChild.toString() + "\n\n";
		
		return str;
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