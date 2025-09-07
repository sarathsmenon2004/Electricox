package com.example.electricox.CHARGINGSTATION.ui;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.electricox.ADAPTERS.PaymentAdapter;
import com.example.electricox.COMMON.RequestPojo;
import com.example.electricox.COMMON.Utility;
import com.example.electricox.R;
import com.google.gson.Gson;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Payment extends Fragment {


    ListView listView;

    List<RequestPojo> paymentlist;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root= inflater.inflate(R.layout.fragment_payment, container, false);

        listView=root.findViewById(R.id.viewBankDetails);

        getPaymentDetails();


        return root;
    }

    private void getPaymentDetails()
    {
        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest request = new StringRequest(Request.Method.POST, Utility.SERVERUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.trim().equals("failed")) {

                    Gson gson = new Gson();
                    paymentlist = Arrays.asList(gson.fromJson(response, RequestPojo[].class));
                    PaymentAdapter adapter = new PaymentAdapter(getActivity(),paymentlist);
                    listView.setAdapter(adapter);
                    registerForContextMenu(listView);

                } else {
                    listView.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "NO_DATA", Toast.LENGTH_SHORT).show();

                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "" + error, Toast.LENGTH_SHORT).show();
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() {
                SharedPreferences prefs = getContext().getSharedPreferences("sharedData", MODE_PRIVATE);
                final String uid = prefs.getString("reg_id", "No_idd");//"No name defined" is the default value.
                final String type = prefs.getString("type", "Notype");
                Map<String, String> map = new HashMap<String, String>();
                map.put("key", "ViewBankingPaymentDetails");
                map.put("id", uid);

                return map;
            }

        };
        queue.add(request);
    }
}