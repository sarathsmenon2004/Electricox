package com.example.electricox;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.example.electricox.ADMIN.AdminHome;
import com.example.electricox.CHARGINGSTATION.Charging_Station;
import com.example.electricox.COMMON.Utility;
import com.example.electricox.USER.User_Nav;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    TextView signUp;

    EditText LoginUserName,LoginPassWord;
    Button login;

    AlertDialog.Builder builder;
    String EMAIL,PASSWORD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        signUp=findViewById(R.id.signUp);

        login=findViewById(R.id.loginButton);
        LoginUserName=findViewById(R.id.login_username);
        LoginPassWord=findViewById(R.id.login_password);


        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {

                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.CAMERA,

        };
        if (!hasPermission(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);

        }

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),RegisterMenu.class));
            }
        });


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                EMAIL = LoginUserName.getText().toString();
                PASSWORD = LoginPassWord.getText().toString();
                if (EMAIL.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Enter Email", Toast.LENGTH_SHORT).show();
                } else if (PASSWORD.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Enter Password", Toast.LENGTH_SHORT).show();
                } else {
                    login_volly();
                }

            }

        });




    }

    private void login_volly()
    {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest request = new StringRequest(Request.Method.POST, Utility.SERVERUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.trim().equals("failed")) {

                    String DATA = response;
                    String resArr[] = DATA.trim().split("#");

                    SharedPreferences.Editor editor = getSharedPreferences("sharedData", MODE_PRIVATE).edit();
                    editor.putString("reg_id", "" + resArr[0]);
                    editor.putString("type", "" + resArr[1]);
                    System.out.println(resArr[0]);
                    editor.commit();

                    if (resArr[1].equals("ADMIN")) {
                        startActivity(new Intent(LoginActivity.this, AdminHome.class));
                        Toast.makeText(LoginActivity.this, "Login Success", Toast.LENGTH_SHORT).show();

                    }
                    if (resArr[1].equals("CHARGING STATION")) {
                        startActivity(new Intent(LoginActivity.this, Charging_Station.class));
                        Toast.makeText(LoginActivity.this, "Login Success", Toast.LENGTH_SHORT).show();

                    }
//
                    if (resArr[1].equals("USER")) {
                        startActivity(new Intent(LoginActivity.this, User_Nav.class));
                        Toast.makeText(LoginActivity.this, "Login Success", Toast.LENGTH_SHORT).show();

                    }

                } else {
                    Toast.makeText(LoginActivity.this, "Login Failed!!", Toast.LENGTH_SHORT).show();

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
                map.put("key", "login");
                map.put("email", EMAIL);
                map.put("pass", PASSWORD);
                return map;
            }
        };
        queue.add(request);
    }


    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setMessage("Exit!! Are You Sure?")
                .setCancelable(false)
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        finishAffinity();
                    }
                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert = builder.create();

        alert.setTitle("Exitt");
        alert.show();
    }


    public static boolean hasPermission(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null)
        {
            for (String permission : permissions)
            {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }

            }
        }
        return true;
    }
}