/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package threedmodels;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Eigenaar
 */
public class PipeBound extends Bound {

    private final List<BallBound> balls;
    private final double radiusSoft, radiusHard;
    
    private final Random r;
    
    public PipeBound(double radiusSoft, double radiusHard){
        balls = new ArrayList<>();
        this.radiusSoft = radiusSoft;
        this.radiusHard = radiusHard;
        this.r = new Random();
    }
    
    public void addPoint(int x, int y, int z){
        balls.add(new BallBound(x,y,z,radiusSoft,radiusHard));
    }
    
    @Override
    public DistanceProperties getDistanceProperties(int x, int y, int z) {
        if(balls.size() > 0){
            BallBound lowestPercentageBall = balls.get(0);
            DistanceProperties lowestDistanceProps = lowestPercentageBall.getDistanceProperties(x, y, z);
            if(lowestDistanceProps.getDistancePointToSoftBound() == 0 || balls.size() == 1){
                lowestPercentageBall.increasePix();
                return lowestDistanceProps;
            }else{
                BallBound secondPercentageBall = balls.get(1);
                DistanceProperties secondDistanceProps = secondPercentageBall.getDistanceProperties(x, y, z);
                if(secondDistanceProps.getDistancePointToSoftBound() == 0)
                    return secondDistanceProps;
                if(secondDistanceProps.getDistancePointToSoftBound() / secondDistanceProps.getDistanceSoftToHardBound() < lowestDistanceProps.getDistancePointToSoftBound()/lowestDistanceProps.getDistanceSoftToHardBound()){
                    BallBound tempB = lowestPercentageBall;
                    DistanceProperties tempDP = lowestDistanceProps;
                    lowestPercentageBall = secondPercentageBall;
                    lowestDistanceProps = secondDistanceProps;
                    secondPercentageBall = tempB;
                    secondDistanceProps = tempDP;
                }
                for(int i = 2; i < balls.size(); ++i){
                    BallBound b = balls.get(i);
                    DistanceProperties dp = b.getDistanceProperties(x, y, z);
                    if(dp.getDistancePointToSoftBound() == 0){
                        secondPercentageBall.increasePix();
                        return dp;
                    }
                    if(dp.getDistancePointToSoftBound() / dp.getDistanceSoftToHardBound() < lowestDistanceProps.getDistancePointToSoftBound() / lowestDistanceProps.getDistanceSoftToHardBound()){
                        secondDistanceProps = lowestDistanceProps;
                        secondPercentageBall = lowestPercentageBall;
                        lowestDistanceProps = dp;
                        lowestPercentageBall = b;
                    }else if(dp.getDistancePointToSoftBound() / dp.getDistanceSoftToHardBound() < secondDistanceProps.getDistancePointToSoftBound() / secondDistanceProps.getDistanceSoftToHardBound()){
                        secondDistanceProps = dp;
                        secondPercentageBall = b;
                    }
                }
                //Calculate distance between point and line (between 2 cores) & check if the intersection-point with the line is between the two cores
                //Zit het tussen de twee "cores"?
                Vector ab = new Vector(secondPercentageBall.getX() - lowestPercentageBall.getX(), secondPercentageBall.getY() - lowestPercentageBall.getY(), secondPercentageBall.getZ() - lowestPercentageBall.getZ());
                Vector bc = new Vector(x - secondPercentageBall.getX(), y - secondPercentageBall.getY(), z - secondPercentageBall.getZ());
                if (ab.inverse().dotProduct(bc) >= 0){
                    Vector ac = new Vector(x - lowestPercentageBall.getX(), y - lowestPercentageBall.getY(), z - lowestPercentageBall.getZ());
                    if(ac.dotProduct(ab) >= 0){
                    
                        //Afstand berekenen
                        int xDiff = lowestPercentageBall.getX() - x;
                        int yDiff = lowestPercentageBall.getY() - y;
                        int zDiff = lowestPercentageBall.getZ() - z;

                        double t = -((double)xDiff*ab.getX() + yDiff*ab.getY() + zDiff*ab.getZ()) / (ab.getX() * ab.getX() + ab.getY()*ab.getY() + ab.getZ()*ab.getZ());
                        double pXDiff = lowestPercentageBall.getX() + t * ab.getX() - x;
                        double pYDiff = lowestPercentageBall.getY() + t * ab.getY() - y;
                        double pZDiff = lowestPercentageBall.getZ() + t * ab.getZ() - z;

                        double distance = Math.sqrt(pXDiff*pXDiff + pYDiff*pYDiff + pZDiff*pZDiff);
                        StringBuilder b = new StringBuilder();
                        if(distance <= radiusSoft){
                            b.append("Inside cylinder [").append(lowestDistanceProps.getDescription()).append("],[").append(secondDistanceProps.getDescription()).append("]");
                            return new DistanceProperties(0,0,null, b.toString());
                        }else{ 
                            b.append("outside cylinder [").append(lowestDistanceProps.getDescription()).append("],[").append(secondDistanceProps.getDescription()).append("]");
                            DistanceProperties dp = new DistanceProperties(distance - radiusSoft, radiusHard - radiusSoft, new Vector(pXDiff + x, pYDiff + y, pZDiff + z), b.toString());
                            if(dp.getDistancePointToSoftBound() / dp.getDistanceSoftToHardBound() < lowestDistanceProps.getDistancePointToSoftBound() / lowestDistanceProps.getDistanceSoftToHardBound())
                               return dp;
                        }
                    }
                }
                lowestPercentageBall.increasePix();
                return lowestDistanceProps;
            }
        }else{
            throw new IllegalArgumentException("No balls added to the PipeBound");
        }
    }
    
    public List<BallBound> getBalls(){
        return balls;
    }

    @Override
    public Color getRandomColor() {
        return balls.get(r.nextInt(balls.size())).getRandomColor();
    }

    
}
