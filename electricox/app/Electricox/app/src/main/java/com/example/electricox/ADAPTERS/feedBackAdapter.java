package com.example.electricox.ADAPTERS;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.electricox.COMMON.RequestPojo;
import com.example.electricox.R;

import java.util.List;

public class feedBackAdapter extends ArrayAdapter<RequestPojo> {

    Activity context;
    List<RequestPojo> rest_List;

    public feedBackAdapter(Activity context, List<RequestPojo> rest_List) {
        super(context, R.layout.activity_feed_back_adapter, rest_List);
        this.context = context;
        this.rest_List = rest_List;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_feed_back_adapter, null, true);

        // Initialize UI components
        TextView Name = view.findViewById(R.id.USERNAME);
        TextView subject = view.findViewById(R.id.SUBJECT);
        TextView description = view.findViewById(R.id.DESCRIPTION);
        RatingBar ratingBar = view.findViewById(R.id.ratingBar);  // Changed to RatingBar
        TextView Slote = view.findViewById(R.id.SLOT_NUM);
        TextView OwnerNumbe = view.findViewById(R.id.OwnerNumber);
        TextView BunkOwnwer = view.findViewById(R.id.BunkOwner);
        TextView goneOmne = view.findViewById(R.id.goneOmne);
        TextView goneTwo = view.findViewById(R.id.goneTwo);

        // Set values based on user type
        if (rest_List.get(position).getUserType().equals("User")) {
            Name.setText(rest_List.get(position).getU_name());
            subject.setText(rest_List.get(position).getSubject());
            description.setText(rest_List.get(position).getDescription());
            Slote.setText(rest_List.get(position).getSlot_number());

            OwnerNumbe.setVisibility(View.GONE);
            BunkOwnwer.setVisibility(View.GONE);
            goneOmne.setVisibility(View.GONE);
            goneTwo.setVisibility(View.GONE);
        } else if (rest_List.get(position).getUserType().equals("Admin")) {
            Name.setText(rest_List.get(position).getU_name());
            subject.setText(rest_List.get(position).getSubject());
            description.setText(rest_List.get(position).getDescription());
            Slote.setText(rest_List.get(position).getSlot_number());
            OwnerNumbe.setText(rest_List.get(position).getB_phone());
            BunkOwnwer.setText(rest_List.get(position).getB_name());

            OwnerNumbe.setVisibility(View.VISIBLE);
            BunkOwnwer.setVisibility(View.VISIBLE);
            goneOmne.setVisibility(View.VISIBLE);
            goneTwo.setVisibility(View.VISIBLE);
        }

        // Set rating as stars based on the value from the list
        float ratingValue = Float.parseFloat(rest_List.get(position).getRating());
        ratingBar.setRating(ratingValue);  // Set rating dynamically

        return view;
    }
}
