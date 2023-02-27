package com.hfad.todoapp.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hfad.todoapp.R;
import com.hfad.todoapp.databinding.FragmentSplashBinding;

public class SplashFragment extends Fragment {

    private FragmentSplashBinding binding;
    private FirebaseAuth mAuth;
    private NavController mNavController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSplashBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        boolean isExist = mAuth.getCurrentUser() != null;
        mNavController = Navigation.findNavController(view);

        binding.navigate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isExist) {
                    mNavController.navigate(R.id.action_splashFragment_to_homeFragment);
                } else {
                    mNavController.navigate(R.id.action_splashFragment_to_signInFragment);
                }
            }
        });
    }
}