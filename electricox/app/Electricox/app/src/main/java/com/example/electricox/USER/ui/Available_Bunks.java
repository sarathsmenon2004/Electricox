package com.example.electricox.USER.ui;

import static android.content.Context.MODE_PRIVATE;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.electricox.ADAPTERS.bunkAdapter;
import com.example.electricox.ADMIN.ViewEVStation;
import com.example.electricox.COMMON.MapActivity;
import com.example.electricox.COMMON.RequestPojo;
import com.example.electricox.COMMON.Slot;
import com.example.electricox.COMMON.Utility;
import com.example.electricox.R;
import com.example.electricox.USER.TrackMapActivity;
import com.example.electricox.databinding.FragmentAvailbleBunksBinding;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Available_Bunks extends Fragment {



    private FragmentAvailbleBunksBinding binding;

    TextInputLayout searchPackage;

    String searchText;


    ListView listView;

    List<RequestPojo> list;
    AlertDialog.Builder builder;

    String bunk_id, owner_id,user_id , price;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentAvailbleBunksBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        searchPackage=root.findViewById(R.id.searchPackage);
        listView=root.findViewById(R.id.viewSearchList);
        builder=new AlertDialog.Builder(getContext());


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {


                builder.setMessage("Do You Wanna Book?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                bunk_id=(list.get(position).getBunk_id());
                                owner_id=(list.get(position).getB_id());
                                price=(list.get(position).getBunk_charging_rate());



                                Intent intent = new Intent(getContext(), Slot.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("owner_id", owner_id);
                                bundle.putString("bunk_id", bunk_id);
                                bundle.putString("price", price);
                                bundle.putString("owner_id", owner_id);
                                intent.putExtras(bundle);
                                startActivity(intent);





                            }
                        }).setNeutralButton("Track" , new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        String Lat = list.get(position).getLat();
                                        String Longg = list.get(position).getLongg();
                                        if (Lat != null && Longg != null && !Lat.isEmpty() && !Longg.isEmpty()) {
                                            Intent intent = new Intent(getContext(), TrackMapActivity.class);
                                            intent.putExtra("lat", Lat);
                                            intent.putExtra("lng", Longg);
                                            startActivity(intent);

                                        } else {
                                            Toast.makeText(getContext(), "Invalid Location Data", Toast.LENGTH_SHORT).show();
                                        }



//                                        if (Lat != null && Longg != null && !Lat.isEmpty() && !Longg.isEmpty()) {
//                                            // Create a URI for Google Maps navigation
//                                            Uri gmmIntentUri = Uri.parse("google.navigation:q=" + Lat + "," + Longg);
//
//                                            // Create an intent to open Google Maps app
//                                            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
//                                            mapIntent.setPackage("com.google.android.apps.maps");
//
//                                            // Check if Google Maps app is installed
//                                            if (mapIntent.resolveActivity(getActivity().getPackageManager()) != null) {
//                                                startActivity(mapIntent);
//                                            } else {
//                                                // If Google Maps app is not installed, open in browser
//                                                Uri webUri = Uri.parse("https://www.google.com/maps/search/?api=1&query=" + Lat + "," + Longg);
//                                                Intent webIntent = new Intent(Intent.ACTION_VIEW, webUri);
//                                                startActivity(webIntent);
//                                            }
//                                        } else {
//                                            Toast.makeText(getContext(), "Invalid Location Data", Toast.LENGTH_SHORT).show();
//                                        }



//                                        if (Lat != null && Longg != null && !Lat.isEmpty() && !Longg.isEmpty()) {
//                                            Intent intent = new Intent(getContext(), MapActivity.class);
//                                            intent.putExtra("latitude", Lat);
//                                            intent.putExtra("longitude", Longg);
//                                            startActivity(intent);
//                                        } else {
//                                            Toast.makeText(getContext(), "Invalid Location Data", Toast.LENGTH_SHORT).show();
//                                        }

                                    }
                        }
                        )


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
                alert.setTitle("Book The Bunk?");
                alert.show();

            }
        });

        showAllPackges();


        searchPackage.setEndIconOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v)
            {
                validate();

            }

        });



        return root;
    }


    private void validate()
    {

        searchText=searchPackage.getEditText().getText().toString();
        if(searchText.isEmpty())
        {
            showAllPackges();

        }
        else
        {
            getSerchResult();
        }
    }

    private void showAllPackges()
    {
        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest request = new StringRequest(Request.Method.POST, Utility.SERVERUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.trim().equals("failed")) {
                    Gson gson = new Gson();
                    list = Arrays.asList(gson.fromJson(response, RequestPojo[].class));
                    bunkAdapter adapter = new bunkAdapter(getActivity(), list);
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
        }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                SharedPreferences prefs = getContext().getSharedPreferences("SharedData", MODE_PRIVATE);
                final String reg_id = prefs.getString("reg_id", "No_id");
                final String type = prefs.getString("type", "NO-Type");
                Map<String, String> map = new HashMap<String, String>();
                map.put("key", "viewEVStationsUser");
                //map.put("reg_id", reg_id);

                return map;
            }

        };
        queue.add(request);
    }



    private void getSerchResult()
    {
        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest request = new StringRequest(Request.Method.POST, Utility.SERVERUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.trim().equals("failed")) {
                    Gson gson = new Gson();
                    list = Arrays.asList(gson.fromJson(response, RequestPojo[].class));
                    bunkAdapter adapter = new bunkAdapter(getActivity(),list);
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
        }
        ) {
            @Override
            protected Map<String, String> getParams() {
                SharedPreferences prefs = getContext().getSharedPreferences("sharedData", MODE_PRIVATE);
                final String reg_id = prefs.getString("reg_id", "No_id");
                final String type = prefs.getString("type", "NO-Type");
                Map<String, String> map = new HashMap<String, String>();
                map.put("key", "viewEVStationsUserSearch");
                map.put("reg_id", reg_id);
                map.put("serchValue",  searchText);

                return map;
            }

        };
        queue.add(request);
    }


}