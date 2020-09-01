
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
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.remember.Model.Notes.java.DeletedNotes;
import com.example.remember.Model.Notes.java.Notes;
import com.example.remember.RecyclerView.NotesAdapter;
import com.example.remember.Room.Adding.PostViewModel;
import com.example.remember.Utils.DateTime;
import com.example.remember.Utils.ReverseList;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import java.util.ArrayList;
import java.util.List;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt;

public class MainActivity extends AppCompatActivity
        implements NotesAdapter.onItemClick,
        SearchView.OnQueryTextListener{
    private static final String TAG = "MainActivity";
    RecyclerView recyclerView;
    FloatingActionButton actionButton;
    NotesAdapter gAdapter;
    Menu menuItem;
    SharedPreferences preferences ;
    SharedPreferences.Editor editor;
    PostViewModel postViewModel;
    private List<Notes> mNotes = new ArrayList<>();
    CoordinatorLayout layout;
    List<Notes> ulta = new ArrayList<>();
    private static final String NamePreference="RememberAppPreference";
    SearchView searchView;
    Notes deletedNote;
    private boolean aBoolean;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Changing the System Status Bar Theme
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        //Shared Preference Related Stuff
        preferences = this.getSharedPreferences(NamePreference,MODE_PRIVATE);
        editor = preferences.edit();
        aBoolean = preferences.getBoolean("ShouldShow",false);
        if(!aBoolean){
            doAllMagic();
        }

         setSupportActionBar((MaterialToolbar)findViewById(R.id.toolbar));

        layout = findViewById(R.id.parent_rel);
        recyclerView = findViewById(R.id.notes_list);
        searchView = findViewById(R.id.search);


        searchView.setOnQueryTextListener(this);
        setRecyclerView();
        postViewModel = ViewModelProviders.of(this).get(PostViewModel.class);
        postViewModel.getAllPosts().observe(this, new Observer<List<Notes>>() {
            @Override
            public void onChanged(List<Notes> posts) {
                mNotes = ReverseList.reverseArrayList(posts);
                gAdapter.setArrayList(mNotes);
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
        }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options,menu);
        this.menuItem = menu;
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.list:
                if(mNotes.size()>0){
                boolean isSwitched = gAdapter.toggleItemViewType();
                changeIcon(isSwitched);
                recyclerView.setLayoutManager(isSwitched?new LinearLayoutManager(this):new StaggeredGridLayoutManager(2,1));}
                else {
                }
                return true;
            case R.id.deleted:
                Intent intent = new Intent(MainActivity.this,Deleted.class);
                startActivity(intent);
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
                if(posts.size()==0){
                    Toast.makeText(MainActivity.this, "No Item Found", Toast.LENGTH_SHORT).show();
                }
                gAdapter.setArrayList(posts);
            }
        });


        return true;
    }



    private void setRecyclerView() {
        gAdapter = new NotesAdapter(this,this);
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
            deletedNote = mNotes.get(viewHolder.getAdapterPosition());
            mNotes.remove(viewHolder.getLayoutPosition());
            postViewModel.deletePost(deletedNote);
            DeletedNotes deletedNotes= new DeletedNotes();
            deletedNotes.setTimestamp(deletedNote.getTimestamp());
            deletedNotes.setContent(deletedNote.getContent());
            deletedNotes.setTitle(deletedNote.getTitle());
            deletedNotes.setDeleted(DateTime.getDateTime().toString());
            deletedNotes.setPreviousID(deletedNotes.getId());
            postViewModel.saveDeletedNotes(deletedNotes);
            gAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
            Snackbar.make(layout, deletedNote.getTitle() + " has been removed", Snackbar.LENGTH_LONG).show();
        }
        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftBackgroundColor(getColor(R.color.red))
                    .addSwipeLeftActionIcon(R.drawable.ic_baseline_delete_forever_24)
                    .addSwipeRightBackgroundColor(getColor(R.color.red))
                    .addSwipeRightActionIcon(R.drawable.ic_baseline_delete_forever_24)
                    .create()
                    .decorate();
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }

    };







    @Override
    public void itemClick(int position) {
        Intent intent = new Intent(MainActivity.this, NoteData.class);
        Log.d(TAG, "itemClick: "+mNotes.get(position).getTitle());
        intent.putExtra("Note_Data", mNotes.get(position));
        startActivity(intent);

    }

    public void doAllMagic() {
        MaterialTapTargetPrompt.Builder builder=  new MaterialTapTargetPrompt.Builder(this)
                .setTarget(R.id.list)
                .setPrimaryText("Switch View Here")
                .setSecondaryText("Change From Grid Layout To Linear Layout");
        builder.show();
        builder.setPromptStateChangeListener(new MaterialTapTargetPrompt.PromptStateChangeListener() {
            @Override
            public void onPromptStateChanged(@NonNull MaterialTapTargetPrompt prompt, int state) {
               if(state == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED || state == MaterialTapTargetPrompt.STATE_NON_FOCAL_PRESSED) {
                   editor.putBoolean("ShouldShow",true);
                   editor.commit();
                   showDeleted();
               }
            }
        });

    }

    private void showAnother() {
        new MaterialTapTargetPrompt.Builder(this)
                .setTarget(R.id.fab)
                .setPrimaryText("Add Notes Here")
                .setSecondaryText("Click the Notes icon to add the Notes")
                .show();
    }

    private void showDeleted() {
        new MaterialTapTargetPrompt.Builder(this)
                .setTarget(R.id.deleted)
                .setPrimaryText("Deleted Notes")
                .setSecondaryText("Deleted Notes will be here")
                .setPromptStateChangeListener(new MaterialTapTargetPrompt.PromptStateChangeListener() {
                    @Override
                    public void onPromptStateChanged(@NonNull MaterialTapTargetPrompt prompt, int state) {
                        if(state== MaterialTapTargetPrompt.STATE_NON_FOCAL_PRESSED){
                            showAnother();
                        }
                    }
                }).show();


    }

}