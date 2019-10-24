package com.otlab.here;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.security.MessageDigest;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toast.makeText(getApplicationContext(), "ㅅㄷㄴㅅ", Toast.LENGTH_SHORT).show();

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
        }

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
