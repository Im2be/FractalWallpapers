/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package threedmodels;

/**
 *
 * @author Eigenaar
 */
public class DistanceProperties {
    
    private final double distancePointSoftBound;
    private final double distanceSoftHardBound;
    
    private final Vector directionVector;
    private final String description;
    
    public DistanceProperties(double distancePointSoftBound, double distanceSoftHardBound, Vector directionVector, String description){
        this.directionVector = directionVector;
        this.distancePointSoftBound = distancePointSoftBound;
        this.distanceSoftHardBound = distanceSoftHardBound;
        this.description = description;
    } 
    
    public DistanceProperties(double distancePointSoftBound, double distanceSoftHardBound, Vector directionVector){
        this.directionVector = directionVector;
        this.distancePointSoftBound = distancePointSoftBound;
        this.distanceSoftHardBound = distanceSoftHardBound;
        this.description = "empty";
    }    
    
    public double getDistancePointToSoftBound(){
        return distancePointSoftBound;
    }
    
    public double getDistanceSoftToHardBound(){
        return distanceSoftHardBound;
    }
    
    public Vector getDirectionVector(){
        return directionVector;
    }

    public String getDescription() {
        return description;
    }
    
}
