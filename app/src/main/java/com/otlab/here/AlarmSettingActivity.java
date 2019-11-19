package com.otlab.here;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.List;

public class AlarmSettingActivity extends AppCompatActivity {
    // 알람설정을 넣어야 할 액티비티
    private ListView alarmsListView;
    private List<Uri> alarmPath;
    private ArrayList<String> alarmName;
    private ArrayList<AlarmSettingItem> listItem;
    private AlarmSettingListViewAdapter customAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_setting);
        initList();
        setListener();
    }

    private void initList() {
        alarmsListView = findViewById(R.id.alarmList);
        alarmPath = new ArrayList<android.net.Uri>();
        alarmName = new ArrayList<>();
        listItem = new ArrayList<>();

        alarmPath = loadLocalRingtoneUris();
        alarmName = loadLocalRingtoneName();

        for (int i = 0; i < alarmPath.size(); i++) {
            Uri x = alarmPath.get(i);
            String y = alarmName.get(i);
            listItem.add(new AlarmSettingItem(x,y));
        }
        customAdapter = new AlarmSettingListViewAdapter(this, R.layout.alarm_setting_item, listItem);
        alarmsListView.setAdapter(customAdapter);
    }

    private void setListener() {
        alarmsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String path = listItem.get(position).getPath().toString();
                String name = listItem.get(position).getAlarmName();
                Intent intent = new Intent(getApplicationContext(), ChooseAlarmActivity.class);
                intent.putExtra("alarmName", name);
                intent.putExtra("alarmPath", path);
                startActivity(intent);
            }
        });
    }

    private List<Uri> loadLocalRingtoneUris() {
        //시스템 벨소리(알람) URI 가져오는 함수

        try {
            RingtoneManager ringtoneManager = new RingtoneManager(getApplicationContext());
            ringtoneManager.setType(RingtoneManager.TYPE_RINGTONE);

            Cursor alarmsCursor = ringtoneManager.getCursor();
            int alarmsCount = alarmsCursor.getCount();

            if (alarmsCount == 0 && alarmsCursor.moveToFirst()) {
                alarmsCursor.close();
                return null;
            }
            while (!alarmsCursor.isAfterLast() && alarmsCursor.moveToNext()) {
                int currentPosition = alarmsCursor.getPosition();
                alarmPath.add(ringtoneManager.getRingtoneUri(currentPosition));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return alarmPath;
    }

    private ArrayList<String> loadLocalRingtoneName() {
        //시스템 벨소리(알람) 이름 가져오는 함수

        try {
            RingtoneManager ringtoneManager = new RingtoneManager(getApplicationContext());
            ringtoneManager.setType(RingtoneManager.TYPE_RINGTONE);

            Cursor alarmsCursor = ringtoneManager.getCursor();
            int alarmsCount = alarmsCursor.getCount();

            if (alarmsCount == 0 && alarmsCursor.moveToFirst()) {
                alarmsCursor.close();
                return null;
            }

            while (!alarmsCursor.isAfterLast() && alarmsCursor.moveToNext()) {
                int currentPosition = alarmsCursor.getPosition();
                alarmPath.add(ringtoneManager.getRingtoneUri(currentPosition));
            }

            for (int i = 0; i < alarmPath.size(); i++) {
                String ringToneName = RingtoneManager.getRingtone(getApplicationContext(), alarmPath.get(i)).getTitle(getApplicationContext());
                alarmName.add(ringToneName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return alarmName;
    }
}
