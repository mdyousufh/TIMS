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

public class Tenant_DB extends AppCompatActivity {

    TextView emailnotverify;
    Button verifybtn;
    FirebaseAuth auth;
    public LinearLayout tenatchat1st,viewNoticeB,payrent,sendReq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tenant_db);
        auth = FirebaseAuth.getInstance();
        Button logout = findViewById ( R.id.logout );
        emailnotverify = findViewById(R.id.emailnotverify);
        verifybtn = findViewById(R.id.verifybtn);
        tenatchat1st = findViewById(R.id.tenatchat1st);
        viewNoticeB = findViewById(R.id.viewNoticeB);
        payrent = findViewById(R.id.payrent);
        sendReq = findViewById(R.id.sendReq);

        if(!Objects.requireNonNull(auth.getCurrentUser()).isEmailVerified()) {

            tenatchat1st.setVisibility(View.GONE);
            viewNoticeB.setVisibility(View.GONE);
            payrent.setVisibility(View.GONE);
            sendReq.setVisibility(View.GONE);


            Toast.makeText ( Tenant_DB.this , "Without Email Verify features are not visible" , Toast.LENGTH_SHORT ).show ( );

            ////////////

        }

        else if(Objects.requireNonNull(auth.getCurrentUser()).isEmailVerified()) {


            tenatchat1st.setOnClickListener(v -> {

                Intent intent = new Intent(Tenant_DB.this, ChatWowner.class);
                startActivity(intent);

            });
            sendReq.setOnClickListener(v -> {

                Intent intent = new Intent(Tenant_DB.this,SendReq.class);
                startActivity(intent);

            });

            payrent.setOnClickListener(v -> {

                Intent intent = new Intent(Tenant_DB.this,PayRent.class);
                startActivity(intent);

            });

            viewNoticeB .setOnClickListener(v -> {

                Intent intent = new Intent(Tenant_DB.this,NoticeBoard.class);
                startActivity(intent);

            });



        }



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
            startActivity ( new Intent ( getApplicationContext (), Login.class ) );
            finish ();
        });
    }
}