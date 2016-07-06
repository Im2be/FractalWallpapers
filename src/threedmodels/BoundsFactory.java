package threedmodels;

import java.util.Random;

public class BoundsFactory {
	private static Random r = new Random();

	public static Bounds getGeneric(){
		return new Bounds(0, r.nextInt(30) + 1, r.nextInt(30) + 225, 255);
	}
	
}
