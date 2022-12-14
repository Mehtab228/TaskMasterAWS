package com.example.taskmaster.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.amplifyframework.auth.AuthUserAttributeKey;
import com.amplifyframework.auth.options.AuthSignOutOptions;
import com.amplifyframework.auth.options.AuthSignUpOptions;
import com.amplifyframework.core.Amplify;
import com.example.taskmaster.R;

public class MainActivity extends AppCompatActivity {
    public static final String TASK_TO_DO_TAG = "viewTask";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pathButtons();
        setUpUserProfile();
//        Amplify.Auth.signUp(
//                "mehtab228",
//                "potato228!",
//                AuthSignUpOptions.builder()
//                        .userAttribute(AuthUserAttributeKey.name(), "Mehtab")
//                        .userAttribute(AuthUserAttributeKey.email(), "mehtabriar228@hotmail.com")
//                        .build(),
//                success -> Log.i(TASK_TO_DO_TAG, "signup success"),
//                failure -> Log.w(TASK_TO_DO_TAG, "failure to signin")
//        );
//
//        Amplify.Auth.confirmSignUp(
//                "mehtab228",
//                "434739",
//                success -> Log.i(TASK_TO_DO_TAG, "confirmed signup"),
//                failure -> Log.w(TASK_TO_DO_TAG, "failed to confirm signup")
//        );
//
//        Amplify.Auth.signIn(
//                "mehtab228",
//                "potato228!",
//                success -> Log.i(TASK_TO_DO_TAG, "confirmed signin"),
//                failure -> Log.w(TASK_TO_DO_TAG, "failed to signin")
//        );
    }

    @Override
    protected void onResume(){
        super.onResume();
        greetingDisplay();
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