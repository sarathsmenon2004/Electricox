package com.example.electricox.ADMIN;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.electricox.LoginActivity;
import com.example.electricox.R;

public class AdminHome extends AppCompatActivity {

    CardView userCard, evOwnerCard, evStationsCard, feedBackCard;
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        userCard=findViewById(R.id.userCard);
        evOwnerCard=findViewById(R.id.evOwnerCard);
        evStationsCard=findViewById(R.id.evStationsCard);
        feedBackCard=findViewById(R.id.feedBackCard);


        userCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(),ViewUser.class));
            }
        });
        evOwnerCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(),ViewEVOwners.class));
            }
        });
        evStationsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(),ViewEVStation.class));
            }
        });
        feedBackCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(),ViewUserFeedBack.class));
            }
        });

    }

    @Override
    public void onBackPressed() {

        builder = new AlertDialog.Builder(AdminHome.this);
        builder.setMessage("Logout!! Are You Sure?")
                .setCancelable(false)
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    }
                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert = builder.create();

        alert.setTitle("Logout");
        alert.show();
    }
}