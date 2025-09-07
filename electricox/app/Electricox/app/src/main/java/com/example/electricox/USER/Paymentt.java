package com.example.electricox.USER;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.electricox.ADMIN.payy;
import com.example.electricox.CHARGINGSTATION.ui.Bookings;
import com.example.electricox.COMMON.Utility;
import com.example.electricox.R;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
public class Paymentt extends AppCompatActivity implements PaymentResultListener {

    String UserId, BookingId, bunkId , bId,totalPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paymentt);


        Intent in=getIntent();
        UserId=in.getExtras().getString("user_id");
        BookingId=in.getExtras().getString("Booking_id");
//        bunkId = in.getExtras().getString("bunkId");
        bId= in.getExtras().getString("bId");
        totalPrice= in.getExtras().getString("price");


        getEamailPhone();

    }

    private void getEamailPhone() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(Paymentt.this);
        builder.setTitle("Enter Contact Info");

        android.view.LayoutInflater inflater = getLayoutInflater();
        android.view.View dialogView = inflater.inflate(R.layout.dialog_email_phone, null);
        builder.setView(dialogView);

        android.widget.EditText emailInput = dialogView.findViewById(R.id.emailInput);
        android.widget.EditText phoneInput = dialogView.findViewById(R.id.phoneInput);

        builder.setPositiveButton("OK", (dialog, which) -> {
            String email = emailInput.getText().toString().trim();
            String phone = phoneInput.getText().toString().trim();

            if (!email.isEmpty() && !phone.isEmpty()) {
                startPayment(email, phone);  // ✅ Pass user input to Razorpay
            } else {
                Toast.makeText(getApplicationContext(), "Please enter both email and phone number", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        builder.show();
    }



    private void startPayment(String email, String phone) {
        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_test_G7rm6McllCyRE0");
        checkout.setImage(R.mipmap.ic_launcher);

        try {
            JSONObject options = new JSONObject();
            options.put("name", "Charging Station Payment");
            options.put("description", "Payment for EV Charging");
            options.put("currency", "INR");

            int amountInPaise = (int) (Float.parseFloat(totalPrice) * 100);
            options.put("amount", 100);

            options.put("prefill.email", email);
            options.put("prefill.contact", phone);

            runOnUiThread(() -> checkout.open(Paymentt.this, options));

        } catch (Exception e) {
            Toast.makeText(this, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest request = new StringRequest(Request.Method.POST, Utility.SERVERUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.trim().equals("failed")) {
                    Toast.makeText(getApplicationContext(), " Payment Successfull! Thanks", Toast.LENGTH_SHORT).show();
                    Intent in = new Intent(getApplicationContext(), User_Nav.class);
                    startActivity(in);

                } else {
                    Toast.makeText(getApplicationContext(), "Failed Payment", Toast.LENGTH_SHORT).show();
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



                SharedPreferences prefs = getApplicationContext().getSharedPreferences("sharedData", MODE_PRIVATE);
                final String uid = prefs.getString("reg_id", "No_idd");//"No name defined" is the default value.
                final String type = prefs.getString("type", "NO-Type");
                Map<String, String> map = new HashMap<String, String>();
                map.put("key", "addbankpayment");
                map.put("bId", bId);
                map.put("user_id", uid);
                map.put("BookingId", BookingId);
//                map.put("BunkId", bunkId);
                map.put("razorpayPaymentID", razorpayPaymentID);
                map.put("price", totalPrice);


                return map;


            }

        };
        queue.add(request);
    }

    @Override
    public void onPaymentError(int code, String response) {
        Toast.makeText(this, "Payment Failed! " + response, Toast.LENGTH_LONG).show();
    }
}