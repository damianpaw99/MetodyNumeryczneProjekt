package edu.ib;

public class VerletIntegrator {
    private ODEUpdate stepper;
    private Particle [] particles;

    public VerletIntegrator(Particle [] particles, ODEUpdate stepper) {
        this.stepper = stepper;
        this.particles=particles;
    }

    public void integrate(double tStart, double tStop, double dt){
        int n=(int)((tStop-tStart)/dt);

        double t=tStart;

        for(int i=0;i<n;i++){
            stepper.update(t,particles[0].getX(),particles[0].getY(),particles[1].getX(),particles[1].getY());

            for(int j=0;j< particles.length;j++){
                double [] aOld=particles[j].a(particles);

                //x
                double vxHalf=particles[j].getVx()+dt*aOld[0]/2;
                particles[j].addX(dt*vxHalf);

                double vyHalf=particles[j].getVy()+dt*aOld[1]/2;
                particles[j].addY(dt*vyHalf);

                double [] aNew=particles[j].a(particles);
                particles[j].addVx(dt*(aOld[0]+aNew[0])/2);
                particles[j].addVy(dt*(aOld[1]+aNew[1])/2);
                //aOld=aNew;

            }
            t+=dt;
        }
    }
}
