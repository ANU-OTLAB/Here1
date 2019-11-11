package com.otlab.here;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class FriendActivity extends Activity {

    ListView friendListView;
    ArrayList<FriendItem> friendList;
    private FriendListViewCustomAdapter customAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);

        initList();
        setListener();

    }
    private void initList(){
        friendListView = findViewById(R.id.setList);
        friendList = new ArrayList<>();
/*
        for (int i = 0; i < listSize; i++) {
            settingItemList.add(new SettingItem(appData.getString("name" + i, ""), appData.getString("distance" + i, ""), appData.getString("destination" + i, ""), appData.getString("time" + i, "")));
        }
*/
        customAdapter = new FriendListViewCustomAdapter(this, R.layout.friendlistview_item, friendList);
        friendListView.setAdapter(customAdapter);
    }
    private void setListener(){

    }
}
