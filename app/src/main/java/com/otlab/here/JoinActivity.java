package com.otlab.here;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class JoinActivity extends AppCompatActivity {

    private String msg;
    private EditText nameText;
    private EditText phText;
    private EditText idText;
    private EditText pwText;
    private Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        loadView();
        setListener();
    }

    private boolean connectServer() {
        if (nameText.length() != 0 && phText.length() != 0 && idText.length() != 0 && pwText.length() != 0)
            try {
                ArrayList<String> sendMsg = new ArrayList();
                sendMsg.add("name");
                sendMsg.add(nameText.getText().toString());
                sendMsg.add("ph");
                sendMsg.add(phText.getText().toString());
                sendMsg.add("id");
                sendMsg.add(idText.getText().toString());
                sendMsg.add("pw");
                sendMsg.add(pwText.getText().toString());
                MessageThread messageThread = new MessageThread(sendMsg, msg, "http://iclab.andong.ac.kr/here/join.jsp");
                msg = (String) messageThread.execute().get();
            } catch (Exception e) {
                Log.d("error^^^^^^^^^", e.toString());
                return false;
            }
        if(msg.equals("JOIN_SUCCESS"))
            return true;
        return false;
    }
    private void loadView(){
        nameText = findViewById(R.id.name);
        phText = findViewById(R.id.ph);
        idText = findViewById(R.id.id);
        pwText = findViewById(R.id.pw);
        submitButton = findViewById(R.id.submit);

        nameText.setText("이기훈");
        phText.setText("010-5393-3752");
        idText.setText("raindrop5393");
        pwText.setText("qwer1234!");
    }
    private void setListener(){
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (connectServer()) {
                        Toast.makeText(getApplicationContext(), "회원가입 성공!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplication(), SplashActivity.class));
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "회원가입 실패!", Toast.LENGTH_SHORT).show();

                    }
                } catch (Exception e) {
                    Log.d("!!!!!", e.toString());
                }

            }
        });
    }
}