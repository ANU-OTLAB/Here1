package com.otlab.here;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

public class MapActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_map);
        Toast.makeText(getApplicationContext(), "ㅅㄷㄴㅅ", Toast.LENGTH_SHORT).show();
    }
}
