package net.feheren_fekete.kimuka.model;

import com.google.firebase.database.Exclude;

import java.util.List;

public class User {

    public static final int GRADE_FREE_CLIMBING = 0;
    public static final int GRADE_BOULDERING = 1;

    @Exclude
    private String key;

    private String uid;
    private String name;
    private boolean canBelay;
    private String grades; // Comma separated list of integers.
    private String note;

    @Exclude
    private List<Integer> gradeNumbers;

    public User() {
    }

    @Exclude
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
        this.gradeNumbers = null;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Exclude
    public String getKey() {
        return key;
    }

    public String getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public boolean getCanBelay() {
        return canBelay;
    }

    public String getGrades() {
        return grades;
    }

    public String getNote() {
        return note;
    }

    @Exclude
    public int getFreeClimbingGrade() {
        if (this.gradeNumbers == null) {
            this.gradeNumbers = ModelUtils.toIntList(this.grades);
        }
        return this.gradeNumbers.get(GRADE_FREE_CLIMBING);
    }

    @Exclude
    public int getBoulderingGrade() {
        if (this.gradeNumbers == null) {
            this.gradeNumbers = ModelUtils.toIntList(this.grades);
        }
        return this.gradeNumbers.get(GRADE_BOULDERING);
    }

}
