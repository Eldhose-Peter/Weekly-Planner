package com.eldhose.weeklyplanner;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class SubNotesRecyclerAdapter extends FirestoreRecyclerAdapter<SubNote, SubNotesRecyclerAdapter.SubNoteViewHolder> {

    public SubNotesRecyclerAdapter(@NonNull FirestoreRecyclerOptions options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull SubNoteViewHolder holder, int position, @NonNull SubNote subNote) {
        holder.title2.setText(subNote.getTitle());
        holder.checkBox2.setChecked(subNote.isCompleted());
    }

    @NonNull
    @Override
    public SubNoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater =LayoutInflater.from(parent.getContext());
        View view =layoutInflater.inflate(R.layout.subnote_row,parent,false);
        return new SubNoteViewHolder(view);

    }


    class SubNoteViewHolder extends RecyclerView.ViewHolder {

        TextView title2;
        CheckBox checkBox2;

        public SubNoteViewHolder(@NonNull View itemView) {
            super(itemView);

            title2 = itemView.findViewById(R.id.title2);
            checkBox2 = itemView.findViewById(R.id.checkbox2);



        }
    }
}
