package com.example.electricox;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.electricox.COMMON.Utility;
import com.google.android.material.button.MaterialButton;

import java.util.HashMap;
import java.util.Map;

public class CharginStationRegistration extends AppCompatActivity {



    EditText csNameEditText, csAddressEditText, csEmailEditText,
            csPhoneEditText, csPasswordEditText;


    String CSNAME,CSADDRESS,CSEMAIL,CSPHONE,CSPASSWORD;

    MaterialButton CSsignUpButton;

    TextView CSlogin_back;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chargin_station_registration);


        csNameEditText = findViewById(R.id.cs_name);
        csAddressEditText = findViewById(R.id.cs_address);
        csEmailEditText = findViewById(R.id.cs_email);
        csPhoneEditText = findViewById(R.id.cs_phone);
        csPasswordEditText = findViewById(R.id.cs_password);
        CSlogin_back=findViewById(R.id.login_back);

        CSsignUpButton = findViewById(R.id.cs_signUp);


        CSlogin_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });

        CSsignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateCs();
            }
        });

    }

    private void validateCs() {
        CSNAME = csNameEditText.getText().toString().trim();
        CSADDRESS = csAddressEditText.getText().toString().trim();
        CSEMAIL = csEmailEditText.getText().toString().trim();
        CSPHONE = csPhoneEditText.getText().toString().trim();
        CSPASSWORD = csPasswordEditText.getText().toString().trim();

        // Regular expression for email validation
        String emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
        // Allowed email domains
        String[] allowedDomains = {"gmail.com", "yahoo.com", "outlook.com", "hotmail.com"};

        if (CSNAME.isEmpty()) {
            showToast("Enter a name");
            csNameEditText.requestFocus();
        } else if (CSADDRESS.isEmpty()) {
            showToast("Enter an address");
            csAddressEditText.requestFocus();
        } else if (CSEMAIL.isEmpty()) {
            showToast("Enter an email");
            csEmailEditText.requestFocus();
        } else if (!CSEMAIL.matches(emailPattern)) {
            showToast("Enter a valid email");
            csEmailEditText.requestFocus();
        } else {
            boolean domainValid = false;
            for (String domain : allowedDomains) {
                if (CSEMAIL.endsWith("@" + domain)) {
                    domainValid = true;
                    break;
                }
            }
            if (!domainValid) {
                showToast("Enter an email with a valid domain (e.g., gmail.com, yahoo.com)");
                csEmailEditText.requestFocus();
            } else if (CSPHONE.isEmpty()) {
                showToast("Enter a phone number");
                csPhoneEditText.requestFocus();
            } else if (CSPHONE.length() < 10) {
                showToast("Enter a valid phone number");
                csPhoneEditText.requestFocus();
            } else if (CSPASSWORD.isEmpty()) {
                showToast("Enter a password");
                csPasswordEditText.requestFocus();
            } else if (!isValidPassword(CSPASSWORD)) {
                showToast("Password must be at least 8 characters and include uppercase, lowercase, number, and special character.");
                csPasswordEditText.requestFocus();
            } else {
                CSRegistrationVolley();
            }
        }
    }


    private boolean isValidPassword(String password) {
        // Password must have at least 8 characters, 1 uppercase, 1 lowercase, 1 number, and 1 special character
        String passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$";
        return password.matches(passwordPattern);
    }





    private void CSRegistrationVolley()
    {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        StringRequest request = new StringRequest(Request.Method.POST, Utility.SERVERUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.trim().equals("Already Exist")) {

                    Toast.makeText(getApplicationContext(), " Already Exists", Toast.LENGTH_SHORT).show();

                } else if (!response.trim().equals("failed")) {

                    Toast.makeText(getApplicationContext(), "Success ..!", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);

                } else {
                    Toast.makeText(getApplicationContext(), " Failed", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(), "my error :" + error, Toast.LENGTH_LONG).show();
                Log.i("My error", "" + error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("key", "CSRegister");
                map.put("name", CSNAME);
                map.put("address", CSADDRESS);
                map.put("phone",CSPHONE);
                map.put("email", CSEMAIL);
                map.put("pass", CSPASSWORD);


                return map;
            }
        };
        queue.add(request);
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),LoginActivity.class));
    }
}