package com.otlab.here;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
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

    public void showMap(View view) {
        final LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            //GPS 설정화면으로 이동
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            startActivity(intent);
        } else {
            Intent intent = new Intent(getApplication(), MapActivity.class);
            startActivity(intent);
        }
    }

    public void goSetting(View view) {
        Intent intent = new Intent(getApplication(), SettingActivity.class);
        startActivity(intent);
    }

    public void goOption(View view) {
        Intent intent = new Intent(getApplication(), OptionActivity.class);
        startActivity(intent);
    }
/*
    try {
        PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
        for (Signature signature : info.signatures) {
            MessageDigest md;
            md = MessageDigest.getInstance("SHA");
            md.update(signature.toByteArray());
            String key = new String(Base64.encode(md.digest(), 0));
            Log.d("Hash key:", "!!!!!!!" + key + "!!!!!!");
        }
    } catch (Exception e) {
        Log.e("name not found", e.toString());
    }*/

}
