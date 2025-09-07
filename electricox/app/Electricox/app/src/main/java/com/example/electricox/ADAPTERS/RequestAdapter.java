package com.example.electricox.ADAPTERS;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.electricox.COMMON.RequestPojo;
import com.example.electricox.R;
import com.example.electricox.USER.FeedBack;
import com.example.electricox.USER.PaymentUser;
import com.example.electricox.USER.Paymentt;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class RequestAdapter extends ArrayAdapter<RequestPojo> {


    Activity context;
    List<RequestPojo> list;
    Fragment fragment;

    MaterialButton payment, feedback;

    public RequestAdapter(Activity context, List<RequestPojo> list, Fragment fragment) {
        super(context, R.layout.activity_request_adapter, list);
        this.context = context;
        this.list = list;
        this.fragment = fragment;


    }

    public RequestAdapter(Activity context, List<RequestPojo> list) {
        super(context, R.layout.activity_request_adapter, list);
        this.context = context;
        this.list = list;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {


        LayoutInflater inflater = context.getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_request_adapter, null, true);


        TextView UserName = view.findViewById(R.id.User_name);
        TextView parkinTime = view.findViewById(R.id.parkinTime);
        TextView SlotNumber = view.findViewById(R.id.SlotNumber);
        TextView status = view.findViewById(R.id.status);
        TextView statusNote = view.findViewById(R.id.statusNote);
        payment = view.findViewById(R.id.paymentButton);
        feedback = view.findViewById(R.id.feedBack);

        if (list.get(position).getUserType().equals("viewchargingStation")) {
            UserName.setText(list.get(position).getU_name());
            parkinTime.setText("Parking Time :" + list.get(position).getBooking_time());
            SlotNumber.setText("Slot:" + list.get(position).getSlot_number());

            if (list.get(position).getBooking_status().equals("requested")) {
                status.setVisibility(View.VISIBLE);
                status.setText("Pending");
            }

            if (list.get(position).getBooking_status().equals("Accepted") && list.get(position).getPayment_status().equals("Payment Pending")) {
                status.setVisibility(View.VISIBLE);
                status.setText("Accpted");
                //payment.setVisibility(View.VISIBLE);
            }
            if (list.get(position).getPayment_status().equals("payed")) {
                status.setVisibility(View.VISIBLE);
                status.setText("Payed");
            }

            if (list.get(position).getBooking_status().equals("Available") && list.get(position).getQr_status().equals("Scanned")) {
                status.setVisibility(View.VISIBLE);
                status.setText("Scanned");
            }


            if (list.get(position).getBooking_status().equals("Cancelled")) {
                status.setVisibility(View.VISIBLE);
                status.setText("Cancelled");
                status.setTextColor(Color.RED); // optional: make it red to stand out
                statusNote.setVisibility(View.VISIBLE);
                statusNote.setText("This booking was cancelled.");

                // Hide payment and feedback buttons if visible
                payment.setVisibility(View.GONE);
                feedback.setVisibility(View.GONE);
                view.setClickable(false); // Prevent click
                view.setOnClickListener(null);
            }

        }
       else if (list.get(position).getUserType().equals("viewUser"))
        {
            UserName.setVisibility(View.GONE);
            parkinTime.setText("Parking Time :"+list.get(position).getBooking_time());
            SlotNumber.setText("Slot:"+list.get(position).getSlot_number());
            if(list.get(position).getBooking_status().equals("requested"))
            {
                status.setVisibility(View.VISIBLE);
                status.setText("Pending");
                statusNote.setText("Your request is pending. The Ev Station needs to confirm your booking.");
                statusNote.setVisibility(View.VISIBLE);
            }


            if (list.get(position).getBooking_status().equals("Cancelled")) {
                status.setVisibility(View.VISIBLE);
                status.setText("Cancelled");
                status.setTextColor(Color.RED);
                statusNote.setVisibility(View.VISIBLE);
                statusNote.setText("This booking has been cancelled. Please make a new booking if required.");
                payment.setVisibility(View.GONE);
                feedback.setVisibility(View.GONE);

                view.setClickable(false); // Prevent click
                view.setOnClickListener(null);
            }

            if (list.get(position).getBooking_status().equals("Accepted") && list.get(position).getPayment_status().equals("Payment Pending")) {
                //status.setVisibility(View.VISIBLE);
                status.setText("Accpted");
                payment.setVisibility(View.VISIBLE);
                statusNote.setText("Booking accepted. Please make the payment to receive your QR code.");
                statusNote.setVisibility(View.VISIBLE);

                payment.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {

                        RequestPojo currentItem = list.get(position);
                        String user_id = currentItem.getU_id();
                        String Booking_id=currentItem.getBooking_id();
                        String bunkId = currentItem.getBunk_id();
                        String bId = currentItem.getB_id();
                        String price = currentItem.getPaymentPrice();



                        Intent in=new Intent(getContext(), Paymentt.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("user_id", user_id);
                        System.out.println(user_id);
                        bundle.putString("Booking_id", Booking_id);
//                        bundle.putString("bunkId", bunkId);
                        bundle.putString("bId", bId);
                        bundle.putString("price", price);

                        in.putExtras(bundle);
                        getContext().startActivity(in);
                }
                });
            }
            if (list.get(position).getPayment_status().equals("payed"))
            {
                status.setVisibility(View.VISIBLE);
                status.setText("Payed");
            }

            if (list.get(position).getBooking_status().equals("Available") && list.get(position).getQr_status().equals("Scanned")) {
                status.setVisibility(View.VISIBLE);
                status.setText("Scanned");
                feedback.setVisibility(View.VISIBLE);
                statusNote.setText("You have successfully Completed!. Please provide feedback on your experience.");
                statusNote.setVisibility(View.VISIBLE);

                feedback.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        RequestPojo currentItem = list.get(position);
                        String user_id = currentItem.getU_id();
                        String Booking_id=currentItem.getBooking_id();
                        String bunk_id= currentItem.getBunk_id();
                        String b_id= currentItem.getB_id();

                        Intent in=new Intent(getContext(), FeedBack.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("user_id", user_id);
                        System.out.println(user_id);
                        bundle.putString("Booking_id", Booking_id);
                        bundle.putString("b_id", b_id);
                        bundle.putString("bunk_id", bunk_id);
                        in.putExtras(bundle);
                        getContext().startActivity(in);
                    }
                }
                );
            }
        }


        return view;
    }
}

