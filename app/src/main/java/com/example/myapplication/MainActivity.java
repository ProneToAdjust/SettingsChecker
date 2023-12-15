package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class MainActivity extends AppCompatActivity{
    private SettingChangeViewModel settingChangeViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Start the foreground service
        Context context = getApplicationContext();
        Intent intent = new Intent(context, SettingCheckerService.class);
        context.startForegroundService(intent);

        settingChangeViewModel = new ViewModelProvider(this).get(SettingChangeViewModel.class);

        LiveData<List<SettingChange>> allSettingChanges = settingChangeViewModel.getAllSettingChanges();
        allSettingChanges.observe(this, settingChanges -> {
            TextView textView = findViewById(R.id.textView);
            ArrayList<String> settingChangesText = new ArrayList<>();
            for (SettingChange settingChange : settingChanges) {
                String change = settingChange.getSettingChanged() + " changed to " + settingChange.getSettingChangedTo() + " from " + settingChange.getSettingChangedFrom();
                String changeText = "onCreate: " + change + " on " + new Date(settingChange.getTimestamp());
                Log.d("SettingChange", changeText);
                settingChangesText.add(changeText);

                try {
                    Log.d("SettingChange", "onCreate: " + new JSONObject(settingChange.getSettingsBefore()).toString(2));
                    Log.d("SettingChange", "onCreate: " + new JSONObject(settingChange.getSettingsAfter()).toString(2));
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
            textView.setText(String.join("\n", settingChangesText));
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
