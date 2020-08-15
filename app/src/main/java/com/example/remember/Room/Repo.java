package com.example.remember.Room;
//Interaction btn Database and UI
//MVM Architecture


import android.content.Context;

import com.example.remember.Model.Notes;

public class Repo {

    Database mDatabase;
    DAO mDao;

    public Repo(Context context) {
        mDatabase = Database.getDatabaseInstance(context);
        mDao = mDatabase.getDAO();
    }

    public void addData(Notes notes){
       mDao.insertData(notes);

    }

    public void removeData(Notes notes){
        mDao.deleteData(notes);
    }
}
