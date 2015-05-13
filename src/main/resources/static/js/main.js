$(document).ready(function () {

    $(".categoriesDataBlock").hide();
    $(".lessonsDataBlock").hide();
    $(".exercisesDataBlock").hide();
    $(".resultsDataBlock").hide();

    $(".coursesSelect").hide();
    $(".categoriesSelect").hide();
    $(".lessonsSelect").hide();
    $(".exerciseSelect").hide();

    $(".resultCompileWrapper").hide();
    $(".resultOutputWrapper").hide();

    $(".radioOutput").click(function () {
        $(".resultCompileWrapper").hide();
        $(".resultOutputWrapper").show();
    });

    $(".radioCompile").click(function () {
        $(".resultCompileWrapper").show();
        $(".resultOutputWrapper").hide();
    });

    $.get("/loadCourses",
        function (data, status) {
            if (data != "") {
                $(".coursesSelect").html(data);
                $(".coursesSelect").show();
            }
            $(".courseTextField").val("")
        });

    $(".coursesSelect").change(function () {
        if ($(".coursesSelect").val() != -1) {
            $(".categoriesDataBlock").show()
            $.get("/getCategoryNumbers",
                {courseId: $(".coursesSelect").val()},
                function (data, status) {
                    if (data != "") {
                        $(".categoriesSelect").html(data);
                        $(".categoriesSelect").show();
                    }
                });
        } else {
            $(".categoriesDataBlock").hide();
            $(".lessonsDataBlock").hide();
            $(".exercisesDataBlock").hide();
            $(".resultsDataBlock").hide();

            $(".categoriesSelect").hide();
            $(".lessonsSelect").hide();
            $(".exerciseSelect").hide();

            $(".resultCompileWrapper").hide();
            $(".resultOutputWrapper").hide();
        }
    });

    $(".categoriesSelect").change(function () {
        if ($(".categoriesSelect").val() != -1) {
            $(".lessonsDataBlock").show();
            $.get("/getLessonNumbers",
                {categoryId: $(".categoriesSelect").val()},
                function (data, status) {
                    if (data != "") {
                        $(".lessonsSelect").html(data);
                        $(".lessonsSelect").show();
                    }
                });
        } else {
            $(".lessonsDataBlock").hide();
            $(".exercisesDataBlock").hide();
            $(".resultsDataBlock").hide();

            $(".lessonsSelect").hide();
            $(".exerciseSelect").hide();

            $(".resultCompileWrapper").hide();
            $(".resultOutputWrapper").hide();
        }
    });

    $(".lessonsSelect").change(function () {
        if ($(".lessonsSelect").val() != -1) {
            $(".exercisesDataBlock").show();
            $.get("/getExerciseNumbers",
                {lessonId: $(".lessonsSelect").val()},
                function (data, status) {
                    if (data != "") {
                        $(".exerciseSelect").html(data);
                        $(".exerciseSelect").show();
                    }
                });
        } else {
            $(".exercisesDataBlock").hide();
            $(".resultsDataBlock").hide();

            $(".exerciseSelect").hide();

            $(".resultCompileWrapper").hide();
            $(".resultOutputWrapper").hide();
        }
    });
    $(".exerciseSelect").change(function () {
        if ($(".exerciseSelect").val() != -1) {
            $(".resultsDataBlock").show();
            $.get("/getResultsForExercise",
                {exerciseId: $(".exerciseSelect").val()},
                function (data, status) {
                    $(".resultsDataBlock").show();
                    if (data[0] == "compile") {
                        alert("in compile");
                        $(".radioCompile").click();
                        $(".classesCompile").val(data[1]);
                        $(".methodsCompile").val(data[2]);
                        $(".fieldsCompile").val(data[3]);
                        $(".resultOutput").val("");
                    } else {
                        alert("in output");
                        $(".radioOutput").click();
                        $(".resultOutput").val(data[1]);
                        $(".classesCompile").val("");
                        $(".methodsCompile").val("");
                        $(".fieldsCompile").val("");
                    }
                });
        } else {
            $(".resultsDataBlock").hide();
            $(".resultCompileWrapper").hide();
            $(".resultOutputWrapper").hide();
        }
    });

    $(".addOrUpdateCourse").click(function () {
        $.get("/addOrUpdateCourse",
            {
                courseId: $(".coursesSelect").val() == null ? -1 : $(".coursesSelect").val(),
                courseName: $(".courseTextField").val()
            },
            function (data, status) {
                $(".coursesSelect").html(data);
                $(".coursesSelect").show();
                $(".categoriesDataBlock").show();
                $(".courseTextField").val("")
            });
    });

    $(".addOrUpdateCategory").click(function () {
        $.get("/addOrUpdateCategory",
            {
                courseId: $(".coursesSelect").val(),
                categoryId: $(".categoriesSelect").val() == null ? -1 : $(".categoriesSelect").val(),
                categoryName: $(".categoryTextField").val()
            },
            function (data, status) {
                $(".categoriesSelect").html(data);
                $(".categoriesSelect").show();
                $(".lessonsDataBlock").show();
                $(".categoryTextField").val("")
            });
    });

    $(".addOrUpdateLesson").click(function () {
        $.get("/addOrUpdateLesson",
            {
                categoryId: $(".categoriesSelect").val(),
                lessonId: $(".lessonsSelect").val() == null ? -1 : $(".lessonsSelect").val(),
                lessonName: $(".lessonTextField").val()
            },
            function (data, status) {
                $(".lessonsSelect").html(data);
                $(".lessonsSelect").show();
                $(".exercisesDataBlock").show()
                $(".lessonTextField").val("")
            });
    });

    $(".addExercise").click(function () {
        $.get("/addExercise",
            {
                lessonId: $(".lessonsSelect").val()
            },
            function (data, status) {
                $(".exerciseSelect").html(data);
                $(".exerciseSelect").show();
                $(".resultsDataBlock").show()
                $(".radioOutput").click();
            });
    });

    $(".addOrUpdateResultOutput").click(function () {
        $.get("/addOrUpdateResultOutput",
            {
                exerciseId: $(".exerciseSelect").val(),
                output: $(".resultOutput").val()
            },
            function (data, status) {
                $(".resultOutput").val(data);
                $(".classesCompile").val("");
                $(".methodsCompile").val("")
                $(".fieldsCompile").val("")
            });
    });

    $(".addOrUpdateResultCompile").click(function () {
        $.get("/addOrUpdateResultCompile",
            {
                exerciseId: $(".exerciseSelect").val(),
                classes: $(".classesCompile").val(),
                methods: $(".methodsCompile").val(),
                fields: $(".fieldsCompile").val()
            },
            function (data, status) {
                $(".classesCompile").val(data[0]);
                $(".methodsCompile").val(data[1]);
                $(".fieldsCompile").val(data[2]);
                $(".resultOutput").val("");
            });
    });
});