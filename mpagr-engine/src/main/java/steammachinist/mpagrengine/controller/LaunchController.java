package steammachinist.mpagrengine.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import steammachinist.mpagrengine.service.aggregation.AggregationLauncherService;
import steammachinist.mpagrengine.service.persistance.EnginePropertiesService;
import steammachinist.mpagrengine.service.scheduling.CronUtils;
import steammachinist.mpagrengine.service.scheduling.SchedulingService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/launch")
public class LaunchController {
    private final AggregationLauncherService aggregationLauncherService;
    private final EnginePropertiesService enginePropertiesService;
    private final SchedulingService schedulingService;

    @GetMapping
    public String getLaunchPage(Model model) {
        model.addAttribute("time", CronUtils.fromCron(enginePropertiesService.get().getLaunchCron()));
        return "launch";
    }

    @PostMapping("/set-time")
    public String setTime(String timeChooser) {
        String cron = CronUtils.toCron(timeChooser);
        enginePropertiesService.save(cron);
        schedulingService.scheduleLaunch(cron);
        return "redirect:/launch";
    }

    @PostMapping("/now")
    public String launchNow() {
        aggregationLauncherService.run();
        return "redirect:/launch";
    }
}
