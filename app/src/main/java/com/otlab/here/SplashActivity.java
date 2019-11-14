package com.otlab.here;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class SplashActivity extends Activity {

    private boolean autoLogin;
    private String name;

    private SharedPreferences appData;

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_splash);
        FirebaseMessaging.getInstance().subscribeToTopic("1");
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();

                        // Log and toast
                        String msg = getString(R.string.msg_token_fmt, token);
                        Log.d(TAG, msg);
                        Toast.makeText(SplashActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });
        //자동 로그인 여부 확인
        load();

        if (autoLogin) {
            Toast.makeText(getApplicationContext(), name+"님 환영합니다.", Toast.LENGTH_SHORT).show();
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
