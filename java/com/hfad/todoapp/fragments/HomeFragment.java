package com.hfad.todoapp.fragments;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hfad.todoapp.R;
import com.hfad.todoapp.Task;
import com.hfad.todoapp.TaskAdapter;
import com.hfad.todoapp.databinding.FragmentHomeBinding;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private ArrayList<Task> taskList;
    private Task task;
    private AddTodoFragment mTodoFragment;
    private TaskAdapter adapter;
    private NavController mNavController;

    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private FirebaseAuth mAuth;
    private static final int PICK_IMAGE_REQUEST = 1;

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
        signOutAcc();
        choosePicture();
        showTodoTask();
    }

    private void getDatabase() {
        database = FirebaseDatabase.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String userId = auth.getCurrentUser().getUid();
        myRef = database.getReference("Tasks");
        taskList = new ArrayList<>();

        binding.todoList.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new TaskAdapter(taskList);
        binding.todoList.setAdapter(adapter);

        myRef.child(userId).addChildEventListener(new ChildEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.exists()) {
                    task = snapshot.getValue(Task.class);
                    if (task != null) {
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

    private void showTodoTask() {
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTodoFragment = new AddTodoFragment();
                if (mTodoFragment != null) {
                    getChildFragmentManager().beginTransaction().remove(mTodoFragment).commit();
                }
                mTodoFragment.show(
                        getChildFragmentManager(),
                        mTodoFragment.TAG
                );
            }
        });
    }

    private void signOutAcc() {
        binding.logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNavController = Navigation.findNavController(v);
                FirebaseAuth.getInstance().signOut();
                mNavController.navigate(R.id.action_homeFragment_to_signInFragment);
            }
        });
    }

    private void choosePicture() {
        binding.userImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        mAuth = FirebaseAuth.getInstance();
        String userId = mAuth.getCurrentUser().getUid();

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Images").child(userId);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imgUri = data.getData();

            StorageReference storageRef = FirebaseStorage.getInstance().getReference();
            StorageReference imageRef = storageRef.child("images/" + UUID.randomUUID().toString());
            UploadTask uploadTask = imageRef.putFile(imgUri);

            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String downloadUri = uri.toString();
                            Picasso.get()
                                    .load(uri)
                                    .resize(200, 200)
                                    .into(binding.userImg);

                            myRef.child("url").setValue(downloadUri);
                        }
                    });
                }
            });

        }
    }
}