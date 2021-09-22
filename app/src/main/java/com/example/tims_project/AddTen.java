package com.example.tims_project;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tims_project.adapter.PendingRequestAdapter;
import com.example.tims_project.model.RequestModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AddTen extends AppCompatActivity {

    RecyclerView recyclerView;
    PendingRequestAdapter adapterUsers;
    List<RequestModel> userList;
    FirebaseAuth firebaseAuth;
    FirebaseAuth mUser;

    public AddTen() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ten);


        recyclerView=findViewById(R.id.user_list_recycle_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        firebaseAuth =FirebaseAuth.getInstance();
        recyclerView.setHasFixedSize(true);
        mUser = FirebaseAuth.getInstance();

        userList= new ArrayList<>();
        getAllUsers();
    }

    private void getAllUsers() {
        final FirebaseUser fuser=FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Request");
        ref.child(Objects.requireNonNull(mUser.getUid())).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //userList.clear();
                for(DataSnapshot ds:snapshot.getChildren()){
                    RequestModel modelUser=ds.getValue(RequestModel.class);
//if(user.getId() != null && user.getId().equals(firebaseUser.getUid())
                    //if(modelUser.getUid()!=null && modelUser.getUid().equals(fuser.getUid())){
                    if(!Objects.requireNonNull(modelUser).getUid().equals(Objects.requireNonNull(fuser).getUid())) {
                        userList.add(modelUser);
                    }

                    adapterUsers=new PendingRequestAdapter(getApplication(),userList);
                    recyclerView.setAdapter(adapterUsers);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}