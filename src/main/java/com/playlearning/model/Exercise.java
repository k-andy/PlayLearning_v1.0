package com.playlearning.model;

import javax.persistence.*;
import java.util.Collection;

/**
 * Created by andy on 5/11/15.
 */
@Entity
@Table(name = "exercises", schema = "", catalog = "playLearning")
public class Exercise {
    private int id;
    private int number;
    private String type;
    private String html;
    private ClasZ classesById;
    private Lesson lessonsByLessonId;
    private Field fieldsesById;
    private Method methodsesById;
    private Result resultsesById;

    @Id
    @Column(name = "id", nullable = false, insertable = true, updatable = true)
    @GeneratedValue(strategy = GenerationType.AUTO)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "number", nullable = false, insertable = true, updatable = true)
    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    @Basic
    @Column(name = "type", nullable = false, insertable = true, updatable = true, length = 30)
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Basic
    @Column(name = "html", nullable = false, insertable = true, updatable = true, length = 30)
    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Exercise that = (Exercise) o;

        if (id != that.id) return false;
        if (number != that.number) return false;
        if (type != null ? !type.equals(that.type) : that.type != null) return false;
        if (html != null ? !html.equals(that.html) : that.html != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + number;
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (html != null ? html.hashCode() : 0);
        return result;
    }

    @OneToOne(mappedBy = "exercisesByExerciseId")
    public ClasZ getClassesById() {
        return classesById;
    }

    public void setClassesById(ClasZ classesById) {
        this.classesById = classesById;
    }

    @ManyToOne
    @JoinColumn(name = "lesson_id", referencedColumnName = "id")
    public Lesson getLessonsByLessonId() {
        return lessonsByLessonId;
    }

    public void setLessonsByLessonId(Lesson lessonsByLessonId) {
        this.lessonsByLessonId = lessonsByLessonId;
    }

    @OneToOne(mappedBy = "exercisesByExerciseId")
    public Field getFieldsesById() {
        return fieldsesById;
    }

    public void setFieldsesById(Field fieldsesById) {
        this.fieldsesById = fieldsesById;
    }

    @OneToOne(mappedBy = "exercisesByExerciseId")
    public Method getMethodsesById() {
        return methodsesById;
    }

    public void setMethodsesById(Method methodsesById) {
        this.methodsesById = methodsesById;
    }

    @OneToOne(mappedBy = "exercisesByExerciseId")
    public Result getResultsesById() {
        return resultsesById;
    }

    public void setResultsesById(Result resultsesById) {
        this.resultsesById = resultsesById;
    }
}
