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
    private int[] gradeNumbers;

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
        this.gradeNumbers = ModelUtils.toIntArray(this.grades);
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getFreeClimbingGrade() {
        return this.gradeNumbers[GRADE_FREE_CLIMBING];
    }

    public int getBoulderingGrade() {
        return this.gradeNumbers[GRADE_BOULDERING];
    }

}
