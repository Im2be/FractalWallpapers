package fractals;

import threedmodels.Bounds;
import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;
import threedmodels.Bound;
import threedmodels.DistanceProperties;
import threedmodels.RectangularBound;


public abstract class Fractal {
    
    private static final double ARCTIC_CIRCLE = 0.11118;
    private static final double TROPIC_OF_CANCER = 0.13055;
    private static final double TROPIC_OF_PISCES = 0.86951;
    private static final double ANTARCTIC_CIRCLE = 0.88888;

    Color currentColor;
    FractalImage fractalImage;
    Stack<Pixel> futurePixels = new Stack<>();
    
    Fractal thisFractal;
    
    Map<TriggeredFractal, Integer> triggeredFractals;
    
    int pixelsWritten = 0;
    
    boolean running = false;
    
    int maxWidth;
    int maxHeight;
    
    Set<Bound> bounds;
    double progressionSpeed;
    
    boolean insideBounds = true;
    
    Point currentPosition;
    
    Random r;
    
    private void _instanciate(){
        r = new Random();
        thisFractal = this;
        triggeredFractals = new HashMap<>();
        progressionSpeed = 0.5;
        bounds = new HashSet<>();
    }
    
    public Fractal(){
        _instanciate();
    }
    
    public Fractal(Color firstColor, double progressionSpeed){
        this(firstColor);
        this.progressionSpeed = progressionSpeed;
    }
    
    public Fractal(Color firstColor){
        _instanciate();
        currentColor = firstColor;
    }
    
    public void addBound(Bound b){
        bounds.add(b);
    }
    
    public void setImage(FractalImage image){
        fractalImage = image;
    }
    
    public void runOnBitmap(){
        if(fractalImage != null){
            BufferedImage image = fractalImage.getImage();
            if(bounds.isEmpty())
                bounds.add(new RectangularBound(0, 50, 205, 255));
            int i = r.nextInt(bounds.size());
            for(Bound b : bounds)
                if(i-- == 0){
                    currentColor = b.getRandomColor();
                    break;
                }
            
            if(currentPosition == null){
                currentPosition = new Point(r.nextInt(image.getWidth()), r.nextInt(image.getHeight()));
            }
            while(futurePixels.isEmpty()){
                Point p = new Point(0, 0);
                ConcurrentHashMap<Point, Boolean> points = fractalImage.getWrittenPoints();
                if(points.keySet().contains(p)){
                    currentPosition = new Point(r.nextInt(image.getWidth()), r.nextInt(image.getHeight()));
                }
                futurePixels.push(new Pixel(currentColor, currentPosition));
            }
            getConsumer().start();
        }
    }
    
    public int getPixelsWritten(){
        return pixelsWritten;
    }
    
    
    public void addTriggeredFractal(TriggeredFractal triggeredFractal, int i) {
        if(triggeredFractal instanceof TriggeredFractal)
            triggeredFractals.put(triggeredFractal, i);
    }
    
    

    protected Color calculateValue(Color prevCol, Set<Bound> bounds) {
        if(bounds.isEmpty()){
            throw new IllegalArgumentException("No bounds omitted!");
        }
        int changeR = prevCol.getRed(), changeG = prevCol.getGreen(), changeB = prevCol.getBlue();
        DistanceProperties lowestDp = null;
        for(Bound b : bounds){
            DistanceProperties properties = b.getDistanceProperties(prevCol);
            if(properties.getDistancePointToSoftBound() == 0){
                lowestDp = properties;
                break;
            }else if(lowestDp == null || properties.getDistancePointToSoftBound() / properties.getDistanceSoftToHardBound() < lowestDp.getDistancePointToSoftBound() / lowestDp.getDistanceSoftToHardBound())
                lowestDp = properties;
        }
        
        double distanceToSoftBound = lowestDp.getDistancePointToSoftBound();
        if(distanceToSoftBound <= 0){
            boolean rand = r.nextBoolean();
            if (rand)
                changeR += (int)Math.round(r.nextDouble() + progressionSpeed - 0.5);
            else
                changeR -= (int)Math.round(r.nextDouble() + progressionSpeed - 0.5);
            rand = r.nextBoolean();
            if (rand)
                changeG += (int)Math.round(r.nextDouble() + progressionSpeed - 0.5);
            else
                changeG -= (int)Math.round(r.nextDouble() + progressionSpeed - 0.5);
            rand = r.nextBoolean();
            if (rand)
                changeB += (int)Math.round(r.nextDouble() + progressionSpeed - 0.5);
            else
                changeB -= (int)Math.round(r.nextDouble() + progressionSpeed - 0.5);
            insideBounds = true;
        }else{
            //Red = X, Green = Y, Blue = Z
            //double probability = distanceToSoftBound / lowestDp.getDistanceSoftToHardBound();
            double x = lowestDp.getDirectionVector().getX();
            double y = lowestDp.getDirectionVector().getY();
            double z = lowestDp.getDirectionVector().getZ();
            double highestVal = Math.abs(x);
            if(highestVal < Math.abs(y))
                highestVal = Math.abs(y);
            if(highestVal < Math.abs(z))
                highestVal = Math.abs(z);
            x /= highestVal;
            y /= highestVal;
            z /= highestVal;

            
            if(r.nextDouble() < Math.abs(x / 2))
                changeR += Math.round(x);
            else
                changeR -= Math.round(r.nextDouble() * x / Math.abs(x));
            if(r.nextDouble() < Math.abs(y / 2))
                changeG += Math.round(y);
            else
                changeG -= Math.round(r.nextDouble() * y / Math.abs(y));
            if(r.nextDouble() < Math.abs(z / 2))
                changeB += Math.round(z);
            else
                changeB -= Math.round(r.nextDouble() * z / Math.abs(z));
            insideBounds = false;

            /*if(r.nextDouble() < Math.abs(x)){
                changeR += (x < 0 && r.nextDouble() < probability ? -1 : 1) * (int)Math.round(r.nextDouble() + progressionSpeed - 0.5);
            }
            if(r.nextDouble() < Math.abs(y)){
                changeG += (y < 0 && r.nextDouble() < probability ? -1 : 1) * (int)Math.round(r.nextDouble() + progressionSpeed - 0.5);
            }
            if(r.nextDouble() < Math.abs(z)){
                changeB += (z < 0 && r.nextDouble() < probability ? -1 : 1) * (int)Math.round(r.nextDouble() + progressionSpeed - 0.5);
            }*/
        }
        if(changeR > 255)
            changeR = 255;
        else if(changeR < 0)
            changeR = 0;
        if(changeG > 255)
            changeG = 255;
        else if(changeG < 0)
            changeG = 0;
        if(changeB > 255)
            changeB = 255;
        else if(changeB < 0)
            changeB = 0;
        
        return new Color(changeR, changeG, changeB);
    }
    
    /*private int calculateValue(Color currentColor, RGBValueType type, Bounds bounds) {
        int change = (int)Math.round(r.nextDouble() + progressionSpeed - 0.5);
        int value;
        switch(type){
            case RED:
                value = currentColor.getRed();
                break;
            case GREEN:
                value = currentColor.getGreen();
                break;
            default:
                value = currentColor.getBlue();
        }
        
        
        
        if(value >= bounds.getHardUpperBound()){
            value -= change;
        } else if(value >= bounds.getSoftUpperBound()){
            double rand = r.nextDouble();
            double compareVal = (bounds.getHardUpperBound() - value) / (bounds.getHardUpperBound() - bounds.getSoftUpperBound()) / 2;
            if(rand < compareVal)
                value += change;
            else
                value -= change;
        } else if(value >= bounds.getSoftLowerBound()){
            double rand = r.nextDouble();
            if (rand < 0.5)
                value += change;
            else
                value -= change;
        } else if(value > bounds.getHardLowerBound()){
            double rand = r.nextDouble();
            double compareVal = (value - bounds.getHardLowerBound()) / (bounds.getSoftLowerBound() - bounds.getHardLowerBound()) / 2;
            if(rand > compareVal)
                value += change;
            else
                value -= change;
            
        } else{
            value += change;
        }
        
        return value;
    }*/
    
    public abstract Thread getConsumer();
    
}   
