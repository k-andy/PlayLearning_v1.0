package com.playlearning.controllers;

import com.playlearning.dao.*;
import com.playlearning.model.*;
import com.playlearning.utils.FileWriteUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.transaction.Transactional;
import java.io.File;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by andy on 5/13/15.
 */
@Controller
public class AdminPageController {
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

    private static final String RESULT_TYPE_OUTPUT = "output";
    private static final String RESULT_TYPE_COMPILE = "compile";
    private static final String PROJECT_CONTENT_DIRECTORY = "src/main/resources/pages/fragments/articles/courses";

    private static final String LESSON_CONTENT_TYPE = "lesson";
    private static final String EXERCISE_CONTENT_TYPE = "exercise";

    private static final String CATEGORIES_DIRECTORY_NAME = "categories";
    private static final String LESSONS_DIRECTORY_NAME = "lessons";
    private static final String EXERCISES_DIRECTORY_NAME = "exercises";
    private static final String PAGE_EXTENSION = ".html";
    private static final String DEFAULT_EMAIL = "test@test.test";
    private static final String SPACE_REPLACEMENT = "_";
    private static final String HTML_FILE_NAME_REPLACE_PATTERN = "[^a-zA-Z0-9]+";

    @RequestMapping("/adminpage")
    public String getAdmin(Model model) {
        model.addAttribute("roles", rolesDao.getAllRoles());
        model.addAttribute("view", "fragments/sections/adminpageSection");
        return "default";
    }

    @RequestMapping("/addUser")
    @ResponseBody
    public String adduser(@ModelAttribute(value = "userName") String userName, @ModelAttribute(value = "password") String password,
                          @ModelAttribute(value = "roleId") int roleId, @ModelAttribute(value = "email") String email) {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        User user = new User();
        user.setPassword(encoder.encode(password));
        user.setName(userName);
        user.setRolesByRoleId(rolesDao.getRoleById(roleId));
        user.setEmail(email.isEmpty() ? DEFAULT_EMAIL : email);

        usersDao.addUser(user);
        return "";
    }

    @RequestMapping("/createFiles")
    @ResponseBody
    public String createFiles(@ModelAttribute(value = "courseId") int courseId) {
        List<Course> allCourses = coursesDao.getAllCourses();

        if (allCourses != null) {
            for (Course course : allCourses) {
                List<Category> allCategoriesForCourse = categoriesDao.getAllCategoriesForCourse(course);
                if (allCategoriesForCourse != null) {
                    for (Category category : allCategoriesForCourse) {
                        List<Lesson> allLessonsForCategory = lessonsDao.getAllLessonsForCategory(category);
                        if (allLessonsForCategory != null) {
                            for (Lesson lesson : allLessonsForCategory) {
                                String lessonDirPath = System.getProperty("user.dir") + File.separator + PROJECT_CONTENT_DIRECTORY +
                                        File.separator + course.getName() + File.separator + CATEGORIES_DIRECTORY_NAME + File.separator +
                                        category.getNumber() + " " +
                                        category.getName().replaceAll("/", SPACE_REPLACEMENT).replaceAll("\\\\", SPACE_REPLACEMENT) + File.separator +
                                        LESSONS_DIRECTORY_NAME + File.separator +
                                        lesson.getNumber() + " " + lesson.getName().replaceAll("/", SPACE_REPLACEMENT).replaceAll("\\\\", SPACE_REPLACEMENT);
                                String exerciseDirPath = lessonDirPath + File.separator + EXERCISES_DIRECTORY_NAME;
                                File file = new File(exerciseDirPath);
                                if (!file.exists()) {
                                    file.mkdirs();
                                }
                                file = new File(lessonDirPath, lesson.getHtml() + PAGE_EXTENSION);
                                FileWriteUtils.createHtmlFile(file);
                                FileWriteUtils.writeHtmlFile(FileWriteUtils.createHtmlContent(LESSON_CONTENT_TYPE, lesson.getName()), file.getPath());
                                List<Exercise> allExercisesForLesson = exerciseDao.getAllExercisesForLesson(lesson);
                                if (allExercisesForLesson != null) {
                                    for (Exercise exercise : allExercisesForLesson) {
                                        file = new File(exerciseDirPath, exercise.getHtml() + PAGE_EXTENSION);
                                        FileWriteUtils.createHtmlFile(file);
                                        FileWriteUtils.writeHtmlFile(FileWriteUtils.createHtmlContent(EXERCISE_CONTENT_TYPE, lesson.getName()), file.getPath());
                                    }
                                }
                            }
                        }
                    }
                }

            }
        }
        return "";
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
    public List<String> getLessonNumbers(@ModelAttribute(value = "categoryId") int categoryId) {
        StringBuilder stringBuilder = new StringBuilder();
        List<String> result = new LinkedList<String>();

        Category category = categoriesDao.getCategoryById(categoryId);
        result.add(category.getNumber() + "");
        List<Lesson> allLessonsForCategory = lessonsDao.getAllLessonsForCategory(category);
        if (allLessonsForCategory != null) {
            stringBuilder.append("<option value=\"-1\">select lesson</option>");
            for (Lesson lesson : allLessonsForCategory) {
                stringBuilder.append("<option value=\"" + lesson.getId() + "\">" + lesson.getName() + "</option>");
            }
            result.add(stringBuilder.toString());
            return result;
        } else {
            return result;
        }
    }

    @RequestMapping("/getExerciseNumbers")
    @ResponseBody
    public List<String> getExerciseNumbers(@ModelAttribute(value = "lessonId") int lessonId) {
        StringBuilder stringBuilder = new StringBuilder();
        List<String> result = new LinkedList<String>();

        Lesson lesson = lessonsDao.getLessonById(lessonId);
        result.add(lesson.getNumber() + "");
        List<Exercise> allExercisesForLesson = exerciseDao.getAllExercisesForLesson(lesson);
        if (allExercisesForLesson != null) {
            stringBuilder.append("<option value=\"-1\">select exercise</option>");
            for (Exercise exercise : allExercisesForLesson) {
                stringBuilder.append("<option value=\"" + exercise.getId() + "\">" + exercise.getNumber() + "</option>");
            }
            result.add(stringBuilder.toString());
            return result;
        } else {
            return result;
        }
    }

    @RequestMapping("/getResultsForExercise")
    @ResponseBody
    public List<String> getResultsForExercise(@ModelAttribute(value = "exerciseId") int exerciseId) {
        List<String> result = new LinkedList<String>();
        Exercise exercise = exerciseDao.getExerciseById(exerciseId);

        result.add(exercise.getType());

        if (exercise.getType().equals(RESULT_TYPE_OUTPUT)) {
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
        stringBuilder.append("<option value=\"-1\">select course</option>");
        for (Course course : allCourses) {
            if (course.getId() == courseId) {
                stringBuilder.append("<option value=\"" + course.getId() + "\" selected=\"true\">" + course.getName() + "</option>");
            } else {
                stringBuilder.append("<option value=\"" + course.getId() + "\">" + course.getName() + "</option>");
            }
        }
        return stringBuilder.toString();
    }

    @RequestMapping("/addOrUpdateCategory")
    @ResponseBody
    public String addOrUpdateCategory(@ModelAttribute(value = "courseId") int courseId, @ModelAttribute(value = "categoryId") int categoryId,
                                      @ModelAttribute(value = "categoryName") String categoryName, @ModelAttribute(value = "categoryNumber") int categoryNumber,
                                      @ModelAttribute(value = "categoryNames") String categoryNames) {
        Category category = new Category();
        Course course = coursesDao.getCourseById(courseId);
        if (categoryNames.isEmpty()) {
            if (categoryId != -1) {
                category = categoriesDao.getCategoryById(categoryId);
            } else {
                category = new Category();
            }
            if (categoryNumber != 0) {
                List<Category> allCategoriesForCourse = categoriesDao.getAllCategoriesForCourse(course);
                if (allCategoriesForCourse != null) {
                    for (Category tmpCategory : allCategoriesForCourse) {
                        if (tmpCategory.getNumber() >= categoryNumber) {
                            tmpCategory.setNumber(tmpCategory.getNumber() + 1);
                            categoriesDao.addCategory(tmpCategory);
                        }
                    }
                }
                category.setNumber(categoryNumber);
            } else {
                category.setNumber(categoriesDao.getLastCategoryNumber(course) + 1);
            }
            addCategory(category, course, categoryName);
        } else {
            String[] categoryNameList = categoryNames.split("\\n");
            for (String categoryNameFromList : categoryNameList) {
                category = new Category();
                category.setNumber(categoriesDao.getLastCategoryNumber(course) + 1);
                addCategory(category, course, categoryNameFromList);
            }
        }

        return getAllCategoriesAsHtmlOptions(course, category.getId());
    }

    private void addCategory(Category category, Course course, String categoryNameFromList) {
        category.setName(categoryNameFromList);
        category.setCoursesByCourseId(course);
        categoriesDao.addCategory(category);
    }

    public String getAllCategoriesAsHtmlOptions(Course course, int categoryId) {
        StringBuilder stringBuilder = new StringBuilder();

        List<Category> allCategoriesForCourse = categoriesDao.getAllCategoriesForCourse(course);
        stringBuilder.append("<option value=\"-1\">select category</option>");
        for (Category category : allCategoriesForCourse) {
            if (category.getId() == categoryId) {
                stringBuilder.append("<option value=\"" + category.getId() + "\" selected=\"true\">" + category.getName() + "</option>");
            } else {
                stringBuilder.append("<option value=\"" + category.getId() + "\">" + category.getName() + "</option>");
            }
        }
        return stringBuilder.toString();
    }

    @RequestMapping("/addOrUpdateLesson")
    @ResponseBody
    public String addOrUpdateLesson(@ModelAttribute(value = "categoryId") int categoryId, @ModelAttribute(value = "lessonId") int lessonId,
                                    @ModelAttribute(value = "lessonName") String lessonName, @ModelAttribute(value = "lessonNumber") int lessonNumber,
                                    @ModelAttribute(value = "lessonNames") String lessonNames) {
        Lesson lesson = new Lesson();
        Category category = categoriesDao.getCategoryById(categoryId);
        if (lessonNames.isEmpty()) {
            if (lessonId != -1) {
                lesson = lessonsDao.getLessonById(lessonId);
            } else {
                lesson = new Lesson();
            }
            if (lessonNumber != 0) {
                List<Lesson> allLessonsForCategory = lessonsDao.getAllLessonsForCategory(category);
                if (allLessonsForCategory != null) {
                    for (Lesson tmpLesson : allLessonsForCategory) {
                        if (tmpLesson.getNumber() >= lessonNumber) {
                            tmpLesson.setNumber(tmpLesson.getNumber() + 1);
                            lessonsDao.addLesson(tmpLesson);
                        }
                    }
                }
                lesson.setNumber(lessonNumber);
            } else {
                lesson.setNumber(lessonsDao.getLastLessonNumber(category) + 1);
            }
            addLesson(lessonName, lesson, category);
        } else {
            String[] lessonNameList = lessonNames.split("\\n");
            for (String lessonNameFromList : lessonNameList) {
                lesson = new Lesson();
                addLesson(lessonNameFromList, lesson, category);
            }
        }

        return getAllLessonsAsHtmlOptions(category, lesson.getId());
    }

    private void addLesson(String lessonName, Lesson lesson, Category category) {
        lesson.setName(lessonName);
        lesson.setHtml(lessonName.replaceAll(HTML_FILE_NAME_REPLACE_PATTERN, SPACE_REPLACEMENT));
        lesson.setCategoriesByCategoryId(category);
        lessonsDao.addLesson(lesson);
    }

    public String getAllLessonsAsHtmlOptions(Category category, int lessonId) {
        StringBuilder stringBuilder = new StringBuilder();

        List<Lesson> allLessonsForCategory = lessonsDao.getAllLessonsForCategory(category);
        stringBuilder.append("<option value=\"-1\">select lesson</option>");
        for (Lesson lesson : allLessonsForCategory) {
            if (lesson.getId() == lessonId) {
                stringBuilder.append("<option value=\"" + lesson.getId() + "\" selected=\"true\">" + lesson.getName() + "</option>");
            } else {
                stringBuilder.append("<option value=\"" + lesson.getId() + "\">" + lesson.getName() + "</option>");
            }
        }
        return stringBuilder.toString();
    }

    @RequestMapping("/addExercise")
    @ResponseBody
    public String addExercise(@ModelAttribute(value = "lessonId") int lessonId) {
        Lesson lesson = lessonsDao.getLessonById(lessonId);
        Exercise exercise = new Exercise();

        exercise.setType(RESULT_TYPE_OUTPUT);
        exercise.setLessonsByLessonId(lesson);
        exercise.setNumber(exerciseDao.getLastExerciseNumber(lesson) + 1);
        exercise.setHtml(lesson.getHtml() + SPACE_REPLACEMENT + exercise.getNumber());

        exerciseDao.addExercise(exercise);

        return getAllExercisesAsHtmlOptions(lesson, exercise.getId());
    }

    public String getAllExercisesAsHtmlOptions(Lesson lesson, int exerciseId) {
        StringBuilder stringBuilder = new StringBuilder();

        List<Exercise> allExercisesForLesson = exerciseDao.getAllExercisesForLesson(lesson);
        stringBuilder.append("<option value=\"-1\">select exercise</option>");
        for (Exercise exercise : allExercisesForLesson) {
            if (exercise.getId() == exerciseId) {
                stringBuilder.append("<option value=\"" + exercise.getId() + "\" selected=\"true\">" + exercise.getNumber() + "</option>");
            } else {
                stringBuilder.append("<option value=\"" + exercise.getId() + "\">" + exercise.getNumber() + "</option>");
            }
        }
        return stringBuilder.toString();
    }

    @RequestMapping("/addOrUpdateResultOutput")
    @ResponseBody
    public String addOrUpdateResultOutput(@ModelAttribute(value = "exerciseId") int exerciseId, @ModelAttribute(value = "resultType") String resultType,
                                          @ModelAttribute(value = "output") String output) {
        Exercise exercise = exerciseDao.getExerciseById(exerciseId);
        Result result = resultsDao.getResultForExercise(exercise) == null ? new Result() : resultsDao.getResultForExercise(exercise);

        result.setExercisesByExerciseId(exercise);
        result.setOutput(output);

        exercise.setType(RESULT_TYPE_OUTPUT);
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

        exercise.setType(RESULT_TYPE_COMPILE);
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
