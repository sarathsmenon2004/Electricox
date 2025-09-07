package com.example.electricox.ADAPTERS;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.electricox.COMMON.RequestPojo;
import com.example.electricox.R;

import java.util.List;

public class EvOwnersAdapter extends ArrayAdapter<RequestPojo> {

    private Activity context;
    private List<RequestPojo> rest_List;

    public EvOwnersAdapter(Activity context, List<RequestPojo> rest_List) {

        super(context, R.layout.activity_ev_owners_adapter, rest_List);
        this.context = context;
        this.rest_List = rest_List;
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_ev_owners_adapter, parent, false);

        TextView name = view.findViewById(R.id.SPName);
        TextView address = view.findViewById(R.id.SPaddress);
        TextView phoneNumber = view.findViewById(R.id.SPnumber);
        TextView email = view.findViewById(R.id.SPemail);
        TextView status = view.findViewById(R.id.SPstatus);

        name.setText(rest_List.get(position).getB_name());
        address.setText("Address : " + rest_List.get(position).getB_address());
        phoneNumber.setText("Mobile :" + rest_List.get(position).getB_phone());
        email.setText("Email :" + rest_List.get(position).getB_email());
        status.setText(" " + rest_List.get(position).getStatus());

         if(rest_List.get(position).getStatus().equals("1")) {
            status.setText("✅" + "Accepted");
        } else {
            status.setText("🚫" + "Blocked");
        }

        return view;

    }
}