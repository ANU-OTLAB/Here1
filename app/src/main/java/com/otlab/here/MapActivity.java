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

    private Button goLeftBtn;
    private Button goRightBtn;
    private TextView selectedSetting;

    private ViewGroup mapViewContainer;
    private MapView mapView;
    private MapPoint myLocation;
    private MapPoint[] destinationLocation;
    private MapPOIItem myMarker;
    private MapPOIItem[] destinationMarker = new MapPOIItem[1];
    private Location location;
    private LocationListener gpsLocationListener;
    private LocationManager locationManager;
    private SharedPreferences appData;

    private int listSize;
    private int settingItemPosition;
    private ArrayList<String> sendMsgList;
    private String[] targetList;
    private String receiveMsg;
    private String[] positionList;
    private String[] buffer;


    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_map);

        Init();
        setListener();


        try {
            initMap();
            loadMap();

        } catch (Exception e) {
            Log.d("??", e.getMessage());
            Toast.makeText(getApplicationContext(), "GPS 연결에 실패하였습니다.", Toast.LENGTH_LONG).show();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }
    }

    private void Init() {
        goLeftBtn = findViewById(R.id.goLeft);
        goRightBtn = findViewById(R.id.goRight);
        selectedSetting = findViewById(R.id.selectedSettingInformation);
        mapViewContainer = findViewById(R.id.map_view);

        appData = getSharedPreferences("appData", MODE_PRIVATE);
        listSize = appData.getInt("listSize", 0);
        settingItemPosition = 0;
        selectedSetting.setText(appData.getString("name" + settingItemPosition, ""));

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    }

    private void setListener() {
        goLeftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (settingItemPosition - 1 < 0) {
                    settingItemPosition = listSize - 1;
                    selectedSetting.setText(appData.getString("name" + settingItemPosition, ""));
                } else {
                    settingItemPosition--;
                    selectedSetting.setText(appData.getString("name" + (settingItemPosition), ""));
                }
                removeMarker();
                reloadAppData();
                initMarker();
            }
        });
        goRightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (settingItemPosition >= listSize - 1) {
                    settingItemPosition = 0;
                    selectedSetting.setText(appData.getString("name" + settingItemPosition, ""));
                } else {
                    settingItemPosition++;
                    selectedSetting.setText(appData.getString("name" + (settingItemPosition), ""));
                }
                removeMarker();
                reloadAppData();
                initMarker();
            }
        });
        gpsLocationListener = new LocationListener() {
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
    }

    private void initMap() {
        if (checkPermission()) {
            requestPermission();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, gpsLocationListener);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, gpsLocationListener);
        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        myLocation = MapPoint.mapPointWithGeoCoord(location.getLatitude(), location.getLongitude());
        if (mapView == null) mapView = new MapView(this);
        mapView.setMapCenterPoint(myLocation, false);
        mapViewContainer.addView(mapView);
    }

    private void loadMap(){
        myMarker = new MapPOIItem();
        setupMarker("내 위치", myLocation, mapView, myMarker);

        sendMsgList = new ArrayList();
        sendMsgList.add("observer");
        sendMsgList.add(appData.getString("id", ""));
        sendMsgList.add("observerX");
        sendMsgList.add(myLocation.getMapPointGeoCoord().latitude + "");
        sendMsgList.add("observerY");
        sendMsgList.add(myLocation.getMapPointGeoCoord().longitude + "");

        reloadAppData();
        initMarker();

    }

    private void reloadAppData(){
        while(sendMsgList.size() > 6) sendMsgList.remove(sendMsgList.size()-1);
        targetList = appData.getString("destinationList" + settingItemPosition, "").split(" ");

        if (targetList.length == 1 && !targetList[0].equals("")) {
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
    }

    private void initMarker(){
        try {
            receiveMsg = (String) (new MessageThread(sendMsgList, "", "http://iclab.andong.ac.kr/here/location.jsp").execute().get());
            positionList = receiveMsg.split("/");
            destinationLocation = new MapPoint[positionList.length];
            destinationMarker = new MapPOIItem[positionList.length];
            for (int i = 0; i < positionList.length; i++) {
                buffer = positionList[i].split(" ");
                destinationLocation[i] = MapPoint.mapPointWithGeoCoord(Double.parseDouble(buffer[0]), Double.parseDouble(buffer[1]));
                destinationMarker[i] = new MapPOIItem();
                setupMarker(targetList[i], destinationLocation[i], mapView, destinationMarker[i]);
            }
        }catch (Exception e){}
    }

    private void removeMarker(){
        for(int i=0 ; i<destinationMarker.length ; i++) {
            mapView.removePOIItem(destinationMarker[i]);
        }
    }

    private void syncMarker(){
        try {
            receiveMsg = (String) (new MessageThread(sendMsgList, "", "http://iclab.andong.ac.kr/here/location.jsp").execute().get());
            positionList = receiveMsg.split("/");
            for (int i = 0; i < positionList.length; i++) {
                buffer = positionList[i].split(" ");
                destinationLocation[i] = MapPoint.mapPointWithGeoCoord(Double.parseDouble(buffer[0]), Double.parseDouble(buffer[1]));
                setupMarker(targetList[i], destinationLocation[i], mapView, destinationMarker[i]);
            }
        }catch (Exception e){}
    }

    private boolean checkPermission() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
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
            sendMsgList.set(4, location.getLatitude() + "");
            sendMsgList.set(6, location.getLongitude() + "");
            syncMarker();
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

    // 이 함수는 그냥 필요합니다 이해할려고 하지 마십시요.
    private static double degtorad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    //이 함수도 필요하답니다 이해할려고 하지 마십시오.
    private static double radtodeg(double rad) {
        return (rad * 180 / Math.PI);
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