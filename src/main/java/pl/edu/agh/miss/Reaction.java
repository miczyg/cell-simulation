package pl.edu.agh.miss;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.stream.Collectors;

public class Reaction {
    private final String name;

    private final List<ParticleType> substrates;
    private final List<ParticleType> products;

    public Reaction(String name, List<ParticleType> products, List<ParticleType> substrates) {
        this.name = name;
        this.products = products;
        this.substrates = substrates;
    }

    @JsonCreator
    public Reaction(@JsonProperty("substrates") List<String> substrates,
                    @JsonProperty("products") List<String> products,
                    @JsonProperty("name") String name) {
        this.name = name;
        this.substrates = substrates.stream().map(ParticleType::valueOf).collect(Collectors.toList());
        this.products = products.stream().map(ParticleType::valueOf).collect(Collectors.toList());
    }

    public String getName() {
        return name;
    }

    public List<ParticleType> getSubstrates() {
        return substrates;
    }

    public List<ParticleType> getProducts() {
        return products;
    }

    @Override
    public String toString() {
        return name;
    }
}
