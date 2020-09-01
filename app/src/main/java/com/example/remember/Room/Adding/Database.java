package com.example.remember.Room.Adding;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.remember.Model.Notes.java.DeletedNotes;
import com.example.remember.Model.Notes.java.Notes;

;

@androidx.room.Database(entities = {Notes.class, DeletedNotes.class},views = {},version = 10)
public abstract class Database extends RoomDatabase {
    
    private static final String db_name = "Notes_DB";
     public static Database instance;

    public static Database getDatabaseInstance(final Context context){
        if (instance==null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    Database.class,
                    db_name)
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }


    public abstract DAO getDAO();

}
