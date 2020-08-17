package com.example.remember.Room;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.remember.Model.Notes.java.Notes;
;


@androidx.room.Database(entities = Notes.class,views = {},version = 4)
public abstract class Database extends RoomDatabase {
    
    private static final String db_name = "Notes_DB";
     public static Database instance;

    public static Database getDatabaseInstance(final Context context){
        if (instance==null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    Database.class,
                    db_name)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

    public abstract DAO getDAO();
}
