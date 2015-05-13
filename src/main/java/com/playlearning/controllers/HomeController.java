package com.playlearning.controllers;

import com.playlearning.beans.NewData;
import com.playlearning.beans.NewUser;
import com.playlearning.dao.*;
import com.playlearning.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.transaction.Transactional;
import java.util.LinkedList;
import java.util.List;

@Controller
public class HomeController {
    @Autowired
    private UsersDao usersDao;
    @Autowired
    private RolesDao rolesDao;
    @Autowired
    private CoursesDao coursesDao;
    @Autowired
    private CategoriesDao categoriesDao;
    @Autowired
    private LessonsDao lessonsDao;
    @Autowired
    private ExerciseDao exerciseDao;
    @Autowired
    private ResultsDao resultsDao;

    @ModelAttribute("roles")
    public List<Role> populateTypes() {
        return rolesDao.getAllRoles();
    }

    @RequestMapping("/")
    public String getGreeting(Model model) {
        model.addAttribute("view", "fragments/sections/homeSection");
        return "default";
    }

    @RequestMapping("/login")
    public String getLogin(Model model) {
        model.addAttribute("view", "fragments/sections/login");
        return "default";
    }

    @RequestMapping("/admin")
    public String getAdmin(Model model) {
        model.addAttribute("newuser", new NewUser());
        model.addAttribute("view", "fragments/sections/admin");
        return "default";
    }

    @RequestMapping(value = "/adduser", method = RequestMethod.POST)
    public String adduser(@ModelAttribute(value = "newuser") NewUser newUser, Model model) {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        User user = new User();
        user.setPassword(encoder.encode(newUser.getPassword()));
        user.setName(newUser.getName());
        user.setRolesByRoleId(rolesDao.getRoleById(newUser.getRoleNumber()));
        user.setEmail("test@test.test");

        usersDao.addUser(user);
        model.addAttribute("view", "fragments/sections/homeSection");
        return "default";
    }

    @RequestMapping("/403")
    public String get403(Model model) {
        model.addAttribute("view", "fragments/sections/403");
        return "default";
    }
}
