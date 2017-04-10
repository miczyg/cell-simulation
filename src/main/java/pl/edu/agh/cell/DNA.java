package pl.edu.agh.cell;

import pl.edu.agh.cell.rna.RNA;
import pl.edu.agh.cell.rna.mRNA;
import pl.edu.agh.cell.rna.tRNA;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class DNA {
    //region Public methods
    /***
     *
     * @param sequence String with sequence of DNA
     * Aditionaly slicing given sequence to genes list
     */
    public DNA(String sequence){
        dna = sequence.toUpperCase();
        genes = new ArrayList<>();
        Pattern p = Pattern.compile("(ATG\\w+TGA)|(ATG\\w+TAG)|(ATG\\w+TAA)");
        Matcher m = p.matcher(dna);
        while (m.find()) {
            genes.add(m.group());
        }
        genes = genes.stream().map(e -> e.substring(3, e.length()-3)).collect(Collectors.toList());
    }

    /***
     *
     * @param dna Unzipped dna sequence
     * @param target targeting RNA particle
     */
    public void transcript(String dna, RNA target/*, Enzyme polymerase*/){
        /*switch (polymerase.pType) {
            case RNA_II:
                target = new mRNA();
                break;
            case RNA_III:
                target = new tRNA();
                break;
        }*/

        target.setSequence(
                Arrays.stream(dna.
                        split("(?!^)")).
                        map(dna2mrna::get).
                        collect(Collectors.joining()
                        )
        );
    }

    public List<String> getGenes() {
        return genes;
    }
    //endregion

    //region Privates
    private String dna;
    private List<String> genes;
    private static final Map<String, String> dna2mrna;
    static {
        dna2mrna = new HashMap<>();
        dna2mrna.put("G", "C");
        dna2mrna.put("C", "G");
        dna2mrna.put("T", "A");
        dna2mrna.put("A", "U");
    }

    private static final List<String> startCodons = Collections.singletonList("ATG");
    private static final List<String> stopCodons = Arrays.asList("TGA", "TAG", "TAA");
    //endregion



}
