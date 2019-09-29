package com.otlab.here;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toast.makeText(getApplicationContext(), "ㅅㄷㄴㅅ", Toast.LENGTH_SHORT).show();
    }

    public void echo(View view) {
        Toast.makeText(this, "안녕", Toast.LENGTH_LONG).show();
    }

    public void showMap(View view) {
        Intent intent = new Intent(getApplication(), MapActivity.class);
        startActivity(intent);
    }

    public void goSetting(View view) {
        Intent intent = new Intent(getApplication(), SettingActivity.class);
        startActivity(intent);
    }

    public void goOption(View view) {
        Intent intent = new Intent(getApplication(), OptionActivity.class);
        startActivity(intent);
    }
}
