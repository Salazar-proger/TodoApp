package com.hfad.todoapp.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.hfad.todoapp.R;
import com.hfad.todoapp.databinding.FragmentSignInBinding;

public class SignInFragment extends Fragment {

    private FragmentSignInBinding binding;
    private NavController mNavController;
    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSignInBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        mNavController = Navigation.findNavController(view);
        buttons();
    }

    private void buttons() {

        binding.navRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mNavController.navigate(R.id.action_signInFragment_to_signUpFragment);
            }
        });

        binding.login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = binding.email.getText().toString();
                String password = binding.password.getText().toString();

                if(!email.isEmpty() && !password.isEmpty()) {
                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {
                                Toast.makeText(getActivity(), "Login successful", Toast.LENGTH_SHORT).show();
                                mNavController.navigate(R.id.action_signInFragment_to_homeFragment);
                            } else {
                                binding.password.setError("Email or password are wrong");
                                binding.password.requestFocus();
                                return;
                            }
                        }
                    });
                }

                if(TextUtils.isEmpty(email)) {
                    binding.email.setError("Email is required");
                    binding.email.requestFocus();
                    return;
                }

                if(TextUtils.isEmpty(password)) {
                    binding.password.setError("Password is required");
                    binding.password.requestFocus();
                    return;
                }
            }
        });
    }
}