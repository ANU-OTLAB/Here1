package com.otlab.here;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

public class SplashActivity extends Activity {

    private boolean autoLogin;
    private String name;

    private SharedPreferences appData;

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_splash);
        //자동 로그인 여부 확인
        load();

        if (autoLogin) {
            Toast.makeText(getApplicationContext(), name + "님 환영합니다.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplication(), MainActivity.class));
            finish();
        } else {
            startActivity(new Intent(getApplication(), LoginActivity.class));
            finish();
        }

    }

    //뒤로가기 버튼 방지
    @Override
    public void onBackPressed() {
    }

    // 설정값을 불러오는 함수(자동 로그인 여부, 이름)
    private void load() {
        appData = getSharedPreferences("appData", MODE_PRIVATE);
        autoLogin = appData.getBoolean("SAVE_LOGIN_DATA", false);
        name = appData.getString("NAME", "");
    }
}
