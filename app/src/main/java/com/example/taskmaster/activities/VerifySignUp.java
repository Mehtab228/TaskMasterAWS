package com.example.taskmaster.activities;

import androidx.appcompat.app.AppCompatActivity;

import com.amplifyframework.core.Amplify;
import com.example.taskmaster.R;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

public class VerifySignUp extends AppCompatActivity {
    public static final String TAG = "VerifyAccountActivity";
    public static final String VERIFY_ACCOUNT_EMAIL_TAG = "Verify_Account_Email_Tag";
    Intent callingIntent;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_sign_up);
        callingIntent = getIntent();
        setUpVerifyForm();
    }

    public void setUpVerifyForm(){
        String userEmail = callingIntent.getStringExtra(SignupActivity.SIGNUP_EMAIL_TAG);
        findViewById(R.id.VerifySignupButtonSubmit).setOnClickListener(view -> {
            String verificationCode = ((EditText) findViewById(R.id.VerifySignupEVEnterCode)).getText().toString();
            Amplify.Auth.confirmSignUp(
                    userEmail,
                    verificationCode,
                    success -> {
                        Log.i(TAG, "Verification succeeded: " + success);
                        Intent goToSignInActivity = new Intent(this, MainActivity.class);
                        goToSignInActivity.putExtra(SignupActivity.SIGNUP_EMAIL_TAG, userEmail);
                        startActivity(goToSignInActivity);
                    },
                    failure -> {
                        Log.i(TAG, "Verification failed with username: " + userEmail + " with this message: " + failure);
                        runOnUiThread(() -> Toast.makeText(VerifySignUp.this, "Account could not be verified", Toast.LENGTH_SHORT).show());
                    }
            );
        });
    }
}