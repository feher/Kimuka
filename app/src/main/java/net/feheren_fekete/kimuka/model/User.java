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
    public List<Integer> grades;
    public String note;

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCanBelay(boolean canBelay) {
        this.canBelay = canBelay;
    }

    public void setGrades(List<Integer> grades) {
        this.grades = grades;
    }

    public void setNote(String note) {
        this.note = note;
    }

}
