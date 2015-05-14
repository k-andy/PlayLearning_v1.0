package com.playlearning.model;

import javax.persistence.*;
import java.util.Collection;

/**
 * Created by andy on 5/9/15.
 */
@Entity
@Table(name = "lessons", schema = "", catalog = "playLearning")
public class Lesson {
    private int id;
    private int number;
    private String html;
    private String name;
    private Collection<Exercise> exercisesById;
    private Category categoriesByCategoryId;

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
    @Column(name = "name", nullable = false, insertable = true, updatable = true)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
    @Column(name = "html", nullable = false, insertable = true, updatable = true)
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

        Lesson that = (Lesson) o;

        if (id != that.id) return false;
        if (number != that.number) return false;
        if (html != null ? !html.equals(that.html) : that.html != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + number;
        result = 31 * result + (html != null ? html.hashCode() : 0);
        return result;
    }

    @OneToMany(mappedBy = "lessonsByLessonId")
    public Collection<Exercise> getExercisesById() {
        return exercisesById;
    }

    public void setExercisesById(Collection<Exercise> exercisesById) {
        this.exercisesById = exercisesById;
    }

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id", nullable = false)
    public Category getCategoriesByCategoryId() {
        return categoriesByCategoryId;
    }

    public void setCategoriesByCategoryId(Category categoriesByCategoryId) {
        this.categoriesByCategoryId = categoriesByCategoryId;
    }
}
