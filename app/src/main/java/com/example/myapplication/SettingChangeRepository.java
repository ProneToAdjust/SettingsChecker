package com.example.myapplication;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

class SettingChangeRepository {

    private SettingChangeDao mSettingChangeDao;
    private LiveData<List<SettingChange>> mAllSettingChanges;

    SettingChangeRepository(Application application) {
        SettingChangeDatabase db = SettingChangeDatabase.getDatabase(application);
        mSettingChangeDao = db.settingChangeDao();
        mAllSettingChanges = mSettingChangeDao.getSettingChanges();
    }

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    LiveData<List<SettingChange>> getAllSettingChanges() {
        return mAllSettingChanges;
    }

    // You must call this on a non-UI thread or your app will throw an exception. Room ensures
    // that you're not doing any long running operations on the main thread, blocking the UI.
    void insert(SettingChange settingChange) {
        SettingChangeDatabase.databaseWriteExecutor.execute(() -> {
            mSettingChangeDao.insert(settingChange);
        });
    }
}
