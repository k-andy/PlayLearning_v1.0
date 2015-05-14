package com.playlearning.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {
    @RequestMapping("/")
    public String getGreeting(Model model) {
        model.addAttribute("view", "fragments/sections/homeSection");
        return "default";
    }

    @RequestMapping("/login")
    public String getLogin(Model model) {
        model.addAttribute("view", "fragments/sections/loginSection");
        return "default";
    }

    @RequestMapping("/403")
    public String get403(Model model) {
        model.addAttribute("view", "fragments/sections/403");
        return "default";
    }
}
