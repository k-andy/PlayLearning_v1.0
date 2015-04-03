package hello;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {

    @RequestMapping("/")
    public String greeting(@RequestParam(value="name", required=false, defaultValue="Lobster") String name, Model model) {
        model.addAttribute("name", name);
        model.addAttribute("view", "fragments/homeSection");
        return "default";
    }

}
