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
public class Vector {
    private final double x;
    private final double y;
    private final double z;
    
    public Vector(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public double getX(){
        return x;
    }
    public double getY(){
        return y;
    }
    public double getZ(){
        return z;
    }
    
    public Vector add(Vector addend) {
        return new Vector(x + addend.x, y + addend.y, z + addend.z);
    }

    public double dotProduct(Vector v) {
        return x*v.x + y*v.y + z*v.z;
    }
    
    public Vector inverse(){
        return new Vector(-x, -y, -z);
    }
}
