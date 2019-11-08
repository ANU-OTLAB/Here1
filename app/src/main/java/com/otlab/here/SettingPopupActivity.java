package com.otlab.here;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

public class SettingPopupActivity extends Activity {

    Intent intent;
    SettingItem.ServiceType serviceType;

    TextView nameText;
    TextView distanceText;
    TextView destinationText;
    TextView timeText;
    TextView popupName;
    int itemPosition;

    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_popup_setting);

        //view 불러 오기
        nameText = findViewById(R.id.SetName);
        distanceText = findViewById(R.id.SetDistance);
        destinationText = findViewById(R.id.SetDestination);
        timeText = findViewById(R.id.SetTime);
        popupName = findViewById(R.id.PopupName);

        //데이터 가져오기
        intent = getIntent();
        serviceType = (SettingItem.ServiceType) intent.getSerializableExtra("service");

        String SettingName = "";
        String Distance = "";
        String Destination = "";
        String Time = "";
        //service 생성 시
        if (serviceType == SettingItem.ServiceType.CREATE) {

        }
        //service 수정 시
        if (serviceType == SettingItem.ServiceType.UPDATE) {
            SettingName = intent.getStringExtra("settingName");
            Distance = intent.getStringExtra("distance");
            Destination = intent.getStringExtra("destination");
            Time = intent.getStringExtra("time");
        }
        //service 삭제 시
        if (serviceType == SettingItem.ServiceType.DELETE) {

        }

        itemPosition = intent.getIntExtra("position", 0);
        //불러 온 데이터 반영
        nameText.setText(SettingName);
        distanceText.setText(Distance);
        destinationText.setText(Destination);
        timeText.setText(Time);
        popupName.setText(SettingName);
    }

    public void mOnClose(View v) {
        //데이터 전달하기

        intent.putExtra("settingName", nameText.getText().toString());
        intent.putExtra("distance", distanceText.getText().toString());
        intent.putExtra("destination", destinationText.getText().toString());
        intent.putExtra("time", timeText.getText().toString());
        intent.putExtra("itemPosition", itemPosition);

        setResult(RESULT_OK, intent);

        //액티비티(팝업) 닫기
        finish();
    }

    public void Delete(View v) {
        Intent intent = new Intent();
        intent.putExtra("itemPosition", itemPosition);

        setResult(RESULT_CANCELED, intent);
        finish();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        return event.getAction() != MotionEvent.ACTION_OUTSIDE;
    }

    @Override
    public void onBackPressed() {
        //안드로이드 백버튼 막기
        return;
    }
}
