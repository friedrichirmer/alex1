package alex;


import processing.core.PApplet;

public class Voronoi {
	
	private float x1;
	private float y1;
	private float x2;
	private float y2;
	
	
	
	public void corner1(double x, double y){
		x1 = (float) x;
		y1 = (float) y;
	}


	public void corner2(double x, double y) {
		x2 = (float) x;
		y2 = (float) y;

	}
	
	public double density() {
		
		return 0;
	}
	
	public void  draw(PApplet p) {
		if (x2 != 0 && y2 != 0) {
			float width;
			float height;
			
			float upperLeftCornerX;
			float upperLeftCornerY;
			
			double density = this.density();
			
			if (x1 < x2) {
				upperLeftCornerX = x1;
				width = x2 - x1;
			}
			else {
				upperLeftCornerX = x2;
				width = x1 - x2;
			}
			
			if (y1 < y2) {
				upperLeftCornerY = y1;
				height = y2 - y1;
			}
			else {
				upperLeftCornerY = y2;
				height = y1 - y2;
			}
			
			p.noFill();
			p.rect(upperLeftCornerX,upperLeftCornerY, width, height, 5);
			p.text("Voronoi-Dichte: " + density + " [Anzahl Personen / m^2]", 500, 500);
		}
		
			}


	public void reset() {
		x1 = 0;
		y1 = 0;
		x2 = 0;
		y2 = 0;
		
	}

}
