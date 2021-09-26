package com.example.tims_project.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.tims_project.R;
import com.example.tims_project.adapter.AdapterPdf;
import com.example.tims_project.adapter.AdapterRemoveNotice;
import com.example.tims_project.uploadedpdf;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RemoveNoticeActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    AdapterRemoveNotice adapterUsers;
    List<uploadedpdf> uploadPDF;
    FirebaseAuth mAuth;
    public RemoveNoticeActivity() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_notice);

        recyclerView=findViewById(R.id.removeNotice);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setHasFixedSize(true);

        mAuth = FirebaseAuth.getInstance();
        // myPDFListView = findViewById(R.id.tenant_notice_board_listView);
        uploadPDF = new ArrayList<>();


        Query posts = FirebaseDatabase.getInstance().getReference()
                .child("Uploads").child(mAuth.getUid());

        posts.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {

                uploadPDF.clear();
                for (DataSnapshot dss : dataSnapshot1.getChildren()) {
                    Log.d("data snap", dss.getValue().toString());
                    Log.d("data snap", dss.getKey());


                    uploadedpdf modelUser = dss.getValue(uploadedpdf.class);

                    uploadPDF.add(modelUser);
                    adapterUsers=new AdapterRemoveNotice(getApplication(),uploadPDF);
                    adapterUsers.notifyDataSetChanged();
                    recyclerView.setAdapter(adapterUsers);
                }



            }
            // adapterUsers=new AdapterRemoveNotice(getApplication(),uploadPDF);
            //                recyclerView.setAdapter(adapterUsers);

            @Override
            public void onCancelled(DatabaseError databaseError) {
                String errormsg = databaseError.toString();
                Log.d(" error ",errormsg);
                Toast.makeText(getApplicationContext(), errormsg, Toast.LENGTH_SHORT).show();
            }
        });
    }
}