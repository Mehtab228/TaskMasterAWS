package com.example.taskmaster.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;


import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;

import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.taskmaster.R;
import com.example.taskmaster.database.TaskMasterDatabase;
import com.example.taskmaster.model.Tasks;


public class AddTask extends AppCompatActivity {
    TaskMasterDatabase taskMasterDatabase;
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        spinner = findViewById(R.id.AddTaskActivitySpinnerState);

        taskMasterDatabase = Room.databaseBuilder(
                        getApplicationContext(),
                        TaskMasterDatabase.class,
                        MainActivity.DATABASE_NAME)
                .fallbackToDestructiveMigration() // If Room gets confused, it tosses your database; don't use this in production!
                .allowMainThreadQueries()
                .build();
        setupTypeSpinner();
        submitButton();
    }

    public void submitButton(){
        Button addTaskButton = AddTask.this.findViewById(R.id.MainViewButtonSubmitAddNewTask);
        addTaskButton.setOnClickListener(view -> {
            Tasks newTask = new Tasks(
                    ((EditText)findViewById(R.id.MainViewEditViewTaskTitle)).getText().toString(),
                    ((EditText)findViewById(R.id.MainViewETAddTaskDescription)).getText().toString(),
                    Tasks.State.valueOf(spinner.getSelectedItem().toString())
            );
            taskMasterDatabase.taskDao().insertTask(newTask);
        });
        addTaskButton.setOnClickListener(view -> Toast.makeText(getApplicationContext(), "Task Submitted", Toast.LENGTH_LONG)
                .show());
    }

    public void setupTypeSpinner(){
        spinner.setAdapter(new ArrayAdapter<>(
                spinner.getContext(),
                android.R.layout.simple_spinner_item,
                Tasks.State.values()
        ));
    }


}