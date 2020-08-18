package com.example.remember.Room.NewDatabase;

import com.example.remember.Model.Notes.java.Notes;


public class AsyncTaskDelete extends android.os.AsyncTask<Notes, Void, Void> {

    DAO mDao;
    private static final String TAG="AsyncTask";



    public AsyncTaskDelete(DAO dao) {
        this.mDao = dao;

    }



    @Override
    protected Void doInBackground(Notes...notes) {
        mDao.deleteNotes(notes);
        return null;
    }
}
