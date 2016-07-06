/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package threedmodels;

import java.awt.Color;

/**
 *
 * @author Eigenaar
 */
public abstract class Bound {
    
    
    private int insidePixels = 0;
    
    public DistanceProperties getDistanceProperties(Color c){
        return getDistanceProperties(c.getRed(), c.getGreen(), c.getBlue());
    }
    
    public void increasePix(){
        ++insidePixels;
    }
    
    public int getAmountOfPixelsWritten(){
        return insidePixels;
    }
    
    public abstract DistanceProperties getDistanceProperties(int x, int y, int z);

    public abstract Color getRandomColor();
    
}
