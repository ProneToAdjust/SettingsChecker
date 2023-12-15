package com.example.myapplication;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "setting_change_table")

public class SettingChange {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "settings_before")
    public String settingsBefore;

    @ColumnInfo(name = "settings_after")
    public String settingsAfter;

    @ColumnInfo(name = "setting_changed")
    public String settingChanged;

    @ColumnInfo(name = "setting_changed_to")
    public String settingChangedTo;

    @ColumnInfo(name = "setting_changed_from")
    public String settingChangedFrom;

    @ColumnInfo(name = "timestamp")
    public Long timestamp;

    public SettingChange(String settingsBefore, String settingsAfter, String settingChanged, String settingChangedTo, String settingChangedFrom, Long timestamp) {
        this.settingsBefore = settingsBefore;
        this.settingsAfter = settingsAfter;
        this.settingChanged = settingChanged;
        this.settingChangedTo = settingChangedTo;
        this.settingChangedFrom = settingChangedFrom;
        this.timestamp = timestamp;
    }

    public String getSettingsBefore() {
        return settingsBefore;
    }

    public String getSettingsAfter() {
        return settingsAfter;
    }

    public String getSettingChanged() {
        return settingChanged;
    }

    public String getSettingChangedTo() {
        return settingChangedTo;
    }

    public String getSettingChangedFrom() {
        return settingChangedFrom;
    }

    public Long getTimestamp() {
        return this.timestamp;
    }
}
