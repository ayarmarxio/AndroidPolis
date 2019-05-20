package com.example.polis;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment {

    private static final String TAG = "MapFragment";

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final Integer LOCATION_PERMISSION_REQUEST_CODE = 1234;

    //vars
    private boolean mLocationPermissionGranted = false;
    private GoogleMap mMap;


    public MapFragment() {
        // Required empty public constructor
        getLocationPermission();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_map, container, false);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        MapActivity mapActivity = new MapActivity();
        Context mapContext = mapActivity.getApplicationContext();
        mapContext = context;

    }



    private void getLocationPermission(){
        Log.d(TAG, "GetLocationPermission: getting location permission");
        String[] permissions = {FINE_LOCATION,
        COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(this.getActivity().getApplicationContext(), FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this.getActivity().getApplicationContext(), COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                mLocationPermissionGranted = true;
            }
            else{
                ActivityCompat.requestPermissions(this.requireActivity(), permissions, LOCATION_PERMISSION_REQUEST_CODE);
            }
        }
    }

    private void initMap(){
        Log.d(TAG, "Initializing Map");
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_fragment);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                Log.d(TAG, "onMapReady: Map is ready");
             mMap = googleMap;
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG,"OnRequestPermissionsResult: called" );
        mLocationPermissionGranted = false;

        switch (requestCode){
            case 1234: {
                if (grantResults.length > 0){
                    for (int i = 0; i < grantResults.length; i++){
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED){
                            mLocationPermissionGranted = false;
                            Log.d(TAG,"OnRequestPermissionsResult: permisssion failed" );
                            return;
                        }
                    }
                    Log.d(TAG,"OnRequestPermissionsResult: permisssion granted" );
                   mLocationPermissionGranted = true;
                   // Initialize Map
                    initMap();

                }
            }
        }
    }
}
