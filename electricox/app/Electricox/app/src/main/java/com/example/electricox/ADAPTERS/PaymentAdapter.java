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

public class PaymentAdapter extends ArrayAdapter<RequestPojo> {

    Activity context;
    List<RequestPojo> rest_List;
    public PaymentAdapter(Activity context, List<RequestPojo> rest_List) {
        super(context, R.layout.activity_payment_adapter, rest_List);
        this.context = context;
        this.rest_List = rest_List;
    }


    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_payment_adapter, parent, false);

        TextView NAME = view.findViewById(R.id.accountHolderName);
        TextView PRICE = view.findViewById(R.id.PRICE);

        TextView ACC_NUMBER = view.findViewById(R.id.accountNumber);
        TextView DATE = view.findViewById(R.id.payment_date);

        NAME.setText(rest_List.get(position).getU_name());
        PRICE.setText(rest_List.get(position).getPaymentPrice()+"/-");
        ACC_NUMBER.setText(rest_List.get(position).getPayment_id());
        DATE.setText(rest_List.get(position).getPaymentDate());



        return view;
    }
}