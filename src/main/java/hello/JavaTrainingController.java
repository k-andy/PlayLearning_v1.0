package hello;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class JavaTrainingController {
    @RequestMapping("/javaTraining")
    public String greeting(@RequestParam(value = "name", required = false, defaultValue = "Lobster") String name, Model model) {
        model.addAttribute("name", name);
        model.addAttribute("view", "fragments/javaTrainingSection");

        Code code = new Code();
        model.addAttribute("code", code);

        return "default";
    }

    @RequestMapping(value = "/processCode", method = RequestMethod.POST)
    public String processForm(@ModelAttribute(value = "code") Code code, Model model) {
        System.out.println("code processing");
        model.addAttribute("view", "fragments/contactsSection");

        return "default";
    }
}
