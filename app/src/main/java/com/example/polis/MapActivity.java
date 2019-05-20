package com.example.polis;

import android.Manifest;
import android.app.Dialog;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.GoogleMap;

public class MapActivity extends AppCompatActivity {

    // private static final String TAG = "MapActivity";
    private BottomNavigationView mMapNav;
    private FrameLayout mMapFrame;

    private MapFragment mapFragment;
    private ReportFragment reportFragment;
    private AccountFragment accountFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        init();
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
                switch (menuItem.getItemId()){
                    case(R.id.nav_map):
                        setFragment(mapFragment);
                        return true;
                    case(R.id.nav_report):
                        setFragment(reportFragment);
                        return true;
                    case(R.id.nav_account):
                        setFragment(accountFragment);
                        return true;
                    default:
                        return false;
                }
            }
        });
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.map_frame, fragment);
        fragmentTransaction.commit();
    }


}
