package pl.edu.agh.miss;

public class Particle {

    private final ParticleType particleType;
    private Location location;
    private double energy;

    public Particle(ParticleType particleType) {
        this.particleType = particleType;
    }

    public ParticleType getParticleType() {
        return particleType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Particle particle = (Particle) o;

        return particleType == particle.particleType;
    }

    @Override
    public int hashCode() {
        return particleType != null ? particleType.hashCode() : 0;
    }

    @Override
    public String toString() {
        return particleType.toString();
    }
}
