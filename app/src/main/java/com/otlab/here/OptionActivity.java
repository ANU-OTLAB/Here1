package com.otlab.here;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class OptionActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_option);

        Button logoutbtn = findViewById(R.id.logout);
        Button whoamibtn = findViewById(R.id.whoami);
        Button option_setbtn = findViewById(R.id.Option_Set);
        Button friend_setbtn = findViewById(R.id.Friend_Set);
        Button developerbtn = findViewById(R.id.intro);

        logoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });
        whoamibtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                whoami();
            }
        });
        option_setbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                option_set();
            }
        });
        friend_setbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                friend_set();
            }
        });
        developerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                developer();
            }
        });
    }
    //로그인 때 저장해놓은 정보를 가져와서 Toast로 메세지 출력
    protected void whoami(){
        SharedPreferences appData = getSharedPreferences("appData", MODE_PRIVATE);
        String id = appData.getString("ID","");
        Toast.makeText(getApplicationContext(), id, Toast.LENGTH_SHORT).show();
    }

    protected void logout(){
        SharedPreferences appData = getSharedPreferences("appData", MODE_PRIVATE);
        SharedPreferences.Editor editor = appData.edit();
        editor.clear();
        editor.commit();
        startActivity(new Intent(getApplication(), SplashActivity.class));
        MainActivity ma = (MainActivity) getIntent().getSerializableExtra("main");
        ma.finish();
        finish();
    }
    //알람세팅 액티비티로 가기
    protected void option_set(){
        Intent intent = new Intent(getApplicationContext(), AlarmSettingActivity.class);
        startActivity(intent);
    }
    //친구관리 액티비티로 가기
    protected void friend_set(){
        Intent intent = new Intent(getApplicationContext(),FriendActivity.class);
        startActivity(intent);
    }
    //개발자 보는걸로 가기
    protected void developer(){
        Intent intent = new Intent(getApplicationContext(),DeveloperActivity.class).putExtra("hi", 1).putExtra("main", getIntent().getSerializableExtra("main"));
        startActivity(intent);
    }
}
