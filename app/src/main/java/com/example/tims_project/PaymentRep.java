package com.example.tims_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

public class PaymentRep extends AppCompatActivity {

    LinearLayout bkash,nagad,rocket;


    @Override                           //com.bKash.customerapp
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_rep);
        bkash = findViewById(R.id.Bikash);
        nagad = findViewById(R.id.Nagad);
        rocket = findViewById(R.id.Rocket);

        bkash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.bKash.customerapp");
                if (launchIntent != null) {
                    startActivity(launchIntent);
                } else {
                    Toast.makeText(PaymentRep.this, "There is no package available in android", Toast.LENGTH_LONG).show();
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
                    Toast.makeText ( PaymentRep.this , "There is no package available in android" , Toast.LENGTH_SHORT ).show ( );

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
                    Toast.makeText ( PaymentRep.this , "There is no package available in android" , Toast.LENGTH_SHORT ).show ( );

                }

            }
        });

    }
}