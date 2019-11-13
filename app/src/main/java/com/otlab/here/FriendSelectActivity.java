package com.otlab.here;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class FriendSelectActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_friend_select);

        ArrayList<String> items = new ArrayList<String>() ;
        items.add("a");
        items.add("a");
        items.add("a");
        items.add("a");
        items.add("a");
        items.add("a");
        items.add("a");
        items.add("a");
        items.add("a");
        items.add("a");
        items.add("a");

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_multiple_choice, items) ;

        ListView listview = findViewById(R.id.listview1) ;
        Button btn = findViewById(R.id.ok);
        listview.setAdapter(adapter) ;

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_OK, getIntent());
                finish();
            }
        });
    }
}
