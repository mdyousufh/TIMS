package com.example.tims_project.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.tims_project.DeleteNo;
import com.example.tims_project.R;
import com.example.tims_project.adapter.AdapterUsers;
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

public class ChatwTenant extends AppCompatActivity {

    RecyclerView recyclerView;
    AdapterUsers adapterUsers;
    List<User> userList;
    FirebaseAuth firebaseAuth;
    FirebaseAuth mUser;
    private AdapterUsers.RecyclerViewClickClickListener listener;
    public ChatwTenant() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatw_tenant);

        recyclerView=findViewById(R.id.user_list_recycle_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        firebaseAuth =FirebaseAuth.getInstance();
        recyclerView.setHasFixedSize(true);
        mUser = FirebaseAuth.getInstance();

        userList=new ArrayList<User>();
        setOnClickListener();
        getAllUsers();
    }

    private void setOnClickListener() {
        listener = new AdapterUsers.RecyclerViewClickClickListener() {
            @Override
            public void onClick(View v, int position) {
                Intent intent=new Intent(getApplicationContext(), ChatActivity.class);
                intent.putExtra("hisUid",userList.get(position).getUid());
                startActivity(intent);
                Log.d("adapter  users","success");

            }
        };
    }
    private void getAllUsers() {
        final FirebaseUser fuser=FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Friend").child(mUser.getUid());
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //userList.clear();
                for(DataSnapshot ds:snapshot.getChildren()){
                    User modelUser=ds.getValue(User.class);
                    userList.add(modelUser);

                    adapterUsers=new AdapterUsers(getApplication(),userList,listener);
                    recyclerView.setAdapter(adapterUsers);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}