package pl.edu.agh.miss;

import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertTrue;

public class ResourcesPoolTest {

    private ResourcesPool resourcesPool;

    @Before
    public void setUp() throws Exception {
        List<Particle> particles = createParticles(
                ParticleType.Aconitase,
                ParticleType.Aconitase,
                ParticleType.Aconitase,
                ParticleType.Ubiquinol,
                ParticleType.H2O,
                ParticleType.Oxalosuccinate
        );

        resourcesPool = new ResourcesPool(particles);
    }

    @Test
    public void add() throws Exception {
        List<Particle> toAdd = createParticles(
                ParticleType.Aconitase,
                ParticleType.Aconitase,
                ParticleType.H2O,
                ParticleType.AcetylCoA
        );

        resourcesPool.add(toAdd);

        List<Particle> shouldHave = createParticles(
                ParticleType.Aconitase,
                ParticleType.Aconitase,
                ParticleType.Aconitase,
                ParticleType.Aconitase,
                ParticleType.Aconitase,
                ParticleType.H2O,
                ParticleType.H2O,
                ParticleType.AcetylCoA,
                ParticleType.Oxalosuccinate
        );

        assertTrue(resourcesPool.has(shouldHave));
    }

    @Test
    public void remove() throws Exception {
        List<Particle> toRemove = createParticles(
                ParticleType.Aconitase,
                ParticleType.Aconitase,
                ParticleType.H2O
        );

        resourcesPool.add(toRemove);

        List<Particle> shouldHave = createParticles(
                ParticleType.Aconitase,
                ParticleType.Ubiquinol,
                ParticleType.Oxalosuccinate
        );

        assertTrue(resourcesPool.has(shouldHave));
    }

    private List<Particle> createParticles(ParticleType... particles) {
        return Stream.of(particles).map(Particle::new).collect(Collectors.toList());
    }
}