package com.otlab.here;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

public class SplashActivity extends Activity {/*check*/

    private boolean autoLogin;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_splash);

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
        SharedPreferences appData = getSharedPreferences("appData", MODE_PRIVATE);
        autoLogin = appData.getBoolean("autoLogin", false);
        name = appData.getString("name", "");
    }
}
