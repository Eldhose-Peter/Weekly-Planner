package com.eldhose.weeklyplanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.eldhose.weeklyplanner.ModelClass.Note;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.thebluealliance.spectrum.SpectrumPalette;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class NewNoteActivity extends AppCompatActivity implements View.OnClickListener {

    FloatingActionButton fab;
    EditText titleEditText , descEditText ;
    TextView dueDateTextView;
    CheckBox isCompletedBox , isRemindedBox;
    Calendar cal = Calendar.getInstance();
    SpectrumPalette palette;

    int color;

    private DatePickerDialog.OnDateSetListener mDateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);

        titleEditText = findViewById(R.id.titleEditText);
        descEditText = findViewById(R.id.descEditText);
        dueDateTextView = findViewById(R.id.dueDateTextView);
        isCompletedBox = findViewById(R.id.isCompleteBox);
        isRemindedBox = findViewById(R.id.isRemindedBox);
        palette = findViewById(R.id.palette);

        color= Color.GRAY;
        palette.setOnColorSelectedListener(
                new SpectrumPalette.OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(int clr) {
                        color = clr;
                    }
                }
        );

        fab = findViewById(R.id.submitFab);
        fab.setOnClickListener(this);

        // to get current date
        String date = (String) DateFormat.format("EEE, MMM d, yyyy",cal);

        dueDateTextView.setText(date);
        dueDateTextView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.submitFab : addNote();
                                    finish();
                                    break;


            case R.id.dueDateTextView :selectDate();
                                        break;
        }
    }

    private void addNote()

    {

        String title = titleEditText.getText().toString();
        String description = descEditText.getText().toString();
        String dueDate = dueDateTextView.getText().toString();
        boolean isCompleted = isCompletedBox.isChecked();
        boolean isReminded = isRemindedBox.isChecked();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Note note = new Note(title,description,dueDate,isCompleted,isReminded,userId,color);

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

    private void selectDate()
    {

        int year = cal.get(Calendar.YEAR );
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        mDateSetListener =new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int y, int m, int d) {

                    Calendar c = Calendar.getInstance();
                    c.set(Calendar.YEAR,y);
                    c.set(Calendar.MONTH,m);
                    c.set(Calendar.DAY_OF_MONTH,d);

                    String date = (String) DateFormat.format("EEE, MMM d, yyyy",c);
                    dueDateTextView.setText(date);
            }
        };

        DatePickerDialog dialog = new DatePickerDialog(NewNoteActivity.this
                ,android.R.style.Theme_Holo_Light_Dialog_MinWidth,mDateSetListener,year,month,day);
        dialog.show();
     }


}
