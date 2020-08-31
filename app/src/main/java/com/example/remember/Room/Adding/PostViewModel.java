package com.example.remember.Room.Adding;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.remember.Model.Notes.java.DeletedNotes;
import com.example.remember.Model.Notes.java.Notes;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PostViewModel extends AndroidViewModel {

    private DAO postDao;
    private ExecutorService executorService;


    public PostViewModel(@NonNull Application application) {
        super(application);
        postDao = Database.getDatabaseInstance(application).getDAO();
        executorService = Executors.newSingleThreadExecutor();
    }

    public  LiveData<List<Notes>> getAllPosts() {
        return postDao.getAllNotes();
    }

   public void savePost(final Notes notes) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                postDao.insert(notes);
            }
        });
    }

    public void deletePost(final Notes...notes) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                postDao.deleteNotes(notes);
            }
        });


    }
    public void updatePost(final Notes notes) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                postDao.updateNotes(notes);
            }
        });

    }
    public LiveData<List<Notes>> getCustomItem(final String s) {
       return postDao.findSearchValue(s);
    }
    public void saveDeletedNotes(final DeletedNotes notes) {
        Log.d("AddingTODELETD", "saveDeletedNotes: ");
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                postDao.addDeletedNotes(notes);
            }
        });
    }

    public LiveData<List<DeletedNotes>> getAllDeletedNotes() {
        return postDao.DELETED_NOTES_LIST();
    }


    public void deleteDeleted(final DeletedNotes notes) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                postDao.deleteDeleted(notes);
            }
        });
    }
    public void addDeleted(final DeletedNotes notes) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                postDao.addDeletedNotes(notes);
            }
        });
    }

    public void deleteAll(List<DeletedNotes> deletedNotes) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                postDao.DADeleted(deletedNotes);
            }
        });
    }
}

