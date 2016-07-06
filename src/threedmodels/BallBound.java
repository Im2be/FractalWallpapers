/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package threedmodels;

import java.awt.Color;
import java.util.Random;

/**
 *
 * @author Eigenaar
 */
public class BallBound extends Bound {

    private final int x, y, z;
    private final double radiusSoft;
    private final double radiusHard;
    
    public BallBound(int x, int y, int z, double radiusSoft, double radiusHard){
        this.x = x;
        this.y = y;
        this.z = z;
        this.radiusSoft = radiusSoft;
        this.radiusHard = radiusHard;
    }
    
    @Override
    public DistanceProperties getDistanceProperties(int x, int y, int z) {
        int diffX = this.getX() - x;
        int diffY = this.getY() - y;
        int diffZ = this.getZ() - z;
        double distance = Math.sqrt(diffX*diffX + diffY*diffY + diffZ*diffZ);
        StringBuilder descBuilder = new StringBuilder("Ball (");
        descBuilder.append(this.x).append(",").append(this.y).append(",").append(this.z).append(")");
        descBuilder.append(": ");
        if(distance < getRadiusSoft()){
            descBuilder.append("inside");
            return new DistanceProperties(0, 0, null, descBuilder.toString());
        }else{
            descBuilder.append("outside");
            Vector v = new Vector(diffX, diffY, diffZ);
            return new DistanceProperties(distance-getRadiusSoft(), getRadiusHard()-getRadiusSoft(), v, descBuilder.toString());
        }
    }

    /**
     * @return the x
     */
    public int getX() {
        return x;
    }

    /**
     * @return the y
     */
    public int getY() {
        return y;
    }

    /**
     * @return the z
     */
    public int getZ() {
        return z;
    }

    /**
     * @return the radiusSoft
     */
    public double getRadiusSoft() {
        return radiusSoft;
    }

    /**
     * @return the radiusHard
     */
    public double getRadiusHard() {
        return radiusHard;
    }
    
    public String toString(){
        StringBuilder descBuilder = new StringBuilder("Ball (");
        descBuilder.append(this.x).append(",").append(this.y).append(",").append(this.z).append(")").append(": ").append(getAmountOfPixelsWritten()).append(" pixels");
        return descBuilder.toString();
    }

    @Override
    public Color getRandomColor() {
        Random rand = new Random();
        double randomDistance = rand.nextDouble() * radiusSoft;
        
        double z = rand.nextDouble() + rand.nextDouble() - 1;
        double theta = rand.nextDouble() * 2*Math.PI;
        double r = Math.sqrt(1 - (z*z));
        double x = r*Math.cos(theta);
        double y = r*Math.sin(theta);
        
        x = this.x + x*randomDistance;
        y = this.y + y*randomDistance;
        z = this.z + z*randomDistance;
        
        if(x < 0)
            x = 0;
        else if(x > 255)
            x = 255;
        if(y < 0)
            y = 0;
        else if(y > 255)
            y = 255;
        if(z < 0)
            z = 0;
        else if(z > 255)
            z = 255;
        
        
        return new Color((int)x, (int)y, (int)z);
    }

    
    
    
}
