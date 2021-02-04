package edu.ib;

public class Particle {
    private double x;
    private double y;
    private double vx;
    private double vy;
    private double m;
    private ForceCalculator f;

    public Particle(double x, double y, double vx, double vy, double m, ForceCalculator f) {
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.m = m;
        this.f=f;
    }

    public double ax(Particle particle){
        double out=24*(2.0/Math.pow(x-particle.getX(),13)-1.0/Math.pow(x-particle.getX(), 7))/m;
        if(Double.isNaN(out)){
            return 0;
        }
        return out;
    }
    public double ay(Particle particle){
        double out=24*(2.0/Math.pow(y-particle.y,13)-1.0/Math.pow(y-particle.y, 7))/m;
        if(Double.isNaN(out)){
            return 0;
        }
        return out;
    }

    public double [] a(Particle [] particle){
        double[] out = {0, 0};
        for (Particle value : particle) {
            if (!value.equals(this)) {
                double r = distance(value);
                double force = f.force(r);
                double slope = (value.y - y) / (value.x - x);
                double arctg = Math.atan(slope);
                double slopexi=1;
                double slopeyi=1;
                if(value.x-x>0){
                    slopexi=-1;
                }
                if(value.y-y>0 || slope==Double.NEGATIVE_INFINITY){
                    slopeyi=-1;
                }
                if(x-value.x!=0) {
                    out[0] += force / m * Math.cos(arctg) * slopexi;
                }
                if(y-value.y!=0) {
                    out[1] += force / m * Math.sin(arctg) * slopeyi;
                }
            }
        }
        return out; //[0] - x [1] - y
    }


    public void addX(double x){
        this.x+=x;
    }
    public void addY(double y){
        this.y+=y;
    }

    public void addVx(double vx){
        this.vx+=vx;
    }
    public void addVy(double vy){
        this.vy+=vy;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getVx() {
        return vx;
    }

    public void setVx(double vx) {
        this.vx = vx;
    }

    public double getVy() {
        return vy;
    }

    public void setVy(double vy) {
        this.vy = vy;
    }

    public double getM() {
        return m;
    }

    public void setM(double m) {
        this.m = m;
    }
    private double distance(Particle particle){
        return Math.sqrt(Math.pow(x-particle.x,2)+Math.pow(y-particle.y,2));
    }
}
