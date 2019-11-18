package com.otlab.here;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class FriendActivity extends Activity {

    private ListView friendListView;
    private Button addFriend;
    private Button delFriend;
    private TextView friendIdtxt;

    private ArrayList<FriendItem> friendList;
    private FriendListViewCustomAdapter customAdapter;
    private SharedPreferences appData; // 나의 아이디를 가져오기 위해서 SharedPreferneces 선언
    private String[] recvData; // 친구목록을 가져올 ArrayList

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);

        loadView();
        initList();
        setListener();
    }

    private void loadView() {
        friendListView = findViewById(R.id.FriendList);
        addFriend = findViewById(R.id.addRequest);
        delFriend = findViewById(R.id.delRequest);
        friendIdtxt = findViewById(R.id.friendId);
    }

    private void setListener() {
        addFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addFriend();
            }
        });
        delFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delFriend();
            }
        });
    }

    private void initList() {
        friendList = new ArrayList<>();
        customAdapter = new FriendListViewCustomAdapter(this, R.layout.friendlistview_item, friendList);
        friendListView.setAdapter(customAdapter);

        String receiveMsg = "";
        ArrayList<String> sendmsg = new ArrayList<>();

        try {
            sendmsg.add("id");
            sendmsg.add(getSharedPreferences("appData", MODE_PRIVATE).getString("ID", " "));

            MessageThread send = new MessageThread(sendmsg, receiveMsg, "http://iclab.andong.ac.kr/here/friendListOutput.jsp");
            receiveMsg = (String) send.execute().get();

            // "/"는 리스트 한 줄 단위 구분 " "는 이름, id, 만료기한 구분
            if (receiveMsg.length() != 0) {
                recvData = receiveMsg.split("/");
                for (int i = 0; i < recvData.length; i++) {
                    String[] buffer = recvData[i].split(" ");
                    friendList.add(new FriendItem(buffer[0], buffer[1], buffer.length==3?buffer[2]:""));
                }
            }
            customAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addFriend() {
        try {
            //친구추가 하기 위해 TextView에 적은 친구 이름 가져오기
            String friendId = friendIdtxt.getText().toString();

            //로그인된 나의 아이디 가져오기
            appData = getSharedPreferences("appData", MODE_PRIVATE);
            String myId = appData.getString("ID", "");
            //MessageThread에 들어갈 파라미터값 생성
            String address = "http://iclab.andong.ac.kr/here/friendAdd.jsp";
            String recv = "";
            ArrayList<String> sendmsg = new ArrayList<>();
            sendmsg.add("MYID");
            sendmsg.add(myId);
            sendmsg.add("FRIENDID");
            sendmsg.add(friendId);

            MessageThread send = new MessageThread(sendmsg, recv, address);
            // 친구추가 성공 or 실패 반환 받기
            String msgReceiveFromServer = (String) send.execute().get();
            // 친구추가에 성공했는지 실패했는지 출력
            Toast.makeText(getApplicationContext(), msgReceiveFromServer, Toast.LENGTH_SHORT).show();
            // 만약 성공했으면 새로고침
            if (msgReceiveFromServer.equals("ADD_SUCCESS")) {
                initList();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void delFriend() {
        try {
            // 친구삭제 성공 or 실패 반환 받기
            String msgReceiveFromServer = null;
            //친구삭제하기위해서 텍스트뷰 아이디 가져오기
            friendIdtxt = findViewById(R.id.friendId);
            String friendId = friendIdtxt.getText().toString();
            //로그인된 나의 아이디 가져오기
            appData = getSharedPreferences("appData", MODE_PRIVATE);
            String myId = appData.getString("ID", "");
            //MessageThread에 들어갈 파라미터값 생성
            String address = "http://iclab.andong.ac.kr/here/friendDelete.jsp";
            String recv = "";
            ArrayList<String> sendmsg = new ArrayList<>();
            sendmsg.add("MYID");
            sendmsg.add(myId);
            sendmsg.add("FRIENDID");
            sendmsg.add(friendId);

            MessageThread send = new MessageThread(sendmsg, recv, address);
            msgReceiveFromServer = (String) send.execute().get();
            // 친구삭제가 성공했는지 실패했는지 출력
            Toast.makeText(getApplicationContext(), msgReceiveFromServer, Toast.LENGTH_SHORT).show();
            // 친구삭제가 성공했으면 새로고침
            if (msgReceiveFromServer.equals("DELETE_SUCCESS")) {
                initList();
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}