package com.example.tuionf.locationtest;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextView tv;
    private TextView tv1;
    private LocationManager mLocationManager;
    private String provider;
    private Location location;
    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = (TextView) findViewById(R.id.tv);
        tv1 = (TextView) findViewById(R.id.tv1);

        //获取LocationManager的实例
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //获取所有当前可用的位置提供器
        List<String>  providersList = mLocationManager.getProviders(true);
        if (providersList.contains(LocationManager.GPS_PROVIDER)){
            provider = LocationManager.GPS_PROVIDER;
        }else if (providersList.contains(LocationManager.NETWORK_PROVIDER)){
            provider = LocationManager.NETWORK_PROVIDER;
        }else {
            Toast.makeText(MainActivity.this,"请打开网络或者GPS",Toast.LENGTH_SHORT).show();
            return;
        }
        try{

             location = mLocationManager.getLastKnownLocation(provider);
            if (location != null){
                showLocation(location);
                mLocationManager.requestLocationUpdates(provider,5000,1,locationListener);
            }
        }catch (SecurityException e){
            e.printStackTrace();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mLocationManager != null){
            try {
                mLocationManager.removeUpdates(locationListener);

            }catch (SecurityException e){
                e.printStackTrace();
            }
        }
    }

    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            showLocation(location);
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };

    private void showLocation(final Location location){
        double latitude  =  location.getLatitude();
        double longitude  =  location.getLongitude();
        String currentPosition = "Latitude is "+latitude +"\n"+ "Longitude is "+longitude;
        tv.setText(currentPosition);

        Geocoder gc = new Geocoder(MainActivity.this, Locale.getDefault());
        List<Address> locationList = null;
        try {
            locationList = gc.getFromLocation(latitude, longitude, 1);
        }catch (Exception e){
            e.printStackTrace();
        }
        //获取address的实例
        Address address = locationList.get(0);
        Log.d(TAG, "showLocation: "+address);
        String countryName = address.getCountryName();
        String cityName = address.getLocality();
        String line = "";
        for (int i=0;address.getAddressLine(i)!=null;i++){
            line = address.getAddressLine(i);
            Log.d(TAG, "showLocation: "+line);
        }

        tv.setText("countryName is "+countryName+" cityName is "+cityName+" line "+address.getAddressLine(0));
        }
}
