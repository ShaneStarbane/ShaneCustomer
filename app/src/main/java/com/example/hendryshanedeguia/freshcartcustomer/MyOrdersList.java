package com.example.hendryshanedeguia.freshcartcustomer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MyOrdersList extends AppCompatActivity {
    public DatabaseReference mDatabaseRef,userOrders;
    private List<OrderInformation> orderList;
    public ListView lv;
    private OrderHistoryAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders_list);
        lv = (ListView) findViewById(R.id.lvMOLlv);
        orderList =new ArrayList<>();
        Intent thisIntent = getIntent();
        final String user = thisIntent.getStringExtra("user");

        userOrders = FirebaseDatabase.getInstance().getReference("Customers").child(user).child("order(s)");

        userOrders.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                       final String theID = snapshot.child("orderID").getValue().toString();
                    mDatabaseRef = FirebaseDatabase.getInstance().getReference("Orders").child("All Orders");
                    mDatabaseRef.addValueEventListener(new ValueEventListener() {
                           @Override
                           public void onDataChange(DataSnapshot dataSnapshot) {
                               for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                   OrderInformation oi = snapshot.getValue(OrderInformation.class);
                                   if(TextUtils.equals(snapshot.child("orderID").getValue().toString(),theID)){

                                       orderList.add(oi);
                                   }

                               }
                               adapter = new OrderHistoryAdapter(MyOrdersList.this, R.layout.my_orders_list_pre, orderList);
                               lv.setAdapter(adapter);

                           }
                           @Override
                           public void onCancelled(DatabaseError databaseError) {

                           }
                       });
                   }

                }



            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            TextView tvOrderID = view.findViewById(R.id.tvMOPreOrderID);
            TextView tvOrderStatus = view.findViewById(R.id.tvMOPreOrderStatus);

            final String orderID = tvOrderID.getText().toString();
            final String orderStatus = tvOrderStatus.getText().toString();
            mDatabaseRef.child(orderID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String orderGross = dataSnapshot.child("orderGross").getValue().toString();
                    String orderVAT = dataSnapshot.child("orderVAT").getValue().toString();
                    String orderPromo = dataSnapshot.child("promo").getValue().toString();
                    String orderBill = dataSnapshot.child("orderBill").getValue().toString();
                    String orderDiscount = dataSnapshot.child("discount").getValue().toString();
                    String cashOnHand = dataSnapshot.child("cashOnHand").getValue().toString();
                    String change = "0.00";
                    String driverID = dataSnapshot.child("orderDriverID").getValue().toString();
                    Intent x = new Intent(getApplicationContext(),MyOrder.class);
                    x.putExtra("user",user);
                    x.putExtra("orderGross",orderGross);
                    x.putExtra("orderVAT",orderVAT);
                    x.putExtra("orderPromo",orderPromo);
                    x.putExtra("orderBill",orderBill);
                    x.putExtra("orderDiscount",orderDiscount);
                    x.putExtra("cashOnHand",cashOnHand);
                    x.putExtra("orderID",orderID);
                    x.putExtra("change",change);
                    x.putExtra("orderStatus",orderStatus);
                    x.putExtra("driverID",driverID);

                    startActivity(x);






                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });






        }
    });
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){

            Intent thisIntent = getIntent();
            String user = thisIntent.getStringExtra("user");
            String cartKey = thisIntent.getStringExtra("cartKey");
            Intent back = new Intent(getApplicationContext(),MainActivity.class);
            back.putExtra("user",user);
            back.putExtra("cartKey",cartKey);
            startActivity(back);
        }
        return super.onKeyDown(keyCode, event);
    }
}
