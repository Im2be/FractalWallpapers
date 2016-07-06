package test;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

import threedmodels.Bounds;
import fractals.Fractal;
import fractals.FractalImage;
import fractals.FreeFractal;
import fractals.TriggeredFractal;
import fractals.interfaces.FractalResultInterface;
import threedmodels.Bound;
import threedmodels.RectangularBound;

public class RandomFractals {
	
	private static Random r = new Random();

	public static void main(String[] args){
		while(true)
			createRandomFractal();
	}
	
	public static void createRandomFractal(){

		FractalImage fi = new FractalImage();
		boolean regular = r.nextBoolean();
		
		Bounds red = getRandomBounds(regular), green = getRandomBounds(regular), blue = getRandomBounds(regular);
		System.out.println(red);
		System.out.println(green);
		System.out.println(blue);
		FreeFractal f = new FreeFractal(getRandomStartingColor(Bounds.VARIABLE, Bounds.VARIABLE, Bounds.VARIABLE), 0.5);
                f.addBound(new RectangularBound(Bounds.VARIABLE, Bounds.VARIABLE, Bounds.VARIABLE));
		fi.addFractal(f);
		fi.addProcessingListener(new FractalResultInterface(){

			@Override
			public void processImage(BufferedImage image) {
				File dir = new File(System.getProperty("user.home") + "/Desktop/RandomFractals/");
				if(dir.exists()){
					File f = new File(System.getProperty("user.home") + "/Desktop/RandomFractals/" + System.currentTimeMillis() + ".bmp");
					try {
						ImageIO.write(image, "bmp", f);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				System.out.println("Pixels written: " + fi.getPixelsWritten());
				for(Fractal f : fi.getAllFractals()){
					String fractalType = "Free";
					if(f instanceof TriggeredFractal)
						fractalType = "Triggered";
					System.out.println(fractalType + "Fractal wrote " + f.getPixelsWritten() + " pixels (" + (int)(Math.round((double)f.getPixelsWritten() / (double)fi.getPixelsWritten() * 100)) + "%)");
					
				}
				System.out.println("");
			}
			
		});
		fi.start();
		fi.join();
		
	}
	
	public static Color getRandomStartingColor(Bounds red, Bounds green, Bounds blue){
		int rC = r.nextInt(red.getSoftUpperBound() - red.getSoftLowerBound()) + red.getSoftLowerBound();
		int gC = r.nextInt(green.getSoftUpperBound() - green.getSoftLowerBound()) + green.getSoftLowerBound();
		int bC = r.nextInt(blue.getSoftUpperBound() - blue.getSoftLowerBound()) + blue.getSoftLowerBound();
		return new Color(rC, gC, bC);
	}
	
	public static Bounds getRandomBounds(boolean makeRegular){
		int hardLower, softLower, softHigher, hardHigher;
		
		if(r.nextBoolean()){
			hardLower = r.nextInt(253);
			hardHigher = r.nextInt(253 - hardLower) + hardLower + 3;
		}else{
			hardHigher = r.nextInt(253) + 3;
			hardLower = r.nextInt(hardHigher - 2);
		}
		if(r.nextBoolean()){
			softLower = r.nextInt(hardHigher - hardLower - 2) + hardLower + 1;
			softHigher = r.nextInt(hardHigher - softLower - 1) + softLower + 2;
		}else{
			softHigher = r.nextInt(hardHigher - hardLower - 2) + hardLower  + 2;
			softLower = r.nextInt(softHigher - hardLower - 1) + hardLower + 1;
		}
		return new Bounds(hardLower, softLower, softHigher, hardHigher);
	}
	
}
