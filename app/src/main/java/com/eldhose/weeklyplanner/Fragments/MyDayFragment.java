package com.eldhose.weeklyplanner.Fragments;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.eldhose.weeklyplanner.Adapters.NotesRecyclerAdapter;
import com.eldhose.weeklyplanner.ModelClass.Note;
import com.eldhose.weeklyplanner.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Calendar;
import java.util.GregorianCalendar;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;


public class MyDayFragment extends Fragment {

    GregorianCalendar  calendar = new GregorianCalendar();
    private FirebaseAuth mAuth;
    NotesRecyclerAdapter notesRecyclerAdapter;
    RecyclerView recyclerView3;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_myday,container,false);

        mAuth = FirebaseAuth.getInstance();
        recyclerView3=view.findViewById(R.id.recyclerView3);


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser()!=null) {
            horizontalCal();
        }
    }

    public void horizontalCal() {

        /* starts before 1 month from now */
        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.MONTH, -1);

        /* ends after 1 month from now */
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.MONTH, 1);

        HorizontalCalendar horizontalCalendar = new HorizontalCalendar.Builder(getActivity(), R.id.dateRecycler)
                .range(startDate, endDate)
                .datesNumberOnScreen(7)
                .build();

        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Calendar date, int position) {
                Toast.makeText(getContext(), DateFormat.format("EEE, MMM d, yyyy", date) + " is selected!", Toast.LENGTH_SHORT).show();

                initRecyclerView3(mAuth.getCurrentUser(),date);
            }


        });

    }

    private void initRecyclerView3(FirebaseUser user,Calendar d)
    {
        String date = (String) DateFormat.format("EEE, MMM d, yyyy",d);

            Query query = FirebaseFirestore.getInstance()
                    .collection("notes")
                    .whereEqualTo("uid",user.getUid())
                    .whereEqualTo("dueDate",date);
                    /*.get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            List<DocumentSnapshot>documentSnapshotList = queryDocumentSnapshots.getDocuments();
                            for(DocumentSnapshot documentSnapshot : documentSnapshotList)
                            {
                                Log.i("harry",documentSnapshot.getData().toString());
                                Log.i("harrypotter","query is succes");
                            }
                        }
                    })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.i("harrypotter",e.toString());
                        }
                    });*/
           // Log.i("Dates :",date);

            FirestoreRecyclerOptions<Note> options= new FirestoreRecyclerOptions.Builder<Note>()
                    .setQuery(query,Note.class)
                    .build();

            notesRecyclerAdapter = new NotesRecyclerAdapter(options);
            recyclerView3.setAdapter(notesRecyclerAdapter);

            //for real time update
            notesRecyclerAdapter.startListening();

        }


}

