package com.example.remember.Room.NewDatabase;

import android.util.Log;

import com.example.remember.Model.Notes.java.Notes;


public class AsyncTask extends android.os.AsyncTask<Notes,Void,Void> {

    DAO mDao;
    private static final String TAG="AsyncTask";



    public AsyncTask(DAO dao) {
        this.mDao = dao;

    }



    @Override
    protected Void doInBackground(Notes...notes) {
        Log.d(TAG, "doInBackground:"+notes);

        Log.d(TAG, "doInBackground: ");
        mDao.updateNotes(notes);
        return null;
    }

}
