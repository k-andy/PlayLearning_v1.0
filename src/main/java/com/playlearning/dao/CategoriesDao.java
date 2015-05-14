package com.playlearning.dao;

import com.playlearning.model.Category;
import com.playlearning.model.Course;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by andy on 5/9/15.
 */
public interface CategoriesDao {
    void addCategory(Category category);
    Category getCategoryById(int id);
    Category getCategory(int number, String name, Course course);
    List<Category> getAllCategoriesForCourse(Course Course);
    int getLastCategoryNumber(Course course);
    void deleteCategory(Category category);
}
