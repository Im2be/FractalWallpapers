/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package threedmodels;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Eigenaar
 */
public class Rectangle {
    
    private final Plane xLow, xHigh, yLow, yHigh, zLow, zHigh;

    public Rectangle(Plane xLow, Plane xHigh, Plane yLow, Plane yHigh, Plane zLow, Plane zHigh){
        this.xLow = xLow;
        this.xHigh = xHigh;
        this.yLow = yLow;
        this.yHigh = yHigh;
        this.zLow = zLow;
        this.zHigh = zHigh;
    }
    
    
    /**
     * @return the xLow
     */
    public Plane getxLow() {
        return xLow;
    }

    /**
     * @return the xHigh
     */
    public Plane getxHigh() {
        return xHigh;
    }

    /**
     * @return the yLow
     */
    public Plane getyLow() {
        return yLow;
    }

    /**
     * @return the yHigh
     */
    public Plane getyHigh() {
        return yHigh;
    }

    /**
     * @return the zLow
     */
    public Plane getzLow() {
        return zLow;
    }

    /**
     * @return the zHigh
     */
    public Plane getzHigh() {
        return zHigh;
    }
    
    public boolean containsPoint(int x, int y, int z){
        if(x >= xLow.getD() && x <= xHigh.getD() && y >= yLow.getD() && y <= yHigh.getD() && z >= yLow.getD() && z <= zHigh.getD())
            return true;
        return false;
    }
    
    public Plane getPlane(RectanglePlaneType type){
        switch(type){
            case XLOW:
                return xLow;
            case XHIGH:
                return xHigh;
            case YLOW:
                return yLow;
            case YHIGH:
                return yHigh;
            case ZLOW:
                return zLow;
            case ZHIGH:
                return zHigh;
            default:
                return null;
        }
    }
   
    public List<RectanglePlaneType> planesNotFacingPoint(int x, int y, int z){
        List<RectanglePlaneType> s = new ArrayList<>();
        if(x < xLow.getD())
            s.add(RectanglePlaneType.XLOW);
        else if(x > xHigh.getD())
            s.add(RectanglePlaneType.XHIGH);
        if(y < yLow.getD())
            s.add(RectanglePlaneType.YLOW);
        else if(y > yHigh.getD())
            s.add(RectanglePlaneType.YHIGH);
        if(z < zLow.getD())
            s.add(RectanglePlaneType.ZLOW);
        else if(z > zHigh.getD())
            s.add(RectanglePlaneType.ZHIGH);
        return s;
    }
    
}
