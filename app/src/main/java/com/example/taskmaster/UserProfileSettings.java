package com.example.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class UserProfileSettings extends AppCompatActivity {
    SharedPreferences preferences;
    public static final String USERNAME_TAG = "username";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_settings);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        saveValuesToSharedPreferences();
    }

    public void saveValuesToSharedPreferences(){
        SharedPreferences.Editor preferenceEditor = preferences.edit();
        Button savePreferencesButton = UserProfileSettings.this.findViewById(R.id.UserProfileSettingsButtonSave);
        savePreferencesButton.setOnClickListener(v -> {
            //Gets the input as a string
            String userNameText = ((EditText) findViewById(R.id.UserProfileSettingsETEnterName)).getText().toString();
            preferenceEditor.putString(USERNAME_TAG, userNameText.toString());
            //Nothing saves if you don't include it
            preferenceEditor.apply();
            Toast.makeText(this, "Username Saved!", Toast.LENGTH_SHORT).show();
            Intent sendBacktoHome = new Intent(UserProfileSettings.this, MainActivity.class);
            startActivity(sendBacktoHome);
        });
    }
}