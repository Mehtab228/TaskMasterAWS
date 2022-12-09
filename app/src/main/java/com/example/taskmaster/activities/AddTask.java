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
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.StatusOfTask;
import com.amplifyframework.datastore.generated.model.Task;
import com.example.taskmaster.R;

public class AddTask extends AppCompatActivity {
    public final static String TAG = "AddNewTask";
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        spinner = findViewById(R.id.AddTaskActivitySpinnerState);

        setupTypeSpinner();
        submitButton();
    }

    public void submitButton() {
        Button addTaskButton = AddTask.this.findViewById(R.id.MainViewButtonSubmitAddNewTask);
        addTaskButton.setOnClickListener(view -> {
            Task newTask = Task.builder()
                    .name(((EditText)findViewById(R.id.MainViewEditViewTaskTitle)).getText().toString())
                    .description(((EditText)findViewById(R.id.MainViewETAddTaskDescription)).getText().toString())
                    .status(StatusOfTask.valueOf(spinner.getSelectedItem().toString()))
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


}