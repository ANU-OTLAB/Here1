package com.otlab.here;

import android.net.Uri;

public class AlarmSettingItem {
    private Uri path;
    private String alarmName;

    public AlarmSettingItem(Uri path, String alarmName){
        this.path = path;
        this.alarmName = alarmName;
    }

    public Uri getPath() {
        return path;
    }

    public void setPath(Uri path) {
        this.path = path;
    }

    public String getAlarmName() {
        return alarmName;
    }

    public void setAlarmName(String alarmName) {
        this.alarmName = alarmName;
    }

}
