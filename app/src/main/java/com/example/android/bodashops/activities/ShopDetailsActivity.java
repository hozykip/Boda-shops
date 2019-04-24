package com.example.android.bodashops.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.android.bodashops.Config;
import com.example.android.bodashops.R;
import com.example.android.bodashops.adapters.RegistrationStepsAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class ShopDetailsActivity extends AppCompatActivity implements LocationListener {

    private final String[] views = {"Personal details", "Shop details"};
    private FloatingActionButton fab;

    private TextInputEditText shopNameEditText, locationEditText, descriptionEditText;
    private String $shopName, $location, $description;
    private ProgressBar progressBar;

    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    protected LocationManager locationManager;
    protected LocationListener locationListener;
    protected Context context;
    String lat;
    String provider;
    protected String latitude, longitude;
    protected boolean gps_enabled, network_enabled;

    private HashMap<String, String> cordinatesMap;
    private int cordinateCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_details);

        preferences = getApplication().getSharedPreferences(Config.PREF_NAME, Config.PRIVATE_MODE);

        cordinatesMap = new HashMap<>();

        ListView mListView = (ListView) findViewById(R.id.myCustomList2);
        fab = (FloatingActionButton) findViewById(R.id.fab_goto_confirm_dets_activity);
        shopNameEditText = (TextInputEditText) findViewById(R.id.shopNameET);
        locationEditText = (TextInputEditText) findViewById(R.id.locationET);
        descriptionEditText = (TextInputEditText) findViewById(R.id.shopDescET);
        progressBar = (ProgressBar) findViewById(R.id.progress_pick_location);

        RegistrationStepsAdapter adapter = new RegistrationStepsAdapter(this, 1, 1);
        adapter.addAll(views);

        mListView.setAdapter(adapter);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchData();
            }
        });

        //get location
        /*locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getApplicationContext(),"No location permission", Toast.LENGTH_SHORT).show();
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);*/
    }

    private void fetchData() {
        $shopName = shopNameEditText.getText().toString();
        $location = locationEditText.getText().toString();
        $description = descriptionEditText.getText().toString();

        if (validateData()){
            storeData();
        }
    }

    private void storeData() {
        editor = preferences.edit();
        editor.putString(Config.SHOPNAMEKEY, $shopName);
        editor.putString(Config.SHOPLOCATIONKEY, $location);
        editor.putString(Config.SHOPDESCRIPTIONKEY, $description);
        editor.apply();

        routeToDetailsConfirmation();
    }

    private void routeToDetailsConfirmation() {
        startActivity(new Intent(ShopDetailsActivity.this,ConfirmDetailsActivity.class));
    }

    private boolean validateData(){
        if (TextUtils.isEmpty($shopName)){
            Toast.makeText(getApplicationContext(),"Shop name is required",Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty($location)){
            Toast.makeText(getApplicationContext(),"Location is required",Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty($description)){
            Toast.makeText(getApplicationContext(),"Shop description is required",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void saveCordinates(double latitude, double longitude) {
        if (cordinatesMap.size() == 0){
            cordinatesMap.put("Latitude", String.valueOf(latitude));
            cordinatesMap.put("Longitude", String.valueOf(longitude));

            getLocationName(latitude, longitude);
        }
    }

    @SuppressLint("RestrictedApi")
    private void getLocationName(double latitude, double longitude) {
        Geocoder gcd = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = gcd.getFromLocation(latitude, longitude, 1);
            if (addresses != null){
                if (addresses.size() > 0) {
                    System.out.println(addresses.get(0).getLocality());
                    String country = addresses.get(0).getCountryName(); //Kenya
                    String locality = addresses.get(0).getLocality(); //Nairobi
                    String subLocality = addresses.get(0).getSubLocality(); //Ngara East
                    String adminArea = addresses.get(0).getAdminArea(); //Nairobi == county
                    String featureName = addresses.get(0).getFeatureName(); //Desai road / YTL

                    if (country != null && locality != null && subLocality != null && adminArea != null && featureName != null){
                        locationEditText.setText(locality+", "+ subLocality+", "+featureName);
                        locationEditText.setInputType(InputType.TYPE_NULL);
                        locationEditText.setTextIsSelectable(false);
                        locationEditText.setEnabled(false);

                        editor = preferences.edit();
                        editor.putString(Config.COUNTRY_KEY, country);
                        editor.putString(Config.LOCALITY_KEY, locality);
                        editor.putString(Config.SUBLOCALITY_KEY, subLocality);
                        editor.putString(Config.ADMINAREA_KEY, adminArea);
                        editor.putString(Config.FEATURENAME_KEY, featureName);
                        editor.apply();

                        progressBar.setVisibility(View.GONE);
                        fab.setVisibility(View.VISIBLE);
                    }else {
                        Toast.makeText(getApplicationContext(), "A field is null",Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                        fab.setVisibility(View.VISIBLE);
                    }

                }
                else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "We are out",Toast.LENGTH_SHORT).show();
                }
            }else {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "We are out",Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(), e.getMessage(),Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        cordinateCount ++;

        if (cordinateCount == 4){
            saveCordinates(location.getLatitude(), location.getLongitude());
            cordinateCount = 0;
        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Latitude","status");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("Latitude","enable");
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("Latitude","disable");
    }
}
