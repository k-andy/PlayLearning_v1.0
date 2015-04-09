package controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SubChapterController {
    @RequestMapping(value = "/subChapterSection", params = {"number"}, method=RequestMethod.GET)
    public String getSubChapterSection(@RequestParam(value = "name", required = false, defaultValue = "Lobster") String name, Model model, @RequestParam String number) {

        model.addAttribute("name", name);
        model.addAttribute("view", "fragments/sections/" + number);

        return "default";
    }

}
