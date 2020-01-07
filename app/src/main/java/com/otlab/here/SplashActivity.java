package com.otlab.here;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

public class SplashActivity extends Activity {

    private SharedPreferences hereData;

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_splash);

        hereData = getSharedPreferences(String.valueOf(R.string.appData), MODE_PRIVATE);
        boolean autoLogin = hereData.getBoolean(String.valueOf(R.string.autoLogin), false);
        String name = hereData.getString(String.valueOf(R.string.name), "");


        new Handler().postDelayed(() -> {
            if (autoLogin) {
                Toast.makeText(getApplicationContext(), name + "님 환영합니다.", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplication(), MainActivity.class));
                finish();
            } else {
                startActivity(new Intent(getApplication(), LoginActivity.class));
                finish();
            }
        }, 3000);

    }

    //뒤로가기 버튼 방지
    @Override
    public void onBackPressed() {
    }

}
