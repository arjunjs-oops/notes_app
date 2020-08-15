package com.example.remember.Room;//ToDo Create A DAO for Database Communication

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.remember.Model.Notes;

import java.util.List;

@Dao
public  interface DAO {

    @Insert
    void insertData(Notes...notes);


    @Delete
    void deleteData(Notes notes);



}