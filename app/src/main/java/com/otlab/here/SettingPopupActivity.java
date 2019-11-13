package com.otlab.here;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class SettingPopupActivity extends Activity {

    Intent intent;
    SettingItem.ServiceType serviceType;
    SettingItem.DestinationType destinationType;
    int itemPosition;

    TextView nameText;
    TextView distanceText;
    TextView destinationText;
    Spinner destinationTypeSpinner;
    TextView validityText;
    TextView popupName;
    Button editBtn;
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
    private void loadView() {
        //View 객체 불러오기
        nameText = findViewById(R.id.setName);
        distanceText = findViewById(R.id.setDistance);
        destinationText = findViewById(R.id.destination);
        destinationTypeSpinner = findViewById(R.id.destinationType);
        validityText = findViewById(R.id.setValidity);
        popupName = findViewById(R.id.popupName);
        editBtn = findViewById(R.id.edit);
        okBtn = findViewById(R.id.ok);
        cancelBtn = findViewById(R.id.cancel);

        //intent 및 serviceType을 불러 옴
        intent = getIntent();
        serviceType = (SettingItem.ServiceType) intent.getSerializableExtra("service");


        if (serviceType == SettingItem.ServiceType.UPDATE) {
            loadSettingInfo();
            okBtn.setText("저장");
            cancelBtn.setText("삭제");
            destinationTypeSpinner.setVisibility(View.INVISIBLE);
            editBtn.setVisibility(View.VISIBLE);
            setLoadButton();
        }
        if (serviceType == SettingItem.ServiceType.CREATE) {
            destinationTypeSpinner.setVisibility(View.VISIBLE);
            editBtn.setVisibility(View.INVISIBLE);
            setSpinner();
        }
    }

    private void setListener() {
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

    private void save() {
        if (inputIsValidate()) {
            //데이터 전달하기 후 종료
            intent.putExtra("settingName", nameText.getText().toString())
                    .putExtra("distance", distanceText.getText().toString())
                    .putExtra("destination", destinationText.getText().toString())
                    .putExtra("validity", validityText.getText().toString())
                    .putExtra("itemPosition", itemPosition);

            setResult(RESULT_OK, intent);
            finish();
        } else {

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    private void loadSettingInfo(){
        itemPosition = intent.getIntExtra("itemPosition", 0);
        popupName.setText(intent.getStringExtra("settingName"));
        nameText.setText(intent.getStringExtra("settingName"));
        distanceText.setText(intent.getStringExtra("distance"));
        destinationText.setText(intent.getStringExtra("destination"));
        validityText.setText(intent.getStringExtra("validity"));
    }

    private void setSpinner(){
        /////////// 여기서부터는 알람대상에들어가는 spinner 제어를 위한 코드입니다. ///////////
        //스피너안의 아이템의 클릭리스너
        destinationTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if(position==0){
                    goFriendSelect();
                }else if(position==1){
                   goMap();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
        // 아이템이 들어갈 리스트 선언
        List<String> spinnerItem = new ArrayList<>();
        spinnerItem.add("친구 목록");
        spinnerItem.add("목적지");
        spinnerItem.add("");
        // 스피너에 들어갈 아이템과 스피너를 연결하는 어댑터 선언, android.R.layout.simple_spinner_item은 안드로이드에서 지원해주는 기본적인 형태
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerItem) {
            @Override
            public int getCount() {return super.getCount() - 1;}
        };
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        destinationTypeSpinner.setAdapter(spinnerAdapter);
        destinationTypeSpinner.setSelection(spinnerAdapter.getCount());
    }

    private void setLoadButton(){
        editBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(destinationType == SettingItem.DestinationType.PERSON) goFriendSelect();
                        if(destinationType == SettingItem.DestinationType.PLACE) goMap();
                    }
                }
        );
    }

    private void goFriendSelect(){
        startActivityForResult(new Intent(SettingPopupActivity.this, FriendSelectActivity.class), 1);
    }
    private void goMap(){
        startActivityForResult(new Intent(getApplicationContext(), MapActivity.class), 1);
    }

    private boolean inputIsValidate() {
        //빈칸 검사
        destinationText.setText("hewi");
        int c = destinationTypeSpinner.getCount();
        if (nameText.getText().toString().length() != 0 && distanceText.getText().toString().length() != 0 && destinationText.getText().toString().length() != 0 && validityText.getText().toString().length() != 0)
            //각 칸의 유효검사
            if (isNum(distanceText.getText().toString()) && destinationTypeSpinner.getSelectedItemPosition() < destinationTypeSpinner.getCount())
                return true;
        return false;
    }

    private boolean isNum(String str) {
        for (int i = 0; i < str.length(); i++) {
            if ((int) str.charAt(i) > (int) '9' && (int) str.charAt(i) < (int) '0') return false;
        }
        return true;
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