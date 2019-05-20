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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import com.google.android.gms.common.GoogleApiAvailability;


public class SignupActivity extends AppCompatActivity implements View.OnClickListener {

    private Button buttonRegister;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextConfirmPassword;
    private TextView textViewToLogin;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    private static final String TAG = "MapActivity";
    private static final int ERROR_DIALOG_REQUEST = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        init();
    }

    private void init() {

        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() != null){
            // profile activity here
            finish();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }

        buttonRegister = (Button) findViewById(R.id.signUpButton);
        buttonRegister.setOnClickListener(this);

        editTextEmail = (EditText) findViewById(R.id.emailTextInput);
        editTextPassword = (EditText) findViewById(R.id.passwordTextInput);
        editTextPassword = (EditText) findViewById(R.id.confirmPasswordTextInput);

        textViewToLogin = (TextView) findViewById(R.id.textViewToLogin);
        textViewToLogin.setOnClickListener(this);

        progressDialog = new ProgressDialog(this);
    }

    @Override
    public void onClick(View v) {
        if (v == buttonRegister){
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
            progressDialog.setMessage("Registering User");
            progressDialog.show();

            registrateUser(email, password);

        }
        if (v == textViewToLogin){
            // We will open the sign in activity
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }
    }

    public void registrateUser(String email, String pass){

        firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();
                if(task.isSuccessful()){
                    Toast.makeText(SignupActivity.this, "Account Created", Toast.LENGTH_SHORT).show();
                    if (isServiceOK()){
                        finish();
                        startActivity(new Intent(getApplicationContext(), MapActivity.class));
                    }
                }

                else {
                    task.getResult().getUser();
                    Toast.makeText(SignupActivity.this, "Could not Register... please try again", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public boolean isServiceOK(){
        Log.d(TAG, "isServiceOK: checking google services version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(SignupActivity.this);

        if(available == ConnectionResult.SUCCESS){
            Log.d(TAG, "isServiceOK: Google Play Services is working");
            return true;
        }
        else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            Log.d(TAG, "isServiceOK: an error occured but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(SignupActivity.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        }
        else {
            Toast.makeText(this, "You can´t make mapo requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }
}