package pl.edu.agh.miss;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.sbml.jsbml.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.agh.miss.genome.dna.DNA;
import pl.edu.agh.miss.genome.dna.Gene;

import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

public class App {
    private static final String FILE_FBA = "ncomms12219-s7.xml";
    private static final String FILE_PARTICLES_NAMES = "particle_names_mapping.json";
    private static final String FILE_GENES = "data/escherichia_coli/full_genes_info.fasta";
    private static final int SUB_SIMULATIONS_NUMBER = 4;
    private static final int SUB_SIMULATIONS_STEPS = 5;
    private static final int SYNCHRONIZATIONS_NUMBER = 10;

    private static final Logger log = LoggerFactory.getLogger(App.class);
    private static final Random random = new Random(123);

    public static void main(String[] args) throws IOException, XMLStreamException, URISyntaxException {
        final Model model = loadModel();

        List<Particle> particles = model.getListOfSpecies().stream()
                .map(Species::getName).map(App::encodeSpeciesName)
                .map(ParticleType::valueOf).map(Particle::new)
                .collect(Collectors.toList());

        List<Reaction> reactions = model.getListOfReactions().stream()
                .map(r -> loadReaction(model, r)).collect(Collectors.toList());

        File fileParticlesNames = new File(getPath(FILE_PARTICLES_NAMES));
        Map<String, String> particleNames = new ObjectMapper().readValue(fileParticlesNames, Map.class);
        File geneFile = new File(FILE_GENES);
        DNA dna = new DNA(geneFile, particleNames);
        List<ParticleType> dnaParticles = dna.getGenes().stream()
                .map(Gene::getParticleType)
                .collect(Collectors.toList());

        log.info("Loaded Reactions: {}", reactions);
        log.info("Loaded Particles: {}", particles);
        log.info("Loaded Particles names: {}", particleNames);

        ResourcesPool resourcesPool = new ResourcesPool(particles);
        log.info("Loaded Resources Pool: \n{}", resourcesPool);

        log.info("--- START SIMULATION ---");
        final long startTime = System.currentTimeMillis();
        for (int i = 0; i < SYNCHRONIZATIONS_NUMBER; i++) {
            log.info("Synchronization #{}", i);
            List<ResourcesPool> resourcesPools = resourcesPool.split(random, SUB_SIMULATIONS_NUMBER);
            resourcesPools.stream().parallel().forEach(r -> simulate(r, reactions, dnaParticles));
            resourcesPool = ResourcesPool.join(resourcesPools);
        }
        final long endTime = System.currentTimeMillis();
        final long deltaTime = endTime - startTime;
        log.info("Simulation took #{}ms", deltaTime);
    }

    private static ResourcesPool simulate(ResourcesPool resourcesPool, List<Reaction> reactions,
                                          List<ParticleType> dnaParticles) {

        Cell cell = new Cell(resourcesPool, reactions);
        for (int i = 1; i <= SUB_SIMULATIONS_STEPS; i++) {
            log.info("Parallel simulation step #{}", i);
            cell.live();

            int particlesToAdd = random.nextInt(dnaParticles.size() / 10);
            random.ints(particlesToAdd, 0, dnaParticles.size())
                    .mapToObj(dnaParticles::get)
                    .map(Particle::new)
                    .forEach(resourcesPool::add);
        }

        return resourcesPool;
    }

    private static Model loadModel() throws XMLStreamException, IOException {
        final String sbmlFile = getPath(FILE_FBA);
        final SBMLReader reader = new SBMLReader();
        final SBMLDocument document = reader.readSBML(sbmlFile);
        return document.getModel();
    }

    private static String getPath(String fileName) {
        return ClassLoader.getSystemResource(fileName)
                .getFile().replace("%20", " ");
    }

    private static Reaction loadReaction(Model model, org.sbml.jsbml.Reaction reaction) {
        final String name = reaction.getName();

        final List<ParticleType> products = reaction
                .getListOfProducts().stream()
                .map(SpeciesReference::getSpecies)
                .map(model::getSpecies)
                .map(Species::getName)
                .map(App::encodeSpeciesName)
                .map(ParticleType::valueOf)
                .collect(Collectors.toList());

        final List<ParticleType> substrates = reaction
                .getListOfReactants().stream()
                .map(SpeciesReference::getSpecies)
                .map(model::getSpecies)
                .map(Species::getName)
                .map(App::encodeSpeciesName)
                .map(ParticleType::valueOf)
                .collect(Collectors.toList());

        return new Reaction(name, products, substrates);
    }

    private static String encodeSpeciesName(String rawSpeciesName) {
        return "_" + rawSpeciesName.replaceAll("[-()\\[\\], ]", "_");
    }
}
