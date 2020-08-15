package com.example.remember.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.remember.Model.Notes;
import com.example.remember.R;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder> {
    onItemClick mItemClick;
    ArrayList<Notes> mNotes;
    public NotesAdapter(onItemClick ItemClick) {
      this.mItemClick = ItemClick;
    }

    public void setNotes(ArrayList<Notes> mNotes){
        this.mNotes = mNotes;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_layout,parent,false);
        return new ViewHolder(view,mItemClick);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.title.setText(mNotes.get(position).getTitle().toString());
        holder.contents.setText(mNotes.get(position).getDescription().toString());

    }

    @Override
    public int getItemCount() {
        return mNotes.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title, contents;
        onItemClick itemClick;

        public ViewHolder(@NonNull View itemView,onItemClick onItemClick) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            contents = itemView.findViewById(R.id.details);
            this.itemClick = onItemClick;
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            itemClick.itemClick(getAdapterPosition());

        }
    }
    public interface onItemClick{
        void itemClick(int position);
    }

}
