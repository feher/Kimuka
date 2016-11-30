package net.feheren_fekete.kimuka.model;

import java.util.ArrayList;
import java.util.List;

public class Availability {

    public static final int ACTIVITY_FREE_TOP_ROPE = 1 << 0;
    public static final int ACTIVITY_FREE_LEAD = 1 << 1;
    public static final int ACTIVITY_FREE_TRAD = 1 << 2;
    public static final int ACTIVITY_BOULDERING = 1 << 3;

    public static final int IF_NO_PARTNER_I_GO_ANYWAY = 0;
    public static final int IF_NO_PARTNER_I_DONT_GO = 1;
    public static final int IF_NO_PARTNER_NOT_DECIDED_YET = 2;

    public String userKey;
    public String userName;
    public double locationLatitude;
    public double locationLongitude;
    public String locationName;
    public String locationAddress;
    public long startTime; // ms since Epoch.
    public long endTime; // ms since Epoch.
    public long activity; // Bitwise combination of ACTIVITY_*.
    public List<String> sharedEquipment;
    public boolean canBelay;
    public List<Integer> grades;
    public String note;
    public int ifNoPartner;
    public List<String> joinedAvailabilityKeys = new ArrayList<>();

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setLocationLatitude(double locationLatitude) {
        this.locationLatitude = locationLatitude;
    }

    public void setLocationLongitude(double locationLongitude) {
        this.locationLongitude = locationLongitude;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public void setLocationAddress(String locationAddress) {
        this.locationAddress = locationAddress;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public void setActivity(long activity) {
        this.activity = activity;
    }

    public void setSharedEquipment(List<String> sharedEquipment) {
        this.sharedEquipment = sharedEquipment;
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

    public void setIfNoPartner(int ifNoPartner) {
        this.ifNoPartner = ifNoPartner;
    }

    public void setJoinedAvailabilityKeys(List<String> joinedAvailabilityKeys) {
        this.joinedAvailabilityKeys = joinedAvailabilityKeys;
    }

}
