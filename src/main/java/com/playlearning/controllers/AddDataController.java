package com.playlearning.controllers;

import com.playlearning.dao.*;
import com.playlearning.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by andy on 5/11/15.
 */
@Controller
public class AddDataController {
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

    private String OUTPUT = "output";
    private String COMPILE = "compile";

    @RequestMapping("/adddata")
    public String getAddData(Model model) {
        model.addAttribute("view", "fragments/sections/addData");
        return "default";
    }

    @RequestMapping("/loadCourses")
    @ResponseBody
    public String loadCourses() {
        return getAllCoursesAsHtmlOptions();
    }

    public String getAllCoursesAsHtmlOptions() {
        StringBuilder stringBuilder = new StringBuilder();

        List<Course> allCourses = coursesDao.getAllCourses();
        if (allCourses == null || allCourses.isEmpty()) {
            return "";
        }
        stringBuilder.append("<option value=\"-1\">select course</option>");
        for (Course course : allCourses) {
            stringBuilder.append("<option value=\"" + course.getId() + "\">" + course.getName() + "</option>");
        }

        return stringBuilder.toString();
    }

    @RequestMapping("/getCategoryNumbers")
    @ResponseBody
    public String getCategoryNumbers(@ModelAttribute(value = "courseId") int courseId) {
        StringBuilder stringBuilder = new StringBuilder();

        List<Category> allCategoriesForCourse = categoriesDao.getAllCategoriesForCourse(coursesDao.getCourseById(courseId));
        if (allCategoriesForCourse != null) {
            stringBuilder.append("<option value=\"-1\">select category</option>");
            for (Category category : allCategoriesForCourse) {
                stringBuilder.append("<option value=\"" + category.getId() + "\">" + category.getName() + "</option>");
            }
            return stringBuilder.toString();
        } else {
            return "";
        }
    }

    @RequestMapping("/getLessonNumbers")
    @ResponseBody
    public String getLessonNumbers(@ModelAttribute(value = "categoryId") int categoryId) {
        StringBuilder stringBuilder = new StringBuilder();

        List<Lesson> allLessonsForCategory = lessonsDao.getAllLessonsForCategory(categoriesDao.getCategoryById(categoryId));
        if (allLessonsForCategory != null) {
            stringBuilder.append("<option value=\"-1\">select lesson</option>");
            for (Lesson lesson : allLessonsForCategory) {
                stringBuilder.append("<option value=\"" + lesson.getId() + "\">" + lesson.getName() + "</option>");
            }
            return stringBuilder.toString();
        } else {
            return "";
        }
    }

    @RequestMapping("/getExerciseNumbers")
    @ResponseBody
    public String getExerciseNumbers(@ModelAttribute(value = "lessonId") int lessonId) {
        StringBuilder stringBuilder = new StringBuilder();

        List<Exercise> allExercisesForLesson = exerciseDao.getAllExercisesForLesson(lessonsDao.getLessonById(lessonId));
        if (allExercisesForLesson != null) {
            stringBuilder.append("<option value=\"-1\">select exercise</option>");
            for (Exercise exercise : allExercisesForLesson) {
                stringBuilder.append("<option value=\"" + exercise.getId() + "\">" + exercise.getNumber() + "</option>");
            }
            return stringBuilder.toString();
        } else {
            return "";
        }
    }

    @RequestMapping("/getResultsForExercise")
    @ResponseBody
    public List<String> getResultsForExercise(@ModelAttribute(value = "exerciseId") int exerciseId) {
        List<String> result = new LinkedList<String>();
        Exercise exercise = exerciseDao.getExerciseById(exerciseId);

        result.add(exercise.getType());

        if (exercise.getType().equals(OUTPUT)) {
            result.add(resultsDao.getResultForExercise(exercise) == null ? "" : resultsDao.getResultForExercise(exercise).getOutput());
        } else {
            String classes = "";
            String methods = "";
            String fields = "";
            ClasZ clasZ = clasZesDao.getClasZForExercise(exercise);
            if (clasZ != null) {
                classes += clasZ.getNames();
            }
            Method method = methodsDao.getMethodForExercise(exercise);
            if (method != null) {
                methods += method.getNames();
            }
            Field field = fieldsDao.getFieldForExercise(exercise);
            if (field != null) {
                fields += field.getNames();
            }

            result.add(classes);
            result.add(methods);
            result.add(fields);
        }
        return result;
    }

    @RequestMapping("/addOrUpdateCourse")
    @ResponseBody
    public String addOrUpdateCourse(@ModelAttribute(value = "courseId") int courseId, @ModelAttribute(value = "courseName") String courseName) {
        Course course;
        if (courseId != -1) {
            course = coursesDao.getCourseById(courseId);
        } else {
            course = new Course();
        }

        course.setName(courseName);
        coursesDao.addCourse(course);

        return getAllCoursesAsHtmlOptions(course.getId());
    }

    public String getAllCoursesAsHtmlOptions(int courseId) {
        StringBuilder stringBuilder = new StringBuilder();

        List<Course> allCourses = coursesDao.getAllCourses();
//        if (allCourses != null) {
        stringBuilder.append("<option value=\"-1\">select course</option>");
        for (Course course : allCourses) {
            if (course.getId() == courseId) {
                stringBuilder.append("<option value=\"" + course.getId() + "\" selected=\"true\">" + course.getName() + "</option>");
            } else {
                stringBuilder.append("<option value=\"" + course.getId() + "\">" + course.getName() + "</option>");
            }
        }

//        }
        return stringBuilder.toString();
    }

    @RequestMapping("/addOrUpdateCategory")
    @ResponseBody
    public String addOrUpdateCategory(@ModelAttribute(value = "courseId") int courseId, @ModelAttribute(value = "categoryId") int categoryId, @ModelAttribute(value = "categoryName") String categoryName) {
        Category category;
        if (categoryId != -1) {
            category = categoriesDao.getCategoryById(categoryId);
        } else {
            category = new Category();
        }

        Course course = coursesDao.getCourseById(courseId);
        category.setName(categoryName);
        category.setNumber(categoriesDao.getLastCategoryNumber(course) + 1);
        category.setCoursesByCourseId(course);
        categoriesDao.addCategory(category);

        return getAllCategoriesAsHtmlOptions(course, category.getId());
    }

    public String getAllCategoriesAsHtmlOptions(Course course, int categoryId) {
        StringBuilder stringBuilder = new StringBuilder();

        List<Category> allCategoriesForCourse = categoriesDao.getAllCategoriesForCourse(course);
//        if (allCategoriesForCourse != null) {
        stringBuilder.append("<option value=\"-1\">select category</option>");
        for (Category category : allCategoriesForCourse) {
            if (category.getId() == categoryId) {
                stringBuilder.append("<option value=\"" + category.getId() + "\" selected=\"true\">" + category.getName() + "</option>");
            } else {
                stringBuilder.append("<option value=\"" + category.getId() + "\">" + category.getName() + "</option>");
            }
        }

//        }
        return stringBuilder.toString();
    }

    @RequestMapping("/addOrUpdateLesson")
    @ResponseBody
    public String addOrUpdateLesson(@ModelAttribute(value = "categoryId") int categoryId, @ModelAttribute(value = "lessonId") int lessonId, @ModelAttribute(value = "lessonName") String lessonName) {
        Lesson lesson;
        if (lessonId != -1) {
            lesson = lessonsDao.getLessonById(lessonId);
        } else {
            lesson = new Lesson();
        }

        Category category = categoriesDao.getCategoryById(categoryId);
        lesson.setName(lessonName);
        lesson.setHtml(lessonName.replace(" ", "_"));
        lesson.setNumber(lessonsDao.getLastLessonNumber(category) + 1);
        lesson.setCategoriesByCategoryId(category);
        lessonsDao.addLesson(lesson);

        return getAllLessonsAsHtmlOptions(category, lesson.getId());
    }

    public String getAllLessonsAsHtmlOptions(Category category, int lessonId) {
        StringBuilder stringBuilder = new StringBuilder();

        List<Lesson> allLessonsForCategory = lessonsDao.getAllLessonsForCategory(category);
//        if (allLessonsForCategory != null) {
        stringBuilder.append("<option value=\"-1\">select lesson</option>");
        for (Lesson lesson : allLessonsForCategory) {
            if (lesson.getId() == lessonId) {
                stringBuilder.append("<option value=\"" + lesson.getId() + "\" selected=\"true\">" + lesson.getName() + "</option>");
            } else {
                stringBuilder.append("<option value=\"" + lesson.getId() + "\">" + lesson.getName() + "</option>");
            }
        }
//        }
        return stringBuilder.toString();
    }

    @RequestMapping("/addExercise")
    @ResponseBody
    public String addExercise(@ModelAttribute(value = "lessonId") int lessonId) {
        Lesson lesson = lessonsDao.getLessonById(lessonId);
        Exercise exercise = new Exercise();

        exercise.setType(OUTPUT);
        exercise.setLessonsByLessonId(lesson);
        exercise.setNumber(exerciseDao.getLastExerciseNumber(lesson) + 1);
        exercise.setHtml(lesson.getHtml() + "_" + exercise.getNumber());

        exerciseDao.addExercise(exercise);

        return getAllExercisesAsHtmlOptions(lesson, exercise.getId());
    }

    public String getAllExercisesAsHtmlOptions(Lesson lesson, int exerciseId) {
        StringBuilder stringBuilder = new StringBuilder();

        List<Exercise> allExercisesForLesson = exerciseDao.getAllExercisesForLesson(lesson);
//        if (allExercisesForLesson != null) {
        stringBuilder.append("<option value=\"-1\">select exercise</option>");
        for (Exercise exercise : allExercisesForLesson) {
            if (exercise.getId() == exerciseId) {
                stringBuilder.append("<option value=\"" + exercise.getId() + "\" selected=\"true\">" + exercise.getNumber() + "</option>");
            } else {
                stringBuilder.append("<option value=\"" + exercise.getId() + "\">" + exercise.getNumber() + "</option>");
            }
        }
//        }
        return stringBuilder.toString();
    }

    @RequestMapping("/addOrUpdateResultOutput")
    @ResponseBody
    public String addOrUpdateResultOutput(@ModelAttribute(value = "exerciseId") int exerciseId, @ModelAttribute(value = "resultType") String resultType, @ModelAttribute(value = "output") String output) {
        Exercise exercise = exerciseDao.getExerciseById(exerciseId);
        Result result = resultsDao.getResultForExercise(exercise) == null ? new Result() : resultsDao.getResultForExercise(exercise);

        result.setExercisesByExerciseId(exercise);
        result.setOutput(output);

        exercise.setType(OUTPUT);
        exerciseDao.addExercise(exercise);
        resultsDao.addResult(result);

        ClasZ clasZForExercise = clasZesDao.getClasZForExercise(exercise);
        Method methodForExercise = methodsDao.getMethodForExercise(exercise);
        Field fieldForExercise = fieldsDao.getFieldForExercise(exercise);

        if (clasZForExercise != null) {
            clasZesDao.deleteClasZ(clasZForExercise);
        }
        if (methodForExercise != null) {
            methodsDao.deleteMethod(methodForExercise);
        }
        if (fieldForExercise != null) {
            fieldsDao.deleteField(fieldForExercise);
        }

        return result.getOutput();
    }

    @RequestMapping("/addOrUpdateResultCompile")
    @ResponseBody
    public List<String> addOrUpdateResultCompile(@ModelAttribute(value = "exerciseId") int exerciseId,
                                                 @ModelAttribute(value = "classes") String classes, @ModelAttribute(value = "methods") String methods,
                                                 @ModelAttribute(value = "fields") String fields) {
        List<String> data = new LinkedList<String>();

        Exercise exercise = exerciseDao.getExerciseById(exerciseId);
        ClasZ clasZ = clasZesDao.getClasZForExercise(exercise) == null ? new ClasZ() : clasZesDao.getClasZForExercise(exercise);
        Method method = methodsDao.getMethodForExercise(exercise) == null ? new Method() : methodsDao.getMethodForExercise(exercise);
        Field field = fieldsDao.getFieldForExercise(exercise) == null ? new Field() : fieldsDao.getFieldForExercise(exercise);

        clasZ.setExercisesByExerciseId(exercise);
        clasZ.setNames(classes);
        clasZesDao.addClasZ(clasZ);

        method.setExercisesByExerciseId(exercise);
        method.setNames(methods);
        methodsDao.addClasZ(method);

        field.setExercisesByExerciseId(exercise);
        field.setNames(fields);
        fieldsDao.addField(field);

        exercise.setType(COMPILE);
        exerciseDao.addExercise(exercise);

        Result resultForExercise = resultsDao.getResultForExercise(exercise);
        if (resultForExercise != null) {
            resultsDao.deleteResult(resultForExercise);
        }

        data.add(clasZ.getNames());
        data.add(method.getNames());
        data.add(field.getNames());

        return data;
    }
}
