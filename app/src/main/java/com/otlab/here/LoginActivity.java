package com.otlab.here;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;

public class LoginActivity extends AppCompatActivity implements Serializable {

    private EditText idText;
    private EditText pwdText;
    private CheckBox checkBox;
    private Button loginBtn;
    private Button joinBtn;

    private SharedPreferences appData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // view 불러오기
        idText = findViewById(R.id.idText);
        pwdText = findViewById(R.id.pwdText);
        checkBox = findViewById(R.id.checkBox);
        loginBtn = findViewById(R.id.loginBtn);
        joinBtn = findViewById(R.id.joinBtn);

        //button 클릭 리스너
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validationCheck()) {
                    if (requestSave()) {
                        goMain();
                    } else {
                        //가입되지 않은 사용자 임을 출력 하거나 오류 출력
                    }
                } else {
                    //유효하지 않은 id, pw이라 출력
                }
            }
        });

        joinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goJoin();
            }
        });
    }

    private void goMain() {
        startActivity(new Intent(getApplication(), MainActivity.class));
        finish();
    }

    private void goJoin() {
        startActivity(new Intent(getApplication(), JoinActivity.class).putExtra("login", this));
    }

    // 서버에 id, pw를 전송 후 로그인 성공여부와 사용자 이름을 불러 옴
    private boolean requestSave() {

        // SharedPreferences 객체만으론 저장 불가능 Editor 사용
        appData = getSharedPreferences("appData", MODE_PRIVATE);
        SharedPreferences.Editor editor = appData.edit();

        // 에디터객체.put타입( 저장시킬 이름, 저장시킬 값 )
        // 저장시킬 이름이 이미 존재하면 덮어씌움
        editor.putBoolean("SAVE_LOGIN_DATA", checkBox.isChecked());
        editor.putString("ID", idText.getText().toString().trim());
        //editor.putString("NAME", "");

        // apply, commit 을 안하면 변경된 내용이 저장되지 않음
        editor.apply();

        return true;
    }

    private boolean validationCheck() {
        return true;
    }
}
