package controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class HomeController {
    @RequestMapping("/")
    public String getGreeting(@RequestParam(value="name", required=false, defaultValue="Lobster") String name, Model model) {
        model.addAttribute("name", name);
        model.addAttribute("view", "fragments/homeSection");
        return "default";
    }

    @RequestMapping("/error")
    public String getError(@RequestParam(value="name", required=false, defaultValue="Lobster") String name, Model model) {
        model.addAttribute("name", name);
        model.addAttribute("view", "fragments/homeSection");
        return "default";
    }

}
