package com.otlab.here;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {/**/

    ImageView mapImg;
    TextView optText;
    TextView setText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadView();
        setListener();
    }

    private void loadView() {
        mapImg = findViewById(R.id.mapImg);
        optText = findViewById(R.id.optText);
        setText = findViewById(R.id.setText);
    }

    private void setListener() {
        //맵으로 가기
        mapImg.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                        if (!(locationManager != null && locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))) {
                            //GPS 설정화면으로 이동
                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            intent.addCategory(Intent.CATEGORY_DEFAULT);
                            startActivity(intent);
                            finish();
                        } else {
                            startActivity(new Intent(getApplication(), MapActivity.class));
                            finish();
                        }
                    }
                }
        );
        //옵션으로 가기
        optText.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(getApplication(), OptionActivity.class));
                        finish();
                    }
                }
        );
        //세팅으로 가기
        setText.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(getApplication(), SettingActivity.class));
                    }
                }
        );
    }
/*
    try {
        PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
        for (Signature signature : info.signatures) {
            MessageDigest md;
            md = MessageDigest.getInstance("SHA");
            md.update(signature.toByteArray());
            String key = new String(Base64.encode(md.digest(), 0));
            Log.d("Hash key:", "!!!!!!!" + key + "!!!!!!");
        }
    } catch (Exception e) {
        Log.e("name not found", e.toString());
    }*/
}
