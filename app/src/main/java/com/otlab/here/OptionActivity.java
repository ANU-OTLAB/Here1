package com.otlab.here;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


public class OptionActivity extends Activity {

    private Button logoutButton;
    private Button alarmSettingButton;
    private Button friendSettingButton;
    private Button developerButton;
    private Button acceptWaitingBtn;
    private LinearLayout whoamiLayout;
    private Button whoamiButton;
    private Button whoamicloseButton;
    private SharedPreferences appData;

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_option);

        loadView();
        setListener();
    }

    private void loadView() {
        logoutButton = findViewById(R.id.logout);
        whoamiLayout = findViewById(R.id.whoami_layout);
        whoamiButton = findViewById(R.id.whoami);
        whoamicloseButton = findViewById(R.id.whoami_close);
        alarmSettingButton = findViewById(R.id.alarm);
        friendSettingButton = findViewById(R.id.friend);
        developerButton = findViewById(R.id.developer);
        acceptWaitingBtn = findViewById(R.id.accept);
    }

    private void setListener() {
        //로그아웃 버튼
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });
        //whoami 버튼
        whoamiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                whoami(whoamiLayout);
            }
        });
        whoamicloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                whoamiclose(whoamiLayout);
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
        //수락대기목록 버튼
        acceptWaitingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), WaitingAcceptActivity.class));
            }
        });
    }

    private void logout() {
        //로그아웃 확인창과 그 메세지
        new AlertDialog.Builder(OptionActivity.this)
                .setTitle("로그아웃")
                .setMessage("로그아웃 하시겠습니까?\n로그아웃 시 저장 된 알람이 삭제됩니다.\n(취소 시 돌아감)")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // 확인시 처리 로직
                        Toast.makeText(getApplicationContext(), "로그아웃을 완료했습니다.", Toast.LENGTH_SHORT).show();
                        getSharedPreferences("appData", MODE_PRIVATE).edit().clear().apply();
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

    //whoami 레이아웃 숨기기/보여주기
    protected void whoami(LinearLayout layout){
        TextView whoaminame = findViewById(R.id.whoami_name);
        TextView whoamiid = findViewById(R.id.whoami_id);

        appData = getSharedPreferences("appData", MODE_PRIVATE);
        String id = appData.getString("id","");

        whoaminame.setText(appData.getString("name", ""));
        whoamiid.setText(id);

        Animation ani = new AlphaAnimation(0, 1);
        if (layout.getVisibility() == View.GONE) {
            ani.setDuration(1000);
            layout.setVisibility(View.VISIBLE);
            layout.setAnimation(ani);
        } else {
            layout.setVisibility(View.GONE);
        }
    }
    protected void whoamiclose(LinearLayout layout){
        if (layout.getVisibility() == View.VISIBLE) {
            layout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }
}
