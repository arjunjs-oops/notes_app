package com.example.remember.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.remember.Model.Notes.java.DeletedNotes;
import com.example.remember.Model.Notes.java.Notes;
import com.example.remember.R;

import java.util.ArrayList;
import java.util.List;

public class DelNoteAdapter
        extends RecyclerView.Adapter<DelNoteAdapter.ViewHolder> {

    List<DeletedNotes> mNotes = new ArrayList<>();

    private static final String TAG = "NotesAdapter";

    public DelNoteAdapter(List<DeletedNotes> mNotes) {
        this.mNotes =mNotes;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.deldemo, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.title.setText(mNotes.get(position).getTitle());
        holder.cdate.setText(mNotes.get(position).getCreated());
        holder.ddate.setText(mNotes.get(position).getDeleted());
        holder.contents.setText(mNotes.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        return mNotes.size();
    }



    public void setArrayList(List<DeletedNotes> notes) {
        if (notes != null) {
            mNotes.clear();
            mNotes.addAll(notes);
        } else {
            mNotes = notes;
        }
        notifyDataSetChanged();
    }



    class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, contents,cdate,ddate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            contents = itemView.findViewById(R.id.details);
            cdate = itemView.findViewById(R.id.datetime);
            ddate= itemView.findViewById(R.id.DELdatetime);

        }


    }
}
