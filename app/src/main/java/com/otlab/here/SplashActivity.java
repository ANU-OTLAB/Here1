package com.otlab.here;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends Activity {

    private boolean saveLoginData;
    private String id;
    private String pwd;

    private SharedPreferences appData;

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_splash);

        appData = getSharedPreferences("appData", MODE_PRIVATE);
        load();

        // 이전에 로그인 정보를 저장시킨 기록이 있다면
        if (saveLoginData) {

        }
        Handler hd = new Handler();



        hd.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(getApplication(), LoginActivity.class));
                SplashActivity.this.finish();
            }
        }, 1000);

    }

    @Override
    public void onBackPressed() {

    }

    // 설정값을 불러오는 함수
    private void load() {
        // SharedPreferences 객체.get타입( 저장된 이름, 기본값 )
        // 저장된 이름이 존재하지 않을 시 기본값
        saveLoginData = appData.getBoolean("SAVE_LOGIN_DATA", false);
        id = appData.getString("ID", "");
        pwd = appData.getString("PWD", "");
    }
}
