package pl.edu.agh.miss.genome.dna;

import pl.edu.agh.miss.ParticleType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created on 2017-05-06.
 */
public class Gene {
    private String name;
    private String dnaSequence;
    private String rnaSequence;
    private ParticleType particleType;
    private int start;
    private int stop;

    public Gene(String name, String dnaSequence, ParticleType products, int start, int stop) {
        this.name = name;
        this.dnaSequence = dnaSequence;
        this.rnaSequence = transcript(this.dnaSequence);
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

    /***
     *
     * @param dna Unzipped genome sequence
     */
    private String transcript(String dna) {
        return  (
                Arrays.stream(dna.
                        split("(?!^)")).
                        map(dna2mrna::get).
                        collect(Collectors.joining()
                        )
        );
    }

    private static final Map<String, String> dna2mrna;

    static {
        dna2mrna = new HashMap<>();
        dna2mrna.put("G", "C");
        dna2mrna.put("C", "G");
        dna2mrna.put("T", "A");
        dna2mrna.put("A", "U");
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
