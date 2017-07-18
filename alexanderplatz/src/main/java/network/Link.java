package network;

import alex.Vehicle;

import java.util.List;

public class Link {

    private final double length;
    private final Node from;
    private final Node to;
    private double currentWeight;
    private double initialWeight;
    private double finishLineX1;
    private double finishLineY1;
	private final int id;
	private int room;

    public Link(Node from, Node to, int id, int room) {
        this.from = from;
        this.to = to;
        this.id = id;
        this.room = room;

       // this.currentWeight = Math.sqrt((from.getX()-to.getX())*(from.getX()-to.getX())+(from.getY()-to.getY())*(from.getY()-to.getY()));

        double dx = from.getX() - to.getX();
        double dy = from.getY() - to.getY();
        
        this.currentWeight = Math.sqrt(dx * dx + dy * dy);
        this.initialWeight = this.currentWeight;
        System.out.println("Initial currentWeight of the link " + this.id + " is " + this.currentWeight);
        this.length = Math.sqrt(dx * dx + dy * dy);

        this.finishLineX1 = -dy;
        this.finishLineY1 = dx;


    }

    public void calculateRecentLinkWeights(List<Vehicle> vehicleList, Double time) {
        int numberOfPassesThroughLinkInThePeriod = 0;
        Double sumOfTravelTimes = 0D;
        for (Vehicle vehicle : vehicleList) {
            if (vehicle.getRoute().contains(this) && !(vehicle.getMapOfEnterLeaveTimes().get(this.getId()) == null)){
                Double timeWhenVehicleEnteredThisLink = vehicle.getMapOfEnterLeaveTimes().get(this.getId())[0];
                Double timeWhenVehicleLeftThisLink = vehicle.getMapOfEnterLeaveTimes().get(this.getId())[1];
                if(timeWhenVehicleEnteredThisLink > time - 20 && timeWhenVehicleEnteredThisLink > 0 &&
                        !(timeWhenVehicleLeftThisLink == null)){
                    Double vehicleTravelTimeOnLink = timeWhenVehicleLeftThisLink - timeWhenVehicleEnteredThisLink;
                    sumOfTravelTimes += vehicleTravelTimeOnLink;
                    numberOfPassesThroughLinkInThePeriod++;
                }
            }
    }
        if (!(numberOfPassesThroughLinkInThePeriod == 0)){
            this.currentWeight = sumOfTravelTimes / numberOfPassesThroughLinkInThePeriod;
//            System.out.println("the new currentWeight of the Link " + this.getId() + " is " + currentWeight);
        }
    }

    public Node getTo() {
        return to;
    }
    
    public Node getFrom() {
    	return from;
    }
      
    public int getId() {
    	return id;
    }

    public boolean hasVehicleReachedEndOfLink(double vehX, double vehY) {

        double vx = vehX - this.to.getX();
        double vy = vehY - this.to.getY();

        double cross = this.finishLineX1 * vy - this.finishLineY1 * vx;
        if (cross > 0) {
            return true;
        }

        return false;
    }

    public double getLength() {
        return length;
    }

	public double getCurrentWeight() {
		return currentWeight;
	}

	public int getRoom() {
		return room;
	}

    public double getInitialWeight() {
        return initialWeight;
    }
}
