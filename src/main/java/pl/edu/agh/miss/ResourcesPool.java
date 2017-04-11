package pl.edu.agh.miss;

import java.util.List;
import java.util.Map;

public class ResourcesPool {
    private final Map<ParticleType, List<Particle>> resources;

    public ResourcesPool(Map<ParticleType, List<Particle>> resources) {
        this.resources = resources;
    }
}
