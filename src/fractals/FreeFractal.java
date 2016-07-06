package fractals;

import threedmodels.Bounds;
import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;

public class FreeFractal extends Fractal {
    
    public FreeFractal(){
        super();
    }
    
    public FreeFractal(Color startColor) {
        super(startColor);
    }
    
    public FreeFractal(Color startColor, double progressionSpeed) {
        super(startColor, progressionSpeed);
    }
    
    @Override
    public Thread getConsumer() {
        return new Thread(new Runnable(){
            @Override
            public void run() {
                long startTime = System.currentTimeMillis();
                pixelsWritten = 0;
                
                LinkedBlockingQueue<Pixel> toBeWrittenPixels = fractalImage.getLinkedQueue();
                
                int newRedValue, newGreenValue, newBlueValue, n, pos;
                Color newCol, prevCol;
                Pixel p, newPix;
                Point currentPoint;
                Collection<Point> points = new ArrayList<>();
                while(futurePixels.size() > 0){
                    p = futurePixels.pop();
                    prevCol = p.getC();
                    currentPoint = p.getP();
                    
                    newCol = calculateValue(prevCol, bounds);
                    points.clear();
                    
                    addPixelToList(new Point(currentPoint.x, currentPoint.y - 1), points);
                    addPixelToList(new Point(currentPoint.x + 1, currentPoint.y), points);
                    addPixelToList(new Point(currentPoint.x, currentPoint.y + 1), points);
                    addPixelToList(new Point(currentPoint.x - 1, currentPoint.y), points);
                    
                    List<Point> pointsList = (List)points;
                    n = pointsList.size();
                    while (n > 1)
                    {
                        pos = r.nextInt(n);
                        Point point = pointsList.get(n - 1);
                        pointsList.set(n-1, pointsList.get(pos));
                        pointsList.set(pos, point);
                        n--;
                    }

                    for(pos = 0; pos < pointsList.size(); pos++){
                        currentPoint = pointsList.get(pos);
                        newPix = new Pixel(newCol, currentPoint);

                        futurePixels.push(newPix);
                        toBeWrittenPixels.add(newPix);
                        pixelsWritten++;
                        for(TriggeredFractal f : triggeredFractals.keySet()){
                            if(pixelsWritten % triggeredFractals.get(f) == 0){
                                f.runOnce();
                            }
                        }
                    }
                    
                }
                running = false;
                for(TriggeredFractal f : triggeredFractals.keySet()){
                    f.runAll();
                }
                fractalImage.removeRunningFractal(thisFractal);
                System.out.println("FreeFractal finished in " + (System.currentTimeMillis() - startTime) + "ms");
            }
            
            void addPixelToList(Point p, Collection<Point> points){
                BufferedImage i = fractalImage.getImage();
                if(p.x < 0)
                    p.x += i.getWidth();
                else if(p.x >= i.getWidth())
                    p.x -= i.getWidth();
                if(p.y < 0)
                    p.y += i.getHeight();
                else if(p.y >= i.getHeight())
                    p.y -= i.getHeight();
                Set<Point> writtenPixels = fractalImage.getWrittenPoints().keySet();
                if(!writtenPixels.contains(p)){
                    fractalImage.getWrittenPoints().put(p, true);
                    points.add(p);
                }
            }
        });
    }
}
