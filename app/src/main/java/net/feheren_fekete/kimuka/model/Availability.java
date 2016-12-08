package net.feheren_fekete.kimuka.model;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.List;

public class Availability {

    public static final int ACTIVITY_TOP_ROPE = 0;
    public static final int ACTIVITY_LEAD = 1;
    public static final int ACTIVITY_TRAD = 2;
    public static final int ACTIVITY_BOULDERING = 3;

    public static final int NEED_PARTNER_UNDEFINED = -1;
    public static final int NEED_PARTNER_YES = 0;
    public static final int NEED_PARTNER_NO = 1;

    public static final int IF_NO_PARTNER_UNDEFINED = -1;
    public static final int IF_NO_PARTNER_I_GO_ANYWAY = 0;
    public static final int IF_NO_PARTNER_I_DONT_GO = 1;
    public static final int IF_NO_PARTNER_NOT_DECIDED_YET = 2;

    @Exclude
    private String key;

    private String userKey;
    private String userName;
    private boolean isHostUser;
    private double locationLatitude;
    private double locationLongitude;
    private String locationName;
    private String locationAddress;
    private long startTime; // ms since Epoch.
    private long endTime; // ms since Epoch.
    private String activity; // Comma separated list of activities (integers).
    private int needPartner;
    private int ifNoPartner;
    private String sharedEquipment; // Comma separated list of equipments (integers).
    private int canBelay;
    private String grades; // Comma separated list of grades (integers).
    private String note;
    private List<String> joinedAvailabilityKeys = new ArrayList<>();

    @Exclude
    public String getKey() {
        return key;
    }

    @Exclude
    public void setKey(String key) {
        this.key = key;
    }

    public String getUserKey() {
        return userKey;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public boolean isHostUser() {
        return isHostUser;
    }

    public void setHostUser(boolean hostUser) {
        isHostUser = hostUser;
    }

    public double getLocationLatitude() {
        return locationLatitude;
    }

    public void setLocationLatitude(double locationLatitude) {
        this.locationLatitude = locationLatitude;
    }

    public double getLocationLongitude() {
        return locationLongitude;
    }

    public void setLocationLongitude(double locationLongitude) {
        this.locationLongitude = locationLongitude;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getLocationAddress() {
        return locationAddress;
    }

    public void setLocationAddress(String locationAddress) {
        this.locationAddress = locationAddress;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public int getNeedPartner() {
        return needPartner;
    }

    public void setNeedPartner(int needPartner) {
        this.needPartner = needPartner;
    }

    public int getIfNoPartner() {
        return ifNoPartner;
    }

    public void setIfNoPartner(int ifNoPartner) {
        this.ifNoPartner = ifNoPartner;
    }

    public String getSharedEquipment() {
        return sharedEquipment;
    }

    public void setSharedEquipment(String sharedEquipment) {
        this.sharedEquipment = sharedEquipment;
    }

    public int getCanBelay() {
        return canBelay;
    }

    public void setCanBelay(int canBelay) {
        this.canBelay = canBelay;
    }

    public String getGrades() {
        return grades;
    }

    public void setGrades(String grades) {
        this.grades = grades;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public List<String> getJoinedAvailabilityKeys() {
        return joinedAvailabilityKeys;
    }

    public void setJoinedAvailabilityKeys(List<String> joinedAvailabilityKeys) {
        this.joinedAvailabilityKeys = joinedAvailabilityKeys;
    }
}
