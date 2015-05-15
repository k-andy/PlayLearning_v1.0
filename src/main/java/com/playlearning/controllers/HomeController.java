package com.playlearning.controllers;

import com.playlearning.utils.DataAccessUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {
    @Autowired
    private DataAccessUtils dataAccessUtils;

    @RequestMapping("/")
    public String getGreeting(Model model) {
        model.addAttribute("coursesCategories", dataAccessUtils.getCourseCategoryListMap());
        return "home";
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
