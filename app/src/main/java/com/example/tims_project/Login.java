package com.example.tims_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.api.Logging;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

public class Login extends AppCompatActivity {

    EditText email,password;
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

        loginBtn.setOnClickListener ( new View.OnClickListener( ) {
            @Override
            public void onClick ( View v ) {
                checkField ( email );
                checkField ( password );

                if(valid){

                     fAuth.signInWithEmailAndPassword ( email.getText ().toString (),password.getText ().toString () ).addOnSuccessListener ( new OnSuccessListener<AuthResult> ( ) {
                         @Override
                         public void onSuccess ( AuthResult authResult ) {
                             Toast.makeText ( Login.this , "Logged in Successfully" , Toast.LENGTH_SHORT ).show ( );
                             checkUserAccessLevel ( authResult.getUser ().getUid () );
                         }
                     } ).addOnFailureListener ( new OnFailureListener ( ) {
                         @Override
                         public void onFailure ( @NonNull @NotNull Exception e ) {
                             Toast.makeText ( Login.this , "Login Failed" , Toast.LENGTH_SHORT ).show ( );
                         }
                     } );



                }
            }
        } );




        gotoRegister.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

                startActivity(new Intent(getApplicationContext(),Register.class));
           }
        });


    }

    private void checkUserAccessLevel (String uid) {

        DocumentReference df = fStore.collection ( "Users").document(uid);
        df.get ().addOnSuccessListener ( new OnSuccessListener<DocumentSnapshot> ( ) {
            @Override
            public void onSuccess ( DocumentSnapshot documentSnapshot ) {

                Log.d ( "TAG","onSuccess: "+documentSnapshot.getData ());

                if(documentSnapshot.getString ( "isTenant" )!=null){

                    startActivity ( new Intent ( getApplicationContext (),Tenant_DB.class ) );
                    finish ();
                }
                if(documentSnapshot.getString ( "isOwner" )!=null) {

                    startActivity ( new Intent ( getApplicationContext ( ) , Owner_DB.class ) );
                    finish ( );
                }
            }
        } );
    }

    public boolean checkField(EditText textField){
        if(textField.getText().toString().isEmpty()){
            textField.setError("Error");
            valid = false;
        }else {
            valid = true;
        }

        return valid;
    }

    @Override
    protected void onStart () {
        super.onStart ( );

        if(FirebaseAuth.getInstance ().getCurrentUser ()!=null){

            startActivity ( new Intent ( getApplicationContext (),Owner_DB.class ));
            finish ();
        }
    }
}

