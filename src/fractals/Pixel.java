package fractals;
import java.awt.Color;
import java.awt.Point;


public class Pixel {
	
	private Color c;
	private Point p;
	
	public Pixel(Color c, Point p){
		this.setC(c);
		this.setP(p);
	}

	public Color getC() {
		return c;
	}

	public void setC(Color c) {
		this.c = c;
	}

	public Point getP() {
		return p;
	}

	public void setP(Point p) {
		this.p = p;
	}
	
}
