package com.example.remember;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.remember.Model.Notes.java.Notes;
import com.example.remember.RecyclerView.NotesAdapter;
import com.example.remember.Room.Repo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
implements NotesAdapter.onItemClick {
    RecyclerView recyclerView;
    FloatingActionButton actionButton;
    NotesAdapter adapter;
    private Repo repo;
    private ArrayList<Notes> mNotes = new ArrayList<>();
    private static final String TAG = "MainActivity";
    CoordinatorLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        layout = findViewById(R.id.parent_rel);
        recyclerView = findViewById(R.id.notes_list);
        repo = new Repo(this);


        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        setTitle("Remember");
        actionButton = findViewById(R.id.fab);
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NoteData.class);
                startActivity(intent);
            }
        });
        setRecyclerView();
        getAllLive();
        Log.d(TAG, "onCreate::"+repo.getAllNotes().getValue());
    }

    private void getAllLive() {
        repo.getAllNotes().observe(this, new Observer<List<Notes>>() {
            @Override
            public void onChanged(@Nullable List<Notes> notes) {
                Log.d(TAG, "onChanged: Called The Data");
                if (mNotes.size() > 0) {
                    mNotes.clear();
                }
                if (notes != null) {
                    mNotes.addAll(notes);
                }

                adapter.notifyDataSetChanged();
            }
        });
    }


    private void setRecyclerView() {
        adapter = new NotesAdapter(this);
        adapter.setNotes(mNotes);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, 1));
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(recyclerView);


    }

    public ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            final int position = viewHolder.getAdapterPosition();
            final Notes removed = mNotes.get(position);
            repo.removeData(removed);
            adapter.notifyDataSetChanged();
            Snackbar.make(layout,
                    removed.getTitle() + " has been removed",
                    Snackbar.LENGTH_LONG)
                    .setAction("Undo",
                            new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    repo.addData(removed);
                    adapter.notifyDataSetChanged();
                }
            }).show();


        }
        }

        ;

        @Override
        public void itemClick(int position) {
            Log.d(TAG, "itemClick" + position + "was Clicked");
            Intent intent = new Intent(MainActivity.this, NoteData.class);
            intent.putExtra("Note_Data",  mNotes.get(position));
            startActivity(intent);

        }
    }


