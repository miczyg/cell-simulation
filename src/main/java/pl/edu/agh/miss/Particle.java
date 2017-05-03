package pl.edu.agh.miss;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Particle {

    private final ParticleType particleType;
    private Location location;
    private double energy;

    public Particle(ParticleType particleType) {
        this.particleType = particleType;
    }

    @JsonCreator
    public Particle(@JsonProperty("name") String name) {
        this(ParticleType.valueOf(name));
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
