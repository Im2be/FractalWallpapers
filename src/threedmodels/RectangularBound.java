/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package threedmodels;

import java.awt.Color;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Eigenaar
 */
public class RectangularBound extends Bound {
    
    private double x0, x1, y0, y1, z0, z1;
    private Rectangle inner, outer;
    
    private double xSize, ySize, zSize; //Only used for getRandomColor()
    
    private Random r;
    
    public RectangularBound(int x, int y, int z, double size0, double size1){
        this(x,y,z,size0,size0,size0,size1,size1,size1);
    }

    public RectangularBound(int x, int y, int z, double x0Size, double y0Size, double z0Size, double x1Size, double y1Size, double z1Size){
        _this(x,y,z,x0Size,y0Size,z0Size,x,y,z,x1Size,y1Size,z1Size);
    }
    
    public RectangularBound(int x0, int y0, int z0, double x0Size, double y0Size, double z0Size, int x1, int y1, int z1, double x1Size, double y1Size, double z1Size){
        _this(x0, y0, z0, x0Size, y0Size, z0Size, x1, y1, z1, x1Size, y1Size, z1Size);
    }
    
    private void _this(int x0, int y0, int z0, double x0Size, double y0Size, double z0Size, int x1, int y1, int z1, double x1Size, double y1Size, double z1Size){
        Plane p0, p1, p2, p3, p4, p5;
        p0 = new Plane(1,0,0,x0 - Math.floor(x0Size / 2));
        p1 = new Plane(1,0,0, x0 + Math.floor(x0Size / 2));
        p2 = new Plane(0,1,0,y0 - Math.floor(y0Size / 2));
        p3 = new Plane(0,1,0, y0 + Math.floor(y0Size / 2));
        p4 = new Plane(0,0,1,z0 - Math.floor(z0Size / 2));
        p5 = new Plane(0,0,1, z0 + Math.floor(z0Size / 2));
        inner = new Rectangle(p0,p1,p2,p3,p4,p5);
        
        p0 = new Plane(1,0,0,x1 - Math.floor(x1Size / 2));
        p1 = new Plane(1,0,0, x1 + Math.floor(x1Size / 2));
        p2 = new Plane(0,1,0,y1 - Math.floor(y1Size / 2));
        p3 = new Plane(0,1,0, y1 + Math.floor(y1Size / 2));
        p4 = new Plane(0,0,1,z1 - Math.floor(z1Size / 2));
        p5 = new Plane(0,0,1, z1 + Math.floor(z1Size / 2));
        outer = new Rectangle(p0,p1,p2,p3,p4,p5);
        
        this.x0 = x0;
        this.y0 = y0;
        this.z0 = z0;
        this.x1 = x1;
        this.y1 = y1;
        this.z1 = z1;
        
        this.xSize = x0Size;
        this.ySize = y0Size;
        this.zSize = z0Size;
        r = new Random();
    }

    public RectangularBound(int hardLower, int softLower, int softUpper, int hardUpper) {
        int softCenter = Math.round((softUpper - softLower)/2) + softLower;
        int hardCenter = Math.round((hardUpper - hardLower)/2) + hardLower;
        int softSize = softUpper - softLower;
        int hardSize = hardUpper - hardLower;
        _this(softCenter, softCenter, softCenter, softSize, softSize, softSize, hardCenter, hardCenter, hardCenter, hardSize, hardSize, hardSize);
    }
    
    public RectangularBound(Bounds redBounds, Bounds greenBounds, Bounds blueBounds){
        this(redBounds.getHardLowerBound(), redBounds.getSoftLowerBound(), redBounds.getSoftUpperBound(), redBounds.getHardUpperBound(),
                greenBounds.getHardLowerBound(), greenBounds.getSoftLowerBound(), greenBounds.getSoftUpperBound(), greenBounds.getHardUpperBound(), 
                blueBounds.getHardLowerBound(), blueBounds.getSoftLowerBound(), blueBounds.getSoftUpperBound(), blueBounds.getHardUpperBound());
    }

    public RectangularBound(int hardLowerRed, int softLowerRed, int softUpperRed, int hardUpperRed, int hardLowerGreen, int softLowerGreen, int softUpperGreen, int hardUpperGreen, int hardLowerBlue, int softLowerBlue, int softUpperBlue, int hardUpperBlue) {
        int softCenterRed = Math.round((softUpperRed - softLowerRed)/2) + softLowerRed;
        int hardCenterRed = Math.round((hardUpperRed - hardLowerRed)/2) + hardLowerRed;
        int softSizeRed = softUpperRed - softLowerRed;
        int hardSizeRed = hardUpperRed - hardLowerRed;
        int softCenterGreen = Math.round((softUpperGreen - softLowerGreen)/2) + softLowerGreen;
        int hardCenterGreen = Math.round((hardUpperGreen - hardLowerGreen)/2) + hardLowerGreen;
        int softSizeGreen = softUpperGreen - softLowerGreen;
        int hardSizeGreen = hardUpperGreen - hardLowerGreen;
        int softCenterBlue = Math.round((softUpperBlue - softLowerBlue)/2) + softLowerBlue;
        int hardCenterBlue = Math.round((hardUpperBlue - hardLowerBlue)/2) + hardLowerBlue;
        int softSizeBlue = softUpperBlue - softLowerBlue;
        int hardSizeBlue = hardUpperBlue - hardLowerBlue;
        _this(softCenterRed, softCenterGreen, softCenterBlue, softSizeRed, softSizeGreen, softSizeBlue, hardCenterRed, hardCenterGreen, hardCenterBlue, hardSizeRed, hardSizeGreen, hardSizeBlue);
    }
    
    
    @Override
    public DistanceProperties getDistanceProperties(int x, int y, int z) {
        if(inner.containsPoint(x, y, z)){
            increasePix();
            return new DistanceProperties(0, 0, null);
        }else{
            List<RectanglePlaneType> s = inner.planesNotFacingPoint(x, y, z);
            Vector v = new Vector(x0 - x, y0 - y, z0 - z);
            double distancePointSoftBound = -1, distanceSoftHardBound = -1;
            Plane p;
            for(RectanglePlaneType type : s){
                p = inner.getPlane(type);
                double t = ((double) p.getD() - p.getA()*x - p.getB()*y - p.getC()*z ) / (p.getA()*(x0 - x) + p.getB()*(y0 - y) + p.getC()*(z0 - z));
                double deltaX = v.getX()*t;
                double deltaY = v.getY()*t;
                double deltaZ = v.getZ()*t;
                double sb = Math.sqrt(deltaX*deltaX + deltaY*deltaY + deltaZ*deltaZ);
                if(distancePointSoftBound < 0 || distanceSoftHardBound > sb)
                    distancePointSoftBound = sb;
                p = outer.getPlane(type);
                double t2 = ((double) p.getD() - p.getA()*x - p.getB()*y - p.getC()*z ) / (p.getA()*(x0 - x) + p.getB()*(y0 - y) + p.getC()*(z0 - z));
                deltaX = v.getX()*t - v.getX()*t2;
                deltaY = v.getY()*t - v.getY()*t2;
                deltaZ = v.getZ()*t - v.getZ()*t2;
                double hb = Math.sqrt(deltaX*deltaX + deltaY*deltaY + deltaZ*deltaZ);
                if(distanceSoftHardBound < 0 || distanceSoftHardBound > hb)
                    distanceSoftHardBound = hb;
            }
            return new DistanceProperties(distancePointSoftBound, distanceSoftHardBound, v);
        }
    }

    @Override
    public Color getRandomColor() {
        int x = (int)(x0 + xSize*(0.5 - r.nextDouble()));
        int y = (int)(y0 + ySize*(0.5 - r.nextDouble()));
        int z = (int)(z0 + zSize*(0.5 - r.nextDouble()));
        return new Color(x, y, z);
    }
    
    
}
