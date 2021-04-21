package com.eldhose.weeklyplanner.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.eldhose.weeklyplanner.ModelClass.Note;
import com.eldhose.weeklyplanner.R;
import com.eldhose.weeklyplanner.ModelClass.SubNote;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.List;

public class NotesRecyclerAdapter extends FirestoreRecyclerAdapter<Note, NotesRecyclerAdapter.NoteViewHolder> {

    OnItemClickListener listener;

    public NotesRecyclerAdapter(@NonNull FirestoreRecyclerOptions<Note> options) {
        super(options);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position, @NonNull Note note) {

        holder.checkBox.setChecked(note.isCompleted());
        holder.titleTextView.setText(note.getTitle());
       // holder.dateTextView.setText(note.getDueDate());
        holder.cardView.setCardBackgroundColor(note.getColor());

       // boolean isExpanded = note.isExpanded();
      //  holder.expandLayout.setVisibility(isExpanded?View.VISIBLE:View.GONE);



       // initRecyclerView2(holder.recyclerView2,getSnapshots().getSnapshot(position));

    }

   /* public void initRecyclerView2 (RecyclerView recyclerView,DocumentSnapshot documentSnapshot){

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

    }*/

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view =inflater.inflate(R.layout.note_row,parent,false);
        return new NoteViewHolder(view);

    }
    public void deleteItem(int p)
    {
        final DocumentReference documentReference = getSnapshots().getSnapshot().getReference();
        documentReference.delete();
        /* final String ref =documentSnapshot.getId();

        FirebaseFirestore.getInstance()
                .collection("subNote")
                .whereEqualTo("docRefid",ref)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        WriteBatch batch = FirebaseFirestore.getInstance().batch();

                        List<DocumentSnapshot> snapshotList = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot snapshot: snapshotList) {
                            batch.delete(snapshot.getReference());
                    }
                        batch.commit();
                }

                });*/


    }

    public class NoteViewHolder extends RecyclerView.ViewHolder {

        TextView titleTextView , dateTextView ,addSubNote;
        CheckBox checkBox;
        CardView cardView;
        RecyclerView recyclerView2;
        ImageView expandButton;
        LinearLayout expandLayout;

        public NoteViewHolder(@NonNull final View itemView) {
            super(itemView);

            titleTextView = itemView.findViewById(R.id.title1);
           // dateTextView =itemView.findViewById(R.id.date1);
            checkBox=itemView.findViewById(R.id.checkbox1);
            cardView = itemView.findViewById(R.id.notell1);

/*
            //for adding subnote on long click
            addSubNote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int position= getAdapterPosition();
                    if(position!=RecyclerView.NO_POSITION && listener!=null){
                        listener.OnItemClick(getSnapshots().getSnapshot(position),position);
                    }
                }
            });

            //for expanding layout
            expandButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                        int p = getAdapterPosition();
                        if(getItem(p).isExpanded())
                            expandButton.setRotation(180);
                        else
                            expandButton.setRotation(0);
                        getItem(p).setExpanded(!getItem(p).isExpanded());
                        notifyItemChanged(p);

                }
            });*/

        }

    }


    public interface OnItemClickListener{
        void OnItemClick(DocumentSnapshot documentSnapshot, int position);

    }

    public void setOnItemClickListener(OnItemClickListener listener)
    {
        this.listener=listener;
    }

}
