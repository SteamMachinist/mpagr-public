package steammachinist.mpagrengine.startup;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import steammachinist.mpagrengine.entity.EngineProperties;
import steammachinist.mpagrengine.service.persistance.EnginePropertiesService;
import steammachinist.mpagrengine.service.scheduling.SchedulingService;

@Component
@RequiredArgsConstructor
public class StartupRunner implements ApplicationRunner {
    private final EnginePropertiesService enginePropertiesService;
    private final SchedulingService schedulingService;

    @Value("${scheduling.default-cron}")
    private String defaultCron;

    @Override
    public void run(ApplicationArguments args) {
        if (!enginePropertiesService.exists()) {
            enginePropertiesService.save(new EngineProperties(1L, defaultCron));
        }
        schedulingService.scheduleLaunch(enginePropertiesService.get().getLaunchCron());
    }
}
