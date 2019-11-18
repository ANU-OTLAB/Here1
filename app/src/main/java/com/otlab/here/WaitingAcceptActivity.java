package com.otlab.here;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class WaitingAcceptActivity extends AppCompatActivity {

    private ListView waitiingAcceptListView;
    private ArrayList<WaitingAcceptItem> waitingAcceptList;
    private WaitingAcceptListViewCustomAdapter customAdapter;
    private SharedPreferences appData; // 나의 아이디를 가져오기 위해서 SharedPreferneces 선언
    private String[] recvData; // 친구목록을 가져올 ArrayList

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_accept);

        waitiingAcceptListView = findViewById(R.id.waitingAcceptList);

        initList();
    }
    private void initList(){
        try{
            waitingAcceptList = new ArrayList<>();
            customAdapter = new WaitingAcceptListViewCustomAdapter(this, R.layout.waiting_accept_item, waitingAcceptList);
            waitiingAcceptListView.setAdapter(customAdapter);
            appData = getSharedPreferences("appData", MODE_PRIVATE);
            String address = "http://iclab.andong.ac.kr/here/authorityOutput.jsp";
            String recv = "";
            ArrayList<String> sendMsg = new ArrayList<>();
            sendMsg.add("target");
            sendMsg.add(appData.getString("ID", ""));
            MessageThread send = new MessageThread(sendMsg, recv, address);
            recv = (String)send.execute().get();

            if(recv.length()>=2) {
                recvData = recv.split("/");
                for (int i = 0; i < recvData.length; i++) {
                    String[] buffer = recvData[i].split(" ");
                    waitingAcceptList.add(new WaitingAcceptItem(buffer[i], buffer[i + 1], "REQUEST"));
                }

                setListener();
            }else{
                waitingAcceptList.add(new WaitingAcceptItem("", "EMPTY", ""));
            }
            customAdapter.notifyDataSetChanged();
        } catch(ExecutionException e){
            e.printStackTrace();
        } catch(InterruptedException e){
            e.printStackTrace();
        }
    }
    private void setListener(){
        waitiingAcceptListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(getApplicationContext(), ChooseActivity.class);
                intent.putExtra("requestId", waitingAcceptList.get(position).getFriendName());
                intent.putExtra("validTime", waitingAcceptList.get(position).getTime());
                startActivity(intent);
                finish();
            }
        });
    }
}
