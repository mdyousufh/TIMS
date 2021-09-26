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
import com.example.tims_project.ui.RemoveNoticeActivity;
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
        uploadN = findViewById(R.id.uploadN);
        chatwT = findViewById(R.id.chatwT);
        noticedel = findViewById(R.id.noticedel);
        payreport = findViewById(R.id.payreport);
        addtent= findViewById(R.id.addtent);
        removTen= findViewById(R.id.removTen);

        if(!Objects.requireNonNull(auth.getCurrentUser()).isEmailVerified()) {


            uploadN.setVisibility(View.GONE);
            removTen.setVisibility(View.GONE);
            addtent.setVisibility(View.GONE);
            payreport.setVisibility(View.GONE);
            noticedel.setVisibility(View.GONE);
            chatwT.setVisibility(View.GONE);

            Toast.makeText ( Owner_DB.this , "Without Email Verify features are not visible" , Toast.LENGTH_SHORT ).show ( );

            ////////////

        }

        else if(Objects.requireNonNull(auth.getCurrentUser()).isEmailVerified())
        {


            uploadN.setOnClickListener(v -> {

                Intent intent = new Intent(Owner_DB.this, UploadNo.class);
                startActivity(intent);

            });

            removTen.setOnClickListener(v -> {

                Intent intent = new Intent(Owner_DB.this, DeleteTen.class);
                startActivity(intent);

            });

            addtent.setOnClickListener(v -> {

                Intent intent = new Intent(Owner_DB.this, AddTen.class);
                startActivity(intent);

            });
            payreport.setOnClickListener(v -> {

                Intent intent = new Intent(Owner_DB.this, PaymentRep.class);
                startActivity(intent);

            });

            chatwT.setOnClickListener(v -> {

                Intent intent = new Intent(Owner_DB.this, ChatWowner.class);
                startActivity(intent);

            });

            noticedel.setOnClickListener(v -> {

<<<<<<< Updated upstream
                Intent intent = new Intent(Owner_DB.this, DeleteNo.class);
=======
                Intent intent = new Intent(Owner_DB.this, RemoveNoticeActivity.class);
>>>>>>> Stashed changes
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
