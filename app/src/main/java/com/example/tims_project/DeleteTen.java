package com.example.tims_project;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tims_project.adapter.AdapterRemove;
import com.example.tims_project.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DeleteTen extends AppCompatActivity {

    RecyclerView recyclerView;
    AdapterRemove adapterUsers;
    List<User> userList;
    FirebaseAuth firebaseAuth;
    FirebaseAuth mUser;
    public DeleteTen() {

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_ten);

        recyclerView=findViewById(R.id.Ruser_list_recycle_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        firebaseAuth =FirebaseAuth.getInstance();
        recyclerView.setHasFixedSize(true);
        mUser = FirebaseAuth.getInstance();

        userList= new ArrayList<>();
        getAllUsers();
    }

    private void getAllUsers() {
        final FirebaseUser fuser=FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Friend").child(Objects.requireNonNull(mUser.getUid()));
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // userList.clear();
                for(DataSnapshot ds:snapshot.getChildren()){
                    User modelUser=ds.getValue(User.class);
                    userList.add(modelUser);

                    adapterUsers=new AdapterRemove(getApplication(),userList);
                    recyclerView.setAdapter(adapterUsers);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}