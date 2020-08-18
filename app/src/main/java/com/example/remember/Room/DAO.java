package com.example.remember.Room;//ToDo Create A DAO for Database Communication

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.remember.Model.Notes.java.Notes;;

import java.util.List;

@Dao
public  interface DAO {
    @Update(entity = Notes.class)
    int updateNotes(Notes...notes);

    @Insert
    long[] insert(Notes...notes);


    @Query("SELECT * FROM notes")
    LiveData<List<Notes>> getAllNotes();


    @Query("SELECT * FROM notes WHERE title LIKE :title")
     LiveData<List<Notes>> getCustomTitle(String...title);


    @Delete
    void deleteNotes(Notes...notes);












}