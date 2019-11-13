package com.otlab.here;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import net.daum.mf.map.api.MapCircle;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

public class MapActivity extends Activity {

    ViewGroup mapViewContainer;
    private MapView mapView;
    private MapPoint myLocation;
    private MapPoint destinationLocation;
    private MapPOIItem myMarker;
    private MapPOIItem[] destinationMarker = new MapPOIItem[1];
    private Location location;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_map);

        final LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        handler = new Handler();

        try {
            if (checkPermission()) {
                requestPermission();
                finish();
            }


                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, gpsLocationListener);
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, gpsLocationListener);

            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                myLocation = MapPoint.mapPointWithGeoCoord(location.getLatitude(), location.getLongitude());

                mapView = new MapView(this);
                mapViewContainer = findViewById(R.id.map_view);

                mapView.setMapCenterPoint(myLocation, false);
                mapViewContainer.addView(mapView);


                myMarker = new MapPOIItem();
                newMarker("내 위치", myLocation, mapView, myMarker);
                //독일마을 좌표
                destinationLocation = MapPoint.mapPointWithGeoCoord(36.54575536601466, 128.7950717506389);
                //커스텀마커//
                destinationMarker[0] = new MapPOIItem();
                destinationMarker[0].setItemName("커스텀 마커"); // 마커이름
                destinationMarker[0].setMapPoint(destinationLocation); // 마커좌표
                destinationMarker[0].setMarkerType(MapPOIItem.MarkerType.CustomImage); //마커를 커스텀마커 타입으로 선언
                destinationMarker[0].setCustomImageResourceId(R.drawable.whoami); //마커에 사용할 이미지 넣기
                mapView.addPOIItem(destinationMarker[0]); // 맵에 표시
                //////////////
                //원그리기//
                MapCircle circle = new MapCircle(destinationLocation, 50, Color.argb(128,255,0,0), Color.argb(128,0,255,0));
                // 원그리기 (중심의 좌표, 반지름, 선의 색깔, 원안에 색깔)
                mapView.addCircle(circle); // 맵에 표시
                ////////////
                double distance = calculate(myLocation.getMapPointGeoCoord().latitude, myLocation.getMapPointGeoCoord().longitude, destinationLocation.getMapPointGeoCoord().latitude, destinationLocation.getMapPointGeoCoord().longitude);// 좌표사이의 거리계산
                // distance를 int형으로 타입캐스팅후 30m안으로 가까이 올때 백그라운드 작업 시작
                if((int)distance < 30){
                    OneTimeWorkRequest testwork = new OneTimeWorkRequest.Builder(BackGroundWorker.class).build(); // 아무런 제약 조건없이 백그라운드 work 생성
                    WorkManager.getInstance().enqueue(testwork); // 생성된 work를 queue에 넣으면 폰이 알아서 시작
                }


        } catch (Exception e) {
            this.recreate();
            Log.d("??", e.getMessage());
            Toast.makeText(getApplicationContext(), "GPS 연결에 실패하였습니다.", Toast.LENGTH_LONG).show();
        }
    }

    private boolean checkPermission(){
        if(Build.VERSION.SDK_INT >= 23
                && ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            return true;
        return false;
    }

    private void requestPermission(){
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
    }

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

    private void refreshPosition() {
        try {
            myMarker.setMapPoint(MapPoint.mapPointWithGeoCoord(location.getLatitude(), location.getLongitude()));

            Task task = new Task(handler, myMarker, destinationMarker);
            task.execute();

        } catch (Exception e) {
        }
    }

    private void newMarker(String name, MapPoint mapPoint, MapView mapView, MapPOIItem marker) {
        try {
            marker.setItemName(name);
            marker.setTag(0);
            marker.setMapPoint(mapPoint);
            marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin);
            mapView.addPOIItem(marker);
            mapView.setZoomLevel(1, true);
        } catch (Exception e) {
        }
    }
    // 이함수는 WGS84 기준의 두 좌표 사이의 거리를 구하는 함수입니다. 저도 뭔소린지 모르겠습니다.
    private double calculate(double lat1, double lon1, double lat2, double lon2){
        double theta = lon1 - lon2;
        double dist = (Math.sin(degtorad(lat1))*Math.sin(degtorad(lat2))) + (Math.cos(degtorad(lat1))*Math.cos(degtorad(lat2))*Math.cos(degtorad(theta)));

        dist = Math.acos(dist);
        dist = radtodeg(dist);
        dist = dist*60*1.1515;
        dist = dist*1609.344;
        return (dist);

    }
    // 이 함수는 그냥 필요합니다 이해할려고 하지 마십시요.
    private static double degtorad(double deg){
        return (deg * Math.PI/180.0);
    }
    //이 함수도 필요하답니다 이해할려고 하지 마십시오.
    private static double radtodeg(double rad){
        return(rad * 180/ Math.PI);
    }
    public void onPause() {
        super.onPause();
        finish();
    }
}