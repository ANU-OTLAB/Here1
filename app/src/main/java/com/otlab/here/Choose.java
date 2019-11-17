package com.otlab.here;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class Choose extends AppCompatActivity {
    private Button acceptBtn;
    private Button denyBtn;
    private TextView friendId;
    private TextView validTime;
    private Intent intent;
    private SharedPreferences appData; // 나의 아이디를 가져오기 위해서 SharedPreferneces 선언
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);

        intent = getIntent();

        acceptBtn = findViewById(R.id.accept);
        denyBtn = findViewById(R.id.accept);

        friendId = findViewById(R.id.requestid);
        friendId.setText("요청한 사람의 ID : " + intent.getStringExtra("requestId"));
        validTime = findViewById(R.id.validtime);
        validTime.setText("만료기간 : " + intent.getStringExtra("validTime"));

        appData = getSharedPreferences("appData", MODE_PRIVATE);

        acceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String recv = "";
                    String address = "http://iclab.andong.ac.kr/here/authorityValidity.jsp";
                    ArrayList<String> sendMsg = new ArrayList<>();
                    sendMsg.add("observer");
                    sendMsg.add(friendId.getText().toString());
                    sendMsg.add("target");
                    sendMsg.add(appData.getString("ID", ""));
                    sendMsg.add("validity");
                    sendMsg.add("ACCEPT");

                    MessageThread send = new MessageThread(sendMsg, recv, address);
                    recv = (String) send.execute().get();

                    Toast.makeText(getApplicationContext(), recv, Toast.LENGTH_SHORT).show();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        denyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String recv = "";
                    String address = "http://iclab.andong.ac.kr/here/authorityValidity.jsp";
                    ArrayList<String> sendMsg = new ArrayList<>();
                    sendMsg.add("observer");
                    sendMsg.add(friendId.getText().toString());
                    sendMsg.add("target");
                    sendMsg.add(appData.getString("ID", ""));
                    sendMsg.add("validity");
                    sendMsg.add("DENY");

                    MessageThread send = new MessageThread(sendMsg, recv, address);
                    recv = (String) send.execute().get();

                    Toast.makeText(getApplicationContext(), recv, Toast.LENGTH_SHORT).show();
                } catch(ExecutionException e){
                    e.printStackTrace();
                } catch(InterruptedException e){
                    e.printStackTrace();
                }
            }
        });
    }
}
