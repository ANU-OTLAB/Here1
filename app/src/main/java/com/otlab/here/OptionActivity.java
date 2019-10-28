package com.otlab.here;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class OptionActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_option);

        Button logoutbtn = findViewById(R.id.logout);
        logoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });

    }

    protected void logout(){
        SharedPreferences appData = getSharedPreferences("appData", MODE_PRIVATE);
        SharedPreferences.Editor editor = appData.edit();
        editor.clear();
        editor.commit();
        startActivity(new Intent(getApplication(), SplashActivity.class));
        MainActivity ma = (MainActivity) getIntent().getSerializableExtra("parent");
        ma.finish();
        finish();
    }
}
