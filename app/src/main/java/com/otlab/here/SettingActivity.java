package com.otlab.here;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class SettingActivity extends Activity {

    ListView listView;
    ArrayList<SettingItem> listitem;
    ListViewCustomAdapter customAdapter;
    FloatingActionButton create;

    SharedPreferences appData;
    SharedPreferences.Editor editor;
    int listSize;

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_setting);

        listView = findViewById(R.id.setList);
        listitem = new ArrayList<>();
        create = findViewById(R.id.Createlist);

        //on Load 데이터 불러 와서 리스트에 보여줌
        appData = getSharedPreferences("appData", MODE_PRIVATE);
        editor = appData.edit();

        listSize = appData.getInt("listSize", 0);
        for (int i = 0; i < listSize; i++) {
            listitem.add(new SettingItem(appData.getString("name" + i, ""), appData.getString("distance" + i, ""), appData.getString("destination" + i, ""), appData.getString("time" + i, "")));
        }

        customAdapter = new ListViewCustomAdapter(this, R.layout.listview_item, listitem);
        listView.setAdapter(customAdapter);

        //listview 아이템 클릭 시 수정, 삭제 팝업
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView adapterView, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), SettingPopupActivity.class);

                intent.putExtra("settingName", listitem.get(position).getSettingName());
                intent.putExtra("distance", listitem.get(position).getDistance());
                intent.putExtra("destination", listitem.get(position).getDestination());
                intent.putExtra("time", listitem.get(position).getTime());
                intent.putExtra("position", position);
                intent.putExtra("service", SettingItem.ServiceType.UPDATE);

                startActivityForResult(intent, 1);
            }
        });
        //create 버튼 리스너
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SettingPopupActivity.class);

                intent.putExtra("service", SettingItem.ServiceType.CREATE);

                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {

                SettingItem.ServiceType serviceType = (SettingItem.ServiceType) data.getSerializableExtra("service");
                String name = data.getStringExtra("settingName");
                String distance = data.getStringExtra("distance");
                String destination = data.getStringExtra("destination");
                String time = data.getStringExtra("time");
                int itemposition = data.getIntExtra("itemPosition", 0);

                if (serviceType == SettingItem.ServiceType.CREATE) {
                    //추가된 데이터 리스트에 반영
                    SettingItem settingItem = new SettingItem(name, distance, destination, time);
                    listitem.add(settingItem);
                    customAdapter.notifyDataSetChanged();
                    //추가 된 데이터 폰에 저장
                    editor.putString("name" + listSize, name);
                    editor.putString("distance" + listSize, distance);
                    editor.putString("destination" + listSize, destination);
                    editor.putString("time" + listSize, time);
                    editor.putInt("listSize", ++listSize);

                }
                if (serviceType == SettingItem.ServiceType.UPDATE) {
                    //변경 된 데이터 리스트에 반영
                    listitem.get(itemposition).setSettingName(name);
                    listitem.get(itemposition).setDistance(distance);
                    listitem.get(itemposition).setDestination(destination);
                    listitem.get(itemposition).setTime(time);
                    customAdapter.notifyDataSetChanged();
                    //변경 된 데이터 폰에 저장
                    editor.putString("name" + listSize, name);
                    editor.putString("distance" + listSize, distance);
                    editor.putString("destination" + listSize, destination);
                    editor.putString("time" + listSize, time);

                }

            } else if (resultCode == RESULT_CANCELED) {
                int deleteposition = data.getIntExtra("itemPosition", 0);
                listitem.remove(deleteposition);
                customAdapter.notifyDataSetChanged();
                //저장 된 데이터 한 칸씩 앞으로
                for (int i = deleteposition; i < listSize - 1; i++) {
                    editor.putString("name" + i, appData.getString("name" + (i + 1), ""));
                    editor.putString("distance" + i, appData.getString("distance" + (i + 1), ""));
                    editor.putString("destination" + i, appData.getString("destination" + (i + 1), ""));
                    editor.putString("time" + i, appData.getString("time" + (i + 1), ""));
                }
                //마지막 저장 된 데이터 지우기
                editor.remove("name" + (listSize - 1));
                editor.remove("distance" + (listSize - 1));
                editor.remove("destination" + (listSize - 1));
                editor.remove("time" + (listSize - 1));
                editor.putInt("listSize", listSize - 1);
            }
            editor.apply();
        }
    }

}

