package steammachinist.mpagrengine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import steammachinist.mpagrengine.entity.EngineProperties;

public interface EnginePropertiesRepository extends JpaRepository<EngineProperties, Long> {
}
