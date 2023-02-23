package com.hfad.todoapp.fragments;

import android.annotation.SuppressLint;
import android.graphics.Paint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hfad.todoapp.Task;
import com.hfad.todoapp.TaskAdapter;
import com.hfad.todoapp.databinding.FragmentHomeBinding;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private ArrayList<Task> taskList;
    private Task task;

    private FirebaseDatabase database;
    private DatabaseReference myRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getDatabase();
    }

    private void getDatabase() {

        database = FirebaseDatabase.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String userId = auth.getCurrentUser().getUid();
        myRef = database.getReference("Tasks");
        taskList = new ArrayList<>();

        binding.todoList.setLayoutManager(new LinearLayoutManager(getContext()));
        TaskAdapter adapter = new TaskAdapter(taskList);
        binding.todoList.setAdapter(adapter);

        binding.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addData(userId);
            }
        });

        myRef.child(userId).addChildEventListener(new ChildEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(snapshot.exists()) {
                    task = snapshot.getValue(Task.class);
                    if(task != null) {
                        taskList.add(task);
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void addData(String userId) {
        String key = myRef.child(userId).push().getKey();
        String title = binding.titleEt.getText().toString();
        String description = binding.descriptionEt.getText().toString();

        task = new Task(title, description, key, false);

        if(!title.isEmpty() && !description.isEmpty()) {
            assert key != null;
            myRef.child(userId).child(key).setValue(task);
        }

        binding.titleEt.setText("");
        binding.descriptionEt.setText("");
    }
}