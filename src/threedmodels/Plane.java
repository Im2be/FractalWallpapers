/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package threedmodels;

/**
 *
 * @author Eigenaar
 * A plane with the equation "ax + by + cz = d"
 */
public class Plane {
    
    private final double a, b, c, d;
    
    public Plane(double a, double b, double c, double d){
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        if(d < 0)
            d = 0;
        else if(d > 255)
            d = 255;
    }

    /**
     * @return the a
     */
    public double getA() {
        return a;
    }

    /**
     * @return the b
     */
    public double getB() {
        return b;
    }

    /**
     * @return the c
     */
    public double getC() {
        return c;
    }

    /**
     * @return the d
     */
    public double getD() {
        return d;
    }
    
    
    
}
