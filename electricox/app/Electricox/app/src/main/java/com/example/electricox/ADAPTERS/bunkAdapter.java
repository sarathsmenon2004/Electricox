package com.example.electricox.ADAPTERS;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.electricox.COMMON.Base64;
import com.example.electricox.COMMON.RequestPojo;
import com.example.electricox.R;

import java.io.IOException;
import java.util.List;

public class bunkAdapter extends ArrayAdapter<RequestPojo> {

    Activity context;
    List<RequestPojo> package_list;
    String base;

    public bunkAdapter(Activity context, List<RequestPojo> package_list) {
        super(context, R.layout.activity_bunk_adapter, package_list);
        this.context = context;
        this.package_list = package_list;


    }


    public View getView(final int position, View convertView, ViewGroup parent)
    {

        LayoutInflater inflater = context.getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_bunk_adapter, null, true);

        ImageView Image = (ImageView) view.findViewById(R.id.bunkImage);
        TextView BunkName = view.findViewById(R.id.BunkName);

        TextView wifi = view.findViewById(R.id.wifi);
        TextView restRoom = view.findViewById(R.id.restRoom);
        TextView waiting_area = view.findViewById(R.id.waiting_area);
        TextView NearBy = view.findViewById(R.id.NearBy);
        TextView Onsite_Support = view.findViewById(R.id.Onsite_Support);
        TextView bunkAddress = view.findViewById(R.id.bunkAddress);
        TextView bunkStatus = view.findViewById(R.id.bunkStatus);
        //TextView lc = view.findViewById(R.id.lc);
        TextView pricePerKw = view.findViewById(R.id.pricePerKw);
        TextView plugTypes = view.findViewById(R.id.plugTypes);
        RatingBar truckRatingBar = view.findViewById(R.id.truckRatingBar);

        //TextView lcl = view.findViewById(R.id.lcl);


        System.out.println(package_list.get(position).getUserType());

        if (package_list.get(position).getUserType().equals("Admin")) {
            base = package_list.get(position).getBunk_image();
            BunkName.setText(package_list.get(position).getBunk_name());
            wifi.setText(package_list.get(position).getWifi_availability());
            restRoom.setText(package_list.get(position).getRestroom());
            waiting_area.setText(package_list.get(position).getWaiting_area());
            NearBy.setText(package_list.get(position).getNearBy_amenities());
            Onsite_Support.setText(package_list.get(position).getOnsite_support());
            bunkAddress.setText(package_list.get(position).getBunk_address());

            String ratingString = package_list.get(position).getRating();
            if (ratingString != null && !ratingString.isEmpty()) {
                double truckRating = Double.parseDouble(ratingString.trim());
                truckRatingBar.setRating((float) truckRating);
            } else {
                truckRatingBar.setRating(0.0f);
            }

            pricePerKw.setText("₹" + package_list.get(position).getBunk_charging_rate());
            plugTypes.setText(package_list.get(position).getPlugType());

            if (package_list.get(position).getStatus().equals("0")) {
                bunkStatus.setText("Pending!");
            } else if (package_list.get(position).getStatus().equals("2")) {
                bunkStatus.setText("Blocked!");
            } else {
                bunkStatus.setText("Accepted!");
            }
        }



        System.out.println(package_list.get(position).getUserType());

        if (package_list.get(position).getUserType().equals("Owner")) {
            base=package_list.get(position).getBunk_image();
            BunkName.setText(package_list.get(position).getBunk_name());
            wifi.setText(package_list.get(position).getWifi_availability());
            restRoom.setText(package_list.get(position).getRestroom());
            waiting_area.setText(package_list.get(position).getWaiting_area());
            NearBy.setText(package_list.get(position).getNearBy_amenities());
            Onsite_Support.setText(package_list.get(position).getOnsite_support());
            bunkAddress.setText(package_list.get(position).getBunk_address());
            double truckRating = Double.parseDouble(package_list.get(position).getRating());
            truckRatingBar.setRating((float) truckRating);
          //  lc.setText(package_list.get(position).getEmergency_number());

            pricePerKw.setText("₹" + package_list.get(position).getBunk_charging_rate());
            plugTypes.setText(package_list.get(position).getPlugType());


            if(package_list.get(position).getStatus().equals("0"))
            {
                bunkStatus.setText("Request Pending!");
            } else if (package_list.get(position).getStatus().equals("2"))
            {
                bunkStatus.setText("Blocked! by Admin");
            }
            else
            {
                bunkStatus.setText("Accepted by Admin!");
            }

        }


        System.out.println(package_list.get(position).getUserType());

        if (package_list.get(position).getUserType().equals("User")) {

            base=package_list.get(position).getBunk_image();
            BunkName.setText(package_list.get(position).getBunk_name());
            wifi.setText(package_list.get(position).getWifi_availability());
            restRoom.setText(package_list.get(position).getRestroom());
            waiting_area.setText(package_list.get(position).getWaiting_area());
            NearBy.setText(package_list.get(position).getNearBy_amenities());
            Onsite_Support.setText(package_list.get(position).getOnsite_support());
            double truckRating = Double.parseDouble(package_list.get(position).getRating());
            truckRatingBar.setRating((float) truckRating);
            bunkAddress.setText(package_list.get(position).getBunk_address());

            pricePerKw.setText("₹" + package_list.get(position).getBunk_charging_rate());
            plugTypes.setText(package_list.get(position).getPlugType());
            //lc.setVisibility(View.GONE);
           // lcl.setVisibility(View.GONE);

            bunkStatus.setVisibility(View.GONE);


        }



        try {
            byte[] imageAsBytes = Base64.decode(base.getBytes());
            Image.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));
        } catch (IOException e) {
            e.printStackTrace();
        }


        return view;
    }
    }