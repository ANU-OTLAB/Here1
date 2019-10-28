package com.otlab.here;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import java.sql.Time;

public class SettingPopupActivity extends Activity {

    TextView NameText;
    TextView DistanceText;
    TextView DestinationText;
    TextView TimeText;
    TextView Popupname;
    int ItemPosition;

    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_popup_setting);

        NameText = (TextView)findViewById(R.id.SetName);
        DistanceText = (TextView)findViewById(R.id.SetDistance);
        DestinationText = (TextView)findViewById(R.id.SetDestination);
        TimeText = (TextView)findViewById(R.id.SetTime);
        Popupname = (TextView)findViewById(R.id.PopupName);

        //데이터 가져오기
        Intent intent = getIntent();
        String SettingName = intent.getStringExtra("settingName");
        String Distance = intent.getStringExtra("distance");
        String Destination = intent.getStringExtra("destination");
        String Time = intent.getStringExtra("time");
        ItemPosition = intent.getIntExtra("position",0);

        NameText.setText(SettingName);
        DistanceText.setText(Distance);
        DestinationText.setText(Destination);
        TimeText.setText(Time);
        Popupname.setText(SettingName);
    }
    public void mOnClose(View v){
        //데이터 전달하기
        Intent intent = new Intent();

        intent.putExtra("R_settingName",NameText.getText().toString());
        intent.putExtra("R_distance",DistanceText.getText().toString());
        intent.putExtra("R_destination", DestinationText.getText().toString());
        intent.putExtra("R_time", TimeText.getText().toString());
        intent.putExtra("R_ItemPosition", ItemPosition);

        setResult(RESULT_OK, intent);

        //액티비티(팝업) 닫기
        finish();
    }
    public void Delete(View v){
        Intent intent = new Intent();

        intent.putExtra("R_ItemPosition",ItemPosition);

        setResult(RESULT_CANCELED,intent);
        finish();
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        if(event.getAction()==MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        //안드로이드 백버튼 막기
        return;
    }
}
