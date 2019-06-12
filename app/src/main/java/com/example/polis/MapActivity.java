package com.example.polis;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.location.FusedLocationProviderClient;


public class MapActivity extends AppCompatActivity {

    private static final String TAG = "MapActivity";

    private BottomNavigationView mMapNav;

    private FrameLayout mMapFrame;

    private MapFragment mapFragment;
    private ReportFragment reportFragment;
    private AccountFragment accountFragment;

    // Access and permission
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final Integer LOCATION_PERMISSION_REQUEST_CODE = 1234;

    //vars
    private boolean mLocationPermissionGranted = false;

    //Google map
    private GoogleMap mGoogleMap;

    // Service to find your location
    private FusedLocationProviderClient mFusedLocationClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        getLocationPermission();
        init();
    }

    private void getLocationPermission() {
        Log.d(TAG, "GetLocationPermission: getting location permission");
        String[] permissions = {FINE_LOCATION, COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(this, FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this, COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionGranted = true;
            } else {
                ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {
            ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    private void init() {

        mMapFrame = (FrameLayout) findViewById(R.id.map_frame);
        mMapNav = (BottomNavigationView) findViewById(R.id.bottomNavigationView);

        mapFragment = new MapFragment();
        reportFragment = new ReportFragment();
        accountFragment = new AccountFragment();

        setFragment(mapFragment);

        mMapNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case (R.id.nav_map):
                        setFragment(mapFragment);
                        return true;
                    case (R.id.nav_report):
                        setFragment(reportFragment);
                        return true;
                    case (R.id.nav_account):
                        setFragment(accountFragment);
                        return true;
                    default:
                        return false;
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "OnRequestPermissionsResult: called");
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case 1234: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            mLocationPermissionGranted = false;
                            Log.d(TAG, "OnRequestPermissionsResult: permisssion failed");
                            return;
                        }
                    }
                    Log.d(TAG, "OnRequestPermissionsResult: permisssion granted");
                    mLocationPermissionGranted = true;
                    init();
                }
            }
        }
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.map_frame, fragment);
        fragmentTransaction.commit();

    }
}
