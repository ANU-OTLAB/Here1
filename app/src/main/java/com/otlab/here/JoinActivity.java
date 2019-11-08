package com.otlab.here;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class JoinActivity extends AppCompatActivity {

    private EditText nameText;
    private EditText phText;
    private EditText idText;
    private EditText pwText;
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        nameText = findViewById(R.id.name);
        phText = findViewById(R.id.ph);
        idText = findViewById(R.id.id);
        pwText = findViewById(R.id.pw);
        btn = findViewById(R.id.submit);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (checkValidation()) {
                        finish();
                    } else {

                    }
                }catch (Exception e){
                    Log.d("!!!!!", e.toString());
                }

            }
        });


    }

    private boolean checkValidation() {

        return nameText.length() != 0 &&
                phText.length() != 0 &&
                idText.length() != 0 &&
                pwText.length() != 0;
    }
}