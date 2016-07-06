package fractals;
import fractals.interfaces.FractalResultInterface;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;


public class FractalImage {
    
    private BufferedImage mainImage;
    
    private ConcurrentHashMap<Point, Boolean> writtenPoints;
    
    private LinkedBlockingQueue<Pixel> toBeWrittenPixels;
    
    private List<Fractal> fractals, runningFractals;
    private List<FractalResultInterface> postProcessors;
    
    private int pixelsWritten;
    
    public FractalImage(){
        mainImage = new BufferedImage(1920, 1080, BufferedImage.TYPE_INT_RGB);
        writtenPoints = new ConcurrentHashMap<Point, Boolean>();
        toBeWrittenPixels = new LinkedBlockingQueue<Pixel>();
        
        fractals = new ArrayList<Fractal>();
        postProcessors = new ArrayList<FractalResultInterface>();
    }
    
    public FractalImage(int width, int height){
        mainImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        writtenPoints = new ConcurrentHashMap<Point, Boolean>();
        toBeWrittenPixels = new LinkedBlockingQueue<Pixel>();
        
        fractals = new ArrayList<Fractal>();
        postProcessors = new ArrayList<FractalResultInterface>();
    }
    
    public void addProcessingListener(FractalResultInterface result){
        postProcessors.add(result);
    }
    
    public void addFractal(Fractal f){
        f.setImage(this);
        fractals.add(f);
    }
    
    public void removeFractal(Fractal f){
        fractals.remove(f);
    }
    
    public void removeRunningFractal(Fractal f){
        runningFractals.remove(f);
    }
    
    public void start(){
        runningFractals = new ArrayList<Fractal>();
        if(fractals.size() == 0){
            addFractal(new FreeFractal());
        }
        for(Fractal f : fractals){
            runningFractals.add(f);
            f.runOnBitmap();
        }
        consumer.start();
    }
    
    public int getPixelsWritten(){
        return pixelsWritten;
    }
    
    Thread consumer = new Thread(new Runnable(){
        @Override
        public void run() {
            int total = mainImage.getWidth() * mainImage.getHeight();
            pixelsWritten = 0;
            while(toBeWrittenPixels.size() > 0 || runningFractals.size() > 0){
                Pixel p = toBeWrittenPixels.poll();
                if(p != null){
                    Point point = p.getP();
                    mainImage.setRGB(point.x, point.y, p.getC().getRGB());
                    pixelsWritten++;
                    if(pixelsWritten % (total / 20) == 0)
                        System.out.println(""+pixelsWritten + "\t/" + total + "(" + (int)((double)pixelsWritten / (double)total * 100) + "%)");
                }
            }
            for(FractalResultInterface r : postProcessors)
                r.processImage(mainImage);
        }
    });
    
    
    public BufferedImage getImage(){
        return mainImage;
    }
    
    public ConcurrentHashMap<Point, Boolean> getWrittenPoints() {
        return writtenPoints;
    }
    
    public void join() {
        try {
            consumer.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    public LinkedBlockingQueue<Pixel> getLinkedQueue() {
        return toBeWrittenPixels;
    }
    
    public List<Fractal> getAllFractals() {
        return fractals;
    }
}
