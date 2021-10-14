package com.example.tims_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

public class PayRent extends AppCompatActivity {

    LinearLayout bkash,nagad,rocket;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_rent);

        bkash = findViewById(R.id.Bikash);
        nagad = findViewById(R.id.Nagad);
        rocket = findViewById(R.id.Rocket);


        bkash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getPackageManager().getLaunchIntentForPackage("com.bKash.customerapp");

                if(intent!=null){
                    startActivity(intent);

                }
                else {
                    Toast.makeText ( PayRent.this , "There is no package available in android" , Toast.LENGTH_SHORT ).show ( );

                }
            }
        });

       nagad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = getPackageManager().getLaunchIntentForPackage("com.konasl.nagad");

                if(intent!=null){
                    startActivity(intent);

                }
                else {
                    Toast.makeText ( PayRent.this , "There is no package available in android" , Toast.LENGTH_SHORT ).show ( );

                }



            }
        });

        rocket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = getPackageManager().getLaunchIntentForPackage("com.dbbl.mbs.apps.main");

                if(intent!=null){
                    startActivity(intent);

                }
                else {
                    Toast.makeText ( PayRent.this , "There is no package available in android" , Toast.LENGTH_SHORT ).show ( );

                }

            }
        });



    }


}