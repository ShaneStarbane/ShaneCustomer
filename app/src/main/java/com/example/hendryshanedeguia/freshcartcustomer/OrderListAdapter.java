package com.example.hendryshanedeguia.freshcartcustomer;

import android.app.Activity;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by HendryShanedeGuia on 30/08/2017.
 */

public class OrderListAdapter extends ArrayAdapter<IndividualOrderInfo> {
    private Activity context;
    private int resource;
    private List<IndividualOrderInfo> listOrders;

    public OrderListAdapter(@NonNull Activity context, @LayoutRes int resource, @NonNull List<IndividualOrderInfo> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        listOrders = objects;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();


        View v = inflater.inflate(resource,parent,false);
        TextView tvName = (TextView) v.findViewById(R.id.tvOrderProdName);
        TextView tvPrice = (TextView) v.findViewById(R.id.tvOrderProdPrice);
        TextView tvQuantity = (TextView) v.findViewById(R.id.tvOrderProdQuantity);
        ImageView ivPic =  (ImageView) v.findViewById(R.id.ivOrderProdImage);
        TextView tvImageURL = (TextView) v.findViewById(R.id.tvOrderProdImageURL);
        TextView tvCurrency = (TextView)v.findViewById(R.id.tvOrderProdCurrency);
        TextView tvID = (TextView)v.findViewById(R.id.tvOrderProdID);

        TextView tvTotalPriceLabel =  (TextView)v.findViewById(R.id.tvTotalPriceLabel);
        TextView tvTotalPrice =  (TextView)v.findViewById(R.id.tvTotalPrice);



        tvID.setText(listOrders.get(position).getOrderItemID());
        tvName.setText(listOrders.get(position).getProdName());
        tvPrice.setText(listOrders.get(position).getProdPrice());
        tvQuantity.setText(listOrders.get(position).getOrderQuantity());

        tvCurrency.setText("₱" + listOrders.get(position).getProdPrice());
        tvImageURL.setText(listOrders.get(position).getProdImageURL());
        Glide.with(context).load(listOrders.get(position).getProdImageURL()).into(ivPic);
        tvTotalPrice.setText(listOrders.get(position).getSubTotal());
        tvTotalPriceLabel.setText("₱"+listOrders.get(position).getSubTotal());


        return v;





    }

}
