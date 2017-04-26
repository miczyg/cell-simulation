package pl.edu.agh.miss.parser;

import org.junit.Test;
import pl.edu.agh.miss.ParticleType;
import pl.edu.agh.miss.Reaction;

import java.util.List;

import static org.junit.Assert.*;

public class ReactionsParserTest {


    @Test
    public void testParseFile() throws Exception {
        ReactionsParser reactionsParser = new ReactionsParser();
        List<Reaction> reactions = reactionsParser.parse("reactions.json");
        assertEquals(2, reactions.size());
    }

}