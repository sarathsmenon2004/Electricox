package com.example.electricox.CHARGINGSTATION.ui;

import static android.content.Context.MODE_PRIVATE;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.electricox.ADAPTERS.RequestAdapter;
import com.example.electricox.CHARGINGSTATION.GenerateQRcode;
import com.example.electricox.COMMON.RequestPojo;
import com.example.electricox.COMMON.Utility;
import com.example.electricox.R;
import com.example.electricox.databinding.FragmentBookingBinding;
import com.google.gson.Gson;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Bookings extends Fragment {

    private FragmentBookingBinding binding;

    ListView listView;
    List<RequestPojo> list;
    ImageView nodata;
    AlertDialog.Builder builder;
    String status,paymentStatus,qrStatus;
    String requestid, userName;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentBookingBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        listView = root.findViewById(R.id.viewRequest);
        builder = new AlertDialog.Builder(getActivity());


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                status = list.get(position).getBooking_status();
                paymentStatus=list.get(position).getPayment_status();
                qrStatus=list.get(position).getQr_status();



                // Check if the status is "requested" before showing the dialog
                if ("requested".equals(status) &&  "not scanned".equals(qrStatus)) {
                    builder.setMessage("Generate QR ")
                            .setCancelable(false)
                            .setPositiveButton("Approve", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // Your approval logic here

                                    String Booking_id = list.get(position).getBooking_id();
                                    String licenceNumber = list.get(position).getLicenceNumber();
                                    String slotNumber = list.get(position).getSlot_number();
                                    String parkingTime = list.get(position).getBooking_time();
                                    String bookingStatus = list.get(position).getBooking_status();
                                    String UserName = list.get(position).getU_name();
                                    String userAddres = list.get(position).getU_address();
                                    String userMobile = list.get(position).getU_phone();
                                    String userEmail = list.get(position).getU_email();
                                    String userid = list.get(position).getU_id();


                                    Intent in = new Intent(getContext(), GenerateQRcode.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putString("Booking_id", Booking_id);
                                    bundle.putString("licenceNumber", licenceNumber);
                                    bundle.putString("slotNumber", slotNumber);
                                    bundle.putString("parkingTime", parkingTime);
                                    bundle.putString("bookingStatus", bookingStatus);
                                    bundle.putString("UserName", UserName);
                                    bundle.putString("userAddres", userAddres);
                                    bundle.putString("userMobile", userMobile);
                                    bundle.putString("userEmail", userEmail);
                                    bundle.putString("User_id", userid);

                                    in.putExtras(bundle);
                                    startActivity(in);
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert = builder.create();
                    alert.setTitle("Approve Pass?");
                    alert.setOnShowListener(new DialogInterface.OnShowListener() {
                        @Override
                        public void onShow(DialogInterface dialogInterface) {
                            alert.getButton(android.app.AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#1d1d1d"));
                            alert.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#94c3ab"));
                        }
                    });
                    alert.show();
                    alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffff")));
                }
            }
        });

        getRequests();
        return root;
    }

    private void getRequests()
    {
        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest request = new StringRequest(Request.Method.POST, Utility.SERVERUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.trim().equals("failed")) {
                    Gson gson = new Gson();
                    list = Arrays.asList(gson.fromJson(response, RequestPojo[].class));
                    RequestAdapter adapter = new RequestAdapter(getActivity(), list);
                    listView.setAdapter(adapter);
                    registerForContextMenu(listView);
                } else {
                    Toast.makeText(getContext(), "NO_DATA", Toast.LENGTH_SHORT).show();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                SharedPreferences prefs = getContext().getSharedPreferences("sharedData", MODE_PRIVATE);
                final String uid = prefs.getString("reg_id", "No_idd");//"No name defined" is the default value.
                final String type = prefs.getString("type", "Notype");
                Map<String, String> map = new HashMap<String, String>();
                map.put("key", "RequeststochargingStation");
                map.put("id", uid);
                return map;
            }
        };
        queue.add(request);
    }


}