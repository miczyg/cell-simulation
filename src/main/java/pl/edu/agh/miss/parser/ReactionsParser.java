package pl.edu.agh.miss.parser;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.agh.miss.Reaction;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReactionsParser {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReactionsParser.class);

    private ObjectMapper mapper = new ObjectMapper();

    public List<Reaction> parse(String fileName) {

        try {
            File file = new File(getClass().getClassLoader().getResource(fileName).getFile());
            Reaction[] reactions = mapper.readValue(file, Reaction[].class);

            return Arrays.asList(reactions);
        } catch (IOException e) {
            LOGGER.error("Unable to parse file.", e);
            return new ArrayList<>();
        }
    }


}
