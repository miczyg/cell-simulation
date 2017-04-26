package pl.edu.agh.miss;

public class Particle {

    private final ParticleType particleType;
    private Location location;
    private double energy;

    public Particle(ParticleType particleType) {
        this.particleType = particleType;
    }

}
