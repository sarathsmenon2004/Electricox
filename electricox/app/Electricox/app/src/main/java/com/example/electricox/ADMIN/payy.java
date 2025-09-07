package com.example.electricox.ADMIN;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.example.electricox.R;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

public class payy extends AppCompatActivity implements PaymentResultListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payy);

        startPayment();
    }


    private void startPayment() {
        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_test_G7rm6McllCyRE0"); // ✅ Replace with your Razorpay Key ID
        checkout.setImage(R.mipmap.ic_launcher);  // ✅ Add a logo (optional)

        try {

            JSONObject options = new JSONObject();

            options.put("name", "Charging Station Payment");
            options.put("description", "Payment for EV Charging");
            options.put("currency", "INR");
            options.put("amount", 500); // ✅ Amount in paise (₹500.00)
            options.put("prefill.email", "test@example.com");  // ✅ Prefill user email (optional)
            options.put("prefill.contact", "9999999999");       // ✅ Prefill user phone number (optional)

            runOnUiThread(() -> checkout.open(payy.this, options));  // ✅ Ensure it runs on UI thread


        } catch (Exception e) {
            Toast.makeText(this, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        Toast.makeText(this, "Payment Successful! ID: " + razorpayPaymentID, Toast.LENGTH_LONG).show();
        finish(); // ✅ Close the payment page
    }

    @Override
    public void onPaymentError(int code, String response) {
        Toast.makeText(this, "Payment Failed! " + response, Toast.LENGTH_LONG).show();
    }
}