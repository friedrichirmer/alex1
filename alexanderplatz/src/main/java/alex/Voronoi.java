package alex;


import be.humphreys.simplevoronoi.Site;
import processing.core.PApplet;

public class Voronoi {
	
	private float upperLeftX;
	private float upperLeftY;
	private float lowerRightX;
	private float lowerRightY;
	
	
	
	public void corner1(double x, double y){
		upperLeftX = (float) x;
		upperLeftY = (float) y;
	}


	public void corner2(double x, double y) {
		lowerRightX = (float) x;
		lowerRightY = (float) y;

	}
	
	public double calcDensity() {
		
		Site site = new Site();
		
		return 0;
	}
	
	public void  draw(PApplet p) {
		if (lowerRightX != 0 && lowerRightY != 0) {
			float width;
			float height;
			
			float upperLeftCornerX;
			float upperLeftCornerY;
			
			double density = this.calcDensity();
			
			if (upperLeftX < lowerRightX) {
				upperLeftCornerX = upperLeftX;
				width = lowerRightX - upperLeftX;
			}
			else {
				upperLeftCornerX = lowerRightX;
				width = upperLeftX - lowerRightX;
			}
			
			if (upperLeftY < lowerRightY) {
				upperLeftCornerY = upperLeftY;
				height = lowerRightY - upperLeftY;
			}
			else {
				upperLeftCornerY = lowerRightY;
				height = upperLeftY - lowerRightY;
			}
			
			p.noFill();
			p.rect(upperLeftCornerX,upperLeftCornerY, width, height, 5);
			p.text("Voronoi-Dichte: " + density + " [Anzahl Personen / m^2]", 500, 500);
		}
		
			}


	public void reset() {
		upperLeftX = 0;
		upperLeftY = 0;
		lowerRightX = 0;
		lowerRightY = 0;
		
	}

}
