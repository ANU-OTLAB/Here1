package com.otlab.here;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class FriendSelectActivity extends AppCompatActivity {

    private ArrayAdapter adapter;
    private ListView listView;
    private Button okBtn;
    Intent intent;

    String receiveMsg = "";
    ArrayList<String> sendmsg = new ArrayList();

    private String[] recvData; // 친구목록을 가져올 ArrayList
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_friend_select);

        listView = findViewById(R.id.listview1);
        okBtn = findViewById(R.id.ok);

        id = getSharedPreferences("appData", MODE_PRIVATE).getString("ID", " ");
        intent = getIntent();

        ArrayList<String> items = new ArrayList();
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_multiple_choice, items);

        try {
            sendmsg.add("id");
            sendmsg.add(id);

            MessageThread send = new MessageThread(sendmsg, receiveMsg, "http://iclab.andong.ac.kr/here/friendListOutput.jsp");
            receiveMsg = (String) send.execute().get();

            // "/"는 리스트 한 줄 단위 구분 " "는 이름, id, 만료기한 구분
            if (receiveMsg.length() != 0) {
                recvData = receiveMsg.split("/");
                for (int i = 0; i < recvData.length; i++) {
                    String[] buffer = recvData[i].split(" ");
                    items.add(buffer[0]);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        adapter.notifyDataSetChanged();

        listView.setAdapter(adapter);

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Destination[] destination = new Destination[listView.getCheckedItemCount()];
                SparseBooleanArray checkedItemPositions =  listView.getCheckedItemPositions();
                for (int i = 0; i < listView.getCount(); i++) {
                    if(checkedItemPositions.get(i))
                        destination[i] = new Destination(SettingItem.DestinationType.PERSON, listView.getItemAtPosition(i).toString().trim(), 0, 0);
                }
                intent.putExtra("destination", destination);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView adapterView, View view, int position, long l) {
                String[] buffer = recvData[position].split(" ");

                Intent intent = new Intent(FriendSelectActivity.this, FriendInfoPopupActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("friendId", listView.getItemAtPosition(position).toString().trim());
                intent.putExtra("friendName", buffer[1]);
                intent.putExtra("friendValidity", buffer.length==3?buffer[2]:"");
                intent.putExtra("position", position);

                startActivity(intent);
            }
        });
    }
}
