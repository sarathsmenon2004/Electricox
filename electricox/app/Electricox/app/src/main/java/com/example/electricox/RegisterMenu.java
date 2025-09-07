package com.example.electricox;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class RegisterMenu extends AppCompatActivity {

    ImageView serviceProvider,user;
    TextView loginpage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_home);

        serviceProvider=findViewById(R.id.charginStationRegistrationForm);
        user=findViewById(R.id.userRegistrationForm);


        serviceProvider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getApplicationContext(), CharginStationRegistration.class);
               startActivity(intent);
            }
        });

        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Userregistration.class);
                startActivity(intent);
            }
        });
    }
}