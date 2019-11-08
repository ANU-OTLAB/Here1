package com.otlab.here;

public class SettingItem {

    private String settingName;
    private String distance;
    private String destination;
    private String time;
    public SettingItem(String settingName, String distance, String destination, String time) {
        this.settingName = settingName;
        this.distance = distance;
        this.destination = destination;
        this.time = time;
    }

    public String getSettingName() {
        return settingName;
    }

    public void setSettingName(String settingName) { this.settingName = settingName; }

    public String getDistance() { return distance; }

    public void setDistance(String distance) { this.distance = distance; }

    public String getDestination() { return destination; }

    public void setDestination(String destination) { this.destination = destination; }

    public String getTime() { return time; }

    public void setTime(String time) {
        this.time = time;
    }

    public enum ServiceType {
        CREATE, UPDATE, DELETE
    }
}
