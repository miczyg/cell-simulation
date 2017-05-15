package pl.edu.agh.miss.genome.dna;

import pl.edu.agh.miss.ParticleType;

/**
 * Created on 2017-05-06.
 */
public class Gene {
    private String name;
    private String dnaSequence;
    private ParticleType particleType;
    private int start;
    private int stop;

    public Gene(String name, String dnaSequence, ParticleType products, int start, int stop) {
        this.name = name;
        this.dnaSequence = dnaSequence;
        this.particleType = products;
        this.start = start;
        this.stop = stop;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ParticleType getParticleType() {
        return particleType;
    }

    public void setParticleType(ParticleType particleType) {
        this.particleType = particleType;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getStop() {
        return stop;
    }

    public void setStop(int stop) {
        this.stop = stop;
    }

    @Override
    public String toString() {
        return "Gene{" +
                "name='" + name + '\'' +
                ", particleType=" + particleType +
                ", start=" + start +
                ", stop=" + stop +
                '}';
    }
}
