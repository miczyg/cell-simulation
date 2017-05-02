package pl.edu.agh.miss;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ResourcesPool {

    private final Map<ParticleType, List<Particle>> resources;

    public ResourcesPool(List<Particle> particles) {
        this.resources = particles.stream().collect(Collectors.groupingBy(Particle::getParticleType));
    }

    public void add(List<Particle> particles) {
        particles.forEach(this::add);
    }

    public void add(Particle particle) {
        resources.putIfAbsent(particle.getParticleType(), new ArrayList<>());
        resources.get(particle.getParticleType()).add(particle);
    }

    public void remove(List<Particle> particles) {
        particles.forEach(this::remove);
    }

    public void remove(Particle particle) {
        resources.get(particle.getParticleType()).remove(particle);
    }

    public boolean has(List<Particle> particles) {
        return particles.stream()
                .collect(Collectors.groupingBy(Particle::getParticleType, Collectors.counting()))
                .entrySet()
                .stream()
                .allMatch(entry -> resources.containsKey(entry.getKey()) &&
                        resources.get(entry.getKey()).size() >= entry.getValue());
    }

    @Override
    public String toString() {
        return resources.entrySet().stream()
                .map(entry -> String.format("%30s -> %d", entry.getKey(), entry.getValue().size()))
                .collect(Collectors.joining("\n"));

    }
}
