package com.example.electricox.ADMIN;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.electricox.ADAPTERS.feedBackAdapter;
import com.example.electricox.COMMON.RequestPojo;
import com.example.electricox.COMMON.Utility;
import com.example.electricox.R;
import com.google.gson.Gson;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewUserFeedBack extends AppCompatActivity {


    ListView listView;

    List<RequestPojo> feedbackList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_user_feed_back);


        getAllFeedback();


        listView=findViewById(R.id.ViewfeedbackListAdmin);
    }

    private void getAllFeedback()
    {
        com.android.volley.RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest request = new StringRequest(Request.Method.POST, Utility.SERVERUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("******", response);
                if (!response.trim().equals("failed")) {
                    Gson gson = new Gson();
                    feedbackList =  Arrays.asList(gson.fromJson(response, RequestPojo[].class));
                    feedBackAdapter adapter = new feedBackAdapter(ViewUserFeedBack.this, feedbackList);
                    listView.setAdapter(adapter);
                    registerForContextMenu(listView);
                } else {

                    Toast.makeText(getApplicationContext(), "No data", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(), "my Error :" + error, Toast.LENGTH_LONG).show();
                Log.i("My Error", "" + error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                SharedPreferences prefs = getApplicationContext().getSharedPreferences("sharedData", MODE_PRIVATE);
                final String uid = prefs.getString("reg_id", "No_idd");//"No name defined" is the default value.
                final String type = prefs.getString("type", "Notype");
                Map<String, String> map = new HashMap<String, String>();
                map.put("key", "getFeedbackAdmin");
                map.put("uid", uid);

                return map;
            }
        };
        queue.add(request);
    }
}