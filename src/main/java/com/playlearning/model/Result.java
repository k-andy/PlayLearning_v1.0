package com.playlearning.model;

import javax.persistence.*;

/**
 * Created by andy on 5/11/15.
 */
@Entity
@Table(name = "results", schema = "", catalog = "playLearning")
public class Result {
    private int id;
    private String output;
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
    @Column(name = "output", nullable = false, insertable = true, updatable = true, length = 65535)
    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Result that = (Result) o;

        if (id != that.id) return false;
        if (output != null ? !output.equals(that.output) : that.output != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (output != null ? output.hashCode() : 0);
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
