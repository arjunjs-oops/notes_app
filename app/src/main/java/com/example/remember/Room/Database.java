package com.example.remember.Room;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.remember.Model.Notes;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@androidx.room.Database(entities = Notes.class,version = 2)
public abstract class Database extends RoomDatabase {
    
    private static final String db_name = "Notes_DB";
     public static Database instance;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);


    public static Database getDatabaseInstance(final Context context){
        if (instance==null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    Database.class,
                    db_name).build();
        }
        return instance;
    }

    public abstract DAO getDAO();
}
