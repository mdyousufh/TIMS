package com.example.tims_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class Tenant_DB extends AppCompatActivity {

    TextView emailnotverify;
    Button verifybtn;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tenant_db);
        auth = FirebaseAuth.getInstance();
        Button logout = findViewById ( R.id.logout );
        emailnotverify = findViewById(R.id.emailnotverify);
        verifybtn = findViewById(R.id.verifybtn);

        if(!Objects.requireNonNull(auth.getCurrentUser()).isEmailVerified())
        {
            verifybtn.setVisibility(View.VISIBLE);
            emailnotverify.setVisibility(View.VISIBLE);


        }

        verifybtn.setOnClickListener(v -> {
            // send verification email;
            auth.getCurrentUser().sendEmailVerification().addOnSuccessListener(unused -> {

                Toast.makeText ( Tenant_DB.this , "Verification Email Sent" , Toast.LENGTH_SHORT ).show ( );
                verifybtn.setVisibility(View.GONE);
                emailnotverify.setVisibility(View.GONE);


            });

        });

        logout.setOnClickListener (view -> {
            FirebaseAuth.getInstance ().signOut ();
            startActivity ( new Intent ( getApplicationContext (),Login.class ) );
            finish ();
        });
    }
}