package com.example.polis;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button buttonLogin;
    private TextView ToSignup;
    //private Button buttonLoginWithFb;

    private EditText editTextEmail;
    private EditText editTextPassword;

    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    private static final String TAG = "MapActivity";
    private static final int ERROR_DIALOG_REQUEST = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() != null){
            // profile activity here
            finish();
            startActivity(new Intent(getApplicationContext(), MapActivity.class));

        }
    }

    @Override
    public void onClick(View v) {
        if (v == buttonLogin){
            String email = editTextEmail.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();

            if (TextUtils.isEmpty(email)) {
                //email is empty
                Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show();
                // stoping function to executing further

            }
            if (TextUtils.isEmpty(password)){
                // password is empty
                Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
                // stoping function to executing further
            }

            // If validations are ok
            // We will first show a progressbar
            progressDialog.setMessage("Login user");
            progressDialog.show();

            loginUser(email, password);
        }

        if (v == ToSignup){
            // We will open the sign in activity
            finish();
            startActivity(new Intent(this, SignupActivity.class));
        }

        //if (v == buttonLoginWithFb){
            // Login with Facebook logic
        //}
    }

    public void init(){
        ToSignup = (TextView) findViewById(R.id.textViewToSignup);
        ToSignup.setOnClickListener(this);

        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);

        buttonLogin = (Button) findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(this);

        //buttonLoginWithFb = (Button) findViewById(R.id.buttonLoginWithFb);
        //buttonLoginWithFb.setOnClickListener(this);

        progressDialog = new ProgressDialog(this);


    }

    public void loginUser(String email, String password){
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();

                        if (task.isSuccessful()){
                            // Start the map activity
                            if (isServiceOK()){
                                finish();
                                startActivity(new Intent(getApplicationContext(), PrincipalActivity.class));
                            }
                        }
                    }
                });
    }

    public boolean isServiceOK(){
        Log.d(TAG, "isServiceOK: checking google services version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MainActivity.this);

        if(available == ConnectionResult.SUCCESS){
            Log.d(TAG, "isServiceOK: Google Play Services is working");
            return true;
        }
        else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            Log.d(TAG, "isServiceOK: an error occured but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(MainActivity.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        }
        else {
            Toast.makeText(this, "You can´t make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }
}
