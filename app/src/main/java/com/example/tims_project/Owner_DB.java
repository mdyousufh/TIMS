package com.example.tims_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tims_project.ui.ChatWowner;
import com.example.tims_project.ui.Login;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class Owner_DB extends AppCompatActivity {

TextView emailnotverify;
Button verifybtn;
FirebaseAuth auth;
public LinearLayout uploadN,chatwT,noticedel,payreport,addtent,removTen;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_db);
        auth = FirebaseAuth.getInstance();
        Button button = findViewById(R.id.Button);
        emailnotverify = findViewById(R.id.emailnotverify);
        verifybtn = findViewById(R.id.verifybtn);
        uploadN = (LinearLayout) findViewById(R.id.uploadN);
        chatwT = (LinearLayout) findViewById(R.id.chatwT);
        noticedel = (LinearLayout) findViewById(R.id.noticedel);
        payreport = (LinearLayout) findViewById(R.id.payreport);
        addtent= (LinearLayout) findViewById(R.id.addtent);
        removTen= (LinearLayout) findViewById(R.id.removTen);


        uploadN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Owner_DB.this,UploadNo.class);
                startActivity(intent);

            }
        });

        removTen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Owner_DB.this,DeleteTen.class);
                startActivity(intent);

            }
        });

        addtent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Owner_DB.this,AddTen.class);
                startActivity(intent);

            }
        });
        payreport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Owner_DB.this,PaymentRep.class);
                startActivity(intent);

            }
        });

        chatwT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Owner_DB.this, ChatWowner.class);
                startActivity(intent);

            }
        });

        noticedel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Owner_DB.this,DeleteNo.class);
                startActivity(intent);

            }
        });


        if(!Objects.requireNonNull(auth.getCurrentUser()).isEmailVerified())
        {
           verifybtn.setVisibility(View.VISIBLE);
           emailnotverify.setVisibility(View.VISIBLE);


        }

        verifybtn.setOnClickListener(v -> {
            // send verification email;
             auth.getCurrentUser().sendEmailVerification().addOnSuccessListener(unused -> {

                 Toast.makeText ( Owner_DB.this , "Verification Email Sent" , Toast.LENGTH_SHORT ).show ( );
                 verifybtn.setVisibility(View.GONE);
                 emailnotverify.setVisibility(View.GONE);


             });

        });


        button.setOnClickListener (view -> {
            FirebaseAuth.getInstance ().signOut ();
            startActivity ( new Intent ( getApplicationContext (), Login.class ) );
            finish ();
        });



    }
}
