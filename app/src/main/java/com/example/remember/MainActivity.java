package com.example.remember;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import com.example.remember.Model.Notes.java.Notes;
import com.example.remember.RecyclerView.NotesAdapter;
import com.example.remember.Room.NewDatabase.Repo;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
implements NotesAdapter.onItemClick,
SearchView.OnQueryTextListener{
    RecyclerView recyclerView;
    FloatingActionButton actionButton;
    NotesAdapter adapter;
    private Repo repo;
    private static final int inListView = 0;
    private  static final int inGridView = 1;
    private int state = inGridView;
    private static final int deleted_state = 1;
    private static final int undo_deleted = 0;
    private  int deletedState;
    private ArrayList<Notes> mNotes = new ArrayList<>();
    CoordinatorLayout layout;
    SearchView searchView;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar((MaterialToolbar)findViewById(R.id.toolbar));
        layout = findViewById(R.id.parent_rel);
        recyclerView = findViewById(R.id.notes_list);
        searchView =(SearchView)findViewById(R.id.search);
        searchView.setOnQueryTextListener(this);
        repo = new Repo(this);
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
        getMenuInflater().inflate(R.menu.options,menu);
        MenuItem item =  menu.findItem(R.id.search);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.list:
                state = inListView;
                setRecyclerView();
                return true;
            case R.id.deleted:
                Intent intent = new Intent(MainActivity.this,Deleted.class);
                startActivity(intent);
                return true;

            default: return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        adapter.getFilter().filter(newText);
        return true;
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
                Log.d(TAG, "setRecyclerView: "+mNotes.size());
                adapter.addArrayList(mNotes);
                adapter.notifyDataSetChanged();
            }
        });
    }


    private void setRecyclerView() {
        adapter = new NotesAdapter(this,mNotes);

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
            deletedState = deleted_state;

            adapter.notifyDataSetChanged();
            Snackbar.make(layout,
                    removed.getTitle() + " has been removed",
                    Snackbar.LENGTH_LONG)
                    .setAction("Undo",
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    repo.addData(removed);
                                    deletedState = undo_deleted;
                                    adapter.notifyDataSetChanged();
                                }
                            }).show();
            if(deletedState==1){
                //TODO :Pass to Deleted Activity
            }
        }


    };


    @Override
    public void itemClick(int position) {
        Intent intent = new Intent(MainActivity.this, NoteData.class);
        intent.putExtra("Note_Data", mNotes.get(position));
        startActivity(intent);

    }


}