package com.otlab.here;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class SettingPopupActivity extends Activity {

    Intent intent;
    SettingItem.ServiceType serviceType;
    int itemPosition;

    TextView nameText;
    TextView distanceText;
    TextView destinationText;
    TextView validityText;
    TextView popupName;
    Button okBtn;
    Button cancelBtn;

    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_popup_setting);

        loadView();
        setListener();
    }

    //view 불러 오기
    private void loadView(){
        //View 객체 불러오기
        nameText = findViewById(R.id.setName);
        distanceText = findViewById(R.id.setDistance);
        destinationText = findViewById(R.id.setDestination);
        validityText = findViewById(R.id.setValidity);
        popupName = findViewById(R.id.popupName);
        okBtn = findViewById(R.id.ok);
        cancelBtn = findViewById(R.id.cancel);

        //intent 및 serviceType을 불러 옴
        intent = getIntent();
        itemPosition = intent.getIntExtra("itemPosition", 0);
        serviceType = (SettingItem.ServiceType) intent.getSerializableExtra("service");
        //intent를 통해 view의 text 값 초기화
        nameText.setText(intent.getStringExtra("settingName"));
        distanceText.setText(intent.getStringExtra("distance"));
        destinationText.setText(intent.getStringExtra("destination"));
        validityText.setText(intent.getStringExtra("time"));
        popupName.setText(intent.getStringExtra("settingName"));

        if(serviceType == SettingItem.ServiceType.UPDATE){
            okBtn.setText("저장");
            cancelBtn.setText("삭제");
        }
    }
    private void setListener(){
        //ok버튼 리스너
        okBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        save();
                    }
                }
        );
        //cancel버튼 리스너
        cancelBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        setResult(RESULT_CANCELED, intent);
                        finish();
                    }
                }
        );
    }

    public void save() {
        //데이터 전달하기 후 종료
        intent.putExtra("settingName", nameText.getText().toString());
        intent.putExtra("distance", distanceText.getText().toString());
        intent.putExtra("destination", destinationText.getText().toString());
        intent.putExtra("validity", validityText.getText().toString());
        intent.putExtra("itemPosition", itemPosition);

        setResult(RESULT_OK, intent);
        finish();
    }

    //바깥레이어 클릭시 안닫히게
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return event.getAction() != MotionEvent.ACTION_OUTSIDE;
    }

    @Override
    public void onBackPressed() {
        return;
    }
}
