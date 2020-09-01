package com.example.remember.RecyclerView;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.remember.Model.Notes.java.Notes;
import com.example.remember.R;
import java.util.ArrayList;
import java.util.List;

public class NotesAdapter
        extends RecyclerView.Adapter<NotesAdapter.ViewHolder> {
    onItemClick mItemClick;
   private List<Notes> notesList = new ArrayList<>();
   private Context context;
   private Boolean isSwitchView = false;
    private static final String TAG = "NotesAdapter";

    public NotesAdapter(Context context,onItemClick onItemClick) {
        this.mItemClick = onItemClick;
        this.context = context;
    }




    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_layout, parent, false);
        return new ViewHolder(view, mItemClick);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.title.setText(notesList.get(position).getTitle());
        holder.date.setText(notesList.get(position).getTimestamp());
        holder.contents.setText(notesList.get(position).getContent());

    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }


    public boolean toggleItemViewType() {
        isSwitchView = !isSwitchView;
        return isSwitchView;
    }

    public void setArrayList(List<Notes> notes) {
        if (notes != null) {
            notesList.clear();
            notesList.addAll(notes);
        } else {
            notesList =notes;
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
