package pl.edu.agh.miss;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.sbml.jsbml.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.agh.miss.genome.dna.DNA;

import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class App {
    private static final String FILE_FBA = "ncomms12219-s7.xml";
    private static final String FILE_PARTICLES_NAMES = "mapping.json";
    private static final int STEPS = 20;

    private static final Logger log = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) throws IOException, XMLStreamException, URISyntaxException {
        final Model model = loadModel();

        List<Particle> particles = model.getListOfSpecies().stream()
                .map(Species::getName).map(App::encodeSpeciesName)
                .map(ParticleType::valueOf).map(Particle::new)
                .collect(Collectors.toList());

        List<Reaction> reactions = model.getListOfReactions().stream()
                .map(r -> loadReaction(model, r)).collect(Collectors.toList());

        HashMap particlesNames = new ObjectMapper().readValue(FILE_PARTICLES_NAMES, HashMap.class);
        File geneFile = new File("data/escherichia_coli/full_genes_info.fasta");
        DNA dna = new DNA(geneFile);
        dna.getGenes().forEach(System.out::println);

        log.info("Loaded Reactions: {}", reactions);
        log.info("Loaded Particles: {}", particles);
        log.info("Loaded Particles names: {}", particlesNames);

        ResourcesPool resourcesPool = new ResourcesPool(particles);
        log.info("Loaded Resources Pool: \n{}", resourcesPool);

        log.info("--- START SIMULATION ---");
        Cell cell = new Cell(resourcesPool, reactions);
        for (int i = 1; i <= STEPS; i++) {
            log.info("Simulation step #{}", i);
            cell.live();
        }
    }

    private static Model loadModel() throws XMLStreamException, IOException {
        final String sbmlFile = ClassLoader.getSystemResource(FILE_FBA)
                .getFile().replace("%20", " ");

        final SBMLReader reader = new SBMLReader();
        final SBMLDocument document = reader.readSBML(sbmlFile);
        return document.getModel();
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
