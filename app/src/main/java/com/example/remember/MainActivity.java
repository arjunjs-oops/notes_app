
package com.example.remember;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import com.example.remember.Model.Notes.java.Notes;
import com.example.remember.RecyclerView.NotesAdapter;
import com.example.remember.Room.Adding.PostViewModel;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NotesAdapter.onItemClick,
        SearchView.OnQueryTextListener{
    private static final String TAG = "MainActivity";
    RecyclerView recyclerView;
    FloatingActionButton actionButton;
    NotesAdapter gAdapter;
    Menu menuItem;
    private  ArrayList<Notes> deletedNotes = new ArrayList<>();
    PostViewModel postViewModel;
    private ArrayList<Notes> mNotes = new ArrayList<>();
    CoordinatorLayout layout;
    SearchView searchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar((MaterialToolbar)findViewById(R.id.toolbar));
        layout = findViewById(R.id.parent_rel);
        recyclerView = findViewById(R.id.notes_list);
        searchView =(SearchView)findViewById(R.id.search);
        searchView.setOnQueryTextListener(this);
        postViewModel = ViewModelProviders.of(this).get(PostViewModel.class);
        postViewModel.getAllPosts().observe(this, new Observer<List<Notes>>() {
            @Override
            public void onChanged(List<Notes> posts) {
                gAdapter.setArrayList(posts);
                gAdapter.notifyDataSetChanged();
            }
        });

        actionButton = findViewById(R.id.fab);
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NoteData.class);
                startActivity(intent);
            }
        });
        setRecyclerView();
    }

    @SuppressLint("RestrictedApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options,menu);
        this.menuItem = menu;
        return super.onCreateOptionsMenu(menu);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.list:
                boolean isSwitched = gAdapter.toggleItemViewType();
                changeIcon(isSwitched);
                recyclerView.setLayoutManager(isSwitched?new LinearLayoutManager(this):new StaggeredGridLayoutManager(2,1));
                gAdapter.notifyDataSetChanged();
                return true;
            default: return super.onOptionsItemSelected(item);
        }
    }

    public void changeIcon(final Boolean isSwitched){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                MenuItem item = menuItem.findItem(R.id.list);

                if (isSwitched) {
                    item.setIcon(R.drawable.ic_baseline_grid_on_24);
                }
                else {
                    item.setIcon(R.drawable.ic_baseline_format_list_bulleted_24);

                }
            }
        });
    }




    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        postViewModel.getCustomItem(newText).observe(this, new Observer<List<Notes>>() {
            @Override
            public void onChanged(List<Notes> posts) {
                gAdapter.setArrayList(posts);
            }
        });


        return true;
    }



    private void setRecyclerView() {
        gAdapter = new NotesAdapter(this, mNotes, this);
        recyclerView.setAdapter(gAdapter);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, 1));
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
            final Notes removed;
            removed = mNotes.get(viewHolder.getAdapterPosition());
            deletedNotes.add(removed);
            mNotes.remove(viewHolder.getLayoutPosition());
            if(removed != null) {
                Log.d(TAG, "onSwiped is called "+removed.getTitle());
                postViewModel.deletePost(removed);
                gAdapter.notifyDataSetChanged();
            }
            Snackbar snackbar = Snackbar.make(layout, removed.getTitle() + " has been removed", Snackbar.LENGTH_LONG);
            snackbar.setAction("Undo",
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                           postViewModel.savePost(removed);
                        }
                    });
            snackbar.show();

        }


    };


    @Override
    public void itemClick(int position) {
        Intent intent = new Intent(MainActivity.this, NoteData.class);
        intent.putExtra("Note_Data", mNotes.get(position));
        startActivity(intent);

    }


}