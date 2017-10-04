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
import android.widget.Toast;

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
                                       Toast.makeText(getApplicationContext(),theID,Toast.LENGTH_SHORT).show();
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
            String orderID = tvOrderID.getText().toString();

            Intent x = new Intent(getApplicationContext(),MyOrder.class);
            x.putExtra("user",user);
            x.putExtra("orderID",orderID);
            startActivity(x);
        }
    });
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){

            Intent thisIntent = getIntent();
            String user = thisIntent.getStringExtra("user");
            Intent back = new Intent(getApplicationContext(),MainActivity.class);
            back.putExtra("user",user);
            startActivity(back);
        }
        return super.onKeyDown(keyCode, event);
    }
}
