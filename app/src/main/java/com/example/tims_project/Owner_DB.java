package com.example.tims_project;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class Owner_DB extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_db);
        Button button = findViewById ( R.id.button );

        button.setOnClickListener (view -> {
            FirebaseAuth.getInstance ().signOut ();
            startActivity ( new Intent ( getApplicationContext (),Login.class ) );
            finish ();
        });
    }
}