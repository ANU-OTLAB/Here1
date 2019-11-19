package com.otlab.here;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class SettingPopupActivity extends Activity {

    Intent intent;
    SettingItem.ServiceType serviceType;
    SettingItem.DestinationType destinationType;
    int itemPosition;
    private SharedPreferences appData; // 나의 아이디를 가져오기 위해서 SharedPreferneces 선언
    Destination[] destination;
    String destinationName;

    TextView nameText;
    TextView distanceText;
    TextView destinationText;
    Spinner destinationTypeSpinner;
    TextView validityText;
    TextView popupName;
    Button editBtn;
    Button okBtn;
    Button cancelBtn;
    Button dateBtn;

    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_popup_setting);

        loadView();
        setListener();
    }

    //view 불러 오기
    private void loadView() {
        appData = getSharedPreferences("appData", MODE_PRIVATE);

        //View 객체 불러오기
        nameText = findViewById(R.id.setName);
        distanceText = findViewById(R.id.setDistance);
        destinationText = findViewById(R.id.destinationText);
        destinationTypeSpinner = findViewById(R.id.destinationType);
        validityText = findViewById(R.id.setValidity);
        popupName = findViewById(R.id.popupName);
        editBtn = findViewById(R.id.edit);
        okBtn = findViewById(R.id.ok);
        cancelBtn = findViewById(R.id.cancel);
        dateBtn = findViewById(R.id.setVaildityBtn);

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
        dateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateView();
            }
        });

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
    }

    private void save() {
        if (inputIsValidate()) {


            //데이터 전달하기 후 종료
            intent.putExtra("settingName", nameText.getText().toString())
                    .putExtra("distance", distanceText.getText().toString())
                    .putExtra("destination", destination)
                    .putExtra("destinationName", destinationText.getText().toString())
                    .putExtra("validity", validityText.getText().toString())
                    .putExtra("itemPosition", itemPosition);
            if(destination == null) intent.putExtra("destinationList",appData.getString("destinationList"+itemPosition, ""));
            //수락 대기 목록 디비에 넘기기
            try {
                String myid = appData.getString("id", "");
                String address = "http://iclab.andong.ac.kr/here/authorityInformationSave.jsp";
                String recv = "";
                ArrayList<String> sendmsg = new ArrayList<>();
                sendmsg.add("observer");
                sendmsg.add(myid);
                sendmsg.add("expiryDate");
                sendmsg.add(validityText.getText().toString());
                sendmsg.add("validity");
                sendmsg.add("REQUEST");
                sendmsg.add("numberOfTarget");
                sendmsg.add(destination.length+"");
                if(destination.length==1){
                    sendmsg.add("target");
                    sendmsg.add(destinationText.getText().toString());
                }else if(destination.length>=2){
                    for(int i=0 ; i<destination.length ; i++){
                        sendmsg.add("target"+(i+1));
                        sendmsg.add(destination[i].getDestinationName());
                    }
                }

                MessageThread send = new MessageThread(sendmsg, recv, address);
                String recvData = (String) send.execute().get();

            } catch(Exception e){
            }
            setResult(RESULT_OK, intent);
            finish();
        } else {

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            destinationTypeSpinner.setVisibility(View.INVISIBLE);
            editBtn.setVisibility(View.VISIBLE);

            destination = (Destination[]) data.getSerializableExtra("destination");
            destinationName = destination[0].getDestinationName()+ (destination.length>=2?"외 "+(destination.length-1)+"명":"");
            destinationText.setText(destinationName);
        }else if(resultCode == RESULT_CANCELED){
            setResult(RESULT_CANCELED, intent);
            finish();
        }
    }

    private void loadSettingInfo(){
        itemPosition = intent.getIntExtra("itemPosition", 0);
        popupName.setText(intent.getStringExtra("settingName"));
        nameText.setText(intent.getStringExtra("settingName"));
        distanceText.setText(intent.getStringExtra("distance"));
        destinationText.setText(intent.getStringExtra("destinationName"));
        validityText.setText(intent.getStringExtra("validity"));
    }

    private void setSpinner(){
        /////////// 여기서부터는 알람대상에들어가는 spinner 제어를 위한 코드입니다. ///////////

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

    private void dateView(){
        // 날짜선택 버튼을 눌렀을때 날짜 다이얼로그를 불러오는 함수
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int day) {
                    month++; // 해주는 이유는 무슨 이유인지는 모르겠으나 항상 고른 달보다 -1되서 나옴 인터넷에 예제들 보니깐 다 +1해주더라
                    String date = year +"-"+ month+"-"+day;
                    Toast.makeText(SettingPopupActivity.this, date, Toast.LENGTH_SHORT).show();// 날짜 제대로 가는지 확인해주기위해 메시지 띄움
                    validityText.setText(date);
                }
            },2019,1,1/*초기상태*/);
            datePickerDialog.setMessage("날짜 선택");//다이얼로그 이름 선언
            datePickerDialog.show();

        }
    }
    @Override
    public void onBackPressed() {
        return;
    }
}