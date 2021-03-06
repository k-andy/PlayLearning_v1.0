package com.playlearning.model;

import javax.persistence.*;
import java.util.Collection;

/**
 * Created by andy on 5/9/15.
 */
@Entity
@Table(name = "courses", schema = "", catalog = "playLearning")
public class Course {
    private int id;
    private String name;
    private String nick;
    private Collection<Category> categoriesById;

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
    @Column(name = "nick", nullable = false, insertable = true, updatable = true, length = 30)
    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    @Basic
    @Column(name = "name", nullable = false, insertable = true, updatable = true, length = 60)
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

        Course that = (Course) o;

        if (id != that.id) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @OneToMany(mappedBy = "coursesByCourseId", fetch = FetchType.EAGER)
    public Collection<Category> getCategoriesById() {
        return categoriesById;
    }

    public void setCategoriesById(Collection<Category> categoriesById) {
        this.categoriesById = categoriesById;
    }
}
