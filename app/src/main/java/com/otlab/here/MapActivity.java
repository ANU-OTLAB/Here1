package com.otlab.here;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.util.ArrayList;

public class MapActivity extends Activity {

    private ViewGroup mapViewContainer;
    private MapView mapView;
    private MapPoint myLocation;
    private MapPoint[] destinationLocation;
    private MapPOIItem myMarker;
    private MapPOIItem[] destinationMarker = new MapPOIItem[1];
    private Location location;
    final LocationListener gpsLocationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            refreshPosition();
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onProviderDisabled(String provider) {
        }
    };
    private Handler handler;
    // 맵뷰 위에 띄워줄 구성품들 선언
    private Button goLeftBtn;
    private Button goRightBtn;
    private TextView selectedSetting;
    private SharedPreferences appData;
    private int listSize;
    private int itemposition;
    String[] targetList;

    // 이 함수는 그냥 필요합니다 이해할려고 하지 마십시요.
    private static double degtorad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    //이 함수도 필요하답니다 이해할려고 하지 마십시오.
    private static double radtodeg(double rad) {
        return (rad * 180 / Math.PI);
    }

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_map);

        loadView();
        setListener();

        ////////////////////////////////////////////////////
        final LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        handler = new Handler();

        try {
            if (checkPermission()) {
                requestPermission();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, gpsLocationListener);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, gpsLocationListener);

            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            myLocation = MapPoint.mapPointWithGeoCoord(location.getLatitude(), location.getLongitude());

            if (mapView == null) mapView = new MapView(this);
            mapViewContainer = findViewById(R.id.map_view);

            mapView.setMapCenterPoint(myLocation, false);
            mapViewContainer.addView(mapView);


            myMarker = new MapPOIItem();
            setupMarker("내 위치", myLocation, mapView, myMarker);

            //이게 내가 최초로 만든 Marker 임


            ArrayList<String> sendMsgList = new ArrayList();
            sendMsgList.add("observerX");
            sendMsgList.add(myLocation.getMapPointGeoCoord().latitude + "");
            sendMsgList.add("observerY");
            sendMsgList.add(myLocation.getMapPointGeoCoord().longitude + "");
            sendMsgList.add("observer");
            sendMsgList.add(appData.getString("id", ""));


            targetList = appData.getString("destinationList" + itemposition, "").split(" ");

            if (targetList.length == 1) {
                sendMsgList.add("numberOfTarget");
                sendMsgList.add(targetList.length + "");
                sendMsgList.add("target");
                sendMsgList.add(targetList[0]);
            } else if (targetList.length >= 2) {
                sendMsgList.add("numberOfTarget");
                sendMsgList.add(targetList.length + "");

                for (int i = 0; i < targetList.length; i++) {
                    sendMsgList.add("target" + (i + 1));
                    sendMsgList.add(targetList[i]);
                }
            }

            String receiveMsg = (String) (new MessageThread(sendMsgList, "", "http://iclab.andong.ac.kr/here/location.jsp").execute().get());
            String[] positionList = receiveMsg.split("/");
            destinationLocation = new MapPoint[positionList.length];
            destinationMarker = new MapPOIItem[positionList.length];
            for (int i = 0; i < positionList.length; i++) {
                String[] buffer = positionList[i].split(" ");
                destinationLocation[i] = MapPoint.mapPointWithGeoCoord(Double.parseDouble(buffer[0]), Double.parseDouble(buffer[1]));
                destinationMarker[i] = new MapPOIItem();
                setupMarker(targetList[i], destinationLocation[i], mapView, destinationMarker[i]);

                /*
                destinationMarker[i].setMarkerType(MapPOIItem.MarkerType.CustomImage); //마커를 커스텀마커 타입으로 선언
                destinationMarker[i].setCustomImageResourceId(R.drawable.whoami); //마커에 사용할 이미지 넣기
                mapView.addPOIItem(destinationMarker[i]); // 맵에 표시
                 */
            }

            MapPOIItem p1 = new MapPOIItem();
            MapPOIItem p2 = new MapPOIItem();
            setupMarker("1", MapPoint.mapPointWithCONGCoord(36.543411, 128.791954), mapView, p1);
            setupMarker("2", MapPoint.mapPointWithCONGCoord(128.791954,  36.543411), mapView, p2);


            //원그리기//
            // 원그리기 (중심의 좌표, 반지름, 선의 색깔, 원안에 색깔)
            //MapCircle circle = new MapCircle(destinationLocation, 50, Color.argb(128,255,0,0), Color.argb(128,0,255,0));
            //mapView.addCircle(circle); // 맵에 표시
            ////////////
            double distance = calculate(myLocation.getMapPointGeoCoord().latitude, myLocation.getMapPointGeoCoord().longitude, destinationLocation[0].getMapPointGeoCoord().latitude, destinationLocation[0].getMapPointGeoCoord().longitude);// 좌표사이의 거리계산
            // distance를 int형으로 타입캐스팅후 30m안으로 가까이 올때 백그라운드 작업 시작
            if ((int) distance < 30) {
                    /*OneTimeWorkRequest testwork = new OneTimeWorkRequest.Builder(BackGroundWorker.class).build(); // 아무런 제약 조건없이 백그라운드 work 생성
                    WorkManager.getInstance().enqueue(testwork); // 생성된 work를 queue에 넣으면 폰이 알아서 시작*/
            }


        } catch (Exception e) {
            Log.d("??", e.getMessage());
            Toast.makeText(getApplicationContext(), "GPS 연결에 실패하였습니다.", Toast.LENGTH_LONG).show();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }
    }

    private void loadView() {
        // 위에 세팅 정보를 가져오고 다른 세팅정보로도 넘어갈수있게 구현
        goLeftBtn = findViewById(R.id.goLeft);
        goRightBtn = findViewById(R.id.goRight);
        selectedSetting = findViewById(R.id.selectedSettingInformation);
        appData = getSharedPreferences("appData", MODE_PRIVATE);
        listSize = appData.getInt("listSize", 0);
        itemposition = 0;
        selectedSetting.setText(appData.getString("name" + itemposition, ""));
    }

    private void setListener() {
        goLeftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (itemposition - 1 < 0) {
                    itemposition = listSize - 1;
                    selectedSetting.setText(appData.getString("name" + itemposition, ""));
                } else {
                    itemposition--;
                    selectedSetting.setText(appData.getString("name" + (itemposition), ""));
                }
            }
        });
        goRightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (itemposition >= listSize - 1) {
                    itemposition = 0;
                    selectedSetting.setText(appData.getString("name" + itemposition, ""));
                } else {
                    itemposition++;
                    selectedSetting.setText(appData.getString("name" + (itemposition), ""));
                }

            }
        });
    }

    private boolean checkPermission() {
        return Build.VERSION.SDK_INT >= 23
                && ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
    }

    private void refreshPosition() {
        try {
            myMarker.setMapPoint(MapPoint.mapPointWithGeoCoord(location.getLatitude(), location.getLongitude()));

        } catch (Exception e) {
        }
    }

    private void setupMarker(String name, MapPoint mapPoint, MapView mapView, MapPOIItem marker) {
        try {
            double x = mapPoint.getMapPointGeoCoord().longitude;
            double y = mapPoint.getMapPointGeoCoord().latitude;
            marker.setItemName(name);
            marker.setMapPoint(mapPoint);
            marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin);
            mapView.addPOIItem(marker);
            mapView.setZoomLevel(1, true);
        } catch (Exception e) {
        }
    }

    // 이함수는 WGS84 기준의 두 좌표 사이의 거리를 구하는 함수입니다. 저도 뭔소린지 모르겠습니다.
    private double calculate(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = (Math.sin(degtorad(lat1)) * Math.sin(degtorad(lat2))) + (Math.cos(degtorad(lat1)) * Math.cos(degtorad(lat2)) * Math.cos(degtorad(theta)));

        dist = Math.acos(dist);
        dist = radtodeg(dist);
        dist = dist * 60 * 1.1515;
        dist = dist * 1609.344;
        return (dist);

    }

    public void onPause() {
        super.onPause();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }
}