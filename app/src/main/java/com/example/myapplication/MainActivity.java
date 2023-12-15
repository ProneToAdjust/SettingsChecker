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
    private OptionChangeViewModel optionChangeViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Context context = getApplicationContext();
        Intent intent = new Intent(context, OptionsCheckerService.class);
        context.startForegroundService(intent);

        optionChangeViewModel = new ViewModelProvider(this).get(OptionChangeViewModel.class);

        LiveData<List<OptionChange>> allOptionChanges = optionChangeViewModel.getAllOptionChanges();
        allOptionChanges.observe(this, optionChanges -> {
            TextView textView = findViewById(R.id.textView);
            ArrayList<String> optionChangesText = new ArrayList<>();
            for (OptionChange optionChange : optionChanges) {
                String change = optionChange.getSettingChanged() + " changed to " + optionChange.getSettingChangedTo() + " from " + optionChange.getSettingChangedFrom();
                String changeText = "onCreate: " + change + " on " + new Date(optionChange.getTimestamp());
                Log.d("OptionChange", changeText);
                optionChangesText.add(changeText);

                try {
                    Log.d("OptionChange", "onCreate: " + new JSONObject(optionChange.settingsBefore).toString(2));
                    Log.d("OptionChange", "onCreate: " + new JSONObject(optionChange.settingsAfter).toString(2));
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
            textView.setText(String.join("\n", optionChangesText));
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
