package com.example.tims_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class Tenant_DB extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tenant_db);
        Button logout = findViewById ( R.id.logout );

        logout.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick ( View view ) {
                FirebaseAuth.getInstance ().signOut ();
                startActivity ( new Intent ( getApplicationContext (),Login.class ) );
                finish ();
            }
        } );
    }
}