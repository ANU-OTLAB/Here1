package com.otlab.here;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class FriendActivity extends AppCompatActivity {
    // 친구관리 액티비티
    //CreateFriendActivity 에서 customAdapter에 접근하기 위해서 Context 선언
    public static Context mContext;
    // 필요한 것들 선언
    ListView FriendList;
    ArrayList<FriendItem> FriendItemList;
    FriendListViewCustomAdapter customAdapter;
    Button Create;
    Button Delete;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);
        mContext = this;

        //CreateFriendAcitivity 에서 넘어오는 position 값 받기
        Intent intent = getIntent();
        position = intent.getIntExtra("position",0);

        FriendList = (ListView)findViewById(R.id.FriendList);
        FriendItemList = new ArrayList<>();

        Create = (Button)findViewById(R.id.CreateFriend);
        Delete = (Button)findViewById(R.id.DeleteFriend);

        // customAdapter 로 FriendAcitivity 와 리스트 아이템의 xml, 리스트 뷰를 연결해준다.
        customAdapter = new FriendListViewCustomAdapter(this, R.layout.friendlistview_item, FriendItemList);
        FriendList.setAdapter(customAdapter);


        Create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                create();
            }
        });
        Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delete();
            }
        });
    }
    // 친구 추가 버튼 눌렀을때 사용되는 메소드
    protected void create(){
        // customAdapter의 크기 측정
        int count = customAdapter.getCount();
        Intent intent = new Intent(getApplicationContext(), CreateFriendActivity.class);
        intent.putExtra("position", count);
        startActivity(intent);
    }

    // 아무리 해도 삭제를 못하겠다.
    protected void delete() {

    }
}
