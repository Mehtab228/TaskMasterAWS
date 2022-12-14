package com.example.taskmaster.activities;

import androidx.appcompat.app.AppCompatActivity;



import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;

import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.StatusOfTask;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.datastore.generated.model.Team;
import com.example.taskmaster.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class AddTask extends AppCompatActivity {
    public final static String TAG = "AddNewTask";
    Spinner spinner;
    Spinner teamSpinner;
    CompletableFuture<List<Team>> teamFuture = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        spinner = findViewById(R.id.AddTaskActivitySpinnerState);
        teamSpinner = findViewById(R.id.AddTaskSpinnerTeams);
        teamFuture = new CompletableFuture<>();
        setupTypeSpinner();
        submitButton();

        Amplify.API.query(
                ModelQuery.list(Team.class),
                success -> {
                    Log.i(TAG,"Read Teams from the database");
                    ArrayList<String> teamNames = new ArrayList<>();
                    ArrayList<Team> teamObjects = new ArrayList<>();
                    for (Team teams: success.getData()) {
                        teamNames.add(teams.getName());
                        teamObjects.add(teams);
                    }
                    teamFuture.complete(teamObjects);
                    runOnUiThread(() -> {
                        setupTeamSpinner(teamNames);
                    });
                },
                failure -> {
                    teamFuture.complete(null);
                    Log.w(TAG, "failed to read teams from database");
                }
        );
    }

    public void submitButton() {
        Button addTaskButton = AddTask.this.findViewById(R.id.MainViewButtonSubmitAddNewTask);
        addTaskButton.setOnClickListener(view -> {
            String selectedTeamSpinner = teamSpinner.getSelectedItem().toString();
            List<Team> teams = null;
            try {
                teams = teamFuture.get();
            } catch (InterruptedException ae){
                Log.e(TAG, "Interrupted Exception while trying to get Teams");
                Thread.currentThread().interrupt();
            } catch (ExecutionException ee){
                Log.e(TAG, "Execution Exception while trying to get Teams");
            }
            Team selectedTeam = teams.stream().filter(team -> team.getName().equals(selectedTeamSpinner)).findAny().orElseThrow(RuntimeException::new);
            Task newTask = Task.builder()
                    .name(((EditText)findViewById(R.id.MainViewEditViewTaskTitle)).getText().toString())
                    .description(((EditText)findViewById(R.id.MainViewETAddTaskDescription)).getText().toString())
                    .status(StatusOfTask.valueOf(spinner.getSelectedItem().toString()))
                    .team(selectedTeam)
                    .build();
            Amplify.API.mutate(
                    ModelMutation.create(newTask),
                    success -> Log.i(TAG,"Add a new task.onCreate(), made a new task successfully"),
                    failure -> Log.w(TAG,"Add a new task.onCreate(), made a new task successfully", failure.getCause())
            );
            Toast.makeText(getApplicationContext(), "Task Submitted", Toast.LENGTH_LONG).show();
        });
    }

    public void setupTypeSpinner() {
        spinner.setAdapter(new ArrayAdapter<>(
                this,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                StatusOfTask.values()
        ));
    }

    public void setupTeamSpinner(ArrayList<String> teamNames){
        teamSpinner.setAdapter(new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                teamNames
        ));
    }


}