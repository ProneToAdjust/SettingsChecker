package com.example.myapplication;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

/**
 * View Model to keep a reference to the word repository and
 * an up-to-date list of all words.
 */

public class OptionChangeViewModel extends AndroidViewModel {

    private OptionChangeRepository mRepository;
    // Using LiveData and caching what getAllOptionChanges returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    private final LiveData<List<OptionChange>> mAllOptionChanges;

    public OptionChangeViewModel(Application application) {
        super(application);
        mRepository = new OptionChangeRepository(application);
        mAllOptionChanges = mRepository.getAllOptionChanges();
    }

    LiveData<List<OptionChange>> getAllOptionChanges() {
        return mAllOptionChanges;
    }

    void insert(OptionChange optionChange) {
        mRepository.insert(optionChange);
    }
}
