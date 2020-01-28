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
import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class NotesRecyclerAdapter extends FirestoreRecyclerAdapter<Note, NotesRecyclerAdapter.NoteViewHolder> {
    private OnItemClickListener listener;

    public NotesRecyclerAdapter(@NonNull FirestoreRecyclerOptions<Note> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull NoteViewHolder holder, int position, @NonNull Note note) {

        holder.checkBox.setChecked(note.isCompleted());
        holder.titleTextView.setText(note.getTitle());


        initRecyclerView2(holder.recyclerView2,getSnapshots().getSnapshot(position));

    }

    private void initRecyclerView2 (RecyclerView recyclerView,DocumentSnapshot documentSnapshot){

        String docRef = documentSnapshot.getId();

        Query query = FirebaseFirestore.getInstance()
                .collection("subNote")
                .whereEqualTo("docRefid",docRef);

        FirestoreRecyclerOptions<SubNote>options = new FirestoreRecyclerOptions.Builder<SubNote>()
                .setQuery(query,SubNote.class)
                .build();

        SubNotesRecyclerAdapter subNotesRecyclerAdapter = new SubNotesRecyclerAdapter(options);
        recyclerView.setAdapter(subNotesRecyclerAdapter);

        subNotesRecyclerAdapter.startListening();

    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view =inflater.inflate(R.layout.note_row,parent,false);
        return new NoteViewHolder(view);

    }

    class NoteViewHolder extends RecyclerView.ViewHolder {

        TextView titleTextView;
        CheckBox checkBox;

        RecyclerView recyclerView2;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);

            titleTextView = itemView.findViewById(R.id.title1);
            checkBox=itemView.findViewById(R.id.checkbox1);

            recyclerView2 = itemView.findViewById(R.id.recyclerView2);
            //for adding subnote on long click
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int position= getAdapterPosition();
                    if(position!=RecyclerView.NO_POSITION && listener!=null){
                        listener.OnItemClick(getSnapshots().getSnapshot(position),position);
                    }
                }
            });
        }
    }

    public interface OnItemClickListener{
        void OnItemClick(DocumentSnapshot documentSnapshot, int position);

    }
    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener=listener;
    }
}
