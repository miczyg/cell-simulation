package pl.edu.agh.miss.genome.dna;

import net.sf.jfasta.FASTAElement;
import net.sf.jfasta.FASTAFileReader;
import net.sf.jfasta.impl.FASTAElementIterator;
import net.sf.jfasta.impl.FASTAFileReaderImpl;
import org.apache.commons.lang3.StringUtils;
import pl.edu.agh.miss.ParticleType;
import pl.edu.agh.miss.genome.rna.RNA;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class DNA {

    //region Public methods

    public DNA(File fastaFile, Map<String, String> particleNames) throws IOException {
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

            if(particleNames.containsKey(protein)) {
                String particleName = particleNames.get(protein);
                final ParticleType particleType = ParticleType.valueOf(particleName);
                final Gene gene = new Gene(name, elem.getSequence(), particleType, location[0], location[1]);
                genes.add(gene);
            } else {
                System.out.println("No particle name mapping for " + protein);
            }

        }
    }



    public List<Gene> getGenes() {
        return genes;
    }
    //endregion

    //region Privates
    private List<Gene> genes;


    private static final String GENOME_REGEX = "(ATG.*?(?:TGA|TAA|TAG))";
    /**
     * from here: https://www.ncbi.nlm.nih.gov/pmc/articles/PMC29761/
     * meets num of genes here: http://book.bionumbers.org/how-many-genes-are-in-a-genome/
     */
    private static final int MIN_GEN_LEN = 60; //from here: https://www.ncbi.nlm.nih.gov/pmc/articles/PMC29761/

    //endregion
}
