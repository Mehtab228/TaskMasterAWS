package com.example.taskmaster.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.taskmaster.R;
import com.example.taskmaster.activities.MainActivity;
import com.example.taskmaster.adapter.TaskRecyclerViewAdapter;
import com.example.taskmaster.model.Tasks;

import java.util.ArrayList;
import java.util.List;

public class AllTasks extends AppCompatActivity {
    public static final String TASK_TITLE_TAG = "title";
    public static final String TASK_BODY_TAG = "body";
    public static final String TASK_STATE_TAG = "state";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_tasks);
        setUpRecyclerView();
    }

    public void setUpRecyclerView(){
        List<Tasks> taskList = new ArrayList<>();
        taskList.add(new Tasks("gym", "go to gym", Tasks.State.ASSIGNED));
        taskList.add(new Tasks("store", "go shopping", Tasks.State.COMPLETE));
        RecyclerView taskRV = findViewById(R.id.AllTasksRecyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        taskRV.setLayoutManager(layoutManager);
        TaskRecyclerViewAdapter adapter = new TaskRecyclerViewAdapter(taskList, this);
        taskRV.setAdapter(adapter);
    }
}