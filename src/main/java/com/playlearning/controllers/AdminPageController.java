package com.playlearning.controllers;

import com.playlearning.model.*;
import com.playlearning.utils.Constants;
import com.playlearning.utils.DataAccessUtils;
import com.playlearning.utils.FileWriteUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by andy on 5/13/15.
 */
@Controller
public class AdminPageController {
    @Autowired
    private DataAccessUtils dataAccessUtils;

    private static final String DEFAULT_EMAIL = "test@test.test";

    @RequestMapping("/admin")
    public String admin(Model model) {
        model.addAttribute("roles", dataAccessUtils.getAllRoles());
        model.addAttribute("coursesCategories", dataAccessUtils.getCourseCategoryListMap());
        model.addAttribute("view", "fragments/sections/adminpageSection");
        return "admin";
    }

    @RequestMapping("/addUser")
    @ResponseBody
    public String adduser(@ModelAttribute(value = "userName") String userName, @ModelAttribute(value = "password") String password,
                          @ModelAttribute(value = "roleId") int roleId, @ModelAttribute(value = "email") String email) {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        User user = new User();
        user.setPassword(encoder.encode(password));
        user.setName(userName);
        user.setRolesByRoleId(dataAccessUtils.getRoleById(roleId));
        user.setEmail(email.isEmpty() ? DEFAULT_EMAIL : email);

        dataAccessUtils.addUser(user);
        return "";
    }

    @RequestMapping("/createFiles")
    @ResponseBody
    public String createFiles(@ModelAttribute(value = "courseId") int courseId) {
        List<Course> allCourses = dataAccessUtils.getAllCourses();

        if (allCourses != null) {
            for (Course course : allCourses) {
                List<Category> allCategoriesForCourse = dataAccessUtils.getAllCategoriesForCourse(course);
                if (allCategoriesForCourse != null) {
                    for (Category category : allCategoriesForCourse) {
                        List<Lesson> allLessonsForCategory = dataAccessUtils.getAllLessonsForCategory(category);
                        if (allLessonsForCategory != null) {
                            for (Lesson lesson : allLessonsForCategory) {
                                String lessonDirPath = Constants.COURSES_PATH +
                                        File.separator + course.getName() + File.separator + Constants.CATEGORIES_DIRECTORY_NAME + File.separator +
                                        category.getNumber() + " " +
                                        category.getName().replaceAll("/", Constants.SPACE_REPLACEMENT.getConstant()).replaceAll("\\\\", Constants.SPACE_REPLACEMENT.getConstant()) +
                                        File.separator + Constants.LESSONS_DIRECTORY_NAME + File.separator +
                                        lesson.getNumber() + " " + lesson.getName().replaceAll("/", Constants.SPACE_REPLACEMENT.getConstant()).replaceAll("\\\\", Constants.SPACE_REPLACEMENT.getConstant());
                                String exerciseDirPath = lessonDirPath + File.separator + Constants.EXERCISES_DIRECTORY_NAME;
                                File file = new File(exerciseDirPath);
                                if (!file.exists()) {
                                    file.mkdirs();
                                }
                                file = new File(lessonDirPath, lesson.getHtml() + Constants.PAGE_EXTENSION);
                                FileWriteUtils.createHtmlFile(file);
                                FileWriteUtils.writeToFile(FileWriteUtils.createHtmlContent(Constants.LESSON_CONTENT_TYPE, lesson.getName()), file.getPath());
                                List<Exercise> allExercisesForLesson = dataAccessUtils.getAllExercisesForLesson(lesson);
                                if (allExercisesForLesson != null) {
                                    for (Exercise exercise : allExercisesForLesson) {
                                        file = new File(exerciseDirPath, exercise.getHtml() + Constants.PAGE_EXTENSION);
                                        FileWriteUtils.createHtmlFile(file);
                                        FileWriteUtils.writeToFile(FileWriteUtils.createHtmlContent(Constants.EXERCISE_CONTENT_TYPE, lesson.getName()), file.getPath());
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

        List<Course> allCourses = dataAccessUtils.getAllCourses();
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

        List<Category> allCategoriesForCourse = dataAccessUtils.getAllCategoriesForCourse(dataAccessUtils.getCourseById(courseId));
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

        Category category = dataAccessUtils.getCategoryById(categoryId);
        result.add(category.getNumber() + "");
        List<Lesson> allLessonsForCategory = dataAccessUtils.getAllLessonsForCategory(category);
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

        Lesson lesson = dataAccessUtils.getLessonById(lessonId);
        result.add(lesson.getNumber() + "");
        List<Exercise> allExercisesForLesson = dataAccessUtils.getAllExercisesForLesson(lesson);
        String pathToHtml = Constants.COURSES_PATH + File.separator + lesson.getCategoriesByCategoryId().getCoursesByCourseId().getName() +
                File.separator + Constants.CATEGORIES_FOLDER_NAME + File.separator + lesson.getCategoriesByCategoryId().getNumber() +
                " " + lesson.getCategoriesByCategoryId().getName() +
                File.separator + Constants.LESSONS_FOLDER_NAME + File.separator + lesson.getNumber() + " " + lesson.getName() +
                File.separator + lesson.getHtml();

        if (allExercisesForLesson != null) {
            stringBuilder.append("<option value=\"-1\">select exercise</option>");
            for (Exercise exercise : allExercisesForLesson) {
                stringBuilder.append("<option value=\"" + exercise.getId() + "\">" + exercise.getNumber() + "</option>");
            }

            result.add(stringBuilder.toString());
            result.add(FileWriteUtils.readFile(pathToHtml + ".html"));
        } else if (new File(pathToHtml + ".html").exists()) {
            result.add(FileWriteUtils.readFile(pathToHtml + ".html"));
        }
        return result;
    }

    @RequestMapping("/getResultsForExercise")
    @ResponseBody
    public List<String> getResultsForExercise(@ModelAttribute(value = "exerciseId") int exerciseId) {
        List<String> result = new LinkedList<String>();
        Exercise exercise = dataAccessUtils.getExerciseById(exerciseId);

        result.add(exercise.getType());

        if (exercise.getType().equals(Constants.RESULT_TYPE_OUTPUT)) {
            result.add(dataAccessUtils.getResultForExercise(exercise) == null ? "" : dataAccessUtils.getResultForExercise(exercise).getOutput());
        } else {
            String classes = "";
            String methods = "";
            String fields = "";
            ClasZ clasZ = dataAccessUtils.getClasZForExercise(exercise);
            if (clasZ != null) {
                classes += clasZ.getNames();
            }
            Method method = dataAccessUtils.getMethodForExercise(exercise);
            if (method != null) {
                methods += method.getNames();
            }
            Field field = dataAccessUtils.getFieldForExercise(exercise);
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
            course = dataAccessUtils.getCourseById(courseId);
        } else {
            course = new Course();
        }

        course.setName(courseName);
        course.setNick(courseName.replaceAll(" ", Constants.SPACE_REPLACEMENT.getConstant()).toLowerCase());
        dataAccessUtils.addCourse(course);

        return getAllCoursesAsHtmlOptions(course.getId());
    }

    public String getAllCoursesAsHtmlOptions(int courseId) {
        StringBuilder stringBuilder = new StringBuilder();

        List<Course> allCourses = dataAccessUtils.getAllCourses();
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
    public String addOrUpdateCategory(@ModelAttribute(value = "courseId") int courseId,
                                      @ModelAttribute(value = "categoryId") int categoryId,
                                      @ModelAttribute(value = "categoryName") String categoryName,
                                      @ModelAttribute(value = "categoryNick") String categoryNick,
                                      @ModelAttribute(value = "categoryNumber") String categoryNumber,
                                      @ModelAttribute(value = "categoryNames") String categoryNames) {
        Category category = new Category();
        Course course = dataAccessUtils.getCourseById(courseId);
        if (categoryNames.isEmpty()) {
            if (categoryId != -1) {
                category = dataAccessUtils.getCategoryById(categoryId);
            } else {
                category = new Category();
            }
            if (!categoryNumber.equals("")) {
                int categoryNumberInt = Integer.parseInt(categoryNumber);
                List<Category> allCategoriesForCourse = dataAccessUtils.getAllCategoriesForCourse(course);
                if (allCategoriesForCourse != null) {
                    for (Category tmpCategory : allCategoriesForCourse) {
                        if (tmpCategory.getNumber() >= categoryNumberInt) {
                            tmpCategory.setNumber(tmpCategory.getNumber() + 1);
                            dataAccessUtils.addCategory(tmpCategory);
                        }
                    }
                }
                category.setNumber(categoryNumberInt);
            } else {
                category.setNumber(dataAccessUtils.getLastCategoryNumber(course) + 1);
            }
            addCategory(category, course, categoryName, (categoryNick.length() == 0 ? categoryName : categoryNick).replaceAll(" ", "-").toLowerCase());
        } else {
            String[] categoryNameList = categoryNames.split("\\n");
            for (String categoryNameFromList : categoryNameList) {
                category = new Category();
                category.setNumber(dataAccessUtils.getLastCategoryNumber(course) + 1);
                String[] strings = categoryNameFromList.split("|");
                addCategory(category, course, strings[0], (strings[1].length() == 0 ? strings[1] : strings[0]).replaceAll(" ", "-").toLowerCase());
            }
        }

        return getAllCategoriesAsHtmlOptions(course, category.getId());
    }

    private void addCategory(Category category, Course course, String categoryNameFromList, String categoryNick) {
        category.setName(categoryNameFromList);
        category.setNick(categoryNick);
        category.setCoursesByCourseId(course);
        dataAccessUtils.addCategory(category);
    }

    public String getAllCategoriesAsHtmlOptions(Course course, int categoryId) {
        StringBuilder stringBuilder = new StringBuilder();

        List<Category> allCategoriesForCourse = dataAccessUtils.getAllCategoriesForCourse(course);
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
    public String addOrUpdateLesson(@ModelAttribute(value = "categoryId") int categoryId,
                                    @ModelAttribute(value = "lessonId") int lessonId,
                                    @ModelAttribute(value = "lessonName") String lessonName,
                                    @ModelAttribute(value = "lessonNick") String lessonNick,
                                    @ModelAttribute(value = "lessonNumber") String lessonNumber,
                                    @ModelAttribute(value = "lessonNames") String lessonNames) {
        Lesson lesson = new Lesson();
        Category category = dataAccessUtils.getCategoryById(categoryId);
        if (lessonNames.isEmpty()) {
            if (lessonId != -1) {
                lesson = dataAccessUtils.getLessonById(lessonId);
            } else {
                lesson = new Lesson();
            }
            if (!lessonNumber.equals("")) {
                int lessonNumberInt = Integer.parseInt(lessonNumber);
                List<Lesson> allLessonsForCategory = dataAccessUtils.getAllLessonsForCategory(category);
                if (allLessonsForCategory != null) {
                    for (Lesson tmpLesson : allLessonsForCategory) {
                        if (tmpLesson.getNumber() >= lessonNumberInt) {
                            tmpLesson.setNumber(tmpLesson.getNumber() + 1);
                            dataAccessUtils.addLesson(tmpLesson);
                        }
                    }
                }
                lesson.setNumber(lessonNumberInt);
            } else {
                lesson.setNumber(dataAccessUtils.getLastLessonNumber(category) + 1);
            }
            addLesson(lessonName, lesson, category, (lessonNick.length() == 0 ? lessonName : lessonNick).replaceAll(" ", Constants.SPACE_REPLACEMENT.getConstant()).toLowerCase());
        } else {
            String[] lessonNameList = lessonNames.split("\\n");
            for (String lessonNameFromList : lessonNameList) {
                lesson = new Lesson();
                String[] strings = lessonNameFromList.split("|");
                addLesson(strings[0], lesson, category, (strings[1].length() == 0 ? strings[1] : strings[0]).replaceAll(" ", Constants.SPACE_REPLACEMENT.getConstant()).toLowerCase());
            }
        }

        return getAllLessonsAsHtmlOptions(category, lesson.getId());
    }

    @RequestMapping("/saveToLessonFileButton")
    @ResponseBody
    public String saveToLessonFileButton(@ModelAttribute(value = "lessonId") int lessonId,
                                         @ModelAttribute(value = "lessonHtml") String lessonHtml) {
        Lesson lesson = dataAccessUtils.getLessonById(lessonId);

        String pathToHtml = Constants.COURSES_PATH + File.separator + lesson.getCategoriesByCategoryId().getCoursesByCourseId().getName() +
                File.separator + Constants.CATEGORIES_FOLDER_NAME + File.separator + lesson.getCategoriesByCategoryId().getNumber() +
                " " + lesson.getCategoriesByCategoryId().getName() +
                File.separator + Constants.LESSONS_FOLDER_NAME + File.separator + lesson.getNumber() + " " + lesson.getName() +
                File.separator + lesson.getHtml();


        FileWriteUtils.writeToFile(lessonHtml, pathToHtml + ".html");
        return "";
    }

    private void addLesson(String lessonName, Lesson lesson, Category category, String lessonNick) {
        lesson.setName(lessonName);
        lesson.setNick(lessonNick);
        lesson.setHtml(lessonName.replaceAll(Constants.HTML_FILE_NAME_REPLACE_PATTERN.getConstant(), Constants.SPACE_REPLACEMENT.getConstant()));
        lesson.setCategoriesByCategoryId(category);
        dataAccessUtils.addLesson(lesson);
    }

    public String getAllLessonsAsHtmlOptions(Category category, int lessonId) {
        StringBuilder stringBuilder = new StringBuilder();

        List<Lesson> allLessonsForCategory = dataAccessUtils.getAllLessonsForCategory(category);
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
        Lesson lesson = dataAccessUtils.getLessonById(lessonId);
        Exercise exercise = new Exercise();

        exercise.setType(Constants.RESULT_TYPE_OUTPUT.getConstant());
        exercise.setLessonsByLessonId(lesson);
        exercise.setNumber(dataAccessUtils.getLastExerciseNumber(lesson) + 1);
        exercise.setHtml(lesson.getHtml() + Constants.SPACE_REPLACEMENT + exercise.getNumber());

        dataAccessUtils.addExercise(exercise);

        return getAllExercisesAsHtmlOptions(lesson, exercise.getId());
    }

    public String getAllExercisesAsHtmlOptions(Lesson lesson, int exerciseId) {
        StringBuilder stringBuilder = new StringBuilder();

        List<Exercise> allExercisesForLesson = dataAccessUtils.getAllExercisesForLesson(lesson);
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
        Exercise exercise = dataAccessUtils.getExerciseById(exerciseId);
        Result result = dataAccessUtils.getResultForExercise(exercise) == null ? new Result() : dataAccessUtils.getResultForExercise(exercise);

        result.setExercisesByExerciseId(exercise);
        result.setOutput(output);

        exercise.setType(Constants.RESULT_TYPE_OUTPUT.getConstant());
        dataAccessUtils.addExercise(exercise);
        dataAccessUtils.addResult(result);

        ClasZ clasZForExercise = dataAccessUtils.getClasZForExercise(exercise);
        Method methodForExercise = dataAccessUtils.getMethodForExercise(exercise);
        Field fieldForExercise = dataAccessUtils.getFieldForExercise(exercise);

        if (clasZForExercise != null) {
            dataAccessUtils.deleteClasZ(clasZForExercise);
        }
        if (methodForExercise != null) {
            dataAccessUtils.deleteMethod(methodForExercise);
        }
        if (fieldForExercise != null) {
            dataAccessUtils.deleteField(fieldForExercise);
        }

        return result.getOutput();
    }

    @RequestMapping("/addOrUpdateResultCompile")
    @ResponseBody
    public List<String> addOrUpdateResultCompile(@ModelAttribute(value = "exerciseId") int exerciseId,
                                                 @ModelAttribute(value = "classes") String classes, @ModelAttribute(value = "methods") String methods,
                                                 @ModelAttribute(value = "fields") String fields) {
        List<String> data = new LinkedList<String>();

        Exercise exercise = dataAccessUtils.getExerciseById(exerciseId);
        ClasZ clasZ = dataAccessUtils.getClasZForExercise(exercise) == null ? new ClasZ() : dataAccessUtils.getClasZForExercise(exercise);
        Method method = dataAccessUtils.getMethodForExercise(exercise) == null ? new Method() : dataAccessUtils.getMethodForExercise(exercise);
        Field field = dataAccessUtils.getFieldForExercise(exercise) == null ? new Field() : dataAccessUtils.getFieldForExercise(exercise);

        clasZ.setExercisesByExerciseId(exercise);
        clasZ.setNames(classes);
        dataAccessUtils.addClasZ(clasZ);

        method.setExercisesByExerciseId(exercise);
        method.setNames(methods);
        dataAccessUtils.addMethod(method);

        field.setExercisesByExerciseId(exercise);
        field.setNames(fields);
        dataAccessUtils.addField(field);

        exercise.setType(Constants.RESULT_TYPE_COMPILE.getConstant());
        dataAccessUtils.addExercise(exercise);

        Result resultForExercise = dataAccessUtils.getResultForExercise(exercise);
        if (resultForExercise != null) {
            dataAccessUtils.deleteResult(resultForExercise);
        }

        data.add(clasZ.getNames());
        data.add(method.getNames());
        data.add(field.getNames());

        return data;
    }
}