package pl.edu.agh.miss;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.List;

public class App {

    private static final String FILE_REACTIONS = "reactions.json";
    private static final String FILE_PARTICLES = "particles.json";
    private static final int STEPS = 20;

    private static final Logger log = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        List<Reaction> reactions = loadEntries(FILE_REACTIONS, Reaction.class);
        List<Particle> particles = loadEntries(FILE_PARTICLES, Particle.class);
        log.info("Loaded Reactions: {}", reactions);
        log.info("Loaded Particles: {}", particles);

        ResourcesPool resourcesPool = new ResourcesPool(particles);
        log.info("Loaded Resources Pool: \n{}", resourcesPool);


        log.info("--- START SIMULATION ---");
        Cell cell = new Cell(resourcesPool, reactions);
        for (int i = 1; i <= STEPS; i++) {
            log.info("Simulation step #{}", i);
            cell.live();
        }
    }

    private static <T> List<T> loadEntries(String fileName, Class<T> valueType) {
        InputStream inputStream = App.class.getClassLoader().getResourceAsStream(fileName);
        return JsonParser.parse(inputStream, valueType);
    }

}
