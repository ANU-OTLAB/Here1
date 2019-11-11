package com.otlab.here;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class OptionActivity extends Activity {

    Button logoutButton;
    Button whoamiButton;
    Button alarmSettingButton;
    Button friendSettingButton;
    Button developerButton;

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_option);

        loadView();
        setListener();
    }

    private void loadView() {
        logoutButton = findViewById(R.id.logout);
        whoamiButton = findViewById(R.id.whoami);
        alarmSettingButton = findViewById(R.id.alarm);
        friendSettingButton = findViewById(R.id.friend);
        developerButton = findViewById(R.id.developer);
    }

    private void setListener() {
        //로그아웃 버튼
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });
        //whoami버튼
        whoamiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), getSharedPreferences("appData", MODE_PRIVATE).getString("ID", ""), Toast.LENGTH_SHORT).show();
            }
        });
        //알람 소리 설정 버튼
        alarmSettingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AlarmSettingActivity.class));
            }
        });
        //친구 관리 버튼
        friendSettingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), FriendActivity.class));
            }
        });
        //개발자 버튼
        developerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), DeveloperActivity.class));
            }
        });
    }

    private void logout() {
        new AlertDialog.Builder(OptionActivity.this)
                .setTitle("로그아웃")
                .setMessage("로그아웃 하시겠습니까?\n로그아웃 시 저장 된 알람이 삭제됩니다.\n(취소 시 돌아감)")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // 확인시 처리 로직
                        Toast.makeText(getApplicationContext(), "로그아웃을 완료했습니다.", Toast.LENGTH_SHORT).show();
                        getSharedPreferences("appData", MODE_PRIVATE).edit().clear().commit();
                        startActivity(new Intent(getApplication(), SplashActivity.class));
                        finish();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // 취소시 처리 로직
                        Toast.makeText(getApplicationContext(), "취소하였습니다.", Toast.LENGTH_SHORT).show();
                    }
                })
                .show();
    }

}
