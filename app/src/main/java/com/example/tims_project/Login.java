package com.example.tims_project;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class Login extends AppCompatActivity {

    EditText email,password;
    TextView forgotTextLink;
    Button loginBtn,gotoRegister;
    boolean valid = true;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        fAuth = FirebaseAuth.getInstance ();
        fStore = FirebaseFirestore.getInstance ();

        email = findViewById(R.id.loginEmail);
        password = findViewById(R.id.loginPassword);
        loginBtn = findViewById(R.id.loginBtn);
        gotoRegister = findViewById(R.id.gotoRegister);
        forgotTextLink = findViewById(R.id.forgotPassword);

        loginBtn.setOnClickListener (v -> {
            checkField ( email );
            checkField ( password );

            if(valid){

                fAuth.signInWithEmailAndPassword ( email.getText ().toString (),password.getText ().toString () ).addOnSuccessListener (authResult -> {
                    Toast.makeText ( Login.this , "Logged in Successfully" , Toast.LENGTH_SHORT ).show ( );
                    checkUserAccessLevel ( Objects.requireNonNull ( authResult.getUser ( ) ).getUid () );
                }).addOnFailureListener (e -> Toast.makeText ( Login.this , "Login Failed" , Toast.LENGTH_SHORT ).show ( ));



            }
        });




        gotoRegister.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(),Register.class)));

        forgotTextLink.setOnClickListener(v -> {

            EditText resetMail = new EditText(v.getContext());
            AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
            passwordResetDialog.setTitle("Reset Password ?");
            passwordResetDialog.setMessage("Enter Your Email To Received Reset Link");
            passwordResetDialog.setView(resetMail);

            passwordResetDialog.setPositiveButton("Yes", (dialog, which) -> {

                String mail = resetMail.getText().toString();
                fAuth.sendPasswordResetEmail(mail).addOnSuccessListener(unused -> Toast.makeText(Login.this, "Reset Link Sent to Your Email", Toast.LENGTH_SHORT).show()).addOnFailureListener(e -> Toast.makeText(Login.this, "Error ! Reset Link is Not Sent" +e.getMessage(), Toast.LENGTH_SHORT).show());
            });

            passwordResetDialog.setNegativeButton("No", (dialog, which) -> {

            });

            passwordResetDialog.create().show();



        });


    }

    private void checkUserAccessLevel (String uid) {

        DocumentReference df = fStore.collection ( "Users").document(uid);
        df.get ().addOnSuccessListener (documentSnapshot -> {

            Log.d ( "TAG","onSuccess: "+documentSnapshot.getData ());

            if(documentSnapshot.getString ( "isTenant" )!=null){

                startActivity ( new Intent ( getApplicationContext (),Tenant_DB.class ) );
                finish ();
            }
            if(documentSnapshot.getString ( "isOwner" )!=null) {

                startActivity ( new Intent ( getApplicationContext ( ) , Owner_DB.class ) );
                finish ( );
            }
        });
    }

    public void checkField(EditText textField){
        if(textField.getText().toString().isEmpty()){
            textField.setError("Error");
            valid = false;
        }else {
            valid = true;
        }

    }

    @Override
    protected void onStart () {
        super.onStart ( );

        if(FirebaseAuth.getInstance ().getCurrentUser ()!=null){

            DocumentReference df = FirebaseFirestore.getInstance ().collection ( "Users").document (FirebaseAuth.getInstance ().getCurrentUser ().getUid ());
            df.get ().addOnSuccessListener (documentSnapshot -> {
               if(documentSnapshot.getString ( "isOwner") !=null){
                   startActivity ( new Intent ( getApplicationContext ( ) , Owner_DB.class ) );
                   finish ( );

               }
                if(documentSnapshot.getString ( "isTenant") !=null) {
                    startActivity ( new Intent ( getApplicationContext ( ) , Tenant_DB.class ) );
                    finish ( );
                }
            }).addOnFailureListener (e -> {
               FirebaseAuth.getInstance ().signOut ();
               startActivity ( new Intent ( getApplicationContext (),Login.class ));
               finish ();
            });
        }
    }
}
