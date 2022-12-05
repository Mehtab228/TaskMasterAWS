package com.example.taskmaster.activities;

import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;
import android.widget.Button;

import android.widget.Toast;

import com.example.taskmaster.R;


public class AddTask extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        Button addTaskButton = AddTask.this.findViewById(R.id.MainViewButtonSubmitAddNewTask);
        addTaskButton.setOnClickListener(view -> Toast.makeText(getApplicationContext(), "Task Submitted", Toast.LENGTH_LONG)
                .show());
    }
}