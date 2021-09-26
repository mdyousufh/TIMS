package com.example.tims_project;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tims_project.adapter.AdapterPdf;
import com.example.tims_project.adapter.AdapterUsers;
import com.example.tims_project.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NoticeBoard extends AppCompatActivity {


    RecyclerView recyclerView;
    AdapterPdf adapterUsers;
    List<uploadedpdf> uploadPDF;
    FirebaseAuth mAuth;
    public NoticeBoard() {

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_board);

        recyclerView=findViewById(R.id.tenant_notice_board_listView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setHasFixedSize(true);

        mAuth = FirebaseAuth.getInstance();
       // myPDFListView = findViewById(R.id.tenant_notice_board_listView);
        uploadPDF = new ArrayList<>();
        Log.d("nnnn"," nn  tyyyy");

        viewAllFiles();

//        myPDFListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                //getting the upload
//                uploadedpdf uploadedpdf = uploadPDF.get(i);
//
//                //Opening the upload file in browser using the upload url
//                Intent intent = new Intent(Intent.ACTION_VIEW);
//                intent.setData(Uri.parse(uploadedpdf.getUrl()));
//                startActivity(intent);
//            }
//        });






    }
    private void viewAllFiles(){
        Log.d("frieyttnd","tyyyy");


        Query friends = FirebaseDatabase.getInstance().getReference().child("Friend").child(mAuth.getUid());

        Log.d("friend",friends.toString());
        friends.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    String friendID = ds.getRef().getKey().toString();
                    Log.d("friendID",friendID);

                    Query posts = FirebaseDatabase.getInstance().getReference("Uploads")
                            .child(friendID);

                    posts.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {

                            uploadPDF.clear();
                            for (DataSnapshot dss : dataSnapshot1.getChildren()) {
                                Log.d("data snap", dss.getValue().toString());
                                Log.d("data snap", dss.getKey());


                                uploadedpdf modelUser = dss.getValue(uploadedpdf.class);

                                uploadPDF.add(modelUser);
                                adapterUsers = new AdapterPdf(getApplication(), uploadPDF);
                                adapterUsers.notifyDataSetChanged();
                                recyclerView.setAdapter(adapterUsers);
                            }



                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            String errormsg = databaseError.toString();
                            Log.d(" error ",errormsg);
                            Toast.makeText(getApplicationContext(), errormsg, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

//    private void viewAllFiles() {
//
//        databaseReference = FirebaseDatabase.getInstance().getReference("Uploads");
//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                for(DataSnapshot postSnapShot: snapshot.getChildren()){
//
//                    uploadedpdf uploadedpdf = postSnapShot.getValue(com.example.tims_project.uploadedpdf.class);
//                    uploadPDF.add(uploadedpdf);
//
//                }
//
//                String[] uploads = new String[uploadPDF.size()];
//                for(int i =0;i<uploads.length;i++){
//
//                    uploads[i] =uploadPDF.get(i).getName();
//
//
//                }
//
//                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1,uploads){
//
//                    @Override
//                    public View getView(int position, View convertView, ViewGroup parent) {
//
//                        View view = super.getView(position, convertView, parent);
//                        TextView mytext = view.findViewById(android.R.id.text1);
//                        mytext.setTextColor(Color.BLACK);
//
//
//
//                        return view;
//                    }
//                };
//                myPDFListView.setAdapter(adapter);
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//
//    }


}