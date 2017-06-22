package networkUtils;

import alex.Vehicle;

/**
 * Created by laemmel on 27.04.17.
 */
public class Link {


    private final double length;
    private final Node from;
    private final Node to;
    private final double weight;


    private double finishLineX1;
    private double finishLineY1;
	private final int id;
	private int room;

    public Link(Node from, Node to, int id, int room) {
        this.from = from;
        this.to = to;
        this.id = id;
        this.room = room;

       // this.weight = Math.sqrt((from.getX()-to.getX())*(from.getX()-to.getX())+(from.getY()-to.getY())*(from.getY()-to.getY()));

        double dx = from.getX() - to.getX();
        
        double dy = from.getY() - to.getY();
        
        this.weight = Math.sqrt(dx * dx + dy * dy);
        this.length = Math.sqrt(dx * dx + dy * dy);

        this.finishLineX1 = -dy;
        this.finishLineY1 = dx;

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

    public boolean hasVehicleReachedEndOfLink(Vehicle vehicle) {

        double vx = vehicle.getX() - this.to.getX();
        double vy = vehicle.getY() - this.to.getY();

        double cross = this.finishLineX1 * vy - this.finishLineY1 * vx;
        if (cross > 0) {
            return true;
        }

        return false;
    }

    public double getLength() {
        return length;
    }

	public double getWeight() {
		return weight;
	}

	public int getRoom() {
		return room;
	}
}
