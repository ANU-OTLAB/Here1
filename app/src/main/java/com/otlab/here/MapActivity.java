package com.otlab.here;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Toast;

import net.daum.mf.map.api.MapView;

public class MapActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_map);
        Toast.makeText(getApplicationContext(), "ㅅㄷㄴㅅ", Toast.LENGTH_SHORT).show();

        try {

            MapView mapView = new MapView(this);

            ViewGroup mapViewContainer = findViewById(R.id.map_view);
            mapViewContainer.addView(mapView);

        } catch (Exception e) {
            this.recreate();
            Log.d("??", e.getMessage());
            Toast.makeText(getApplicationContext(), "GPS 연결에 실패하였습니다.", Toast.LENGTH_LONG).show();
        }
    }
}
