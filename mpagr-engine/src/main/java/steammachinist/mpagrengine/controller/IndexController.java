package steammachinist.mpagrengine.controller;

import org.springframework.web.bind.annotation.GetMapping;

@org.springframework.stereotype.Controller
public class IndexController {
    @GetMapping
    public String index() {
        return "index";
    }
}
