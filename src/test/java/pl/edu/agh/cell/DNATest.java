package pl.edu.agh.cell;

import org.junit.Test;
import pl.edu.agh.miss.genome.dna.DNA;
import pl.edu.agh.miss.genome.rna.RNA;
import pl.edu.agh.miss.genome.rna.mRNA;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class DNATest {
    @Test
    public void transcript() throws Exception {
        String testDNA = "CGTCCATTC";
        String testmRNA = "GCAGGUAAG";
        RNA testrna = new RNA();
        mRNA res = new mRNA();
        res.setSequence(testmRNA);
        DNA dna = new DNA(testDNA);
        dna.transcript(testDNA, testrna);
        assertEquals(res.getSequence(), testrna.getSequence());

    }

    @Test
    public void getGenes() throws Exception {
        DNA dna = new DNA("somerubbishATG_match1_TGArubbishoncemoreATG_match2_TAGmatch2ATG_m3_TAAlastrubbish");
        List<String> unwinded = dna.getGenes();
        List<String> testSlice = Arrays.asList("_MATCH1_", "_MATCH2_", "_M3_");
        assertEquals(testSlice, unwinded);
    }

}