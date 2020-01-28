package com.eldhose.weeklyplanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class NewNoteActivity extends AppCompatActivity implements View.OnClickListener {

    FloatingActionButton fab;
    EditText titleEditText , descEditText ,dueDateEditText;
    CheckBox isCompletedBox , isRemindedBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);

        titleEditText = findViewById(R.id.titleEditText);
        descEditText = findViewById(R.id.descEditText);
        dueDateEditText = findViewById(R.id.dueDateEditText);
        isCompletedBox = findViewById(R.id.isCompleteBox);
        isRemindedBox = findViewById(R.id.isRemindedBox);

        fab = findViewById(R.id.submitFab);
        fab.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.submitFab : addNote();
                finish();
        }
    }

    private void addNote()

    {

        String title = titleEditText.getText().toString();
        String description = descEditText.getText().toString();
        String dueDate = dueDateEditText.getText().toString();
        boolean isCompleted = isCompletedBox.isChecked();
        boolean isReminded = isRemindedBox.isChecked();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Note note = new Note(title,description,dueDate,isCompleted,isReminded,userId);

                FirebaseFirestore.getInstance()
                        .collection("notes")
                        .add(note)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Toast.makeText(NewNoteActivity.this, "New note is added", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(NewNoteActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });



    }

}
