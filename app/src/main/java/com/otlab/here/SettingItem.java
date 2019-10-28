package com.otlab.here;

public class SettingItem {
    private String SettingName;
    private String Distance;
    private String Destination;
    private String time;

    public String getSettingName() {
        return SettingName;
    }
    public String getDistance() {
        return Distance;
    }

    public String getDestination() {
        return Destination;
    }

    public String getTime() {
        return time;
    }

    public void setDistance(String distance) {
        Distance = distance;
    }
    public void setSettingName(String settingName) {
        SettingName = settingName;
    }

    public void setDestination(String destination) {
        Destination = destination;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public SettingItem(String settingName, String distance, String destination, String time) {
        SettingName = settingName;
        Distance = distance;
        Destination = destination;
        this.time = time;
    }
}
