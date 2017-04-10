package pl.edu.agh.miss;

import java.io.File;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import net.sf.jfasta.impl.*;
//import org.yeastrc.fasta.*;
import net.sf.jfasta.*;


public class Transcriptor {
    public String transcriptDNAtoMRNA(String DNA) {
        if(DNA == null || DNA.isEmpty()) return DNA;
        DNA = DNA.toUpperCase();
        return Arrays.stream(DNA.split("(?!^)")).map(dna2mrna::get).collect(Collectors.joining());
    }

    public void transcriptSequence(String sequence){
        List<String> dnaMatrices = spliceDNA(sequence);

        for(String mat : dnaMatrices){
            System.out.println(mat);
        }
    }

    /***
     *
     * @param DNA String DNA sequence
     * @return Slices of DNA read for transcription
     */
    public List<String> spliceDNA(String DNA) {
        List<String> allMatches = new ArrayList<>();

        Pattern p = Pattern.compile("(ATG\\w+TGA)|(ATG\\w+TAG)|(ATG\\w+TAA)");
        Matcher m = p.matcher(DNA);
        while (m.find()) {
            allMatches.add(m.group());
        }

        return allMatches.stream().map(e -> e.substring(3, e.length()-3)).collect(Collectors.toList());
    }

    private static final Map<String, String> dna2mrna;
    static
    {
        dna2mrna = new HashMap<>();
        dna2mrna.put("G", "C");
        dna2mrna.put("C", "G");
        dna2mrna.put("T", "A");
        dna2mrna.put("A", "U");
    }

    private static final List<String> startCodons = Collections.singletonList("ATG");
    private static final List<String> stopCodons = Arrays.asList("TGA", "TAG", "TAA");
    public static void main(String [] args) throws Exception {
        String filename = args[0];
        Transcriptor transcriptor = new Transcriptor();

        final File fastaFile  = new File(filename);
        final File mRNAFile = new File(filename + "out.fa");
        final FASTAFileReader reader = new FASTAFileReaderImpl(fastaFile);
        final FASTAElementIterator it = reader.getIterator();
        List<FASTAElement> outElements = new ArrayList<>();
        while (it.hasNext()) {
            FASTAElement elem = it.next();
            String dna =  elem.getSequence();
            String header = elem.getHeader();
            header = header.replaceAll("dna", "mRNA");
            String mRNA = transcriptor.transcriptDNAtoMRNA(dna);
            outElements.add(new FASTAElementImpl(header, mRNA));
        }

        new FASTAFileWriter(mRNAFile).write(new FASTAFileImpl(outElements));
    }
}
