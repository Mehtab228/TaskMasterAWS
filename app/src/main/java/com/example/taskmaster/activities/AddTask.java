package com.example.taskmaster.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.documentfile.provider.DocumentFile;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;

import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.StatusOfTask;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.datastore.generated.model.Team;
import com.example.taskmaster.R;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class AddTask extends AppCompatActivity {
    public final static String TAG = "AddNewTask";
    Spinner spinner;
    Spinner teamSpinner;
    CompletableFuture<List<Team>> teamFuture = null;
    ActivityResultLauncher<Intent> activityResultLauncher;
    private String s3ImageKey = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        activityResultLauncher = getImagePickingActivityResultLauncher();
        spinner = findViewById(R.id.AddTaskActivitySpinnerState);
        teamSpinner = findViewById(R.id.AddTaskSpinnerTeams);
        teamFuture = new CompletableFuture<>();
        setupTypeSpinner();
        submitButton();
        setupAddImageBttn();

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



    private void setupAddImageBttn(){
        findViewById(R.id.AddTaskButtonAddImage).setOnClickListener(v -> {
            launchImageSelectionIntent();
        });
    }

    private void launchImageSelectionIntent(){
        Intent imageFilePickingIntent = new Intent(Intent.ACTION_GET_CONTENT); // one of several file picking activities built into Android
        imageFilePickingIntent.setType("*/*");  // only allow one kind or category of file; if you don't have this, you get a very cryptic error about "No activity found to handle Intent
        imageFilePickingIntent.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{"image/jpeg", "image/png"}); // only pick JPEG and PNG images

        // Launch Android's built-in file picking activity using our newly created ActivityResultLauncher below
        activityResultLauncher.launch(imageFilePickingIntent);
    }

    private ActivityResultLauncher<Intent> getImagePickingActivityResultLauncher(){
        ActivityResultLauncher<Intent> imagePickingActivityResultLauncher =
                registerForActivityResult(
                        new ActivityResultContracts.StartActivityForResult(),
                        result -> {
                            Uri pickedImageFileUri = result.getData().getData();
                            try{
                                // take in the file URI and turn it into a inputStream
                                InputStream pickedImageInputStream = getContentResolver().openInputStream(pickedImageFileUri);
                                String pickedImageFilename = DocumentFile.fromSingleUri(this, pickedImageFileUri).getName();
                                Log.i(TAG, "Succeeded in getting input stream from file on phone! Filename is: " + pickedImageFilename);
                                uploadInputStreamToS3(pickedImageInputStream, pickedImageFilename, pickedImageFileUri);
                            } catch (FileNotFoundException fnfe){
                                Log.e(TAG, "Could not get file from file picker" + fnfe.getMessage());
                            }
                        }
                );
        return imagePickingActivityResultLauncher;
    }

    private void uploadInputStreamToS3(InputStream pickedImageInputStream, String pickedImageFilename, Uri pickedImageFileUri){
        Amplify.Storage.uploadInputStream(
                pickedImageFilename,
                pickedImageInputStream,
                success -> {
                    Log.i(TAG, "Succeeded in getting file uploaded to S3! Key is: " + success.getKey());
                    s3ImageKey = success.getKey();
                    ImageView superPetImage = findViewById(R.id.AddTaskImageView);
                    InputStream pickedImageInputStreamCopy = null; // need to make a copy because InputStreams cannot be reused!
                    try {
                        pickedImageInputStreamCopy = getContentResolver().openInputStream(pickedImageFileUri);
                    } catch (FileNotFoundException fnfe) {
                        Log.e(TAG, "Could not get file stream from URI! " + fnfe.getMessage(), fnfe);
                    }
                    superPetImage.setImageBitmap(BitmapFactory.decodeStream(pickedImageInputStreamCopy));
                },
                failure -> Log.e(TAG, "Failure in uploading file to S3 with filename: " + pickedImageFilename + " with error: " + failure.getMessage())
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
                    .s3ImageKey(s3ImageKey)
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