package pl.edu.agh.miss.genome.dna;

import net.sf.jfasta.FASTAElement;
import net.sf.jfasta.FASTAFileReader;
import net.sf.jfasta.impl.FASTAElementIterator;
import net.sf.jfasta.impl.FASTAFileReaderImpl;
import pl.edu.agh.miss.genome.rna.RNA;

import java.io.File;
import java.io.IOException;
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
    public DNA(String sequence) {
        dna = sequence.toUpperCase();
        genes = extractGenes(dna);
    }

    public DNA(File fastaFile) throws IOException {
        final FASTAFileReader reader = new FASTAFileReaderImpl(fastaFile);
        final FASTAElementIterator it = reader.getIterator();
        List<FASTAElement> outElements = new ArrayList<>();
        StringBuilder builder = new StringBuilder();
        while (it.hasNext()) {
            FASTAElement elem = it.next();
            builder.append(elem.getSequence());
        }
        dna = builder.toString().toUpperCase();

        genes = extractGenes(dna);
    }

    private List<String> extractGenes(String dnaSequence) {
        ArrayList<String> genesList = new ArrayList<>();
        Pattern p = Pattern.compile(GENOME_REGEX);
        Matcher m = p.matcher(dnaSequence);
        while (m.find()) {
            if (m.group().length() >  MIN_GEN_LEN + 6) {
                genesList.add(m.group());
            }
        }
        return genesList.stream().map(e -> e.substring(3, e.length() - 3)).collect(Collectors.toList());
    }

    /***
     *
     * @param dna Unzipped genome sequence
     * @param target targeting RNA particle
     */
    public void transcript(String dna, RNA target/*, Particle enzyme*/) {
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

    private static final String GENOME_REGEX = "(ATG.*?(?:TGA|TAA|TAG))";
    /**
     * from here: https://www.ncbi.nlm.nih.gov/pmc/articles/PMC29761/
     * meets num of genes here: http://book.bionumbers.org/how-many-genes-are-in-a-genome/
     */
    private static final int MIN_GEN_LEN = 60; //from here: https://www.ncbi.nlm.nih.gov/pmc/articles/PMC29761/

    //endregion

    public static void main(String[] args) throws IOException {
        File dnaFile = new File("data/escherichia_coli/GCF_000005845.2_ASM584v2_genomic.fna");
        DNA dna = new DNA(dnaFile);
        List<String> genom = dna.getGenes();
        String seq = dna.dna;
        System.out.println(genom.size());
        String accD = "1 ttatttggtg atattttttt caatatcatg cagcaaacgg tgcaacattg ccgtgtctcg\n" +
                "       61 ttgctctaaa agccccaggc gttgttgtaa ccagtcgacc agttttatgt catctgccac\n" +
                "      121 tgccagagtc gtcagcaatg tcatggctcg ttcgcgtaaa gcttgcagtt gatgttggtc\n" +
                "      181 tgccgttgca tcacttttcg ccggttgttg tattaatgtt gctaattgat agcaatagac\n" +
                "      241 catcaccgcc tgccccagat tgagcgaagg ataatccgcc accatcggca caccagtaag\n" +
                "      301 aacgtcagcc aacgctaact cttcgttagt caacccggaa tcttcgcgac caaacaccag\n" +
                "      361 cgcggcatgg ctcatccatg aagatttttc ctctaacagc ggcaccagtt caactggcgt\n" +
                "      421 ggcgtagtaa tgatatttcg cccgactgcg cgcagtggtg gcgacagtga aatcgacatc\n" +
                "      481 gtgtaacgat tcagccaatg tcgggaaaac tttaatatta tcaataatat caccagatcc\n" +
                "      541 atgtgcgacc cagcgggtgg ctggctccag gtgtgcctga ctatcgacaa tccgcagatc\n" +
                "      601 gctaaacccc atcgttttca ttgcccgcgc cgctgcccca atattttctg ctctggcggg\n" +
                "      661 tgcgaccaga ataatcgtta tacgcat";
        accD = accD.toUpperCase().replaceAll("\n", "").replaceAll(" ", "").replaceAll("\\d","");
        System.out.println(accD);
        if(seq.contains(seq)){
            System.out.println("CONTAINS");
        }
        for (String g: genom) {
            if(g.equals(accD)){
                System.out.println("MATCH");
            }
        }
    }


}
