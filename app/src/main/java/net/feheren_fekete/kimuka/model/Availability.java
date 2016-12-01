package net.feheren_fekete.kimuka.model;

import java.util.ArrayList;
import java.util.List;

public class Availability {

    public static final int ACTIVITY_TOP_ROPE = 0;
    public static final int ACTIVITY_LEAD = 1;
    public static final int ACTIVITY_TRAD = 2;
    public static final int ACTIVITY_BOULDERING = 3;

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
    public String activity; // Comma separated list of activities (integers).
    public boolean doesNeedPartner;
    public int ifNoPartner;
    public String sharedEquipment; // Comma separated list of equipments (integers).
    public boolean canBelay;
    public String grades; // Comma separated list of grades (integers).
    public String note;
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

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public void setDoesNeedPartner(boolean doesNeedPartner) {
        this.doesNeedPartner = doesNeedPartner;
    }

    public void setSharedEquipment(String sharedEquipment) {
        this.sharedEquipment = sharedEquipment;
    }

    public void setCanBelay(boolean canBelay) {
        this.canBelay = canBelay;
    }

    public void setGrades(String grades) {
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
