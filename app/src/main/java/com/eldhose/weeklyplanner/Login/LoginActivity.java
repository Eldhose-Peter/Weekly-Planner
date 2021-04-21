package com.eldhose.weeklyplanner.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eldhose.weeklyplanner.MainActivity;
import com.eldhose.weeklyplanner.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    RelativeLayout relativeLay1 , relativeLay2;
    TextView signUpTextView;
    EditText emailEditText , passwordEditText;
    Button loginButton;
    private FirebaseAuth mAuth;


    Handler handler= new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            relativeLay1.setVisibility(View.VISIBLE);
            relativeLay2.setVisibility(View.VISIBLE);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);

        loginButton =findViewById(R.id.loginButton);
        signUpTextView = findViewById(R.id.signUpTextView);
        relativeLay1 = findViewById(R.id.relativeLay1);
        relativeLay2 = findViewById(R.id.relativeLay2);

        handler.postDelayed(runnable,2000);

        //start main Activity if already signed in
        if(FirebaseAuth.getInstance().getCurrentUser() != null) {
            startActivity(new Intent(this, MainActivity.class));
            this.finish();
        }

        mAuth = FirebaseAuth.getInstance();

        signUpTextView.setOnClickListener(this);
        loginButton.setOnClickListener(this);
    }

    public void loginUser()
    {
        String email = emailEditText.getText().toString().trim();
        String pass = passwordEditText.getText().toString().trim();

        if(email.isEmpty()|pass.isEmpty()){

            Toast.makeText(this, "Feilds cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email,pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(LoginActivity.this, "Welcome back User", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginActivity.this,MainActivity.class));
                            finish();
                        }

                          else {
                            Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                });
    }

    @Override
    public void onClick(View view) {

        switch (view.getId())
        {
            case R.id.signUpTextView : startActivity(new Intent(this, SignUpActivity.class));
                                        break;

            case R.id.loginButton : loginUser();
                                    break;
        }

    }
}
