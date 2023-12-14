package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

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
            for (OptionChange optionChange : optionChanges) {
                Log.d("OptionChange", "onCreate: " + optionChange.getChange() + " " + new Date(optionChange.getTimestamp()).toString());
            }
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
