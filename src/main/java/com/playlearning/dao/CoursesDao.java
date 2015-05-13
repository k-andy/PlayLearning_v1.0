package com.playlearning.dao;

import com.playlearning.model.Course;

import java.util.List;

/**
 * Created by andy on 5/9/15.
 */
public interface CoursesDao {
    void addCourse(Course course);
    Course getCourseById(int id);
    Course getCourseByName(String name);
    List<Course> getAllCourses();
    void deleteCourse(Course course);
}
