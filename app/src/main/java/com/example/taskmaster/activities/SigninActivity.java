package com.example.taskmaster.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.amplifyframework.auth.AuthUser;
import com.amplifyframework.core.Amplify;
import com.example.taskmaster.R;

public class SigninActivity extends AppCompatActivity {
    public static final String TAG = "signInActivity";
    Intent callingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        callingIntent = getIntent();

        signInForm();
        setupSignInForm();
    }

    public void signInForm(){
        String userEmail = callingIntent.getStringExtra(SignupActivity.SIGNUP_EMAIL_TAG);
        EditText emailEditText = findViewById(R.id.SignInETPassword);
        emailEditText.setText(userEmail);
    }

    public void setupSignInForm(){
        findViewById(R.id.SigninButtonSignIn).setOnClickListener(view -> {
            String userEmail = ((EditText) findViewById(R.id.editTextTextEmailAddress)).getText().toString();
            String password = ((EditText) findViewById(R.id.SignInETPassword)).getText().toString();
            Amplify.Auth.signIn(
                    userEmail,
                    password,
                    success -> {
                        Log.i(TAG, "Login succeeded: " + success);
                        Intent goToMainActivity = new Intent(this, MainActivity.class);
                        goToMainActivity.putExtra(SignupActivity.SIGNUP_EMAIL_TAG, userEmail);
                        startActivity(goToMainActivity);
                    },
                    failure -> {
                        Log.i(TAG, "Login failed with username: " + userEmail + " with this message: " + failure);
                        runOnUiThread(() -> Toast.makeText(SigninActivity.this, "Could Not Login", Toast.LENGTH_SHORT).show());
                    }
            );
        });
    }
}