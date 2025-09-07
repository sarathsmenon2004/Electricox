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

public class Userregistration extends AppCompatActivity {

    EditText userNameEditText, userAddressEditText, userEmailEditText,
            userPhoneEditText, userPasswordEditText;

    String USERNAME, USERADDRESS, USEREMAIL, USERPHONE, USERPASSWORD;

    MaterialButton signUpButton;
    TextView login_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userregistration);

        userNameEditText = findViewById(R.id.user_name);
        userAddressEditText = findViewById(R.id.user_address);
        userEmailEditText = findViewById(R.id.user_email);
        userPhoneEditText = findViewById(R.id.user_phone);
        userPasswordEditText = findViewById(R.id.user_password);
        login_back = findViewById(R.id.login_back);

        signUpButton = findViewById(R.id.user_signUp);

        login_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateUser();
            }
        });
    }

    private void validateUser() {
        USERNAME = userNameEditText.getText().toString().trim();
        USERADDRESS = userAddressEditText.getText().toString().trim();
        USEREMAIL = userEmailEditText.getText().toString().trim();
        USERPHONE = userPhoneEditText.getText().toString().trim();
        USERPASSWORD = userPasswordEditText.getText().toString().trim();

        // Regular expression for email validation
        String emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
        // Allowed email domains
        String[] allowedDomains = {"gmail.com", "yahoo.com", "outlook.com", "hotmail.com"};

        if (USERNAME.isEmpty()) {
            showToast("Enter a name");
            userNameEditText.requestFocus();
        } else if (USERADDRESS.isEmpty()) {
            showToast("Enter an address");
            userAddressEditText.requestFocus();
        } else if (USEREMAIL.isEmpty()) {
            showToast("Enter an email");
            userEmailEditText.requestFocus();
        } else if (!USEREMAIL.matches(emailPattern)) {
            showToast("Enter a valid email");
            userEmailEditText.requestFocus();
        } else {
            boolean domainValid = false;
            for (String domain : allowedDomains) {
                if (USEREMAIL.endsWith("@" + domain)) {
                    domainValid = true;
                    break;
                }
            }
            if (!domainValid) {
                showToast("Enter an email with a valid domain (e.g., gmail.com, yahoo.com)");
                userEmailEditText.requestFocus();
            } else if (USERPHONE.isEmpty()) {
                showToast("Enter a phone number");
                userPhoneEditText.requestFocus();
            } else if (USERPHONE.length() < 10) {
                showToast("Enter a valid phone number");
                userPhoneEditText.requestFocus();
            } else if (USERPASSWORD.isEmpty()) {
                showToast("Enter a password");
                userPasswordEditText.requestFocus();
            } else if (!isValidPassword(USERPASSWORD)) {
                showToast("Password must be at least 8 characters and include uppercase, lowercase, number, and special character.");
                userPasswordEditText.requestFocus();
            } else {
                UserRegistrationVolley();
            }
        }
    }

    private boolean isValidPassword(String password) {
        // Password must have at least 8 characters, 1 uppercase, 1 lowercase, 1 number, and 1 special character
        String passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$";
        return password.matches(passwordPattern);
    }

    private void UserRegistrationVolley() {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        StringRequest request = new StringRequest(Request.Method.POST, Utility.SERVERUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.trim().equals("Already Exist")) {
                    Toast.makeText(getApplicationContext(), "Already Exists", Toast.LENGTH_SHORT).show();
                } else if (!response.trim().equals("failed")) {
                    Toast.makeText(getApplicationContext(), "Success ..!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error: " + error, Toast.LENGTH_LONG).show();
                Log.i("Volley Error", "" + error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("key", "userRegister");
                map.put("name", USERNAME);
                map.put("address", USERADDRESS);
                map.put("phone", USERPHONE);
                map.put("email", USEREMAIL);
                map.put("pass", USERPASSWORD);
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
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
    }
}
