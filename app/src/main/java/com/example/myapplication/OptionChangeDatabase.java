package com.example.myapplication;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Database(entities = {OptionChange.class}, version = 1, exportSchema = false)
abstract class OptionChangeDatabase extends RoomDatabase {

    abstract OptionChangeDao optionChangeDao();

    // marking the instance as volatile to ensure atomic access to the variable
    private static volatile OptionChangeDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static OptionChangeDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (OptionChangeDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            OptionChangeDatabase.class, "option_change_database")
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
                OptionChangeDao dao = INSTANCE.optionChangeDao();
                dao.deleteAll();
            });
        }
    };
}
