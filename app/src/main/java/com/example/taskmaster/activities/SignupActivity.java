package com.example.taskmaster.activities;

import androidx.appcompat.app.AppCompatActivity;

import com.amplifyframework.auth.AuthUserAttributeKey;
import com.amplifyframework.auth.options.AuthSignUpOptions;
import com.amplifyframework.core.Amplify;
import com.example.taskmaster.R;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

public class SignupActivity extends AppCompatActivity {
    public static final String TAG = "SignUpActivity";
    public static final String SIGNUP_EMAIL_TAG = "Signup_Email_Tag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        signUpForm();
    }

    public void signUpForm(){
        findViewById(R.id.SignUpButton).setOnClickListener(view -> {
            String userEmail = ((EditText) findViewById(R.id.SignUpeditTextTextEmailAddress)).getText().toString();
            String userPassword = ((EditText) findViewById(R.id.SignUpeditTextTextPassword)).getText().toString();
            Amplify.Auth.signUp(
                    userEmail,
                    userPassword,
                    AuthSignUpOptions.builder()
                            .userAttribute(AuthUserAttributeKey.name(), userEmail)
                            .userAttribute(AuthUserAttributeKey.email(), userPassword)
                            .build(),
                    success -> {
                        Log.i(TAG, "SignUp Success! " + success);
                        Intent goToVerifyActivity = new Intent(this, VerifySignUp.class);
                        goToVerifyActivity.putExtra(SIGNUP_EMAIL_TAG, userEmail);
                        startActivity(goToVerifyActivity);
                    },
                    failure -> {
                        Log.w(TAG, "Signup failed with username: " + userEmail + "with message " + failure.getMessage());
                        runOnUiThread(() -> Toast.makeText(this, "Signup Failed", Toast.LENGTH_SHORT).show());
                    }
            );
        });
    }
}