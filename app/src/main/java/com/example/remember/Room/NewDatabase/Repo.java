package com.example.remember.Room.NewDatabase;
//Interaction btn Database and UI
//MVM Architecture


import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.remember.Model.Notes.java.Notes;

import java.util.List;

public class Repo {
    private static final String TAG = "Repo";

    Database mDatabase;
    DAO mDao;

    public Repo(Context context) {
        mDatabase = Database.getDatabaseInstance(context);
        mDao = mDatabase.getDAO();
    }

    public void addData(final Notes notes) {
        new AsyncTaskAdd(mDatabase.getDAO()).execute(notes);

    }
    public LiveData<List<Notes>> getAllNotes(){
        return mDao.getAllNotes();
    }


    public void removeData(final Notes notes){
        new AsyncTaskDelete(mDatabase.getDAO()).execute(notes);
    }


    public void updateNote(final Notes notes){
        Log.d(TAG, "Updating....");
        new AsyncTask(mDatabase.getDAO()).execute(notes);

    }



    public LiveData<List<Notes>> AsyncTaskCustom(String...title){ return mDao.getCustomTitle(title);}
}
