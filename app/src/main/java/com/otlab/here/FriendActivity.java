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
    private ArrayList<FriendItem> friendList;
    private FriendListViewCustomAdapter customAdapter;
    private SharedPreferences appData; // 나의 아이디를 가져오기 위해서 SharedPreferneces 선언
    private String[] recvData; // 친구목록을 가져올 ArrayList
    private TextView friendIdtxt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);

        initList();

        Button addFriend = (Button)findViewById(R.id.addrequest);
        addFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddFriend();
            }
        });
        Button delFriend = (Button)findViewById(R.id.delrequest);
        delFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DelFriend();
            }
        });
    }
    private void initList(){
        ////수정(코드더럽힌거 제가 수정하겠습니다.)////
        friendListView = findViewById(R.id.FriendList); // 여기에 잘못된 값 들어가있었음
        friendList = new ArrayList<>();
        customAdapter = new FriendListViewCustomAdapter(this, R.layout.friendlistview_item, friendList);
        friendListView.setAdapter(customAdapter);
/*
        for (int i = 0; i < listSize; i++) {
            settingItemList.add(new SettingItem(appData.getString("name" + i, ""), appData.getString("distance" + i, ""), appData.getString("destination" + i, ""), appData.getString("time" + i, "")));
        }
*/
        /////////////////////// 더러움 주의 /////////////////////////
        try {
            appData = getSharedPreferences("appData", MODE_PRIVATE);
            //MessageThread 를 사용하기 위한 파라미터 생성//
            String address = "http://iclab.andong.ac.kr/here/friendListOutput.jsp";
            String recv = "";
            ArrayList<String> sendmsg = new ArrayList<>();
            sendmsg.add("id");
            sendmsg.add(appData.getString("ID", " ") /* 로그인 되어있는 ID 가져오기*/);
            ///////////////////////////////////////////////
            MessageThread send = new MessageThread(sendmsg, recv, address);
            recv = (String)send.execute().get();
            // 가져온 배열로 아이템을 리스트에 추가
            if(recv.length()!=0) {
                // JSP에서 받은 친구정보를 공백칸에 따라 나누기
                recvData = recv.split(" ");
                for (int i = 0; i < recvData.length; i = i + 2) {
                    friendList.add(new FriendItem(recvData[i], recvData[i + 1]));
                }
            }
            customAdapter.notifyDataSetChanged();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        /////////////////////////////////////////////////////////////걍 여기 칠게 ㅇㅋ?

    }
    private void AddFriend(){
        /// 코드 제가정리하겠습니다////
        try {
            //친구추가 하기 위해 TextView에 적은 친구 이름 가져오기
            friendIdtxt = (TextView)findViewById(R.id.friendid);
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
            if(msgReceiveFromServer.equals("ADD_SUCCESS")){
                initList();
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    private void DelFriend(){
        try {
            // 친구삭제 성공 or 실패 반환 받기
            String msgReceiveFromServer = null;
            //친구삭제하기위해서 텍스트뷰 아이디 가져오기
            friendIdtxt = (TextView)findViewById(R.id.friendid);
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
            if(msgReceiveFromServer.equals("DELETE_SUCCESS")){
                initList();
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}