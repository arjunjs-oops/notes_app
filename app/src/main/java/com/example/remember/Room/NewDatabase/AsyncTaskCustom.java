package com.example.remember.Room.NewDatabase;

import android.os.AsyncTask;

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