package com.example.polis;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.PersistableBundle;
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

import com.example.polis.Models.Credentials;
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
    private TextView resetTextView;


    private EditText editTextEmail;
    private EditText editTextPassword;

    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    private static final String TAG = "MapActivity";
    private static final int ERROR_DIALOG_REQUEST = 9001;

    private String email;
    private String password;


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
            email = editTextEmail.getText().toString().trim();
            password = editTextPassword.getText().toString().trim();

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
            Intent signUpIntent = new Intent(MainActivity.this, SignupActivity.class);
            Credentials credentials = new Credentials();
            credentials.setEmail(editTextEmail.getText().toString().trim());
            credentials.setPassword(editTextPassword.getText().toString().trim());

            signUpIntent.putExtra("credentials", credentials);
            startActivity(signUpIntent);
        }
        if (v == resetTextView){
            finish();
            startActivity(new Intent(this, ResetActivity.class));
        }
    }

    public void init(){
        ToSignup = (TextView) findViewById(R.id.textViewToSignup);
        ToSignup.setOnClickListener(this);

        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);

        buttonLogin = (Button) findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(this);


        progressDialog = new ProgressDialog(this);

        resetTextView = (TextView) findViewById(R.id.resetTextView);
        resetTextView.setOnClickListener(this);


    }

    public void loginUser(String email, String password){
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();

                        if (!task.isSuccessful()){
                            Toast.makeText(getApplicationContext(), "Please, check your user name or password", Toast.LENGTH_SHORT).show();

                        }
                        else{
                            if (isServiceOK()){
                                // Start the map activity
                                finish();
                                startActivity(new Intent(getApplicationContext(), MapActivity.class));
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

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);

        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        outState.putString("email", email);
        outState.putString("password", password);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        this.email = savedInstanceState.getString("email");
        this.password = savedInstanceState.getString("password");

    }
}
