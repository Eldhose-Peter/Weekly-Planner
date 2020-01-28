package com.eldhose.weeklyplanner;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

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

public class WeeklyFragment extends Fragment implements View.OnClickListener {

    FloatingActionButton fab;
    RecyclerView recyclerView1;
    NotesRecyclerAdapter notesRecyclerAdapter;
    private FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_weekly,container,false);

        mAuth = FirebaseAuth.getInstance();

        fab = view.findViewById(R.id.newNoteFab);
        fab.setOnClickListener(this);

        recyclerView1 = view.findViewById(R.id.recyclerView1);


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser()!=null)
            initRecyclerView(mAuth.getCurrentUser());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.newNoteFab : startActivity(new Intent(getActivity(),NewNoteActivity.class));

        }
    }

    private void initRecyclerView(FirebaseUser user)
    {
        Query query = FirebaseFirestore.getInstance()
                .collection("notes")
                .whereEqualTo("uid",user.getUid());

        FirestoreRecyclerOptions<Note>options= new FirestoreRecyclerOptions.Builder<Note>()
                .setQuery(query,Note.class)
                .build();

        notesRecyclerAdapter = new NotesRecyclerAdapter(options);
        recyclerView1.setAdapter(notesRecyclerAdapter);

        //for real time update
        notesRecyclerAdapter.startListening();

        notesRecyclerAdapter.setOnItemClickListener(new NotesRecyclerAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(DocumentSnapshot documentSnapshot, int position) {
                 String docRef = documentSnapshot.getId();
                 showAlertDialog(docRef);


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
