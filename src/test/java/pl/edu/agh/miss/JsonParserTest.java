package pl.edu.agh.miss;

import org.junit.Test;

import java.io.InputStream;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class JsonParserTest {

    @Test
    public void testParseFile() throws Exception {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("reactions.json");
        List<Reaction> reactions = JsonParser.parse(inputStream, Reaction.class);
        assertEquals(2, reactions.size());
    }

}