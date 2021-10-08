package com.example.tims_project;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
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
    DownloadManager manager;

    private AdapterPdf.RecyclerViewPdfClickClickListener listener;
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

        setOnClickListener();

        viewAllFiles();



    }

    private void setOnClickListener() {
        listener = new AdapterPdf.RecyclerViewPdfClickClickListener() {
            @Override
            public void onClick(View v, int position) {

                String pdflink=uploadPDF.get(position).getUrl();

                Log.d("downloadddd  link",pdflink);


                DownloadManager.Request request=new DownloadManager.Request(Uri.parse(pdflink));

                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB){
                    request.allowScanningByMediaScanner();
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                }
                DownloadManager downloadManager=(DownloadManager)getSystemService(Context.DOWNLOAD_SERVICE);
                request.setMimeType("application/pdf");
                request.allowScanningByMediaScanner();
                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
                downloadManager.enqueue(request);
                Toast.makeText(getApplicationContext(),"download",Toast.LENGTH_SHORT).show();

            }
        };
    }
    private void viewAllFiles(){



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
                                adapterUsers = new AdapterPdf(getApplication(), uploadPDF,listener);
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

}