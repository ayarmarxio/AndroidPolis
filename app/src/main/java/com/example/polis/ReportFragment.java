package com.example.polis;


import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.polis.Models.Report;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Console;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class ReportFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private View mView;
    Button calendarBtn;
    TextView mTv;
    EditText detailEditText;
    Button submitBtn;
    String detailedText;
    String dateToSaveInstance;
    Button logOutBtn;

    private Location locationPoint;
    private String incidentName;

    Calendar calendar;
    DatePickerDialog datePickerDialog;

    private Date timeStampToSave;
    private Spinner spinnerType;

    private FusedLocationProviderClient mFusedLocationClient;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy - HH:mm");

    private FirebaseAuth auth;
    public ReportFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_report, container, false);
        submitBtn = (Button) mView.findViewById(R.id.submitButton);
        submitBtn.setOnClickListener(this);

        logOutBtn = (Button) mView.findViewById(R.id.logOutBtn);
        logOutBtn.setOnClickListener(this);

        return mView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        // Check if the savedInstanceSTate contains the date String key.

        if (savedInstanceState != null) {
            // Consider doing complex tasks like parsing etc. in the background.
            // Parsing a date shouldn't be the issue here but doing many things like this may
            // be the cause of the performance issue.
            DateFormat format = new SimpleDateFormat("yyyy, mm, dd hh:mm:ss");
            try {
                timeStampToSave = format.parse(savedInstanceState.getString(dateToSaveInstance));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            incidentName = savedInstanceState.getString(incidentName);
            detailedText = savedInstanceState.getString(detailedText);
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getDeviceLocation();

        calendarBtn = (Button) getView().findViewById(R.id.calendar_button);
        mTv = (TextView) getView().findViewById(R.id.testTextView);
        detailEditText = (EditText) getView().findViewById(R.id.detailText);



        spinnerType = (Spinner) getView().findViewById(R.id.spinnerType);
        spinnerType.setOnItemSelectedListener(this);

        calendarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int mYear, int mMonth, int mDayOfMonth) {
                        mTv.setText(mDayOfMonth + "/" + mMonth + "/" + mYear);
                        Date date = calendar.getTime();
                        timeStampToSave = date;
                        /*
                         * crate new SimpleDateFormat instance with desired date format.
                         * We are going to use yyyy-mm-dd hh:mm:ss here.
                         */
                        DateFormat dateFormat1 = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");

                        //to convert Date to String, use format method of SimpleDateFormat class.
                        dateToSaveInstance = dateFormat1.format(timeStampToSave);

                    }
                }, day, month, year);
                datePickerDialog.updateDate(2019, 05, 01);
                datePickerDialog.show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (submitBtn == v) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference newRegistryRef = db.collection("reports").document();
            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            detailedText = detailEditText.getText().toString().trim();

            Report report = new Report();

            report.setIncidentName(this.incidentName);
            report.setTimeStamp(timeStampToSave);
            report.setLocationPoint(locationPoint);
            report.setRegistryId(newRegistryRef.getId());
            report.setUserId(userId);
            report.setDetailText(detailedText);


            newRegistryRef.set(report).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(getContext(), "You rported succesfully", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getContext(), "Information Failed to Save", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

        if (logOutBtn == v){
            auth.getInstance().signOut();
            startActivity(new Intent(getActivity(), MainActivity.class));
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getId() == R.id.spinnerType) {
            this.incidentName = this.spinnerType.getSelectedItem().toString().trim();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void getDeviceLocation() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        Log.d("TAG", "get Last Known Location: getting current device location");

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Task location = mFusedLocationClient.getLastLocation();
        location.addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful()) {
                    Location currentLocation = (Location) task.getResult();
                    locationPoint = currentLocation;
                } else {
                    Toast.makeText(getContext(), "Unable to get current location", Toast.LENGTH_SHORT).show();
                    ;
                }
            }
        });
    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putString("incidentType", incidentName);
        savedInstanceState.putString("detailedText   ", detailedText);
        savedInstanceState.putString("stateDate", dateToSaveInstance);
    }


}
