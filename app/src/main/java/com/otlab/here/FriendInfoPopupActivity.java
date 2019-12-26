package com.otlab.here;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class FriendInfoPopupActivity extends AppCompatActivity {

    private Intent intent;

    private TextView friendIdText;
    private TextView friendNameText;
    private TextView friendValidityText;
    private Button okButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_friend_info_popup);

        intent = getIntent();

        loadView();
        setListener();
    }

    private void loadView() {
        friendIdText = findViewById(R.id.friendId);
        friendNameText = findViewById(R.id.friendName);
        friendValidityText = findViewById(R.id.friendValidity);
        okButton = findViewById(R.id.okButton);

        friendIdText.setText(intent.getStringExtra("friendId"));
        friendNameText.setText(intent.getStringExtra("friendName"));
        friendValidityText.setText(intent.getStringExtra("friendValidity")!=null ? intent.getStringExtra("friendValidity") : "");
    }

    private void setListener() {
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
