package com.example.electricox.ADAPTERS;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.electricox.COMMON.RequestPojo;
import com.example.electricox.R;

import java.util.List;

public class UserADapter extends ArrayAdapter<RequestPojo> {

    private Activity context;
    private List<RequestPojo> rest_List;

    public UserADapter(Activity context, List<RequestPojo> rest_List)
    {
        super(context, R.layout.activity_user_adapter, rest_List);
        this.context = context;
        this.rest_List = rest_List;
    }


    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_user_adapter, parent, false);

        TextView user_name = view.findViewById(R.id.userName);
        TextView address = view.findViewById(R.id.address);
        TextView mail = view.findViewById(R.id.mail);
        TextView phoneNumber = view.findViewById(R.id.number);
        TextView status = view.findViewById(R.id.status);



        user_name.setText(rest_List.get(position).getU_name());
        address.setText("Address : "+rest_List.get(position).getU_address());
        mail.setText("Mail :"+rest_List.get(position).getU_email());
        phoneNumber.setText("Mobile :"+rest_List.get(position).getU_phone());


        if (rest_List.get(position).getStatus().equals("1"))
        {
            status.setText("✅"+"Active");
        }
        else
        {
            status.setText("🚫"+"Blocked");
        }

        return view;
    }
}