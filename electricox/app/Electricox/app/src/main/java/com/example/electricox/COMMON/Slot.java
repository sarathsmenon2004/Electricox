package com.example.electricox.COMMON;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.electricox.R;
import com.example.electricox.USER.EnterDetails;
import com.google.gson.Gson;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// ... (import statements remain the same)

public class Slot extends AppCompatActivity {

    private String selectedSlot = "";
    List<RequestPojo> list;
    CardView[] cardViews;
    String bunkk_id , owner_id , price ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slot);

        Intent intent = getIntent();
        bunkk_id = intent.getExtras().getString("bunk_id");
        price = intent.getExtras().getString("price");
        owner_id = intent.getExtras().getString("owner_id");

        cardViews = new CardView[]{
                findViewById(R.id.slot1), findViewById(R.id.slot2), findViewById(R.id.slot3), findViewById(R.id.slot4),
                findViewById(R.id.slot5), findViewById(R.id.slot6), findViewById(R.id.slot7), findViewById(R.id.slot8),
                findViewById(R.id.slot9), findViewById(R.id.slot10), findViewById(R.id.slot11), findViewById(R.id.slot12),
                findViewById(R.id.slot13), findViewById(R.id.slot14), findViewById(R.id.slot15), findViewById(R.id.slot16),
                findViewById(R.id.slot17), findViewById(R.id.slot18), findViewById(R.id.slot19), findViewById(R.id.slot20),
                findViewById(R.id.slot21), findViewById(R.id.slot22), findViewById(R.id.slot23), findViewById(R.id.slot24),
                findViewById(R.id.slot25), findViewById(R.id.slot26), findViewById(R.id.slot27), findViewById(R.id.slot28)
        };

        String initialBunkId = bunkk_id;

        getSlotStatus(initialBunkId);

        for (final CardView cardView : cardViews) {
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView textView = (TextView) cardView.getChildAt(0);
                    selectedSlot = textView.getText().toString();

                    // Assuming you have bunkId available when a card is clicked
                    String bunkId = bunkk_id;
                    String ow_id=owner_id;
                    String pricee = price;

                    goToEnterDetails(selectedSlot, bunkId, ow_id , pricee);
                }
            });
        }
    }

    private void getSlotStatus(final String bunkId) {
        clearLayout(); // Reset all card slots to default

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        StringRequest request = new StringRequest(Request.Method.POST, Utility.SERVERUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.trim().equals("failed")) {
                    Gson gson = new Gson();
                    list = Arrays.asList(gson.fromJson(response, RequestPojo[].class));

                    for (RequestPojo requestPOJO : list) {
                        String slot = requestPOJO.getSlot_number();
                        String status = requestPOJO.getBooking_status();
                        String bunk_id = requestPOJO.getBunk_id();

                        // Default slot color (white)
                        int cardColor = getResources().getColor(R.color.white);

                        // Change color if slot is requested or accepted
                        if ("requested".equalsIgnoreCase(status)) {
                            cardColor = getResources().getColor(R.color.green);
                        } else if ("Accepted".equalsIgnoreCase(status)) {
                            cardColor = getResources().getColor(R.color.red);
                        }

                        // Find the matching slot card and apply color and interaction state
                        for (CardView cardView : cardViews) {
                            TextView textView = (TextView) cardView.getChildAt(0);
                            String cardSlot = textView.getText().toString();

                            if (slot != null && slot.equals(cardSlot)) {
                                cardView.setCardBackgroundColor(cardColor);

                                // Disable card if not available/requested
                                if (!"Available".equalsIgnoreCase(status) && !"requested".equalsIgnoreCase(status)) {
                                    cardView.setEnabled(false);
                                    cardView.setClickable(false);
                                } else {
                                    cardView.setEnabled(true);
                                    cardView.setClickable(true);
                                }
                                break;
                            }
                        }
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("VolleyError", error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                SharedPreferences prefs = getApplicationContext().getSharedPreferences("sharedData", MODE_PRIVATE);
                final String login_id = prefs.getString("login_id", "No_id");
                Map<String, String> map = new HashMap<>();
                map.put("key", "getSlotStatus");
                map.put("bunk_id", bunkId);
                return map;
            }
        };

        queue.add(request);
    }


    private void clearLayout() {
        for (CardView cardView : cardViews) {
            cardView.setCardBackgroundColor(getResources().getColor(R.color.white));
            cardView.setEnabled(true);
            cardView.setClickable(true);
        }
    }

    private void goToEnterDetails(String slot, String bunkId , String owner_id, String pricee)
    {
        Intent in = new Intent(Slot.this, EnterDetails.class);
        Bundle bundle = new Bundle();
        bundle.putString("slotNumber", slot);
        bundle.putString("bunk_id", bunkId);
        bundle.putString("owner_id", owner_id);
        bundle.putString("price", pricee);
        in.putExtras(bundle);
        startActivity(in);
    }
}


