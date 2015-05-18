package com.playlearning.utils;

import com.playlearning.dao.*;
import com.playlearning.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by andy on 5/15/15.
 */
@Component
public class DataAccessUtils {
    @Autowired
    private RolesDao rolesDao;
    @Autowired
    private UsersDao usersDao;
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
    @Autowired
    private ClasZesDao clasZesDao;
    @Autowired
    private MethodsDao methodsDao;
    @Autowired
    private FieldsDao fieldsDao;

    public List<Role> getAllRoles() {
        return rolesDao.getAllRoles();
    }

    public List<Course> getAllCourses() {
        return coursesDao.getAllCourses();
    }

    public List<Exercise> getAllExercisesForLesson(Lesson lesson) {
        return exerciseDao.getAllExercisesForLesson(lesson);
    }

    public Role getRoleById(int roleId) {
        return rolesDao.getRoleById(roleId);
    }

    public Exercise getExerciseById(int exerciseId) {
        return exerciseDao.getExerciseById(exerciseId);
    }

    public Result getResultForExercise(Exercise exercise) {
        return resultsDao.getResultForExercise(exercise);
    }

    public ClasZ getClasZForExercise(Exercise exercise) {
        return clasZesDao.getClasZForExercise(exercise);
    }

    public Method getMethodForExercise(Exercise exercise) {
        return methodsDao.getMethodForExercise(exercise);
    }

    public Field getFieldForExercise(Exercise exercise) {
        return fieldsDao.getFieldForExercise(exercise);
    }

    public void addUser(User user) {
        usersDao.addUser(user);
    }

    public void addLesson(Lesson lesson) {
        lessonsDao.addLesson(lesson);
    }

    public void addCourse(Course course) {
        coursesDao.addCourse(course);
    }

    public void addCategory(Category category) {
        categoriesDao.addCategory(category);
    }

    public void addExercise(Exercise exercise) {
        exerciseDao.addExercise(exercise);
    }

    public void addResult(Result result) {
        resultsDao.addResult(result);
    }

    public void deleteClasZ(ClasZ clasZ) {
        clasZesDao.deleteClasZ(clasZ);
    }

    public void deleteMethod(Method method) {
        methodsDao.deleteMethod(method);
    }

    public void deleteField(Field field) {
        fieldsDao.deleteField(field);
    }

    public void addClasZ(ClasZ clasZ) {
        clasZesDao.addClasZ(clasZ);
    }

    public void addMethod(Method method) {
        methodsDao.addMethod(method);
    }

    public void addField(Field field) {
        fieldsDao.addField(field);
    }

    public void deleteResult(Result result) {
        resultsDao.deleteResult(result);
    }

    public int getLastCategoryNumber(Course course) {
        return categoriesDao.getLastCategoryNumber(course);
    }

    public int getLastLessonNumber(Category category) {
        return lessonsDao.getLastLessonNumber(category);
    }

    public int getLastExerciseNumber(Lesson lesson) {
        return exerciseDao.getLastExerciseNumber(lesson);
    }

    public Map<Course, List<Category>> getCourseCategoryListMap() {
        Map<Course, List<Category>> courseListMap = new LinkedHashMap<Course, List<Category>>();
        List<Course> allCourses = coursesDao.getAllCourses();
        if (allCourses == null) {
            return courseListMap;
        }
        for (Course course : allCourses) {
            List<Category> allCategoriesForCourse = categoriesDao.getAllCategoriesForCourse(course);
            if (allCategoriesForCourse != null && allCategoriesForCourse.size() > 7) {
                allCategoriesForCourse = allCategoriesForCourse.subList(0, 6);
            }
            if (allCategoriesForCourse != null) {
                courseListMap.put(course, allCategoriesForCourse);
            }
        }
        return courseListMap;
    }

    public Course getCourseById(int courseId) {
        return coursesDao.getCourseById(courseId);
    }

    public Category getCategoryById(int categoryId) {
        return categoriesDao.getCategoryById(categoryId);
    }

    public Lesson getLessonById(int lessonId) {
        return lessonsDao.getLessonById(lessonId);
    }

    public List<Category> getAllCategoriesForCourse(Course course) {
        return categoriesDao.getAllCategoriesForCourse(course);
    }

    public List<Lesson> getAllLessonsForCategory(Category category) {
        return lessonsDao.getAllLessonsForCategory(category);
    }
}
