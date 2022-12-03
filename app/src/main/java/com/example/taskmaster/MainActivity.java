package com.example.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pathButtons();
        setUpUserProfile();
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
}