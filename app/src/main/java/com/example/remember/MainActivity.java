package com.example.remember;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.remember.Model.Notes;
import com.example.remember.RecyclerView.NotesAdapter;
import com.example.remember.Room.Repo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
implements NotesAdapter.onItemClick{
    RecyclerView recyclerView;
    FloatingActionButton actionButton;
    NotesAdapter adapter;
//    Repo repo;
    private  ArrayList<Notes> notes = new ArrayList<>();
    private static final String TAG = "MainActivity";
    CoordinatorLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        layout = findViewById(R.id.parent_rel);
        recyclerView = findViewById(R.id.notes_list);
        getSupportActionBar();
        setTitle(R.string.app_name);
//        repo = new Repo(getApplicationContext());
        actionButton = findViewById(R.id.fab);
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,NoteData.class);
                startActivity(intent);
            }
        });
        setRecyclerView();
        //ToDo: Create A Database and Its DAO
        dummyData();
    }

    private void dummyData() {

        for (int i = 0; i < 10; i++) {
            Notes list = new Notes();
            list.setTitle("Title: "+i);
            list.setDate("Dec 1st 2020");
            list.setDescription("Hello it's Me I've Wondering if after All this\n Years Its Like to me");
            notes.add(list);
            adapter.notifyDataSetChanged();
        }
    }




    private void setRecyclerView() {
        adapter = new NotesAdapter(this);
        adapter.setNotes(notes);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(recyclerView);


    }
    public  ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT|ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
           final int position =  viewHolder.getAdapterPosition();
         final Notes removed =   notes.remove(position);
//            repo.removeData(notes.get(position));
           adapter.notifyDataSetChanged();
            Snackbar.make(layout,removed.getTitle()+" has been removed", Snackbar.LENGTH_LONG).setAction("Undo", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    notes.add(position,removed);
                    adapter.notifyDataSetChanged();
                }
            }).show();



        }
    };

    @Override
    public void itemClick(int position) {
        Log.d(TAG, "itemClick"+position+"was Clicked");
        Intent intent = new Intent(MainActivity.this,NoteData.class);
        intent.putExtra("Note_Data", (Parcelable) notes.get(position));
        startActivity(intent);

    }
}
