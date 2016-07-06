package threedmodels;

import java.util.HashSet;
import java.util.Set;

public class Bounds {
    
    private int hardLowerBound, hardUpperBound, softLowerBound, softUpperBound;
    
    public static final Bounds VARIABLE = new Bounds(0, 30, 225, 255);
    public static final Bounds FULL = new Bounds(225, 230, 250, 255);
    public static final Bounds EMPTY = new Bounds(0, 5, 25, 30);
    
    public Bounds(int hardLowerBound, int softLowerBound, int softUpperBound, int hardUpperBound){
        this.setHardLowerBound(hardLowerBound);
        this.setSoftLowerBound(softLowerBound);
        this.setSoftUpperBound(softUpperBound);
        this.setHardUpperBound(hardUpperBound);
    }
    
    public int getHardLowerBound() {
        return hardLowerBound;
    }
    
    public void setHardLowerBound(int hardLowerBound) {
        this.hardLowerBound = hardLowerBound;
    }
    
    public int getHardUpperBound() {
        return hardUpperBound;
    }
    
    public void setHardUpperBound(int hardUpperBound) {
        this.hardUpperBound = hardUpperBound;
    }
    
    public int getSoftLowerBound() {
        return softLowerBound;
    }
    
    public void setSoftLowerBound(int softLowerBound) {
        this.softLowerBound = softLowerBound;
    }
    
    public int getSoftUpperBound() {
        return softUpperBound;
    }
    
    public void setSoftUpperBound(int softUpperBound) {
        this.softUpperBound = softUpperBound;
    }
    
    public String toString(){
        return "Bounds: " + hardLowerBound + " " + softLowerBound + " " + softUpperBound + " " + hardUpperBound;
    }
    
}
