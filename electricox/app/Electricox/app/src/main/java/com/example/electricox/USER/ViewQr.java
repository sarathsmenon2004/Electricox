package com.example.electricox.USER;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.electricox.COMMON.Base64;
import com.example.electricox.COMMON.Utility;
import com.example.electricox.R;
import com.example.electricox.USER.ui.Your_bookings;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ViewQr extends AppCompatActivity {


    ImageView QrImageView;
    TextInputEditText QrID;
    Button Submit;

    String Qr,QrId;
    String Qrcodeid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_qr);

        QrImageView = findViewById(R.id.qrImageView);
        QrID = findViewById(R.id.qrIdEditText);
        Submit = findViewById(R.id.submitButton);

        Intent intent = getIntent();
        Qr = intent.getExtras().getString("qrcode");
        QrId = intent.getExtras().getString("qrId");



        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Validate();
            }
        });


        try {
            byte[] imageAsBytes = Base64.decode(Qr.getBytes());
            QrImageView.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void Validate()
    {
        Qrcodeid=QrID.getText().toString();
        if(Qrcodeid.isEmpty())
        {
            Toast.makeText(this, "Enter Qr ID", Toast.LENGTH_SHORT).show();
        }
        else
        {
            if(Qrcodeid.equals(QrId))
            {
               // Toast.makeText(this, "Sucesss", Toast.LENGTH_SHORT).show();
                updatePass();
            }
            else
            {
                Toast.makeText(this, "invalid Qr id", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void updatePass() {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest request = new StringRequest(Request.Method.POST, Utility.SERVERUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.trim().equals("failed")) {
                    startActivity(new Intent(getApplicationContext(), User_Nav.class));

                } else {

                    Toast.makeText(getApplicationContext(), "NO_DATA", Toast.LENGTH_SHORT).show();


                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "" + error, Toast.LENGTH_SHORT).show();
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() {
                SharedPreferences prefs = getApplicationContext().getSharedPreferences("sharedData", MODE_PRIVATE);
                final String reg_id = prefs.getString("login_id", "No_id");
                final String type = prefs.getString("type", "NO-Type");
                Map<String, String> map = new HashMap<String, String>();
                map.put("key", "updatePass");
                map.put("id", QrId);


                return map;
            }

        };
        queue.add(request);
    }
}