package com.example.hendryshanedeguia.freshcartcustomer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyOrder extends AppCompatActivity {
    public DatabaseReference mDatabaseRef,pendingDBF,cancelledDBF;
    private List<IndividualOrderInfo> orderList;
    public ListView lv;
    private ItineraryListAdapter adapter;
    Button btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);
        lv = (ListView) findViewById(R.id.lvMO);
        orderList =new ArrayList<>();
        Intent thisIntent = getIntent();
        String orderID = thisIntent.getStringExtra("orderID");
        String user = thisIntent.getStringExtra("user");

        btnCancel = (Button) findViewById(R.id.btnMOCancelOrder);
        pendingDBF = FirebaseDatabase.getInstance().getReference("Orders").child("Pending Orders");
        cancelledDBF = FirebaseDatabase.getInstance().getReference("Orders").child("Cancelled Orders");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Orders").child("All Orders").child(orderID).child("order(s)");
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cancelledID = cancelledDBF.push().getKey();
                Map updateRemainingDetails = new HashMap();
                updateRemainingDetails.put("orderStatus", "Cancelled");
                updateRemainingDetails.put("pendingID","null");
                updateRemainingDetails.put("cancelID",cancelledID);
                mDatabaseRef.updateChildren(updateRemainingDetails);



            }
        });
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot snapshot: dataSnapshot.getChildren()){

                    IndividualOrderInfo ioi = snapshot.getValue(IndividualOrderInfo.class);
                    orderList.add(ioi);
                }

                adapter = new ItineraryListAdapter(MyOrder.this,R.layout.order_list_custom_layout,orderList);
                lv.setAdapter(adapter);
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){

            Intent thisIntent = getIntent();
            String user = thisIntent.getStringExtra("user");
            Intent back = new Intent(getApplicationContext(),MyOrdersList.class);
            back.putExtra("user",user);
            startActivity(back);
        }
        return super.onKeyDown(keyCode, event);
    }
}
