package com.example.myapplication;

import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.provider.Settings;

public class SettingCheckerObserver extends ContentObserver {

    public static final Uri SETTINGS_URI = Settings.Global.CONTENT_URI;
    private OnSettingsChangedListener listener;

    public SettingCheckerObserver(Handler handler, OnSettingsChangedListener listener) {
        super(handler);
        this.listener = listener;
    }

    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);
        // Handle the settings change
        if (listener != null) {
            listener.onSettingsChanged();
        }
    }

    public interface OnSettingsChangedListener {
        void onSettingsChanged();
    }
}

