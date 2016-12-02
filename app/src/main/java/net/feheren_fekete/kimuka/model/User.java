package net.feheren_fekete.kimuka.model;

import com.google.firebase.database.Exclude;

import java.util.List;

public class User {

    public static final int GRADE_FREE_CLIMBING = 0;
    public static final int GRADE_BOULDERING = 1;

    @Exclude
    public String key;

    public String uid;
    public String name;
    public boolean canBelay;
    public String grades; // Comma separated list of integers.
    public String note;

    @Exclude
    private List<Integer> gradeNumbers;

    public void setKey(String key) {
        this.key = key;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCanBelay(boolean canBelay) {
        this.canBelay = canBelay;
    }

    public void setGrades(String grades) {
        this.grades = grades;
        this.gradeNumbers = ModelUtils.toIntList(this.grades);
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Exclude
    public int getFreeClimbingGrade() {
        return this.gradeNumbers.get(GRADE_FREE_CLIMBING);
    }

    @Exclude
    public int getBoulderingGrade() {
        return this.gradeNumbers.get(GRADE_BOULDERING);
    }

}
