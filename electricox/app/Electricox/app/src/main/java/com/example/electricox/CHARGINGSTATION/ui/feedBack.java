package com.example.electricox.CHARGINGSTATION.ui;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.electricox.ADAPTERS.feedBackAdapter;
import com.example.electricox.COMMON.RequestPojo;
import com.example.electricox.COMMON.Utility;
import com.example.electricox.R;
import com.example.electricox.databinding.FragmentFeedbackBinding;
import com.google.gson.Gson;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class feedBack extends Fragment {

    private FragmentFeedbackBinding binding;

    ListView listView;

    List<RequestPojo> feedbackList;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentFeedbackBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        getAllFeedback();


        listView=root.findViewById(R.id.ViewfeedbackList);


        return root;
    }

    private void getAllFeedback()
    {
        com.android.volley.RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest request = new StringRequest(Request.Method.POST, Utility.SERVERUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("******", response);
                if (!response.trim().equals("failed")) {
                    Gson gson = new Gson();
                    feedbackList =  Arrays.asList(gson.fromJson(response, RequestPojo[].class));
                    feedBackAdapter adapter = new feedBackAdapter(getActivity(), feedbackList);
                    listView.setAdapter(adapter);
                    registerForContextMenu(listView);
                } else {

                    Toast.makeText(getContext(), "No data", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getContext(), "my Error :" + error, Toast.LENGTH_LONG).show();
                Log.i("My Error", "" + error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                SharedPreferences prefs = getContext().getSharedPreferences("sharedData", MODE_PRIVATE);
                final String uid = prefs.getString("reg_id", "No_idd");//"No name defined" is the default value.
                final String type = prefs.getString("type", "Notype");
                Map<String, String> map = new HashMap<String, String>();
                map.put("key", "getFeedback");
                map.put("uid", uid);

                return map;
            }
        };
        queue.add(request);
    }


}