package com.example.polis;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText emailResetText;
    private Button resetButton;
    private Button backToLogin;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset);

        init();

        mAuth = FirebaseAuth.getInstance();
    }

    private void init() {

        emailResetText = (EditText) findViewById(R.id.emailTextResetInput);
        resetButton = (Button) findViewById(R.id.sendResetEmail);
        resetButton.setOnClickListener(this);
        backToLogin = (Button) findViewById(R.id.backToLoginBtn);
        backToLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == resetButton){
            String userEmail = emailResetText.getText().toString().trim();

            if (TextUtils.isEmpty(userEmail)){
                Toast.makeText(ResetActivity.this, "Please write your email first", Toast.LENGTH_SHORT).show();
            }
            else {
                mAuth.sendPasswordResetEmail(userEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(ResetActivity.this, "PLease check your email account", Toast.LENGTH_SHORT).show();
                            finish();
                            startActivity(new Intent(ResetActivity.this, MainActivity.class));
                        }
                        else {
                            String message = task.getException().getMessage();
                            Toast.makeText(ResetActivity.this, "Error ocurred" + message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
        if (v == backToLogin){
            finish();
            startActivity(new Intent(ResetActivity.this, MainActivity.class));
        }
    }
}