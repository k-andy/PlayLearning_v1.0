package com.playlearning.controllers;

import com.playlearning.dao.CategoriesDao;
import com.playlearning.dao.CoursesDao;
import com.playlearning.dao.LessonsDao;
import com.playlearning.model.Category;
import com.playlearning.model.Lesson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.*;

@Controller
public class ContentController {
    @Autowired
    private CoursesDao coursesDao;
    @Autowired
    private CategoriesDao categoriesDao;
    @Autowired
    private LessonsDao lessonsDao;
    private Map<Category, List<Lesson>> categoryListMap = new LinkedHashMap<Category, List<Lesson>>();

    @RequestMapping("/content")
    public String getContent(@ModelAttribute(value = "courseId") int courseId, Model model) {
        List<Category> allCategoriesForCourse = categoriesDao.getAllCategoriesForCourse(coursesDao.getCourseById(courseId));
        if (allCategoriesForCourse != null) {
            for (Category category : allCategoriesForCourse) {
                List<Lesson> lessons = lessonsDao.getAllLessonsForCategory(category);
                if (lessons != null) {
                    categoryListMap.put(category, lessons);
                }
            }
        }
        model.addAttribute("content", categoryListMap);
        model.addAttribute("view", "fragments/sections/contentSection");
        return "default";
    }
}
