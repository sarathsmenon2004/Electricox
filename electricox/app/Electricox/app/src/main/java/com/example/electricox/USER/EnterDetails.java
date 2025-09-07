package com.example.electricox.USER;

import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.electricox.COMMON.Slot;
import com.example.electricox.COMMON.Utility;
import com.example.electricox.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.HashMap;
import java.util.Map;

public class EnterDetails extends AppCompatActivity {


    Button uploadButton;
    TextInputEditText uploadDrivingLicence;
    String Slot , price , bunk_id, owner_id;
    TextView SlotNumber ,  priceTextView  , totalCostTextView;
    TextView parkingTimeEditText, selectedTimeTextView ;

    String selectedTime;
    String slotStatus;
    Spinner durationSpinner;

    String calculatedTotalCost;

    String SLOTNUMBER, VEHICLETYPE, PARKTIME, DRIVINGLICENS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_details);


        uploadDrivingLicence = findViewById(R.id.uploadDrivingLicence);
        uploadButton = findViewById(R.id.uploadButton);
        SlotNumber = findViewById(R.id.sloteNumber);
        priceTextView = findViewById(R.id.priceTextView);
        parkingTimeEditText = findViewById(R.id.parkingTimeEditText);
        selectedTimeTextView = findViewById(R.id.selectedTimeTextView);
        durationSpinner = findViewById(R.id.durationSpinner);
        totalCostTextView = findViewById(R.id.totalCostTextView);

        Intent in = getIntent();
        Slot = in.getExtras().getString("slotNumber");

        bunk_id=in.getExtras().getString(("bunk_id"));
        owner_id=in.getExtras().getString(("owner_id"));
        price=in.getExtras().getString(("price"));


        SlotNumber.setText("Your Slot is : " + Slot);
        priceTextView.setText("Price per kW : ₹" + price);


        durationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    // Retrieve the selected duration
                    int selectedDuration = Integer.parseInt(parent.getItemAtPosition(position).toString());

                    // Calculate and display the total cost
                    calculateTotalCost(selectedDuration);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    totalCostTextView.setText("Invalid duration selected");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Optionally handle the case where no selection is made
                totalCostTextView.setText("Please select a duration");
            }

            private void calculateTotalCost(int duration) {
                try {
                    // Ensure 'price' is a valid number
                    double pricePerKw = Double.parseDouble(price); // make sure 'price' is defined as a String or double elsewhere
                    double chargingPower = 7.4; // in kW

                    // Calculate total energy consumed in kWh
                    double totalEnergy = chargingPower * duration;

                    // Calculate total cost
                    double totalCost = totalEnergy * pricePerKw;

                    calculatedTotalCost = String.valueOf(totalCost);

                    // Display the total cost
                    totalCostTextView.setText(String.format("Total Cost: ₹%.2f", totalCost));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    totalCostTextView.setText("Invalid price");
                }
            }
        });


        parkingTimeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        EnterDetails.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                String amPm;
                                if (hourOfDay >= 12) {
                                    amPm = "PM";
                                    if (hourOfDay > 12) {
                                        hourOfDay -= 12;
                                    }
                                } else {
                                    amPm = "AM";
                                }

                                // Handle the selected time with AM/PM
                                selectedTime = String.format("%02d:%02d %s", hourOfDay, minute, amPm);
                                selectedTimeTextView.setText(selectedTime);
                                selectedTimeTextView.setVisibility(View.VISIBLE);
                            }
                        },
                        0,
                        0,
                        false
                );

                timePickerDialog.show();
            }
        });


        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SLOTNUMBER = Slot;
                DRIVINGLICENS = uploadDrivingLicence.getText().toString().trim();
                PARKTIME = selectedTime;

                if (DRIVINGLICENS.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please enter your vehicle number", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (PARKTIME == null || PARKTIME.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please select a parking time", Toast.LENGTH_SHORT).show();
                    return;
                }

                String selectedDurationStr = durationSpinner.getSelectedItem().toString();
                if (selectedDurationStr.equals("Select Duration") || selectedDurationStr.trim().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please select a valid duration", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    final int selectedDuration = Integer.parseInt(selectedDurationStr);
                    final double pricePerKw = Double.parseDouble(price);
                    final double totalCost = selectedDuration * pricePerKw;

                    addParkingDetails(totalCost, selectedDuration);
                } catch (NumberFormatException e) {
                    Toast.makeText(getApplicationContext(), "Invalid data. Please try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });



    }


    private void addParkingDetails(double totalCost, int selectedDuration) {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        StringRequest request = new StringRequest(Request.Method.POST, Utility.SERVERUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.trim().equals("failed")) {
                    Toast.makeText(getApplicationContext(), "Success ..!", Toast.LENGTH_SHORT).show();
                    Intent in = new Intent(EnterDetails.this,Slot.class);
                 
                    startActivity(in);
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
                SharedPreferences prefs = getApplicationContext().getSharedPreferences("sharedData", MODE_PRIVATE);
                final String uid = prefs.getString("reg_id", "No_idd");//"No name defined" is the default value.
                final String type = prefs.getString("type", "Notype");
                Map<String, String> map = new HashMap<String, String>();
                map.put("key", "addParkingRequest");
                map.put("login_id", uid);
                map.put("bunk_id", bunk_id);
                map.put("SLOTNUMBER", SLOTNUMBER);
                map.put("DRIVINGLICENS", DRIVINGLICENS);
                map.put("PARKTIME", PARKTIME);
                map.put("owner_id", owner_id);

                map.put("selectedDuration", String.valueOf(selectedDuration));
                map.put("totalCost", calculatedTotalCost);
                return map;
            }
        };
        queue.add(request);
    }

    private void getParkingDetails() {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        StringRequest request = new StringRequest(Request.Method.POST, Utility.SERVERUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.trim().equals("failed")) {
                    String DATA = response;
                    String resArr[] = DATA.trim().split("#");
                    slotStatus = resArr[0];
                    Intent in = new Intent(EnterDetails.this,Slot.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("slotNumber", SLOTNUMBER);
                    bundle.putString("slotStatus", slotStatus);
                    in.putExtras(bundle);
                    startActivity(in);
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
                SharedPreferences prefs = getApplicationContext().getSharedPreferences("sharedData", MODE_PRIVATE);
                final String uid = prefs.getString("reg_id", "No_idd");//"No name defined" is the default value.
                final String type = prefs.getString("type", "Notype");
                Map<String, String> map = new HashMap<String, String>();
                map.put("key", "getParkingRequest");
                map.put("login_id", uid);
                map.put("SLOTNUMBER", Slot);
                map.put("bunk_id", bunk_id);
                return map;
            }
        };
        queue.add(request);
    }


}