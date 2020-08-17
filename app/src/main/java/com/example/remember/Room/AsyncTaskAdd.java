package com.example.remember.Room;

import android.util.Log;

import com.example.remember.Model.Notes.java.Notes;


public class AsyncTaskAdd extends android.os.AsyncTask<Notes, Void, Void> {

    DAO mDao;
    private static final String TAG="AsyncTask";



    public AsyncTaskAdd(DAO dao) {
        this.mDao = dao;

    }


    @Override
    protected Void doInBackground(Notes... notes) {
        Log.d(TAG, "doInBackground: ");
        mDao.insert(notes);
        return null;
    }
}
