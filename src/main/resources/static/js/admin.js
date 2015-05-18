$(document).ready(function () {
    $(".htmlResult").hide();
    $(".editLessonHtmlContainer").hide();
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
                $(".coursesSelect").html(data).show();
            }
            $(".courseTextField").val("")
        });

    $(".coursesSelect").change(function () {
        if ($(".coursesSelect").val() != -1) {
            $(".categoriesDataBlock").show();
            $.get("/getCategoryNumbers",
                {courseId: $(".coursesSelect").val()},
                function (data, status) {
                    if (data != "") {
                        $(".categoriesSelect").html(data).show();
                    } else {
                        $(".categoriesSelect").html("").hide();
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
            $(".categoryNumber").text("");
            $(".lessonNumber").text("");
        }
    });

    $(".categoriesSelect").change(function () {
        if ($(".categoriesSelect").val() != -1) {
            $(".lessonsDataBlock").show();
            $.get("/getLessonNumbers",
                {categoryId: $(".categoriesSelect").val()},
                function (data, status) {
                    if (data.length == 2) {
                        $(".lessonsSelect").html(data[1]).show();
                    } else {
                        $(".lessonsSelect").html("").hide();
                    }
                    $(".categoryNumber").text(data[0]);
                    $(".lessonNumber").text("");

                });
        } else {
            $(".lessonsDataBlock").hide();
            $(".exercisesDataBlock").hide();
            $(".resultsDataBlock").hide();

            $(".lessonsSelect").hide();
            $(".exerciseSelect").hide();

            $(".resultCompileWrapper").hide();
            $(".resultOutputWrapper").hide();
            $(".categoryNumber").text("");
            $(".lessonNumber").text("");
        }
    });

    $(".lessonsSelect").change(function () {
        if ($(".lessonsSelect").val() != -1) {
            $(".exercisesDataBlock").show();
            $.get("/getExerciseNumbers",
                {lessonId: $(".lessonsSelect").val()},
                function (data, status) {
                    if (data.length == 3) {
                        $(".exerciseSelect").html(data[1]).show();
                        $(".htmlEditorTextArea").val(data[2]).show();
                    } else if (data.length == 2) {
                        $(".exerciseSelect").html("").hide();
                        $(".htmlEditorTextArea").val(data[1]).show();
                    } else {
                        $(".exerciseSelect").html("").hide();
                        $(".htmlEditorTextArea").val("");
                    }
                    $(".lessonNumber").text(data[0]);
                });
        } else {
            $(".exercisesDataBlock").hide();
            $(".resultsDataBlock").hide();
            $(".exerciseSelect").hide();

            $(".resultCompileWrapper").hide();
            $(".resultOutputWrapper").hide();
            $(".lessonNumber").text("");
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
                        $(".radioCompile").click();
                        $(".classesCompile").val(data[1]);
                        $(".methodsCompile").val(data[2]);
                        $(".fieldsCompile").val(data[3]);
                        $(".resultOutput").val("");
                    } else {
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
        if ($(".courseTextField").val().length != 0) {
            $(".courseTextField").removeClass('emptyField');
            $.get("/addOrUpdateCourse",
                {
                    courseId: $(".coursesSelect").val() == null ? -1 : $(".coursesSelect").val(),
                    courseName: $(".courseTextField").val()
                },
                function (data, status) {
                    $(".coursesSelect").html(data).show();
                    $(".categoriesDataBlock").show();
                    $(".courseTextField").val("")
                });
        } else {
            $(".courseTextField").addClass('emptyField');
        }
    });

    $(".addOrUpdateCategory").click(function () {
        if ($(".categoryTextField").val().length != 0 || $(".categoryTextArea").val().length != 0) {
            $(".categoryTextField").removeClass('emptyField');
            $.get("/addOrUpdateCategory",
                {
                    courseId: $(".coursesSelect").val(),
                    categoryId: $(".categoriesSelect").val() == null ? -1 : $(".categoriesSelect").val(),
                    categoryName: $(".categoryTextField").val(),
                    categoryNick: $(".categoryNickTextField").val(),
                    categoryNumber: $(".categoryNumberTextField").val(),
                    categoryNames: $(".categoryTextArea").val()
                },
                function (data, status) {
                    $(".categoriesSelect").html(data).show();
                    $(".lessonsDataBlock").show();
                    $(".categoryTextField").val("")
                    $(".categoryTextArea").val("")
                    $(".categoryNumberTextField").val("")
                });
        } else {
            $(".categoryTextField").addClass('emptyField');
        }
    });

    $(".addOrUpdateLesson").click(function () {
        if ($(".lessonTextField").val().length != 0 || $(".lessonTextArea").val().length != 0) {
            $(".lessonTextField").removeClass('emptyField');
            $.get("/addOrUpdateLesson",
                {
                    categoryId: $(".categoriesSelect").val(),
                    lessonId: $(".lessonsSelect").val() == null ? -1 : $(".lessonsSelect").val(),
                    lessonName: $(".lessonTextField").val(),
                    lessonNick: $(".lessonNickTextField").val(),
                    lessonNumber: $(".lessonNumberTextField").val(),
                    lessonNames: $(".lessonTextArea").val()
                },
                function (data, status) {
                    $(".lessonsSelect").html(data).show();
                    $(".exercisesDataBlock").show();
                    $(".lessonTextField").val("")
                    $(".lessonTextArea").val("")
                    $(".lessonNumberTextField").val("")
                });
        } else {
            $(".lessonTextField").addClass('emptyField');
        }
    });

    $(".addExercise").click(function () {
        $.get("/addExercise",
            {
                lessonId: $(".lessonsSelect").val()
            },
            function (data, status) {
                $(".exerciseSelect").html(data).show();
                $(".resultsDataBlock").show();
                $(".radioOutput").click();
                $(".resultOutput").val("")
                $(".classesCompile").val("");
                $(".methodsCompile").val("");
                $(".fieldsCompile").val("")
            });
    });

    $(".addOrUpdateResultOutput").click(function () {
        if ($(".resultOutput").val().length != 0) {
            $(".resultOutput").removeClass('emptyField');
            $.get("/addOrUpdateResultOutput",
                {
                    exerciseId: $(".exerciseSelect").val(),
                    output: $(".resultOutput").val()
                },
                function (data, status) {
                    $(".resultOutput").val(data);
                    $(".classesCompile").val("");
                    $(".methodsCompile").val("");
                    $(".fieldsCompile").val("")
                });
        } else {
            $(".resultOutput").addClass('emptyField');
        }
    });

    $(".addOrUpdateResultCompile").click(function () {
        if ($(".classesCompile").val().length != 0) {
            $(".classesCompile").removeClass('emptyField');
            $(".methodsCompile").removeClass('emptyField');
            $(".fieldsCompile").removeClass('emptyField');
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
        } else {
            $(".classesCompile").removeClass('emptyField');
        }
    });

    $(".addUser").click(function () {
        if ($(".userNameTextField").val().length != 0 && $(".passwordTextField").val().length != 0 && $(".emailTextField").val().length != 0) {
            $(".userNameTextField").removeClass('emptyField');
            $(".passwordTextField").removeClass('emptyField');
            $(".emailTextField").removeClass('emptyField');
            $.get("/addUser",
                {
                    userName: $(".userNameTextField").val(),
                    password: $(".passwordTextField").val(),
                    email: $(".emailTextField").val(),
                    roleId: $(".selectRole").val()
                },
                function (data, status) {
                    userName: $(".userNameTextField").val("");
                    password: $(".passwordTextField").val("");
                    password: $(".emailTextField").val("");
                });
        } else {
            $(".userNameTextField").length == 0 ? $(".userNameTextField").addClass('emptyField') : $(".userNameTextField").removeClass('emptyField');
            $(".passwordTextField").length == 0 ? $(".passwordTextField").addClass('emptyField') : $(".passwordTextField").removeClass('emptyField');
            $(".emailTextField").length == 0 ? $(".emailTextField").addClass('emptyField') : $(".emailTextField").removeClass('emptyField');
        }
    });

    $(".createFiles").click(function () {
        $.get("/createFiles",
            {
                courseId: $(".coursesSelect").val()
            },
            function (data, status) {
                userName: $(".userNameTextField").val("");
                password: $(".passwordTextField").val("");
            });
    });

    $(".showHideUsers").click(function () {
        $(".adminUserWrapper").toggle();
    });

    $(".showHideContent").click(function () {
        $(".adminContentWrapper").toggle();
    });

    $(".showLesson").click(function () {
        $(".lessonContainer").show();
        $(".exerciseContainer").hide();
    });

    $(".showExercise").click(function () {
        $(".exerciseContainer").show();
        $(".lessonContainer").hide();
    });
    $('.headerButton').click(function () {
        $('.htmlEditorTextArea')
            .selection('insert', {text: "<div class='lessonHeaderWrapper'>", mode: 'before'})
            .selection('insert', {text: '</div>', mode: 'after'});
    });
    $('.lTextButton').click(function () {
        $('.htmlEditorTextArea')
            .selection('insert', {text: "<div class='lessonTextWrapper'>", mode: 'before'})
            .selection('insert', {text: '</div>', mode: 'after'});
    });

    $('.oListButton').click(function () {
        var list = "";
        var selectedText = $('.htmlEditorTextArea').selection().split('\n');

        list += "<div class='orderedListWrapper'>";
        list += "<ol>";
        $.each(selectedText, function (index, value) {
            list += "<li>" + value + "</option>";
        });
        list += "</ol>";
        list += "</div>";
        $('.htmlEditorTextArea').selection('replace', {text: list});
    });
    $('.uoListButton').click(function () {
        var list = "";
        var selectedText = $('.htmlEditorTextArea').selection().split('\n');

        list += "<div class='unorderedListWrapper'>";
        list += "<ul>";
        $.each(selectedText, function (index, value) {
            list += "<li>" + value + "</option>";
        });
        list += "</ul>";
        list += "</div>";
        $('.htmlEditorTextArea').selection('replace', {text: list});
    });
    $('.boldButton').click(function () {
        $('.htmlEditorTextArea')
            .selection('insert', {text: "<span class='boldTextWrapper'>", mode: 'before'})
            .selection('insert', {text: '</span>', mode: 'after'});
    });
    $('.italicButton').click(function () {
        $('.htmlEditorTextArea')
            .selection('insert', {text: "<span class='italicTextWrapper'>", mode: 'before'})
            .selection('insert', {text: '</span>', mode: 'after'});
    });
    $('.underlButton').click(function () {
        $('.htmlEditorTextArea')
            .selection('insert', {text: "<span class='underlinedTextWrapper'>", mode: 'before'})
            .selection('insert', {text: '</span>', mode: 'after'});
    });
    $('.codeButton').click(function () {
        $('.htmlEditorTextArea')
            .selection('insert', {text: "<div class='codeSnippetWrapper'><pre>", mode: 'before'})
            .selection('insert', {text: '</pre></div>', mode: 'after'});
    });
    $('.paragraphButton').click(function () {
        $('.htmlEditorTextArea')
            .selection('insert', {text: "<div class='paragraphWrapper'><p>", mode: 'before'})
            .selection('insert', {text: '</p></div>', mode: 'after'});
    });
    $('.imageButton').click(function () {
        $('.htmlEditorTextArea')
            .selection('insert', {text: "<div class='imageWrapper'>", mode: 'before'})
            .selection('insert', {text: '</div>', mode: 'after'});
    });
    $('.visualizeButton').click(function () {
        $(".htmlResult").html($(".htmlEditorTextArea").val());
        $(".htmlEditorTextArea").toggle();
        $(".htmlResult").toggle();
    });
    $('.addLessonHtmlButton').click(function () {
        $(".editLessonHtmlContainer").toggle();
    });

    $(".saveToLessonFileButton").click(function () {
        $.get("/saveToLessonFileButton",
            {
                lessonId: $(".lessonsSelect").val(),
                lessonHtml: $(".htmlEditorTextArea").val()
            },
            function (data, status) {
                $(".htmlEditorTextArea").val("");
                $(".htmlResult").html("");
            });
    });

});