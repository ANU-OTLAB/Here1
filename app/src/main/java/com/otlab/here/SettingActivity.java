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

    private ListView settingItemListView;
    private ArrayList<SettingItem> settingItemList;
    private ListViewCustomAdapter customAdapter;
    private FloatingActionButton createButton;

    private SharedPreferences appData;
    private SharedPreferences.Editor editor;
    private int listSize;

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_setting);

        initList();
        setListener();

    }

    private void initList() {
        settingItemListView = findViewById(R.id.setList);
        createButton = findViewById(R.id.createList);
        settingItemList = new ArrayList<>();

        //on Load 데이터 불러 와서 리스트에 보여줌
        appData = getSharedPreferences("appData", MODE_PRIVATE);
        editor = appData.edit();
        listSize = appData.getInt("listSize", 0);

        for (int i = 0; i < listSize; i++) {
            settingItemList.add(new SettingItem(appData.getString("name" + i, ""), appData.getString("distance" + i, "") + "M", appData.getString("destinationName" + i, ""), appData.getString("validity" + i, "")));
        }

        customAdapter = new ListViewCustomAdapter(this, R.layout.listview_item, settingItemList);
        settingItemListView.setAdapter(customAdapter);
    }

    private void setListener() {

        //listview 아이템 클릭 시 수정, 삭제 팝업
        settingItemListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView adapterView, View view, int position, long id) {
                String[] distanceInt = settingItemList.get(position).getDistance().split("M");

                Intent intent = new Intent(getApplicationContext(), SettingPopupActivity.class);

                intent.putExtra("settingName", settingItemList.get(position).getSettingName())
                        .putExtra("distance", distanceInt[0])
                        .putExtra("destinationName", settingItemList.get(position).getDestination())
                        .putExtra("destinationType", SettingItem.DestinationType.UNKNOWN)
                        .putExtra("validity", settingItemList.get(position).getValidity())
                        .putExtra("itemPosition", position)
                        .putExtra("service", SettingItem.ServiceType.UPDATE);

                startActivityForResult(intent, 1);
            }
        });
        //createButton 버튼 리스너
        createButton.setOnClickListener(new View.OnClickListener() {
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
        SettingItem.ServiceType serviceType = (SettingItem.ServiceType) data.getSerializableExtra("service");
        //requestCode 확인... 예의상 놔둠(없어도 됨)
        if (requestCode == 1) {
            //PopupActivity에서 확인,저장 버튼 눌렀을 때
            if (resultCode == RESULT_OK) {

                String name = data.getStringExtra("settingName");
                String distance = data.getStringExtra("distance")+"M";
                String destinationName = data.getStringExtra("destinationName");
                String validity = data.getStringExtra("validity");
                int itemPosition = data.getIntExtra("itemPosition", 0);
                Destination[] destination = (Destination[])data.getSerializableExtra("destination");
                String destinationList;
                if(destination != null) {
                    destinationList = destination[0].getDestinationName();
                    for (int i = 1; i < destination.length; i++) {
                        destinationList += " ";
                        destinationList += destination[i].getDestinationName();
                    }
                }else{
                    destinationList = data.getStringExtra("destinationList");
                }

                if (serviceType == SettingItem.ServiceType.CREATE) {
                    //추가된 데이터 리스트에 반영
                    SettingItem settingItem = new SettingItem(name, distance, destinationName, validity);
                    settingItemList.add(settingItem);
                    customAdapter.notifyDataSetChanged();
                    //추가 된 데이터 폰에 저장
                    editor.putString("name" + listSize, name)
                            .putString("distance" + listSize, distance)
                            .putString("destinationName" + listSize, destinationName)
                            .putString("destinationList" + listSize, destinationList)
                            .putString("validity" + listSize, validity)
                            .putInt("listSize", ++listSize);

                }
                if (serviceType == SettingItem.ServiceType.UPDATE) {
                    //변경 된 데이터 리스트에 반영
                    settingItemList.get(itemPosition).setSettingName(name);
                    settingItemList.get(itemPosition).setDistance(distance);
                    settingItemList.get(itemPosition).setDestination(destinationName);
                    settingItemList.get(itemPosition).setValidity(validity);
                    customAdapter.notifyDataSetChanged();
                    //변경 된 데이터 폰에 저장
                    editor.putString("name" + itemPosition, name)
                            .putString("distance" + itemPosition, distance)
                            .putString("destinationName" + itemPosition, destinationName)
                            .putString("destinationList" + itemPosition, destinationList)
                            .putString("validity" + itemPosition, validity);

                }

            } else if (resultCode == RESULT_CANCELED) {
                if (serviceType == SettingItem.ServiceType.UPDATE) {
                    int deleteposition = data.getIntExtra("itemPosition", 0);
                    settingItemList.remove(deleteposition);
                    customAdapter.notifyDataSetChanged();
                    //저장 된 데이터 한 칸씩 앞으로
                    for (int i = deleteposition; i < listSize - 1; i++) {
                        editor.putString("name" + i, appData.getString("name" + (i + 1), ""))
                                .putString("distance" + i, appData.getString("distance" + (i + 1), ""))
                                .putString("destinationName" + i, appData.getString("destinationName" + (i + 1), ""))
                                .putString("destinationList" + i, appData.getString("destinationList" + (i + 1), ""))
                                .putString("validity" + i, appData.getString("validity" + (i + 1), ""));
                    }
                    //마지막 저장 된 데이터 지우기
                    editor.remove("name" + (listSize - 1))
                            .remove("distance" + (listSize - 1))
                            .remove("destinationName" + (listSize - 1))
                            .remove("destinationList" + (listSize - 1))
                            .remove("validity" + (listSize - 1))
                            .putInt("listSize", listSize - 1);
                }
            }
            editor.apply();
        }
    }

}

