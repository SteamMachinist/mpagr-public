package steammachinist.mpagradmin;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/service-control")
public class EngineControlController {
    private final EngineControlService controlService;

    @GetMapping
    public String getControlPage() {
        return "service-control";
    }

    @PostMapping("/start")
    public String startEngine() {
        controlService.startEngine();
        return "redirect:/service-control";
    }

    @PostMapping("/stop")
    public String stopEngine() {
        controlService.stopEngine();
        return "redirect:/service-control";
    }
}
