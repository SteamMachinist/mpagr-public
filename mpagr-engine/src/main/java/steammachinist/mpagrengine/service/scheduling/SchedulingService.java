package steammachinist.mpagrengine.service.scheduling;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;
import steammachinist.mpagrengine.service.aggregation.AggregationLauncherService;

import java.util.TimeZone;
import java.util.concurrent.ScheduledFuture;

@Service
@RequiredArgsConstructor
public class SchedulingService {
    private final TaskScheduler taskScheduler;
    private final AggregationLauncherService aggregationLauncherService;

    private ScheduledFuture<?> scheduledFuture;

    public void scheduleLaunch(String cron) {
        if (scheduledFuture != null) {
            scheduledFuture.cancel(true);
        }
        scheduledFuture = taskScheduler.schedule(aggregationLauncherService,
                new CronTrigger(cron, TimeZone.getTimeZone(TimeZone.getDefault().getID())));
    }
}
