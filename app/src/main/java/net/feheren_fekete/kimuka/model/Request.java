package net.feheren_fekete.kimuka.model;

import com.google.firebase.database.Exclude;

public class Request {

    @Exclude
    private String key;

    private String senderKey;
    private String senderName;
    private String receiverKey;
    private String receiverName;
    private long startTime; // ms since Epoch.
    private long endTime; // ms since Epoch.
    private String activity; // Comma separated list of activities (integers).
    private String sharedEquipment; // Comma separated list of equipments (integers).
    private int canBelay;
    private String note;

    @Exclude
    public String getKey() {
        return key;
    }

    @Exclude
    public void setKey(String key) {
        this.key = key;
    }

    public String getSenderKey() {
        return senderKey;
    }

    public void setSenderKey(String senderKey) {
        this.senderKey = senderKey;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getReceiverKey() {
        return receiverKey;
    }

    public void setReceiverKey(String receiverKey) {
        this.receiverKey = receiverKey;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
