package com.example.myapplication;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class SettingChangeViewModel extends AndroidViewModel {

    private SettingChangeRepository mRepository;
    // Using LiveData and caching what getAllSettingChanges returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    private final LiveData<List<SettingChange>> mAllSettingChanges;

    public SettingChangeViewModel(Application application) {
        super(application);
        mRepository = new SettingChangeRepository(application);
        mAllSettingChanges = mRepository.getAllSettingChanges();
    }

    LiveData<List<SettingChange>> getAllSettingChanges() {
        return mAllSettingChanges;
    }

    void insert(SettingChange settingChange) {
        mRepository.insert(settingChange);
    }
}
