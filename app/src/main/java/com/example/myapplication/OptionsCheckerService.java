package com.example.myapplication;

import android.app.ForegroundServiceStartNotAllowedException;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ServiceInfo;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.ServiceCompat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OptionsCheckerService extends Service  implements OptionsCheckerObserver.OnSettingsChangedListener{
    private Map<String, String> lastReading = new HashMap<>();

    private NotificationManager notificationManager;
    private OptionsCheckerObserver optionsCheckerObserver;

    private ArrayList<String> settingsToMonitor = new ArrayList<String>() {{
        add(Settings.Global.ADB_ENABLED);
        add(Settings.Global.AIRPLANE_MODE_ON);
        add(Settings.Global.ALWAYS_FINISH_ACTIVITIES);
        add(Settings.Global.AUTO_TIME);
        add(Settings.Global.AUTO_TIME_ZONE);
        add(Settings.Global.BLUETOOTH_ON);
        add(Settings.Global.DATA_ROAMING);
        add(Settings.Global.DEBUG_APP);
        add(Settings.Global.DEVELOPMENT_SETTINGS_ENABLED);
        add(Settings.Global.HTTP_PROXY);
        add(Settings.Global.NETWORK_PREFERENCE);
        add(Settings.Global.SECURE_FRP_MODE);
        add(Settings.Global.STAY_ON_WHILE_PLUGGED_IN);
        add(Settings.Global.USB_MASS_STORAGE_ENABLED);
        add(Settings.Global.WIFI_DEVICE_OWNER_CONFIGS_LOCKDOWN);
        add(Settings.Global.WIFI_MAX_DHCP_RETRY_COUNT);
        add(Settings.Global.WIFI_MOBILE_DATA_TRANSITION_WAKELOCK_TIMEOUT_MS);
        add(Settings.Global.WAIT_FOR_DEBUGGER);
        add(Settings.Global.WIFI_ON);
    }};

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        lastReading = getDeveloperOptions(this);
    }

    private void startForeground() {
        NotificationChannel channel = new NotificationChannel("CHANNEL_ID", "DevOptionsCheckerChannel", NotificationManager.IMPORTANCE_MIN);
        channel.setDescription("DevOptionsCheckerChannel");

        notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
        Notification notification = new NotificationCompat.Builder(this, "CHANNEL_ID").build();

        try {
            int type = 0;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                type = ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC;
            }
            ServiceCompat.startForeground(this, 100, notification, type);
            Log.d("OptionChange", "startForeground");
        } catch (Exception e) {
            Log.d("OptionChange", e.getMessage());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && e instanceof ForegroundServiceStartNotAllowedException
            ) {
                // App not in a valid state to start foreground service
                // (e.g started from bg)
            }
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startForeground();
        optionsCheckerObserver = new OptionsCheckerObserver(new Handler(), this);
        getContentResolver().registerContentObserver(
                OptionsCheckerObserver.SETTINGS_URI,
                true,
                optionsCheckerObserver
        );
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void monitorSettings() {
        Map<String, String> currentSettings = getDeveloperOptions(this);
        if (!currentSettings.equals(lastReading)) {
//            Log.d("MainActivity", currentSettings.toString());
            // get what changed
            for (String setting : settingsToMonitor) {
                if (!(currentSettings.get(setting).equals(lastReading.get(setting)))) {
                    String change = setting + " changed from " + lastReading.get(setting) + " to " + currentSettings.get(setting);
//                    Log.d("MainActivity", change);

                    OptionChange optionChange = new OptionChange(change, System.currentTimeMillis());
                    OptionChangeDatabase.databaseWriteExecutor.execute(() -> {
                        OptionChangeDatabase db = OptionChangeDatabase.getDatabase(getApplicationContext());
                        OptionChangeDao dao = db.optionChangeDao();
                        dao.insert(optionChange);
                    });
                }
            }
            lastReading = currentSettings;
        }
    }

    public Map<String, String> getDeveloperOptions(Context context) {
        Map<String, String> settings = new HashMap<>();
        for (String setting : settingsToMonitor) {

            String settingValue = Settings.Global.getString(context.getContentResolver(), setting);
            settingValue = settingValue == null ? "null" : settingValue;
            settings.put(setting, settingValue);
        }
        return settings;
    }

    @Override
    public void onSettingsChanged() {
        monitorSettings();
    }
}
