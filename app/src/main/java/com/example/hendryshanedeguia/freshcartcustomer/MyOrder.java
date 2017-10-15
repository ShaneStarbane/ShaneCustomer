package com.example.hendryshanedeguia.freshcartcustomer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyOrder extends AppCompatActivity {
    public DatabaseReference mDatabaseRef,pendingDBF,cancelledDBF,customersDBF,pendingIDDBF,OrdersDBF;
    private List<IndividualOrderInfo> orderList;
    public ListView lv;
    private ItineraryListAdapter adapter;
    Button btnCancel,btnTrackOrder;
    TextView tvMOGross,tvMOVAT,tvMOPromo,tvMOTotalBill,tvMOBill,tvMOChange,tvMODiscount,tvMOOrderID,thePendingTV,tvCancels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);
        lv = (ListView) findViewById(R.id.lvMO);
        orderList =new ArrayList<>();
        Intent thisIntent = getIntent();
        String orderID = thisIntent.getStringExtra("orderID");
        String user = thisIntent.getStringExtra("user");
        String orderGross = thisIntent.getStringExtra("orderGross");
        String orderVAT = thisIntent.getStringExtra("orderVAT");
        String orderPromo = thisIntent.getStringExtra("orderPromo");
        String orderBill = thisIntent.getStringExtra("orderBill");
        String orderDiscount = thisIntent.getStringExtra("orderDiscount");
        String cashOnHand = thisIntent.getStringExtra("cashOnHand");
        String change = thisIntent.getStringExtra("change");
        String orderStatus = thisIntent.getStringExtra("orderStatus");

        tvCancels = (TextView) findViewById(R.id.tvCancels);
        thePendingTV  = (TextView) findViewById(R.id.tvPendingID);
        tvMOGross = (TextView) findViewById(R.id.tvMOGross);
        tvMOVAT = (TextView) findViewById(R.id.tvMOVAT);
        tvMOPromo = (TextView) findViewById(R.id.tvMOPromo);
        tvMOTotalBill = (TextView) findViewById(R.id.tvMOTotalBill);
        tvMOBill = (TextView) findViewById(R.id.tvMOBill);
        tvMOChange = (TextView) findViewById(R.id.tvMOChange);
        tvMODiscount = (TextView) findViewById(R.id.tvMODiscount);
        tvMOOrderID = (TextView) findViewById(R.id.tvMOOrderID);
        btnCancel = (Button) findViewById(R.id.btnMOCancelOrder);
        btnTrackOrder = (Button) findViewById(R.id.btnTrackOrder);

        tvMOGross.setText(orderGross);
        tvMOVAT.setText(orderVAT);
        tvMOPromo.setText(orderPromo);
        tvMOTotalBill.setText(cashOnHand);
        tvMOBill.setText(orderBill);
        tvMOChange.setText(change);
        tvMODiscount.setText(orderDiscount);
        tvMOOrderID.setText(orderID);
        if(TextUtils.equals(orderStatus,"Cancelled")){
            btnCancel.setVisibility(View.INVISIBLE);
        }
        customersDBF = FirebaseDatabase.getInstance().getReference("Customers").child(user);
        pendingDBF = FirebaseDatabase.getInstance().getReference("Orders").child("Pending Orders");
        cancelledDBF = FirebaseDatabase.getInstance().getReference("Orders").child("Cancelled Orders");
        pendingIDDBF = FirebaseDatabase.getInstance().getReference("Orders").child("All Orders").child(orderID);
        OrdersDBF = FirebaseDatabase.getInstance().getReference("Orders").child("All Orders").child(orderID);
        pendingIDDBF.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String thePendingid  =  dataSnapshot.child("pendingID").getValue().toString();
                thePendingTV.setText(thePendingid);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Orders").child("All Orders").child(orderID).child("order(s)");
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
        customersDBF.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String cancels =  dataSnapshot.child("timesCancelled").getValue().toString();
                tvCancels.setText(cancels);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cancelledID = cancelledDBF.push().getKey();
                Map updateRemainingDetails = new HashMap();
                updateRemainingDetails.put("orderStatus", "Cancelled");
                updateRemainingDetails.put("pendingID","Cancelled");
                updateRemainingDetails.put("cancelID",cancelledID);
                OrdersDBF.updateChildren(updateRemainingDetails);
                pendingDBF.child(thePendingTV.getText().toString()).removeValue();
                Map addToCancelled = new HashMap();
                addToCancelled.put("orderID",tvMOOrderID.getText().toString());
                cancelledDBF.child(cancelledID).setValue(addToCancelled);
                String currentTimesCancelled = tvCancels.getText().toString();
                int currentTimesCancelledConverted = Integer.parseInt(currentTimesCancelled) + 1;
                Map tallyCancels = new HashMap();
                tallyCancels.put("timesCancelled",String.valueOf(currentTimesCancelledConverted));
                customersDBF.updateChildren(tallyCancels);
            }
        });
        btnTrackOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent thisIntent =  getIntent();
                String orderGross = thisIntent.getStringExtra("orderGross");
                String orderVAT = thisIntent.getStringExtra("orderVAT");
                String orderPromo = thisIntent.getStringExtra("promo");
                String orderBill = thisIntent.getStringExtra("orderBill");
                String orderDiscount = thisIntent.getStringExtra("discount");
                String cashOnHand =thisIntent.getStringExtra("cashOnHand");
                String change = "0.00";
                String driverID = thisIntent.getStringExtra("driverID");
                String user = thisIntent.getStringExtra("user");
                String orderID = thisIntent.getStringExtra("orderID");
                String orderStatus = thisIntent.getStringExtra("orderStatus");


                Intent x = new Intent(getApplicationContext(),OrderLocation.class);
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
        });

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            Intent thisIntent = getIntent();
            String user = thisIntent.getStringExtra("user");
            String cartKey = thisIntent.getStringExtra("cartKey");
            Intent back = new Intent(getApplicationContext(),MyOrdersList.class);
            back.putExtra("user",user);
            back.putExtra("cartKey",cartKey);

            startActivity(back);
        }
        return super.onKeyDown(keyCode, event);
    }
}
