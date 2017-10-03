package com.example.hendryshanedeguia.freshcartcustomer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

public class CheckOut extends AppCompatActivity {
    TextView VAT,Promo,TotalBill,tvCartKey,Gross,Discount,Name;
    Button btnPlaceOrder;
    DatabaseReference allOrdersDBF;
    DatabaseReference pendingOrdersDBF;
    EditText etCOCashOnHand,etCOAddress,etCONote;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);
        NumberFormat formatter = new DecimalFormat("#0.00");
        pendingOrdersDBF = FirebaseDatabase.getInstance().getReference("Orders").child("Pending Orders");

        VAT =  (TextView) findViewById(R.id.COVAT);
        btnPlaceOrder = (Button) findViewById(R.id.btnPlaceOrder);
        tvCartKey = (TextView) findViewById(R.id.COCartKey);
        Name = (TextView) findViewById(R.id.COCustName);
        TotalBill = (TextView) findViewById(R.id.COTotalBill);
        Gross = (TextView) findViewById(R.id.COgrossPayment);
        Promo = (TextView) findViewById(R.id.COPromos);
        Discount = (TextView) findViewById(R.id.CODiscounts);
        etCOAddress = (EditText) findViewById(R.id.COClientAddress);
        etCOCashOnHand = (EditText) findViewById(R.id.COCashOnHand);
        etCONote = (EditText) findViewById(R.id.NoteForDriver);





        final Intent thisIntent = getIntent();
        if(thisIntent.hasExtra("cartKey")){
            String theKey = thisIntent.getStringExtra("cartKey");
            tvCartKey.setText(theKey);
        }
        if(thisIntent.hasExtra("currentBill")){
            String theBill = thisIntent.getStringExtra("currentBill");
            double Bill = Double.parseDouble(theBill);
            double vatBill = Bill *.12;
            VAT.setText(String.valueOf(formatter.format(vatBill)));
            Gross.setText(theBill);
        }
        Promo.setText("0.00");
        Discount.setText("0.00");
        allOrdersDBF = FirebaseDatabase.getInstance().getReference("Orders").child("All Orders").child(tvCartKey.getText().toString());

        allOrdersDBF.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String custName = dataSnapshot.child("custUsername").getValue().toString();
                Name.setText(custName);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




        double billAddedVAT = Double.parseDouble(Gross.getText().toString()) +  Double.parseDouble(VAT.getText().toString());
        TotalBill.setText(String.valueOf(formatter.format(billAddedVAT)));

        if(thisIntent.hasExtra("promoPile")){
            String promos = thisIntent.getStringExtra("promoPile");
        }
        btnPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (thisIntent.hasExtra("cartKey")) {
                    String theKey = thisIntent.getStringExtra("cartKey");
                    tvCartKey.setText(theKey);



                    Map updateRemainingDetails = new HashMap();
                    updateRemainingDetails.put("orderStatus", "Pending");
                    updateRemainingDetails.put("custAddress",etCOAddress.getText().toString());
                    updateRemainingDetails.put("cashOnHand",etCOCashOnHand.getText().toString());
                    updateRemainingDetails.put("noteForDriver",etCONote.getText().toString());
                    allOrdersDBF.updateChildren(updateRemainingDetails);
                    String pendingUID = pendingOrdersDBF.push().getKey();
                    Map addedData = new HashMap();
                    addedData.put("orderID",theKey);
                    pendingOrdersDBF.child(pendingUID).setValue(addedData);
                }
            }
        });
    }
}

