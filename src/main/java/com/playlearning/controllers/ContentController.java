package com.playlearning.controllers;

import com.playlearning.model.Category;
import com.playlearning.model.Lesson;
import com.playlearning.utils.DataAccessUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.File;

@Controller
public class ContentController {
    private static final String COURSES_PATH = "fragments/articles/courses";
    private static final String CATEGORIES_FOLDER_NAME = "categories";
    private static final String LESSONS_FOLDER_NAME = "lessons";
    @Autowired
    private DataAccessUtils dataAccessUtils;

    @RequestMapping("/course")
    public String getContent(@ModelAttribute(value = "courseId") int courseId, Model model) {
        model.addAttribute("course", dataAccessUtils.getCourseById(courseId));
        model.addAttribute("coursesCategories", dataAccessUtils.getCourseCategoryListMap());
        return "course";
    }

    @RequestMapping("/course/category")
    public String category(@ModelAttribute(value = "categoryId") int categoryId, Model model) {
        model.addAttribute("category", dataAccessUtils.getCategoryById(categoryId));
        model.addAttribute("coursesCategories", dataAccessUtils.getCourseCategoryListMap());
        return "category";
    }

    @RequestMapping("/course/category/lesson")
    public String lesson(@ModelAttribute(value = "lessonId") int lessonId, Model model) {
        Lesson lesson = dataAccessUtils.getLessonById(lessonId);
        Category category = lesson.getCategoriesByCategoryId();
        model.addAttribute("html", COURSES_PATH + File.separator + category.getCoursesByCourseId().getName() +
                File.separator + CATEGORIES_FOLDER_NAME + File.separator + category.getNumber() + " " + category.getName() +
                File.separator + LESSONS_FOLDER_NAME + File.separator + lesson.getNumber() + " " + lesson.getName() +
                File.separator + lesson.getHtml());
        model.addAttribute("lesson", lesson);
        model.addAttribute("coursesCategories", dataAccessUtils.getCourseCategoryListMap());
        return "lesson";
    }
}
