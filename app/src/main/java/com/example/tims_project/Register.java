package com.example.tims_project;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    EditText fullName,email,password,phone;
    Button registerBtn,goToLogin;
    boolean valid = true;

    FirebaseAuth fAuth;

    FirebaseFirestore fStore;

    CheckBox isOwnerBox,isTenantBox;

    @SuppressLint("CutPasteId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        fullName = findViewById(R.id.registerName);
        email = findViewById(R.id.registerEmail);
        password = findViewById(R.id.registerPassword);
        phone = findViewById(R.id.registerPhone);
        registerBtn = findViewById(R.id.registerBtn);
        goToLogin = findViewById(R.id.gotoLogin);
        isOwnerBox = findViewById ( R.id.isOwner );
        isTenantBox = findViewById ( R.id.isTenant );

        //check box logics

        isOwnerBox.setOnCheckedChangeListener ((compoundButton, b) -> {
            if(compoundButton.isChecked ())
            {
                isTenantBox.setChecked ( false );

            }
        });
        isTenantBox.setOnCheckedChangeListener ((compoundButton, b) -> {
            if(compoundButton.isChecked ())
            {
                isOwnerBox.setChecked ( false );

            }
        });


        registerBtn.setOnClickListener(view -> {
            checkField(fullName);
            checkField(email);
            checkField(password);
            checkField(phone);
            isOwnerBox = findViewById ( R.id.isOwner );
            isTenantBox = findViewById ( R.id.isTenant );

            //checkbox validation

            if(!(isOwnerBox.isChecked () || isTenantBox.isChecked ())){

                Toast.makeText ( Register.this , "Select Account Type" , Toast.LENGTH_SHORT ).show ( );
                return;

            }

            if(valid)
            {
                fAuth.createUserWithEmailAndPassword(email.getText().toString(),password.getText().toString()).addOnSuccessListener(authResult -> {
                    FirebaseUser user = fAuth.getCurrentUser();
                    Toast.makeText(Register.this, "Successfully Account Created", Toast.LENGTH_SHORT).show();
                    assert user != null;
                    DocumentReference df =fStore.collection("Users").document(user.getUid());

                    Map<String,Object> userInfo = new HashMap<>();
                    userInfo.put("FullName",fullName.getText().toString());
                    userInfo.put("UserEmail",email.getText().toString());
                    userInfo.put("PhoneNumber",phone.getText().toString());

                    //specify if user is admin

                    if(isOwnerBox.isChecked ()) {

                        userInfo.put ( "isOwner", "1");
                    }

                    if(isTenantBox.isChecked ()){

                        userInfo.put ( "isTenant","1" );
                    }


                    df.set(userInfo);
                    startActivity(new Intent(getApplicationContext(),Login.class));
                    finish();
                }) .addOnFailureListener(e -> Toast.makeText(Register.this, "Failed to Created Account", Toast.LENGTH_SHORT).show());



            }



        });
        goToLogin.setOnClickListener (view -> startActivity ( new Intent ( getApplicationContext ( ) , Login.class ) ));
    }

    public void checkField(EditText textField){
        if(textField.getText().toString().isEmpty()){
            textField.setError("Error");
            valid = false;
        }else {
            valid = true;
        }

    }
}