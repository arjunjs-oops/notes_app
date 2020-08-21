package com.example.remember.RecyclerView;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.remember.Model.Notes.java.Notes;
import com.example.remember.R;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class NotesAdapter
        extends RecyclerView.Adapter<NotesAdapter.ViewHolder> {
    onItemClick mItemClick;
    List<Notes> mNotes;
    List<Notes> mDuplicate;
    Context context;
    Boolean isSwitchView = false;
    private static final String TAG = "NotesAdapter";

    public NotesAdapter(Context context, ArrayList<Notes> mmNotes, onItemClick onItemClick) {
        this.mItemClick = onItemClick;
        this.context = context;
        this.mNotes = mmNotes;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_layout, parent, false);
        return new ViewHolder(view, mItemClick);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.title.setText(mNotes.get(position).getTitle());
        holder.date.setText(mNotes.get(position).getTimestamp());
        holder.contents.setText(mNotes.get(position).getContent());

    }

    @Override
    public int getItemCount() {
        return mNotes.size();
    }


    public boolean toggleItemViewType() {
        isSwitchView = !isSwitchView;
        return isSwitchView;
    }

    public void setArrayList(List<Notes> notes) {
        if (notes != null) {
            mNotes.clear();
            mNotes.addAll(notes);
        } else {
            mNotes = notes;
        }
        notifyDataSetChanged();
    }



    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title, contents,date;
        onItemClick itemClick;

        public ViewHolder(@NonNull View itemView,onItemClick onItemClick) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            contents = itemView.findViewById(R.id.details);
            date = itemView.findViewById(R.id.datetime);
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
