package com.example.taskmaster.activities;

import static com.amplifyframework.core.Amplify.Auth;

import androidx.appcompat.app.AppCompatActivity;
import com.amplifyframework.analytics.AnalyticsEvent;
import com.amplifyframework.core.Amplify;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.amplifyframework.auth.AuthUser;
import com.amplifyframework.auth.AuthUserAttributeKey;
import com.amplifyframework.auth.cognito.result.AWSCognitoAuthSignOutResult;
import com.amplifyframework.auth.cognito.result.GlobalSignOutError;
import com.amplifyframework.auth.cognito.result.HostedUIError;
import com.amplifyframework.auth.cognito.result.RevokeTokenError;
import com.amplifyframework.auth.options.AuthSignOutOptions;
import com.amplifyframework.auth.options.AuthSignUpOptions;
import com.amplifyframework.core.Amplify;
import com.example.taskmaster.R;

import java.util.Date;

public class MainActivity extends AppCompatActivity {
    public static final String TASK_TO_DO_TAG = "viewTask";
    public static final String TAG = "logout";
    public AuthUser authUser = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Amplify.Auth.getCurrentUser(
                success -> {
                    Log.i(TAG, "There is a user");
                    authUser = success;
                },
                failure -> {
                    Log.w(TAG, "There is no current authenticated User");
                    authUser = null;
                }
        );

        AnalyticsEvent event = AnalyticsEvent.builder()
                .name("Main Activity visited")
                .addProperty("Tracking Activity", "Main Activity was opened")
                .addProperty("Time", Long.toString(new Date().getTime()))
                .build();

        Amplify.Analytics.recordEvent(event);

        pathButtons();
        setUpUserProfile();
        signOutButton();
        getLocation();
    }

    @Override
    protected void onResume(){
        super.onResume();
        greetingDisplay();
        signOutButton();
    }

    public void getLocation(){
        Button location = MainActivity.this.findViewById(R.id.MainActivityLocationButton);
        location.setOnClickListener(view ->{
            Intent goToLocation = new Intent(this, Location.class);
            startActivity(goToLocation);
    });
    }


    public void pathButtons(){
        Button allTasks = MainActivity.this.findViewById(R.id.MainViewButtonAllTasks);
        allTasks.setOnClickListener(v -> {
            Intent goToAllTasks = new Intent(MainActivity.this, AllTasks.class);
            startActivity(goToAllTasks);
        });

        Button signIn = MainActivity.this.findViewById(R.id.MainActivityButtonSignIn);
        signIn.setOnClickListener(view ->{
            Intent goToSignIn = new Intent(this, SigninActivity.class);
            startActivity(goToSignIn);
        });
        Button signUp = MainActivity.this.findViewById(R.id.MainActivityButtonSignUp);
        signUp.setOnClickListener(view ->{
            Intent goToSignUp = new Intent(this, SignupActivity.class);
            startActivity(goToSignUp);
        });

        // Get an element by its id
        Button addTaskBttn = MainActivity.this.findViewById(R.id.MainViewButtonAddTask);
        //add an event listener to the button
        addTaskBttn.setOnClickListener(view -> {
            //set up intent
            Intent goToTaskFormActivity = new Intent(MainActivity.this, AddTask.class);
            //Launch Intent
            startActivity(goToTaskFormActivity);
            //set up log
            Log.e("", "Error");
        });

        // get current authenticted user
        // if user is null -> show signUp button, hide signIn button
        if (authUser == null){
            // not signed in: see sign up sign in hide logout
            signIn.setVisibility(View.VISIBLE);
            signUp.setVisibility(View.VISIBLE);
        } else {
            String username = authUser.getUsername();
            Log.i(TAG, "Username is: " + username);
            // signed in. hide sign up and sign in and show logout
            signIn.setVisibility(View.INVISIBLE);
            signUp.setVisibility(View.INVISIBLE);

        }
    }

    public void signOutButton(){
        Button signOutButton = this.findViewById(R.id.MainActivityButtonLogOut);
        signOutButton.setOnClickListener(view ->
                Auth.signOut( signOutResult -> {
                    if (signOutResult instanceof AWSCognitoAuthSignOutResult.CompleteSignOut) {
                        // Sign Out completed fully and without errors.
                        Log.i("AuthQuickStart", "Signed out successfully");
                        startActivity(new Intent(this, MainActivity.class));
                    } else if (signOutResult instanceof AWSCognitoAuthSignOutResult.PartialSignOut partialSignOutResult) {
                        // Sign Out completed with some errors. User is signed out of the device.

                        HostedUIError hostedUIError = partialSignOutResult.getHostedUIError();
                        if (hostedUIError != null) {
                            Log.e("AuthQuickStart", "HostedUI Error", hostedUIError.getException());
                            // Optional: Re-launch hostedUIError.getUrl() in a Custom tab to clear Cognito web session.
                        }

                        GlobalSignOutError globalSignOutError = partialSignOutResult.getGlobalSignOutError();
                        if (globalSignOutError != null) {
                            Log.e("AuthQuickStart", "GlobalSignOut Error", globalSignOutError.getException());
                            // Optional: Use escape hatch to retry revocation of globalSignOutError.getAccessToken().
                        }

                        RevokeTokenError revokeTokenError = partialSignOutResult.getRevokeTokenError();
                        if (revokeTokenError != null) {
                            Log.e("AuthQuickStart", "RevokeToken Error", revokeTokenError.getException());
                            // Optional: Use escape hatch to retry revocation of revokeTokenError.getRefreshToken().
                        }
                    } else if (signOutResult instanceof AWSCognitoAuthSignOutResult.FailedSignOut failedSignOutResult) {
                        // Sign Out failed with an exception, leaving the user signed in.
                        Log.e("AuthQuickStart", "Sign out Failed", failedSignOutResult.getException());
                    }
                })

        );

        if (authUser == null){
            // not signed in: see sign up sign in hide logout
            signOutButton.setVisibility(View.INVISIBLE);
        } else {
            String username = authUser.getUsername();
            Log.i(TAG, "Username is: " + username);
            // signed in. hide sign up and sign in and show logout
            signOutButton.setVisibility(View.VISIBLE);
        }
    }

    public void setUpUserProfile(){
        Button mySettingsButton = MainActivity.this.findViewById(R.id.MainActivitySettingsButtonIntent);
        mySettingsButton.setOnClickListener(v -> {
            Intent goToUserProfileSettings = new Intent(MainActivity.this, UserProfileSettings.class);
            startActivity(goToUserProfileSettings);
        });
    }

    public void greetingDisplay(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String username = preferences.getString(UserProfileSettings.USERNAME_TAG, "Please go to settings and enter a username!");
        ((TextView)findViewById(R.id.MainActivityTVDisplayName)).setText(String.format("Welcome Back %s%s", username.substring(0, 1).toUpperCase(), username.substring(1)));
    }

}