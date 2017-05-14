package pl.edu.agh.miss.genome.dna;

import net.sf.jfasta.FASTAElement;
import net.sf.jfasta.FASTAFileReader;
import net.sf.jfasta.impl.FASTAElementIterator;
import net.sf.jfasta.impl.FASTAFileReaderImpl;
import org.apache.commons.lang3.StringUtils;
import pl.edu.agh.miss.genome.rna.RNA;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class DNA {

    //region Public methods

    public DNA(File fastaFile) throws IOException {
        final FASTAFileReader reader = new FASTAFileReaderImpl(fastaFile);
        final FASTAElementIterator it = reader.getIterator();
        List<FASTAElement> outElements = new ArrayList<>();
        genes = new LinkedList<>();
        while (it.hasNext()) {
            FASTAElement elem = it.next();
            String header = elem.getHeader();
            String name = StringUtils.substringBetween(header, "gene=", "]");
            if (name.equals("prfB")) continue; //suppress strange gene location exception -> TODO: sth else then continue :D
            String protein = StringUtils.substringBetween(header, "protein=", "]");
            int[] location;
            try {
                location = Arrays.stream(StringUtils.substringBetween(header, "location=", "]")
                        .split("\\.\\."))
                        .mapToInt(Integer::parseInt)
                        .toArray();
            }catch (Exception e){
                location = Arrays.stream(StringUtils.substringBetween(header, "location=complement(", ")")
                        .split("\\.\\."))
                        .mapToInt(Integer::parseInt)
                        .toArray();
            }
            genes.add(new Gene(name, elem.getSequence(), protein, location[0], location[1]));
        }
    }

    /***
     *
     * @param dna Unzipped genome sequence
     * @param target targeting RNA particle
     */
    public void transcript(String dna, RNA target) {
        target.setSequence(
                Arrays.stream(dna.
                        split("(?!^)")).
                        map(dna2mrna::get).
                        collect(Collectors.joining()
                        )
        );
    }

    public List<Gene> getGenes() {
        return genes;
    }
    //endregion

    //region Privates
    private List<Gene> genes;
    private static final Map<String, String> dna2mrna;

    static {
        dna2mrna = new HashMap<>();
        dna2mrna.put("G", "C");
        dna2mrna.put("C", "G");
        dna2mrna.put("T", "A");
        dna2mrna.put("A", "U");
    }

    private static final String GENOME_REGEX = "(ATG.*?(?:TGA|TAA|TAG))";
    /**
     * from here: https://www.ncbi.nlm.nih.gov/pmc/articles/PMC29761/
     * meets num of genes here: http://book.bionumbers.org/how-many-genes-are-in-a-genome/
     */
    private static final int MIN_GEN_LEN = 60; //from here: https://www.ncbi.nlm.nih.gov/pmc/articles/PMC29761/

    //endregion

    public static void main(String[] args) throws IOException {
        File geneFile = new File("data/escherichia_coli/full_genes_info.fasta");
        DNA dna = new DNA(geneFile);
        dna.getGenes().forEach(g -> System.out.println(g.getProducts()));
        System.out.println(dna.genes.size());

    }


}
