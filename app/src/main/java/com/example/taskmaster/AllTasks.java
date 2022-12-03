package com.example.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class AllTasks extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_tasks);
        taskDetailsExtra();
    }

    public void taskDetailsExtra(){
        Intent callingIntent = getIntent();
        String taskName = null;
        if(callingIntent != null){
            taskName = callingIntent.getStringExtra(MainActivity.TASK_TO_DO_TAG);
        }
        TextView taskToDoDetails = findViewById(R.id.AllTasksTVTaskToDo);
        if (taskName != null){
            taskToDoDetails.setText(taskName);
        } else {
            taskToDoDetails.setText("No Tasks Available");
        }
    }
}