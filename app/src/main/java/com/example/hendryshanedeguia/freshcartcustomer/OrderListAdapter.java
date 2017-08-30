package com.example.hendryshanedeguia.freshcartcustomer;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.widget.ArrayAdapter;

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

}
