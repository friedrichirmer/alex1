package alex;

import java.util.ArrayList;
import java.util.List;

public class Test {
	
	public static void main (String[] args){
		
		List<Vehicle> list1 = new ArrayList<Vehicle>();
		List<Vehicle> list2 = new ArrayList<Vehicle>();
		list1.add(new Vehicle(0,0,1));
		list1.add(new Vehicle(0,0,2));
		list1.add(new Vehicle(0,0,3));
		list1.add(new Vehicle(0,0,4));
		list1.add(new Vehicle(0,0,5));
		
		System.out.println("Liste 1: \n " + schreibeListe(list1));
		System.out.println("Liste 2: \n " + schreibeListe(list2));
		
		list2.addAll(list1);
		
		System.out.println("Liste 1: \n " + schreibeListe(list1));
		System.out.println("Liste 2: \n " + schreibeListe(list2));
		
		list2.addAll(list1);
		
		System.out.println("Liste 1: \n " + schreibeListe(list1));
		System.out.println("Liste 2: \n " + schreibeListe(list2));
		
	}
	
	static String schreibeListe(List l){
		String str = "";
		for(int i = 0; i < l.size(); i++){
			str += "" + i + ": " + l.get(i).toString() + "\n";
		}
		return str;
	}
}
