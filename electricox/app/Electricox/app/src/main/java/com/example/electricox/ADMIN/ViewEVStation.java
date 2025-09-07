package com.example.electricox.ADMIN;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.electricox.ADAPTERS.EvOwnersAdapter;
import com.example.electricox.ADAPTERS.bunkAdapter;
import com.example.electricox.COMMON.RequestPojo;
import com.example.electricox.COMMON.Utility;
import com.example.electricox.R;
import com.google.gson.Gson;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewEVStation extends AppCompatActivity {

    ListView listView;

    List<RequestPojo> list;

    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_evstation);

        listView=findViewById(R.id.viewEVstationlist);

        builder=new AlertDialog.Builder(this);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                builder.setMessage("")
                        .setCancelable(false)
                        .setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                              String  bunk_id=(list.get(position).getBunk_id());
                                acceptbunk(bunk_id);

                            }
                        })

                        .setNeutralButton(
                                "Block",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id)
                                    {
                                       String bunkId=(list.get(position).getBunk_id());
                                        blockbunk(bunkId);
                                    }
                                })

                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert = builder.create();
                alert.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {
                        alert.getButton(android.app.AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#1d1d1d"));
                        alert.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#94c3ab"));
                        alert.getButton(android.app.AlertDialog.BUTTON_NEUTRAL).setTextColor(Color.parseColor("#ff6600"));

                    }
                });
                alert.setTitle("Do You Wanna Accept?");
                alert.show();

            }
        });


        getEVStations();
    }

    private void blockbunk(String regId)
    {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest request = new StringRequest(Request.Method.POST, Utility.SERVERUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.trim().equals("failed")) {

                    Toast.makeText(getApplicationContext(), "Blocked", Toast.LENGTH_SHORT).show();
                    recreate();


                } else {
                    Toast.makeText(getApplicationContext(), "cant Block now", Toast.LENGTH_SHORT).show();
                }
            }

        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "" + error, Toast.LENGTH_SHORT).show();
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> map = new HashMap<String, String>();
                map.put("key", "blockbunk");
                map.put("reg_id", regId);

                return map;
            }

        };
        queue.add(request);
    }

    private void acceptbunk(String regId)
    {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest request = new StringRequest(Request.Method.POST, Utility.SERVERUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.trim().equals("failed")) {

                    Toast.makeText(getApplicationContext(), "Accepted", Toast.LENGTH_SHORT).show();
                    recreate();


                } else {
                    Toast.makeText(getApplicationContext(), "cant accpet now", Toast.LENGTH_SHORT).show();
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
                Map<String, String> map = new HashMap<String, String>();
                map.put("key", "acceptbunk");
                map.put("reg_id", regId);

                return map;
            }

        };
        queue.add(request);
    }

    private void getEVStations()
    {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest request = new StringRequest(Request.Method.POST, Utility.SERVERUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.trim().equals("failed")) {
                    Gson gson = new Gson();
                    list = Arrays.asList(gson.fromJson(response, RequestPojo[].class));
                    bunkAdapter adapter = new bunkAdapter(ViewEVStation.this, list);
                    listView.setAdapter(adapter);
                    registerForContextMenu(listView);

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
                SharedPreferences prefs = getApplicationContext().getSharedPreferences("SharedData", MODE_PRIVATE);
                final String reg_id = prefs.getString("reg_id", "No_id");
                final String type = prefs.getString("type", "NO-Type");
                Map<String, String> map = new HashMap<String, String>();
                map.put("key", "viewEVStations");
                //map.put("reg_id", reg_id);

                return map;
            }

        };
        queue.add(request);
    }
}