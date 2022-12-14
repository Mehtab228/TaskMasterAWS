package com.example.taskmaster.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Team;
import com.example.taskmaster.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class UserProfileSettings extends AppCompatActivity {
    SharedPreferences preferences;
    public static final String USERNAME_TAG = "username";
    public static final String SELECTED_TAG = "selectedTeam";
    public static final String TAG = "User Profile Settings";
    Spinner spinner;
    CompletableFuture<List<Team>> teamFuture = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_settings);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        saveValuesToSharedPreferences();
        teamFuture = new CompletableFuture<>();
        spinner = findViewById(R.id.UserProfileSettingsSpinner);
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

    public void saveValuesToSharedPreferences(){
        SharedPreferences.Editor preferenceEditor = preferences.edit();
        Button savePreferencesButton = UserProfileSettings.this.findViewById(R.id.UserProfileSettingsButtonSave);
        savePreferencesButton.setOnClickListener(v -> {
            String selectedTeamSpinner = spinner.getSelectedItem().toString();
            //Gets the input as a string
            String userNameText = ((EditText) findViewById(R.id.UserProfileSettingsETEnterName)).getText().toString();
            preferenceEditor.putString(USERNAME_TAG, userNameText);
            preferenceEditor.putString(SELECTED_TAG, selectedTeamSpinner);
            //Nothing saves if you don't include it
            preferenceEditor.apply();
            Toast.makeText(this, "Username Saved!", Toast.LENGTH_SHORT).show();
            Intent sendBacktoHome = new Intent(UserProfileSettings.this, MainActivity.class);
            startActivity(sendBacktoHome);
        });
    }

    public void setupTeamSpinner(ArrayList<String> teamNames){
        spinner.setAdapter(new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                teamNames
        ));
    }
}