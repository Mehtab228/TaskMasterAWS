package com.example.taskmaster.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.example.taskmaster.R;

public class MainActivity extends AppCompatActivity {
public static final String TASK_TO_DO_TAG = "viewTask";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pathButtons();
        setUpUserProfile();
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
            Log.e("", "All Tasks Error");
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