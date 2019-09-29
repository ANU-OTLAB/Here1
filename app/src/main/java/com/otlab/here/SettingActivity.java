package com.otlab.here;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class SettingActivity extends Activity {

    TextView txtView;

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_setting);
        txtView = (TextView)findViewById(R.id.result);
    }

    public void onButtonClick(View view) {
        Intent intent = new Intent(this, SettingPopupActivity.class);
        intent.putExtra("data", "Test Popup");
        startActivityForResult(intent, 1);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==1){
            if(resultCode==RESULT_OK){
                String result = data.getStringExtra("result");
                txtView.setText(result);

            }
        }
    }
}
