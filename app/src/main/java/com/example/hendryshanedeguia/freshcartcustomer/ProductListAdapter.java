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
 * Created by HendryShanedeGuia on 29/08/2017.
 */


public class ProductListAdapter extends ArrayAdapter<ProductInformation> {
    private Activity context;
    private int resource;
    private List<ProductInformation> listProducts;

    public ProductListAdapter(@NonNull Activity context, @LayoutRes int resource, @NonNull List<ProductInformation> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        listProducts = objects;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();


        View v = inflater.inflate(resource,null);
        TextView tvName = (TextView) v.findViewById(R.id.tvName);
        TextView tvDes = (TextView) v.findViewById(R.id.tvDescription);
        TextView tvPrice = (TextView) v.findViewById(R.id.tvPrice);
        TextView tvCurrency = (TextView) v.findViewById(R.id.tvCurrency);
        ImageView ivPic =  (ImageView) v.findViewById(R.id.ivPic);
        TextView tvImageURL = (TextView) v.findViewById(R.id.tvImageUrl);
        TextView tvID2 = (TextView) v.findViewById(R.id.tvID2);
        TextView tvCategory2 = (TextView) v.findViewById(R.id.tvCategory2);

        tvName.setText(listProducts.get(position).getProdName());
        tvDes.setText(listProducts.get(position).getProdDes());
        tvPrice.setText(listProducts.get(position).getProdPrice());
        tvID2.setText(listProducts.get(position).getProdID());
        tvCategory2.setText(listProducts.get(position).getProdCategory());
        tvCurrency.setText("₱");
        tvImageURL.setText(listProducts.get(position).getImageURL());

        Glide.with(context).load(listProducts.get(position).getImageURL()).into(ivPic);

        return v;





    }
}
