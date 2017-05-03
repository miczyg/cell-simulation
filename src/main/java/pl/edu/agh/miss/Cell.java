package pl.edu.agh.miss;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

public class Cell {

    private static final Logger log = LoggerFactory.getLogger(Cell.class);

    private final ResourcesPool resourcesPool;
    private final List<Reaction> reactions;

    public Cell(ResourcesPool resourcesPool, List<Reaction> reactions) {
        this.resourcesPool = resourcesPool;
        this.reactions = reactions;
    }

    public void live() {
        for (Reaction reaction : reactions) {
            react(reaction);
        }
    }

    private void react(Reaction reaction) {
        // TODO temporary instantiating new Particle from ParticleType
        List<Particle> substrates = reaction.getSubstrates().stream().map(Particle::new).collect(Collectors.toList());
        List<Particle> products = reaction.getProducts().stream().map(Particle::new).collect(Collectors.toList());

        if (resourcesPool.has(substrates)) {
            log.info("Reaction '{}'", reaction.getName());

            resourcesPool.remove(substrates);
            resourcesPool.add(products);

            log.info("ResourcesPool: \n{}", resourcesPool);
        } else {
            log.debug("Not enough resources to make reaction '%s'", reaction.getName());
        }
    }

}
