package com.example.hendryshanedeguia.freshcartcustomer;

import android.app.Activity;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by HendryShanedeGuia on 03/10/2017.
 */

public class OrderHistoryAdapter extends ArrayAdapter<OrderInformation> {
    private Activity context;
    private int resource;
    private List<OrderInformation> listOrders;
    public OrderHistoryAdapter(@NonNull Activity context, @LayoutRes int resource, @NonNull List<OrderInformation> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        listOrders = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();


        View v = inflater.inflate(resource,parent, false);
        TextView tvOrderID = (TextView) v.findViewById(R.id.tvMOPreOrderID);
        TextView tvOrderBill = (TextView) v.findViewById(R.id.tvMOPreBill);
        TextView tvOrderStatus = (TextView) v.findViewById(R.id.tvMOPreOrderStatus);

        tvOrderID.setText(listOrders.get(position).getOrderID());
        tvOrderBill.setText(listOrders.get(position).getOrderBill());
        tvOrderStatus.setText(listOrders.get(position).getOrderStatus());



        return v;

    }
}
