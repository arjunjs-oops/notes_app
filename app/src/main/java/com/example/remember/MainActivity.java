package com.example.remember;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.room.Delete;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.remember.Model.Notes.java.Notes;
import com.example.remember.RecyclerView.NotesAdapter;
import com.example.remember.Room.Repo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
implements NotesAdapter.onItemClick {
    RecyclerView recyclerView;
    FloatingActionButton actionButton;
    NotesAdapter adapter;
    private Repo repo;
    private static final int inListView = 0;
    private  static final int inGridView = 1;
    private int state = inGridView;
    private ArrayList<Notes> mNotes = new ArrayList<>();
    CoordinatorLayout layout;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        layout = findViewById(R.id.parent_rel);
        recyclerView = findViewById(R.id.notes_list);
        repo = new Repo(this);
        setMenuOptions();
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options, menu);
        MenuItem menuItem = menu.findItem(R.id.search);
        return true;
    }


    private void setMenuOptions() {
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.list:
                        Toast.makeText(MainActivity.this, "Clicked List", Toast.LENGTH_SHORT).show();
                        state = inListView;
                        setRecyclerView();
                        break;
                    case R.id.deleted:
                        Intent intent = new Intent(MainActivity.this, Deleted.class);
                        startActivity(intent);
                        Toast.makeText(MainActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });


    }

    private void getAllLive() {
        repo.getAllNotes().observe(this, new Observer<List<Notes>>() {
            @Override
            public void onChanged(@Nullable List<Notes> notes) {
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
        adapter.setNotes(getApplicationContext());
        adapter.setArray(mNotes);
        recyclerView.setAdapter(adapter);
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(recyclerView);

        if(state ==1) {
            recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, 1));
        }
        else {
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        }


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

    };

    @Override
    public void itemClick(int position) {
        Intent intent = new Intent(MainActivity.this, NoteData.class);
        intent.putExtra("Note_Data", mNotes.get(position));
        startActivity(intent);

    }

    private void updateCustomNotes(String s) {
        repo.AsyncTaskCustom(s).observe((LifecycleOwner) getApplicationContext(), new Observer<List<Notes>>() {
            @Override
            public void onChanged(List<Notes> notes) {

                if (notes.size() > 0) {
                    mNotes.clear();
                    mNotes.addAll(notes);
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }
}