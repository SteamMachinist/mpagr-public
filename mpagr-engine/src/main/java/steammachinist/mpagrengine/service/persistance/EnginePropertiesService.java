package steammachinist.mpagrengine.service.persistance;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import steammachinist.mpagrengine.entity.EngineProperties;
import steammachinist.mpagrengine.repository.EnginePropertiesRepository;

@Service
@RequiredArgsConstructor
public class EnginePropertiesService {
    private final EnginePropertiesRepository repository;

    @Value("${scheduling.default-cron}")
    private String defaultCron;

    public boolean exists() {
        return repository.existsById(1L);
    }

    public EngineProperties get() {
        return repository.findById(1L).orElseThrow(EntityNotFoundException::new);
    }

    public EngineProperties getDefault() {
        return new EngineProperties(2L, defaultCron);
    }

    public void save(EngineProperties engineProperties) {
        repository.save(engineProperties);
    }

    public void save(String newLaunchCron) {
        EngineProperties engineProperties = get();
        engineProperties.setLaunchCron(newLaunchCron);
        repository.save(engineProperties);
    }
}
