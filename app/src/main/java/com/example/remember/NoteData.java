package com.example.remember;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.remember.Model.Notes;
import com.example.remember.Room.Repo;

public class NoteData extends AppCompatActivity implements
        View.OnTouchListener,
        GestureDetector.OnGestureListener,
        GestureDetector.OnDoubleTapListener, View.OnClickListener {
    EditText setTitle,addNotes;
    TextView getTitle;
    RelativeLayout mCheck,mEdit;
    ImageView check,edit;
    GestureDetector mDetector;
    Notes mNotes;
    private static final int in_Edit_Mode = 1;
    private static final int in_ViewMode = 0;
    private int state;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_activity);
//        repo = new Repo(getApplicationContext());
        instantiate();
      onClickEventManager();


        if (getIntents()) {
            disableInteraction();
            inViewMode();
        } else {
            setDefault();
            inEditMode();
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void onClickEventManager(){
        mDetector = new GestureDetector(this, this);
        addNotes.setOnTouchListener(this);
        mEdit.setOnClickListener(this);
        mCheck.setOnClickListener(this);
        getTitle.setOnClickListener(this);
    }

    private void instantiate() {
        mEdit = findViewById(R.id.mEdit);
        mCheck = findViewById(R.id.mcheck);
        edit = findViewById(R.id.edit);
        check = findViewById(R.id.check);
        setTitle =findViewById(R.id.edit_text);
        getTitle = findViewById(R.id.set_text);
        addNotes = findViewById(R.id.add_notes);

    }
    private void setDefault(){
        setTitle.setText("Initial Note");
        addNotes.setText("This is my Data");
    }

    private void inViewMode(){
        state = in_ViewMode;
        mEdit.setVisibility(View.VISIBLE);
        mCheck.setVisibility(View.GONE);
        check.setVisibility(View.GONE);
        edit.setVisibility(View.VISIBLE);
        setTitle.setVisibility(View.GONE);
        getTitle.setVisibility(View.VISIBLE);
        disableInteraction();
    }

    private void inEditMode(){
        state = in_Edit_Mode;
        mEdit.setVisibility(View.GONE);
        mCheck.setVisibility(View.VISIBLE);
        check.setVisibility(View.VISIBLE);
        edit.setVisibility(View.GONE);
        setTitle.setVisibility(View.VISIBLE);
        getTitle.setVisibility(View.GONE);
        setTitle.setFocusable(true);

    }

    private boolean getIntents(){
        if (getIntent().hasExtra("Note_Data")){
             mNotes =  getIntent().getParcelableExtra("Note_Data");
            return true;

        }
        else {
            return false;
        }
    }

    private void checkData(){
//        String noteDiscription = addNotes.getText().toString();
//      if(!getTitle.toString().contains(setTitle.toString())|| !addNotes.getText().toString().contains(noteDiscription)) {
//          mFinal.setTitle(getTitle.getText().toString());
//          mFinal.setDescription(addNotes.getText().toString());
////          repo.addData(mFinal);
//      }
    }

    private void enableInteraction(){
        addNotes.setSelection(setTitle.getText().length());
        addNotes.setFocusableInTouchMode(true);
        addNotes.setFocusable(true);
        addNotes.setCursorVisible(true);
        addNotes.setKeyListener(new EditText(this).getKeyListener());
        addNotes.requestFocus();


    }
    private void disableInteraction(){
        addNotes.setFocusableInTouchMode(false);
        addNotes.setFocusable(false);
        addNotes.setCursorVisible(true);
        addNotes.setOnKeyListener(null);
        addNotes.setOnKeyListener(null);
        addNotes.clearFocus();


    }
    private void disableSoftKey(){
        InputMethodManager manager = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = getCurrentFocus();
        if (view == null) {
            view = new View(this);
        }
        manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return mDetector.onTouchEvent(motionEvent);
    }


    @Override
    public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent motionEvent) {
        inEditMode();
        return true;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onBackPressed() {
        if(state==in_Edit_Mode){
            onClick(mCheck);

        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.mcheck:
                inViewMode();
                disableSoftKey();
                checkData();
                break;
            case R.id.set_text:
                inEditMode();
                break;
            case R.id.mEdit:
                finish();

                break;



        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("state",state);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        int saved = savedInstanceState.getInt("state");
        if(saved==in_Edit_Mode){
            inEditMode();
        }
    }
}
