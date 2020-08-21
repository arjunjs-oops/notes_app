package com.example.remember.Room.Adding;//ToDo Create A DAO for Database Communication

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
    void insert(Notes...notes);


    @Query("SELECT * FROM notes")
    LiveData<List<Notes>> getAllNotes();


    @Query("SELECT * FROM notes WHERE title LIKE '%' || :query || '%'")
    LiveData<List<Notes>> findSearchValue(String query);


    @Delete
    void deleteNotes(Notes...notes);












}