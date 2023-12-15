package com.example.myapplication;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

class OptionChangeRepository {

    private OptionChangeDao mOptionChangeDao;
    private LiveData<List<OptionChange>> mAllOptionChanges;

    OptionChangeRepository(Application application) {
        OptionChangeDatabase db = OptionChangeDatabase.getDatabase(application);
        mOptionChangeDao = db.optionChangeDao();
        mAllOptionChanges = mOptionChangeDao.getOptionChanges();
    }

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    LiveData<List<OptionChange>> getAllOptionChanges() {
        return mAllOptionChanges;
    }

    // You must call this on a non-UI thread or your app will throw an exception. Room ensures
    // that you're not doing any long running operations on the main thread, blocking the UI.
    void insert(OptionChange optionChange) {
        OptionChangeDatabase.databaseWriteExecutor.execute(() -> {
            mOptionChangeDao.insert(optionChange);
        });
    }
}
