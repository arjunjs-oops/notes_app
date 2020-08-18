package com.example.remember.Room;

import android.content.Context;
import android.os.AsyncTask;

import com.example.remember.Model.Notes.java.Notes;

public class AsyncTaskCustom extends AsyncTask<String,Void,Void> {

    DAO mDao;


    public AsyncTaskCustom(DAO mDao) {
        this.mDao = mDao;

    }



    @Override
    protected Void doInBackground(String... strings) {
        mDao.getCustomTitle(strings);
        return null;
    }
}