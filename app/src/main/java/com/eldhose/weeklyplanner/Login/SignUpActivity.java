package com.eldhose.weeklyplanner.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.eldhose.weeklyplanner.Login.LoginActivity;
import com.eldhose.weeklyplanner.MainActivity;
import com.eldhose.weeklyplanner.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    EditText emailEditTextS , passwordEditTextS;
    Button signUpButtonS;
    TextView loginTextViewS;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        emailEditTextS = findViewById(R.id.emailEditTextS);
        passwordEditTextS = findViewById(R.id.passwordEditTextS);
        signUpButtonS = findViewById(R.id.signUpButtonS);
        loginTextViewS = findViewById(R.id.loginTextViewS);

        mAuth = FirebaseAuth.getInstance();

        signUpButtonS.setOnClickListener(this);
        loginTextViewS.setOnClickListener(this);

    }

    public void registerUser(){
        String email = emailEditTextS.getText().toString().trim();
        String pass = passwordEditTextS.getText().toString().trim();

        if(email.isEmpty()|pass.isEmpty()){

            Toast.makeText(this, "Feilds cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            Toast.makeText(this, "Enter valid email id", Toast.LENGTH_SHORT).show();
            return;
        }

        if(pass.length()<6)
        {
            Toast.makeText(this, "Min Password length required is 5  ", Toast.LENGTH_SHORT).show();
            return;
        }


        mAuth.createUserWithEmailAndPassword(email,pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(getApplicationContext(), "Welcome Dude", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            finish();
                        }

                        else
                        {
                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                Toast.makeText(getApplicationContext(), "You are already registered", Toast.LENGTH_SHORT).show();
                            }
                            else
                                Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }
                });

    }

    @Override
    public void onClick(View view) {
         switch (view.getId()){
             case R.id.loginTextViewS :startActivity(new Intent(this, LoginActivity.class));
                                            break;

             case R.id.signUpButtonS : registerUser();
                                        break;
         }

    }
}
