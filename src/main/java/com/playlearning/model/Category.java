package com.playlearning.model;

import javax.persistence.*;
import java.util.Collection;

/**
 * Created by andy on 5/9/15.
 */
@Entity
@Table(name = "categories", schema = "", catalog = "playLearning")
public class Category {
    private int id;
    private int number;
    private String name;
    private Course coursesByCourseId;
    private Collection<Lesson> lessonsesById;

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
    @Column(name = "name", nullable = false, insertable = true, updatable = true)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Category that = (Category) o;

        if (id != that.id) return false;
        if (number != that.number) return false;
        if (name != that.name) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + number;
        return result;
    }

    @ManyToOne()
    @JoinColumn(name = "course_id", referencedColumnName = "id", nullable = false)
    public Course getCoursesByCourseId() {
        return coursesByCourseId;
    }

    public void setCoursesByCourseId(Course coursesByCourseId) {
        this.coursesByCourseId = coursesByCourseId;
    }

    @OneToMany(mappedBy = "categoriesByCategoryId")
    public Collection<Lesson> getLessonsesById() {
        return lessonsesById;
    }

    public void setLessonsesById(Collection<Lesson> lessonsesById) {
        this.lessonsesById = lessonsesById;
    }
}
