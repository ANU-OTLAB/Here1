package com.otlab.here;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;

public class SettingActivity extends Activity implements View.OnClickListener {

    //TextView txtView;
    ListView listView;
    ArrayList<SettingItem> listitem;
    ListViewCustomAdapter customAdapter;
    FloatingActionButton create;
    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_setting);
        listView = (ListView) findViewById(R.id.setList);
        listitem = new ArrayList<>();
        create = findViewById(R.id.Createlist);

        SettingItem settingItem1 = new SettingItem("야","500","안동", "4");

        listitem.add(settingItem1);
        customAdapter = new ListViewCustomAdapter(this,R.layout.listview_item, listitem);
        listView.setAdapter(customAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView adapterView, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), SettingPopupActivity.class);
                intent.putExtra("settingName", listitem.get(position).getSettingName());
                intent.putExtra("distance", listitem.get(position).getDistance());
                intent.putExtra("destination", listitem.get(position).getDestination());
                intent.putExtra("time", listitem.get(position).getTime());
                intent.putExtra("position", position);
                startActivityForResult(intent,1);
            }
        });
    }
    @Override
    public void onClick(View v){
    }
    /*public void onButtonClick(View view) {
        Intent intent = new Intent(this, SettingPopupActivity.class);
        intent.putExtra("data", "Test Popup");
        startActivityForResult(intent, 1);
    }*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==1){
            if(resultCode==RESULT_OK){
                String name = data.getStringExtra("R_settingName");
                String distance = data.getStringExtra("R_distance");
                String destination = data.getStringExtra("R_destination");
                String time = data.getStringExtra("R_time");
                int itemposition = data.getIntExtra("R_ItemPosition",0);

                listitem.get(itemposition).setSettingName(name);
                listitem.get(itemposition).setDistance(distance);
                listitem.get(itemposition).setDestination(destination);
                listitem.get(itemposition).setTime(time);

                customAdapter.notifyDataSetChanged();
            }
            else if(resultCode==RESULT_CANCELED){
                int deleteposition = data.getIntExtra("R_ItemPosition", 0);
                listitem.remove(deleteposition);
                customAdapter.notifyDataSetChanged();
            }
        }
    }
    public void CreateClick(View view){
        SettingItem settingItem = new SettingItem("","","","");
        listitem.add(settingItem);
        customAdapter.notifyDataSetChanged();
    }
}

