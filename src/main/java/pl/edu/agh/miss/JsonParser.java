package pl.edu.agh.miss;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class JsonParser {

    private static final Logger log = LoggerFactory.getLogger(JsonParser.class);
    private static final ObjectMapper mapper = new ObjectMapper();

    public static <T> List<T> parse(InputStream inputStream, Class<T> valueType) {
        try {
            JavaType type = mapper.getTypeFactory().constructCollectionType(List.class, valueType);
            return mapper.readValue(inputStream, type);
        } catch (IOException e) {
            log.error("Unable to parse file.", e);
            return new ArrayList<>();
        }
    }

}
