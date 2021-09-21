package com.example.tims_project;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.webkit.MimeTypeMap;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Objects;

public class FirebaseInstanceDatabase {
    private final FirebaseDatabase instance = FirebaseDatabase.getInstance();
    private final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private final StorageReference storageReference = FirebaseStorage.getInstance().getReference("uploads");







    public MutableLiveData<DatabaseReference> getTokenRef() {
        final MutableLiveData<DatabaseReference> getTokenReference = new MutableLiveData<>();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens");
        getTokenReference.setValue(reference);


        return getTokenReference;
    }










}
