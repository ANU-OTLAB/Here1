package com.otlab.here;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
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

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

public class MapActivity extends Activity {

    ViewGroup mapViewContainer = null;
    private MapView mapView = null;
    private MapPoint myMapPoint = null;
    private MapPoint desMapPoint = null;
    private MapPOIItem myMarker = null;
    private MapPOIItem[] desMarker = new MapPOIItem[1];
    private Location location = null;
    private Handler handler = null;
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

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_map);
        Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_SHORT).show();
        final LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        handler = new Handler();


        try {


            if (Build.VERSION.SDK_INT >= 23
                    && ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 0);
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
            } else {


                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, gpsLocationListener);
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, gpsLocationListener);

                myMapPoint = MapPoint.mapPointWithGeoCoord(location.getLatitude(), location.getLongitude());

                mapView = new MapView(this);
                mapViewContainer = findViewById(R.id.map_view);
                mapView.setMapCenterPoint(myMapPoint, false);
                mapViewContainer.addView(mapView);


                myMarker = new MapPOIItem();
                newMarker("내 위치", myMapPoint, mapView, myMarker);

                desMapPoint = MapPoint.mapPointWithGeoCoord(37.576947, 126.976830);
                desMarker[0] = new MapPOIItem();
                newMarker("상대 위치", desMapPoint, mapView, desMarker[0]);
            }
        } catch (Exception e) {
            this.recreate();
            Log.d("??", e.getMessage());
            Toast.makeText(getApplicationContext(), "GPS 연결에 실패하였습니다.", Toast.LENGTH_LONG).show();
        }
    }

    private void refreshPosition() {
        try {
            myMarker.setMapPoint(MapPoint.mapPointWithGeoCoord(location.getLatitude(), location.getLongitude()));

            Task task = new Task(handler, myMarker, desMarker);
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

    public void onPause() {
        super.onPause();
        finish();
    }
}
