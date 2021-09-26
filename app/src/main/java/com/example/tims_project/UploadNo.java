package com.example.tims_project;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tims_project.ui.RemoveNoticeActivity;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Objects;

public class UploadNo extends AppCompatActivity {


    EditText editPDFName;
    Button btn_upload;

    FirebaseAuth mUser;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_no);

        editPDFName = (EditText) findViewById(R.id.txt_pdfName);
        btn_upload = (Button) findViewById(R.id.btn_upload);
        //removeButton = (Button) findViewById(R.id.btn_remove);
        mUser = FirebaseAuth.getInstance();

        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference("Uploads");

        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPDFFile();
            }
        });

//        removeButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(UploadNo.this, RemoveNoticeActivity.class);
//                startActivity(intent);
//            }
//        });





    }

    private void selectPDFFile() {

        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select PDF File"),1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == RESULT_OK && data !=null && data.getData()!=null)
        {

         uploadPDFFile(data.getData());

        }
    }

    private void uploadPDFFile(Uri data) {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading......");
        progressDialog.show();

        StorageReference reference = storageReference.child("uploads/"+System.currentTimeMillis()+".pdf");
        reference.putFile(data)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
                        while(!uri.isComplete());
                        Uri url = uri.getResult();

                        uploadedpdf uploadedpdf = new uploadedpdf(editPDFName.getText().toString(),url.toString());
                        databaseReference.child(mUser.getUid()).push().setValue(uploadedpdf);
                        Toast.makeText(UploadNo.this, "File Uploaded", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {


           double progress = (100.0*snapshot.getBytesTransferred())/snapshot.getTotalByteCount();
           progressDialog.setMessage("Uploaded: "+(int)progress+"%");


            }
        });
    }
}