package net.feheren_fekete.kimuka.model;

import org.json.JSONException;
import org.json.JSONObject;

public class Filter {

    private String userKey;
    private String userName;
    private Double locationLatitude;
    private Double locationLongitude;
    private Long startTime; // ms since Epoch.
    private Long endTime; // ms since Epoch.
    private String activity; // Comma separated list of activities (integers).
    private Integer needPartner;
    private String sharedEquipment; // Comma separated list of equipments (integers).
    private Integer canBelay;

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

    public String toJson() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userKey", userKey);
            jsonObject.put("userName", userName);
            jsonObject.put("locationLatitude", locationLatitude);
            jsonObject.put("locationLongitude", locationLongitude);
            jsonObject.put("startTime", startTime);
            jsonObject.put("endTime", endTime);
            jsonObject.put("activity", activity);
            jsonObject.put("needPartner", needPartner);
            jsonObject.put("sharedEquipment", sharedEquipment);
            jsonObject.put("canBelay", canBelay);
        } catch (JSONException e) {
            // TODO: Handle?
            throw new RuntimeException();
        }
        return jsonObject.toString();
    }

    public void fromJson(JSONObject jsonObject) {
        userKey = jsonObject.has("userKey") ? jsonObject.optString("userKey") : null;
        userName = jsonObject.has("userName") ? jsonObject.optString("userName", userName) : null;
        locationLatitude = jsonObject.has("locationLatitude") ? jsonObject.optDouble("locationLatitude") : null;
        locationLongitude = jsonObject.has("locationLongitude") ? jsonObject.optDouble("locationLongitude", locationLongitude) : null;
        startTime = jsonObject.has("startTime") ? jsonObject.optLong("startTime", startTime) : null;
        endTime = jsonObject.has("endTime") ? jsonObject.optLong("endTime", endTime) : null;
        activity = jsonObject.has("activity") ? jsonObject.optString("activity", activity) : null;
        needPartner = jsonObject.has("needPartner") ? jsonObject.optInt("needPartner", needPartner) : null;
        sharedEquipment = jsonObject.has("sharedEquipment") ? jsonObject.optString("sharedEquipment", sharedEquipment) : null;
        canBelay = jsonObject.has("canBelay") ? jsonObject.optInt("canBelay", canBelay) : null;
    }

}