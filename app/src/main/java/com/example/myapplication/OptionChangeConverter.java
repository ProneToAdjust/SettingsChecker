package com.example.myapplication;

import androidx.room.TypeConverter;

import java.util.Date;

public class OptionChangeConverter {
    @TypeConverter
    public static Date fromString(String value) {
        return value == null ? null : new Date();
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }
}
