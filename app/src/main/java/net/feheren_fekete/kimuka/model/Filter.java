package net.feheren_fekete.kimuka.model;

import org.json.JSONException;
import org.json.JSONObject;

public class Filter {

    private String name;
    private String userKey;
    private String userName;
    private Double locationLatitude;
    private Double locationLongitude;
    private String locationName;
    private String locationAddress;
    private Long startTime; // ms since Epoch.
    private Long endTime; // ms since Epoch.
    private String activity; // Comma separated list of activities (integers).
    private Integer needPartner;
    private String sharedEquipment; // Comma separated list of equipments (integers).
    private Integer canBelay;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Double getLocationLatitude() {
        return locationLatitude;
    }

    public void setLocationLatitude(Double locationLatitude) {
        this.locationLatitude = locationLatitude;
    }

    public Double getLocationLongitude() {
        return locationLongitude;
    }

    public void setLocationLongitude(Double locationLongitude) {
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

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public Integer getNeedPartner() {
        return needPartner;
    }

    public void setNeedPartner(Integer needPartner) {
        this.needPartner = needPartner;
    }

    public String getSharedEquipment() {
        return sharedEquipment;
    }

    public void setSharedEquipment(String sharedEquipment) {
        this.sharedEquipment = sharedEquipment;
    }

    public Integer getCanBelay() {
        return canBelay;
    }

    public void setCanBelay(Integer canBelay) {
        this.canBelay = canBelay;
    }

    public String toJson() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userKey", userKey);
            jsonObject.put("userName", userName);
            jsonObject.put("locationLatitude", locationLatitude);
            jsonObject.put("locationLongitude", locationLongitude);
            jsonObject.put("locationName", locationName);
            jsonObject.put("locationAddress", locationAddress);
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
        userName = jsonObject.has("userName") ? jsonObject.optString("userName") : null;
        locationLatitude = jsonObject.has("locationLatitude") ? jsonObject.optDouble("locationLatitude") : null;
        locationLongitude = jsonObject.has("locationLongitude") ? jsonObject.optDouble("locationLongitude") : null;
        locationName = jsonObject.has("locationName") ? jsonObject.optString("locationName") : null;
        locationAddress = jsonObject.has("locationAddress") ? jsonObject.optString("locationAddress") : null;
        startTime = jsonObject.has("startTime") ? jsonObject.optLong("startTime") : null;
        endTime = jsonObject.has("endTime") ? jsonObject.optLong("endTime") : null;
        activity = jsonObject.has("activity") ? jsonObject.optString("activity") : null;
        needPartner = jsonObject.has("needPartner") ? jsonObject.optInt("needPartner") : null;
        sharedEquipment = jsonObject.has("sharedEquipment") ? jsonObject.optString("sharedEquipment") : null;
        canBelay = jsonObject.has("canBelay") ? jsonObject.optInt("canBelay") : null;
    }

}