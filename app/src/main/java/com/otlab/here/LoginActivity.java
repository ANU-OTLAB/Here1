package com.otlab.here;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {/*check*/

    String msgReceiveFromServer = null;
    String name; //SharedPreferences.Editor 객체 중복 생성을 방지하기 위해 임시로 저장
    String id;
    private SharedPreferences appData;

    private EditText idText;
    private EditText pwText;
    private CheckBox checkBox;
    private Button loginBtn;
    private Button joinBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        loadView();
        setListener();
    }

    private void goMain() {
        startActivity(new Intent(getApplication(), MainActivity.class));
        finish();
    }

    private void goJoin() {
        startActivity(new Intent(getApplication(), JoinActivity.class));
        finish();
    }

    // 서버에 id, pw를 전송 후 로그인 성공여부와 사용자 이름을 불러 옴
    private boolean requestValidationCheck() {
        try {
            ArrayList<String> sendMsg = new ArrayList<>();
            sendMsg.add("id");
            sendMsg.add(idText.getText().toString());
            sendMsg.add("pw");
            sendMsg.add(pwText.getText().toString());

            //new MessageThread(보낼 메시지 ArrayList<String>, 받을 메세지 객체, 주소스트링)
            MessageThread messageThread = new MessageThread(sendMsg, msgReceiveFromServer, "http://iclab.andong.ac.kr/here/login.jsp");
            msgReceiveFromServer = (String) messageThread.execute().get();
        } catch (Exception e) {
        }

        if (msgReceiveFromServer.equals("LOGIN_FAILED"))
            return false;

        //이름 추출
        String[] msgList = msgReceiveFromServer.split("/");
        name = msgList[1];

        return true;
    }

    // view 불러오기
    private void loadView() {
        idText = findViewById(R.id.idText);
        pwText = findViewById(R.id.pwdText);
        checkBox = findViewById(R.id.checkBox);
        loginBtn = findViewById(R.id.loginBtn);
        joinBtn = findViewById(R.id.joinBtn);
    }

    private void setListener() {
        //loginButton 클릭 리스너
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //서버와의 통신 확인
                if (requestValidationCheck()) {

                    // SharedPreferences 객체만으론 저장 불가능 Editor 사용
                    appData = getSharedPreferences("appData", MODE_PRIVATE);
                    SharedPreferences.Editor editor = appData.edit();

                    id = appData.getString("id", "");
                    if (!id.equals(idText.getText().toString().trim())) {
                        editor.clear().apply();
                    }
                    editor.putBoolean("autoLogin", checkBox.isChecked());
                    editor.putString("id", idText.getText().toString().trim());
                    editor.putString("name", name);

                    // apply, commit 을 안하면 변경된 내용이 저장되지 않음
                    editor.apply();
                    goMain();
                } else {
                    Toast.makeText(getApplicationContext(), "ID 또는 비밀번호를 확인 해 주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //joinButton 클릭 리스너
        joinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goJoin();
            }
        });
    }
}
