package controllers;

import utils.XmlParser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.xml.bind.JAXBException;

@Controller
public class ContentController {
    @RequestMapping("/contentSection")
    public String getContent(@RequestParam(value="name", required=false, defaultValue="Lobster") String name, Model model) {
        model.addAttribute("name", name);

        try {
            model.addAttribute("content", XmlParser.parseContentsXml().getChapters());
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        model.addAttribute("view", "fragments/sections/contentSection");
        return "default";
    }
}
