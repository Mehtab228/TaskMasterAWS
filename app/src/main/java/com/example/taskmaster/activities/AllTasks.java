package com.example.taskmaster.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.TextView;

import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.StatusOfTask;
import com.amplifyframework.datastore.generated.model.Task;
import com.example.taskmaster.R;
import com.example.taskmaster.activities.MainActivity;
import com.example.taskmaster.adapter.TaskRecyclerViewAdapter;
import com.google.android.play.core.tasks.Tasks;

import java.util.ArrayList;
import java.util.List;

public class AllTasks extends AppCompatActivity {
    public static final String TASK_TITLE_TAG = "title";
    public static final String TASK_BODY_TAG = "body";
    public static final String TASK_STATE_TAG = "state";
    private List<Task> taskList;
    public final String TAG = "AllTasksActivity";
    TaskRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_tasks);
        setUpRecyclerView();
    }

    public void onResume(){
        super.onResume();
        Amplify.API.query(
                ModelQuery.list(Task.class),
                success -> {
                    Log.i(TAG, "Read tags successfully");
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
                    String teamName = preferences.getString(UserProfileSettings.SELECTED_TAG, "Please go to settings and select a team!");
                    for (Task databaseTask : success.getData()){
                        if (databaseTask.getTeam().getName().equals(teamName)){taskList.add(databaseTask);}
                    }
                   runOnUiThread(() -> adapter.notifyDataSetChanged());
                },
                failure -> {
                    Log.e(TAG, "Failed to read from database");
                }
        );
    }

    public void setUpRecyclerView(){
        taskList = new ArrayList<>();
        RecyclerView taskRV = findViewById(R.id.AllTasksRecyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        taskRV.setLayoutManager(layoutManager);
         adapter = new TaskRecyclerViewAdapter(taskList, this);
        taskRV.setAdapter(adapter);
    }
}