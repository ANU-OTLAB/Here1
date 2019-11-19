package com.otlab.here;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;

public class ChooseAlarmActivity extends AppCompatActivity {
    private MediaPlayer myAudio;
    private String path;
    private String alarmName;
    private Button selectBtn;
    private Button cancelBtn;
    private TextView alarmPath;
    private Intent intent;
    private SharedPreferences appData;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_alarm);

        intent = getIntent();
        path = intent.getStringExtra("alarmPath");
        alarmName = intent.getStringExtra("alarmName");
        alarmPath = findViewById(R.id.alarmTxt);
        alarmPath.setText(alarmName);

        playTest();
        setListener();
    }

    private void setListener(){
        selectBtn = findViewById(R.id.alarmSelect);
        selectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                appData = getSharedPreferences("appData", MODE_PRIVATE);
                editor = appData.edit();
                editor.putString("alarmPath", path);
                editor.apply();
                stopTest();
                finish();
            }
        });
        cancelBtn = findViewById(R.id.alarmCancel);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopTest();
                finish();
            }
        });
    }
    private void playTest(){
        //샘플반복재생
        myAudio = new MediaPlayer();
        try {
            myAudio.setDataSource(path);
            myAudio.setLooping(true);
            myAudio.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        myAudio.start();
    }
    private void stopTest(){
        //샘플재생멈춤
        if(myAudio.isPlaying()){
            myAudio.stop();
        }
    }
}
