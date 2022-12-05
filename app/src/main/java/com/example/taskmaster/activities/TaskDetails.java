package com.example.taskmaster.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.taskmaster.R;

public class TaskDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details);
        taskDetailsExtra();
    }

    public void taskDetailsExtra(){
        Intent callingIntent = getIntent();
        String taskName = null;
        String taskBody = null;
        String taskState = null;
        if(callingIntent != null){
            taskName = callingIntent.getStringExtra(AllTasks.TASK_TITLE_TAG);
            taskBody = callingIntent.getStringExtra(AllTasks.TASK_BODY_TAG);
            taskState = callingIntent.getStringExtra(AllTasks.TASK_STATE_TAG);
        }
        TextView taskToDoDetails = findViewById(R.id.taskDetailsTVTaskToDo);
        TextView taskToDoBody = findViewById(R.id.TaskDetailsTVBody);
        TextView taskToDoState = findViewById(R.id.TaskDetailsTVState);
        if (taskName != null){
            taskToDoDetails.setText(taskName);
            taskToDoBody.setText(taskBody);
            taskToDoState.setText(taskState);
        } else {
            taskToDoDetails.setText("No Tasks Available");
        }
    }
}