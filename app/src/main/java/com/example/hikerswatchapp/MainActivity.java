package com.example.hikerswatchapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    LocationManager locationManager;
    LocationListener locationListener;

    TextView latView;
    TextView longView;
    TextView altView;
    TextView accView;
    TextView addressView;

    static String address;
    static Location loc;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            }
        }

    }

    public void updateLocationInfo(Location location){
        loc = location;

        latView.setText(String.valueOf(location.getLatitude()));
        longView.setText(String.valueOf(location.getLongitude()));
        altView.setText(String.valueOf(location.getAltitude()));
        accView.setText(String.valueOf(location.getAccuracy()));

        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

        try {
            address = "could not find address!";
            List<Address> listAddress= geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (listAddress != null && listAddress.size() > 0){

                address = "";

                if (listAddress.get(0).getThoroughfare() != null){
                    address += listAddress.get(0).getThoroughfare() + ", ";
                }

                if (listAddress.get(0).getLocality() != null){
                    address += listAddress.get(0).getLocality() + ", ";
                }

                if (listAddress.get(0).getPostalCode() != null){
                    address += listAddress.get(0).getPostalCode() + ", ";
                }

                if (listAddress.get(0).getAdminArea() != null){
                    address += listAddress.get(0).getAdminArea();
                }


            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        addressView.setText(address);


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        latView = (TextView) findViewById(R.id.latitudeView);
        longView = (TextView) findViewById(R.id.longitudeView);
        altView = (TextView) findViewById(R.id.altitudeView);
        accView = (TextView) findViewById(R.id.accuracyView);
        addressView = (TextView) findViewById(R.id.addressView);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                updateLocationInfo(location);

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (lastKnownLocation != null) {
                updateLocationInfo(lastKnownLocation);
            }
        }

    }

    public void openMap(View view) {
        if (loc != null) {
            Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
            startActivity(intent);
        }

    }
}