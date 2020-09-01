package com.example.remember;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.remember.Model.Notes.java.DeletedNotes;
import com.example.remember.Model.Notes.java.Notes;
import com.example.remember.RecyclerView.DelNoteAdapter;
import com.example.remember.RecyclerView.NotesAdapter;
import com.example.remember.Room.Adding.PostViewModel;
import com.example.remember.Utils.ReverseList;
import com.example.remember.Utils.ReverseListDeleted;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class Deleted extends AppCompatActivity {
    RecyclerView recyclerView;
    DelNoteAdapter gAdapter;
    PostViewModel viewModel;
    List<DeletedNotes> deletedNotes = new ArrayList<>();
    private static final String TAG = "Deleted";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deleted);
        recyclerView = findViewById(R.id.listOfDeletedNotes);
        setRecyclerView();
        viewModel = ViewModelProviders.of(this).get(PostViewModel.class);
        viewModel.getAllDeletedNotes().observe(this, new Observer<List<DeletedNotes>>() {
            @Override
            public void onChanged(List<DeletedNotes> deletedNotes) {
                Log.d(TAG, "onChanged: "+deletedNotes.size());
                gAdapter.setArrayList(ReverseListDeleted.reverseArrayList(deletedNotes));
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getWindow().setStatusBarColor(Color.WHITE);
        }

    }

    private void setRecyclerView() {
        gAdapter = new DelNoteAdapter(deletedNotes);
        recyclerView.setAdapter(gAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(recyclerView);

    }


    public ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            DeletedNotes deletedNotes1 = deletedNotes.get(position);
            if (direction == ItemTouchHelper.RIGHT) {
                gAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
                viewModel.deleteDeleted(deletedNotes1);


            } else {
                Notes notes = new Notes();
                notes.setTitle(deletedNotes1.getTitle());
                notes.setTimestamp(deletedNotes1.getTimestamp());
                notes.setContent(deletedNotes1.getContent());
                gAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
                viewModel.savePost(notes);
                viewModel.deleteDeleted(deletedNotes1);
            }
        }


        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftBackgroundColor(getColor(R.color.mypersonal))
                    .addSwipeLeftActionIcon(R.drawable.ic_baseline_add_to_home_screen_24)
                    .addSwipeRightBackgroundColor(getColor(R.color.red))
                    .addSwipeRightActionIcon(R.drawable.ic_baseline_delete_forever_24)
                    .create()
                    .decorate();
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };

}
