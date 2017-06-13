package pl.edu.agh.miss.genome.dna;

import net.sf.jfasta.FASTAElement;
import net.sf.jfasta.FASTAFileReader;
import net.sf.jfasta.impl.FASTAElementIterator;
import net.sf.jfasta.impl.FASTAFileReaderImpl;
import org.apache.commons.lang3.StringUtils;
import pl.edu.agh.miss.ParticleType;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class DNA {

    //region Privates
    private List<Gene> genes;
    //endregion

    //region Public methods
    public List<Gene> getGenes() {
        return genes;
    }

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
    //endregion
}
