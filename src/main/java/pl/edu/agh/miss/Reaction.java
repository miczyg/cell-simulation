package pl.edu.agh.miss;


import java.util.List;

public class Reaction {
    private final String name;

    private final List<ParticleType> substrates;
    private final List<ParticleType> products;

    public Reaction(String name, List<ParticleType> products, List<ParticleType> substrates) {
        this.name = name;
        this.products = products;
        this.substrates = substrates;
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
