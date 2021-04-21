package com.eldhose.weeklyplanner.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.eldhose.weeklyplanner.Login.LoginActivity;
import com.eldhose.weeklyplanner.R;
import com.google.firebase.auth.FirebaseAuth;

public class ProfileFragment extends Fragment implements View.OnClickListener {

    private Button logoutButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile,container,false);

        logoutButton = view.findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(this);

        return view;
    }

    private void logoutUser(){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getActivity(), LoginActivity.class));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.logoutButton :  logoutUser();

        }
    }
}
