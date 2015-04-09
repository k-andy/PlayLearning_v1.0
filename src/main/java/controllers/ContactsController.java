package controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ContactsController {
    @RequestMapping("/contactsSection")
    public String getContacts(@RequestParam(value = "name", required = false, defaultValue = "Lobster") String name, Model model) {
        model.addAttribute("name", name);
        model.addAttribute("view", "fragments/sections/contactsSection");
        return "default";
    }
}
