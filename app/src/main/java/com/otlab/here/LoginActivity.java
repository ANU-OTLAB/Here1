package com.otlab.here;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private String msgReceiveFromServer = null;
    private String name; //SharedPreferences.Editor 객체 중복 생성을 방지하기 위해 임시로 저장
    private String id;
    private SharedPreferences appData;

    private EditText idText;
    private EditText pwText;
    private CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.loginBtn:

                //서버와의 통신 확인
                if (requestValidationCheck()) {

                    // SharedPreferences 객체만으론 저장 불가능 Editor 사용
                    appData = getSharedPreferences(String.valueOf(R.string.appData), MODE_PRIVATE);
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

                    startActivity(new Intent(getApplication(), MainActivity.class));
                    finish();

                } else {
                }
                break;

            case R.id.joinBtn:
                startActivity(new Intent(getApplication(), JoinActivity.class));
                finish();
                break;
        }
    }

    // view 불러오기
    private void init() {
        idText = findViewById(R.id.idText);
        pwText = findViewById(R.id.pwdText);
        checkBox = findViewById(R.id.checkBox);
        findViewById(R.id.loginBtn).setOnClickListener(this);
        findViewById(R.id.joinBtn).setOnClickListener(this);
    }

    // 서버에 id, pw를 전송 후 로그인 성공여부와 사용자 이름을 불러 옴
    private boolean requestValidationCheck() {
        try {
            ArrayList<String> sendMsg = new ArrayList<>();
            sendMsg.add("id");
            sendMsg.add(idText.getText().toString());
            sendMsg.add("pw");
            sendMsg.add(pwText.getText().toString());

            //MessageThread(보낼 메시지 : ArrayList<String>, 주소 : String)
            MessageThread messenger = new MessageThread(sendMsg, "http://iclab.andong.ac.kr/here/login.jsp");
            msgReceiveFromServer = (String)messenger.execute().get();

            if (msgReceiveFromServer.equals("fail")) {
                return false;
            }

            //이름 추출
            String[] msgList = msgReceiveFromServer.split("/");
            name = msgList[1];
            return true;

        } catch (Exception e) {
            msgReceiveFromServer = "fail";
            return false;
        }

    }

}
