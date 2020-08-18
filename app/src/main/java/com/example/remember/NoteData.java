package com.example.remember;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.remember.Model.Notes.java.Notes;
import com.example.remember.Room.NewDatabase.Repo;
//import com.example.remember.Utils.DateTime;

public class NoteData extends AppCompatActivity implements
        View.OnTouchListener,
        GestureDetector.OnGestureListener,
        GestureDetector.OnDoubleTapListener,
        View.OnClickListener,
        TextWatcher {

    EditText setTitle,addNotes;
    TextView getTitle;
    RelativeLayout mCheck,mEdit;
    ImageView check,edit;
    GestureDetector mDetector;
    Notes mNotes;
    Notes mFinal;
    Repo repo;
    boolean isNewNote;
    private static final String TAG = "NoteData";
    private static final int in_Edit_Mode = 1;
    private static final int in_ViewMode = 0;
    private int state;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_activity);
        repo = new Repo(this);
        instantiate();
      onClickEventManager();


        if (getIntents()) {
            setValues();
            inViewMode();
        } else {
            setInNew();
            inEditMode();
            isNewNote = true;
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void saveChanges(){
        if(isNewNote){
            Log.d(TAG, "Add");
        insertData();
        }
        else{
            Log.d(TAG, "Edit");
            update();
        }
    }
    private void update(){
        Log.d(TAG, "insertData:"+mFinal.getContent()+" \n"+mFinal.getTitle()+mFinal.getId());
        repo.updateNote(mFinal);
    }

    private void insertData(){

        repo.addData(mFinal);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void onClickEventManager(){
        mDetector = new GestureDetector(this, this);
        addNotes.setOnTouchListener(this);
        mEdit.setOnClickListener(this);
        mCheck.setOnClickListener(this);
        getTitle.setOnClickListener(this);
        setTitle.addTextChangedListener(this);
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
    private void setValues(){
        setTitle.setText(mNotes.getTitle());
        getTitle.setText(mNotes.getTitle());
        addNotes.setText(mNotes.getContent());
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setInNew(){
        setTitle.setText("New Note");
        getTitle.setText("Add Title");
        mNotes = new Notes();
        mFinal = new Notes();
        mNotes.setTitle(getTitle.getText().toString());
        mFinal.setTitle(getTitle.getText().toString());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void inViewMode(){
        state = in_ViewMode;
        mEdit.setVisibility(View.VISIBLE);
        mCheck.setVisibility(View.GONE);

        check.setVisibility(View.GONE);
        edit.setVisibility(View.VISIBLE);

        setTitle.setVisibility(View.GONE);
        getTitle.setVisibility(View.VISIBLE);

        String temp = addNotes.getText().toString()
                .replace("\n","")
                .replace(" ","");

        if(temp.length()>0){
            mFinal.setTitle(setTitle.getText().toString());
            mFinal.setContent(addNotes.getText().toString());



            if(!mFinal.getContent().equals(mNotes.getContent())||
            !mFinal.getTitle().equals(mNotes.getTitle())){
                Log.d(TAG, "ID is:  "+mFinal.getId());
                saveChanges();
            }

        }

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

        enableInteraction();

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private boolean getIntents(){
        if (getIntent().hasExtra("Note_Data")){
             mNotes =  getIntent().getParcelableExtra("Note_Data");
             mFinal = new Notes();
             mFinal.setTitle(mNotes.getTitle());
             mFinal.setContent(mNotes.getContent());
             mFinal.setTimestamp(mNotes.getTimestamp());
            mFinal.setId(mNotes.getId());

            isNewNote = false;
             inViewMode();
            return true;

        }
        else {
            isNewNote = true;
            state = in_ViewMode;
            return false;
        }
    }


    private void enableInteraction(){
        addNotes.setSelection(addNotes.getText().length());
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBackPressed() {
        if(state==in_Edit_Mode){
            onClick(mCheck);

        }
        else {
            super.onBackPressed();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.mcheck:
                disableSoftKey();
                inViewMode();
                break;
            case R.id.set_text:
                inEditMode();
                setTitle.requestFocus();
                setTitle.setSelection(setTitle.length());
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

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        getTitle.setText(charSequence);
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
