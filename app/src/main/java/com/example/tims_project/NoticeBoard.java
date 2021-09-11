package com.example.tims_project;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class NoticeBoard extends AppCompatActivity {


    ListView myPDFListView;
    DatabaseReference databaseReference;
    List<uploadedpdf> uploadPDF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_board);

        myPDFListView = findViewById(R.id.tenant_notice_board_listView);
        uploadPDF = new ArrayList<>();

        viewAllFiles();

        myPDFListView.setOnItemClickListener((parent, view, position, id) -> {

            uploadedpdf uploadedpdf = uploadPDF.get(position);

            Intent intent = new Intent();
            intent.setType(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(uploadedpdf.getUrl()));
            startActivity(intent);

        });






    }

    private void viewAllFiles() {

        databaseReference = FirebaseDatabase.getInstance().getReference("Uploads");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot postSnapShot: snapshot.getChildren()){

                    uploadedpdf uploadedpdf = postSnapShot.getValue(com.example.tims_project.uploadedpdf.class);
                    uploadPDF.add(uploadedpdf);

                }

                String[] uploads = new String[uploadPDF.size()];
                for(int i =0;i<uploads.length;i++){

                    uploads[i] =uploadPDF.get(i).getName();


                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1,uploads){

                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {

                        View view = super.getView(position, convertView, parent);
                        TextView mytext = view.findViewById(android.R.id.text1);
                        mytext.setTextColor(Color.BLACK);



                        return view;
                    }
                };
                myPDFListView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


}