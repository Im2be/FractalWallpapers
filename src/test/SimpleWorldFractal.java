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
import java.awt.Desktop;
import threedmodels.BallBound;
import threedmodels.PipeBound;
import threedmodels.RectangularBound;

public class SimpleWorldFractal {
    
    public static void main(String[] args) {
        FractalImage fi = new FractalImage(1920,1080);
        
        Fractal seaFractal = new FreeFractal();
        seaFractal.addBound(new RectangularBound(0, 1, 10, 20, 0, 1, 10, 20, 150, 175, 250, 255));
        /*PipeBound p = new PipeBound(20, 30);
        p.addPoint(5, 212, 5);
        p.addPoint(5, 159, 5);
        p.addPoint(178, 140, 5);
        p.addPoint(120, 82, 5);*/
        PipeBound p1 = new PipeBound(20, 30);
        p1.addPoint(5, 210, 5);
        p1.addPoint(50, 50, 50);
        p1.addPoint(150, 150, 150);
        p1.addPoint(250, 250, 250);
        Random r = new Random();
        int continentAmount = 10;
        for(int i = 0; i < continentAmount; i++){
            TriggeredFractal landFractal = new TriggeredFractal();
            //landFractal.addBound(new RectangularBound(0, 1, 10, 20, 150, 175, 250, 255, 0, 1, 10, 20));
            //landFractal.addBound(p);
            landFractal.addBound(p1);
            seaFractal.addTriggeredFractal(landFractal, r.nextInt(5) + 5);
            fi.addFractal(landFractal);
        }
        fi.addFractal(seaFractal);
        
        fi.addProcessingListener(new FractalResultInterface(){
            @Override
            public void processImage(BufferedImage image) {
                try {
                    File dir = new File(System.getProperty("user.home") + "/Desktop/Fractals/");
                    if(dir.exists()){
                        File f = new File(System.getProperty("user.home") + "/Desktop/Fractals/" + System.currentTimeMillis() + ".bmp");
                        ImageIO.write(image, "bmp", f);
                        Desktop.getDesktop().open(f);
                    }
                    
                    System.out.println("Pixels written: " + fi.getPixelsWritten());
                    int totalTriggeredPix = 0;
                    for(Fractal f : fi.getAllFractals()){
                        String fractalType = "Free";
                        if(f instanceof TriggeredFractal){
                            fractalType = "Triggered";
                            totalTriggeredPix += f.getPixelsWritten();
                        }
                        System.out.println(" - " + fractalType + "Fractal wrote " + f.getPixelsWritten() + " pixels (" + (int)(Math.round((double)f.getPixelsWritten() / (double)fi.getPixelsWritten() * 100)) + "%)");
                    }
                    System.out.println("total: " + totalTriggeredPix);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        fi.start();
        fi.join();
    }
}
