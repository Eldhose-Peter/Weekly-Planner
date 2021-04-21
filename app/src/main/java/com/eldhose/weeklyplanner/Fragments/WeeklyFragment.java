package com.eldhose.weeklyplanner.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.eldhose.weeklyplanner.MainActivity;
import com.eldhose.weeklyplanner.NewNoteActivity;
import com.eldhose.weeklyplanner.ModelClass.Note;
import com.eldhose.weeklyplanner.Adapters.NotesRecyclerAdapter;
import com.eldhose.weeklyplanner.R;
import com.eldhose.weeklyplanner.ModelClass.SubNote;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class WeeklyFragment extends Fragment implements View.OnClickListener{

    FloatingActionButton fab;
    RecyclerView recyclerView1;
    NotesRecyclerAdapter notesRecyclerAdapter;
    private FirebaseAuth mAuth;
    Spinner spinner;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_weekly,container,false);

        mAuth = FirebaseAuth.getInstance();

        fab = view.findViewById(R.id.newNoteFab);
        fab.setOnClickListener(this);

        recyclerView1 = view.findViewById(R.id.recyclerView1);

        spinner = view.findViewById(R.id.spinner1);
        setSort();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser()!=null) {
            setSort();


        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.newNoteFab : startActivity(new Intent(getActivity(), NewNoteActivity.class));

        }
    }

    //swipe to delete
    ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            // Take action for the swiped item
            if(direction==ItemTouchHelper.LEFT) {
                notesRecyclerAdapter.deleteItem(viewHolder.getAdapterPosition());
                //setSort();
              //  ((NotesRecyclerAdapter.NoteViewHolder) viewHolder).deleteItem();
            }


        }

        public void onChildDraw (Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive){

            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addBackgroundColor(ContextCompat.getColor(getContext(), R.color.white))
                    .addActionIcon(R.drawable.delete_icon)
                    .create()
                    .decorate();

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };


    private void initRecyclerView(FirebaseUser user ,String order)
    {
        Query query = FirebaseFirestore.getInstance()
                .collection("notes")
                .whereEqualTo("uid",user.getUid())
                .orderBy(order);

        FirestoreRecyclerOptions<Note>options= new FirestoreRecyclerOptions.Builder<Note>()
                .setQuery(query,Note.class)
                .build();

        notesRecyclerAdapter = new NotesRecyclerAdapter(options);
        recyclerView1.setAdapter(notesRecyclerAdapter);
        notesRecyclerAdapter.setOnItemClickListener(new NotesRecyclerAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(DocumentSnapshot documentSnapshot, int position) {
                String docRef = documentSnapshot.getId();
                showAlertDialog(docRef);
            }
        });

        //for real time update
        notesRecyclerAdapter.startListening();


        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView1);

    }



    private void setSort()
    {
        //spinner adapter
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.sort_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
                switch (pos){
                    case 0 :
                        initRecyclerView(mAuth.getCurrentUser(),"dueDate");
                        break;

                    case 1 :
                        initRecyclerView(mAuth.getCurrentUser(),"color");
                        break;

                    case 2 :
                        initRecyclerView(mAuth.getCurrentUser(),"title");
                        break;

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

                Toast.makeText(getActivity(), "date", Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void showAlertDialog(final String docRefid)
    {
        final EditText subNoteEditText = new EditText(getActivity());

        new AlertDialog.Builder(getActivity())
                .setTitle("Add SubNote")
                .setView(subNoteEditText)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                            String text =subNoteEditText.getText().toString();
                            SubNote subNote = new SubNote(text,false,docRefid);

                            FirebaseFirestore.getInstance()
                                    .collection("subNote")
                                    .add(subNote)
                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            Toast.makeText(getActivity(), "Sub note Is Added", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getActivity(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                    }
                })
                .setNegativeButton("Cancel",null)
                .show();
    }




 }

