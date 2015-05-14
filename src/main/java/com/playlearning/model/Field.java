package com.playlearning.model;

import javax.persistence.*;

/**
 * Created by andy on 5/11/15.
 */
@Entity
@Table(name = "fields", schema = "", catalog = "playLearning")
public class Field {
    private int id;
    private String names;
    private Exercise exercisesByExerciseId;

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
    @Column(name = "names", nullable = false, insertable = true, updatable = true, length = 60)
    public String getNames() {
        return names;
    }

    public void setNames(String name) {
        this.names = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Field that = (Field) o;

        if (id != that.id) return false;
        if (names != null ? !names.equals(that.names) : that.names != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (names != null ? names.hashCode() : 0);
        return result;
    }

    @OneToOne
    @JoinColumn(name = "exercise_id", referencedColumnName = "id")
    public Exercise getExercisesByExerciseId() {
        return exercisesByExerciseId;
    }

    public void setExercisesByExerciseId(Exercise exercisesByExerciseId) {
        this.exercisesByExerciseId = exercisesByExerciseId;
    }
}
