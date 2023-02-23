package com.hfad.todoapp.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hfad.todoapp.R;
import com.hfad.todoapp.databinding.FragmentSignUpBinding;

public class SignUpFragment extends Fragment {

    private FragmentSignUpBinding binding;
    private NavController mNavController;
    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSignUpBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mNavController = Navigation.findNavController(view);
        mAuth = FirebaseAuth.getInstance();
        buttons();
    }

    private void buttons() {

        binding.navLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mNavController.navigate(R.id.action_signUpFragment_to_signInFragment);
            }
        });

        binding.register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = binding.emailReg.getText().toString();
                String pass = binding.passwordReg.getText().toString();
                String confirmPass = binding.confirmPass.getText().toString();

                if (!email.isEmpty() && !pass.isEmpty() && !confirmPass.isEmpty()) {
                    if(pass.equals(confirmPass)) {
                        mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(getActivity(), task -> {
                            if(task.isSuccessful()) {
                                Toast.makeText(getActivity(), "Registration successful", Toast.LENGTH_SHORT).show();
                                mNavController.navigate(R.id.action_signUpFragment_to_homeFragment);
                                FirebaseUser user = mAuth.getCurrentUser();
                            } else {
                                Log.d("ERROR", "Reg fail");
                                Toast.makeText(getActivity(), "One of the fields is empty, " +
                                        "or password and confirm password are not same", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }
        });
    }
}