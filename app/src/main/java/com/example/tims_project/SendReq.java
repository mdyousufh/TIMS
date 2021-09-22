package com.example.tims_project;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tims_project.adapter.AdaperOwner;
import com.example.tims_project.model.ModelUser;
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

public class SendReq extends AppCompatActivity {

    RecyclerView recyclerView;
    AdaperOwner adapterUsers;
    List<ModelUser> userList;
    FirebaseAuth firebaseAuth;
    SearchView searchView;

    public SendReq() {
        // Required empty public constructor
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_req);

        recyclerView=findViewById(R.id.user_list_recycle_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        firebaseAuth =FirebaseAuth.getInstance();
        recyclerView.setHasFixedSize(true);

        userList= new ArrayList<>();

//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String q) {
//                if(!TextUtils.isEmpty(q.trim())){
//                    searchUsers(q);
//                }else{
//                    getAllUsers();
//                }
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String q) {
//                if(!TextUtils.isEmpty(q.trim())){
//                    searchUsers(q);
//                }else{
//                    getAllUsers();
//                }
//                return false;
//            }
//        });
        getAllUsers();




//        getAllUsers();
    }

    private void getAllUsers() {

        final FirebaseUser fuser=FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Owner");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //userList.clear();
                for(DataSnapshot ds:snapshot.getChildren()){
                    ModelUser modelUser=ds.getValue(ModelUser.class);

                    if(!Objects.requireNonNull(modelUser).getUserEmail().equals(Objects.requireNonNull(fuser).getEmail())) {
                        userList.add(modelUser);
                    }

                    adapterUsers=new AdaperOwner(getApplication(),userList);
                    recyclerView.setAdapter(adapterUsers);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void searchUsers(final String query) {

        final FirebaseUser fuser=FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Owner");
        ref.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                for(DataSnapshot ds:snapshot.getChildren()){
                    ModelUser modelUser=ds.getValue(ModelUser.class);


                    if(!Objects.requireNonNull(modelUser).getUserEmail().equals(Objects.requireNonNull(fuser).getEmail())) {
                        if (modelUser.getFullName().toLowerCase().contains(query.toLowerCase()) ||
                                modelUser.getUserEmail().toLowerCase().contains(query.toLowerCase())) {
                            userList.add(modelUser);
                        }
                    }


                    adapterUsers=new AdaperOwner(getApplication(),userList);
                    adapterUsers.notifyDataSetChanged();
                    recyclerView.setAdapter(adapterUsers);

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();

        menuInflater.inflate(R.menu.menu,menu);
        MenuItem item=menu.findItem(R.id.action_search);
        SearchView searchView=(SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String q) {
                if(!TextUtils.isEmpty(q.trim())){
                    searchUsers(q);
                }else{
                    getAllUsers();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String q) {
                if(!TextUtils.isEmpty(q.trim())){
                    searchUsers(q);
                }else{
                    getAllUsers();
                }
                return false;
            }
        });




        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

    }
}