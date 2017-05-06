package pl.edu.agh.miss.genome.dna;

/**
 * Created on 2017-05-06.
 */
public class Gene {
    private String name;
    private String dnaSequence;
    private String products;
    private int start;
    private int stop;

    public Gene(String name, String dnaSequence, String products, int start, int stop) {
        this.name = name;
        this.dnaSequence = dnaSequence;
        this.products = products;
        this.start = start;
        this.stop = stop;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProducts() {
        return products;
    }

    public void setProducts(String products) {
        this.products = products;
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
                ", products=" + products +
                ", start=" + start +
                ", stop=" + stop +
                '}';
    }
}
