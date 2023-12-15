package com.example.myapplication;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Database(entities = {SettingChange.class}, version = 1, exportSchema = false)
abstract class SettingChangeDatabase extends RoomDatabase {

    abstract SettingChangeDao settingChangeDao();

    // marking the instance as volatile to ensure atomic access to the variable
    private static volatile SettingChangeDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static SettingChangeDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (SettingChangeDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            SettingChangeDatabase.class, "setting_change_database")
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            // Clear the database every time it is created
            // Remove if you want to keep the data
            databaseWriteExecutor.execute(() -> {
                SettingChangeDao dao = INSTANCE.settingChangeDao();
                dao.deleteAll();
            });
        }
    };
}
