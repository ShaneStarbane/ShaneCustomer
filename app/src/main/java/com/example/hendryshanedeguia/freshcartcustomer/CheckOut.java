package com.example.hendryshanedeguia.freshcartcustomer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
    DatabaseReference customersDBF;
    DatabaseReference billDBF;
    EditText etCOCashOnHand,etCOAddress,etCONote;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);
        final Intent thisIntent = getIntent();
        final NumberFormat formatter = new DecimalFormat("#0.00");
        String user = thisIntent.getStringExtra("user");
        pendingOrdersDBF = FirebaseDatabase.getInstance().getReference("Orders").child("Pending Orders");
        customersDBF = FirebaseDatabase.getInstance().getReference("Customers").child(user);


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





            String theKey = thisIntent.getStringExtra("cartKey");
            tvCartKey.setText(theKey);


            billDBF = FirebaseDatabase.getInstance().getReference("Orders").child("All Orders").child(theKey);
            billDBF.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String theBill = dataSnapshot.child("orderGross").getValue().toString();
                    String theVAT = dataSnapshot.child("orderVAT").getValue().toString();
                    VAT.setText(theVAT);
                    Gross.setText(theBill);
                    double gross = Double.parseDouble(theBill);
                    double vat  = Double.parseDouble(theVAT);
                    double billAddedVAT = gross+vat;
                    TotalBill.setText(String.valueOf(formatter.format(billAddedVAT)));

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });





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






        if(thisIntent.hasExtra("promoPile")){
            String promos = thisIntent.getStringExtra("promoPile");
        }
        btnPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (thisIntent.hasExtra("cartKey")) {
                    String theKey = thisIntent.getStringExtra("cartKey");
                    tvCartKey.setText(theKey);
                    String user = thisIntent.getStringExtra("user");
                    customersDBF.child(user);
                    Toast.makeText(getApplicationContext(),user+"",Toast.LENGTH_SHORT).show();



                    Map updateRemainingDetails = new HashMap();
                    String pendingUID = pendingOrdersDBF.push().getKey();
                    updateRemainingDetails.put("orderStatus", "Pending");
                    updateRemainingDetails.put("custAddress",etCOAddress.getText().toString());
                    updateRemainingDetails.put("cashOnHand",etCOCashOnHand.getText().toString());
                    updateRemainingDetails.put("noteForDriver",etCONote.getText().toString());
                    updateRemainingDetails.put("orderGross",Gross.getText().toString());
                    updateRemainingDetails.put("orderBill",TotalBill.getText().toString());
                    updateRemainingDetails.put("pendingID",pendingUID);

                    allOrdersDBF.updateChildren(updateRemainingDetails);

                    Map addedData = new HashMap();
                    addedData.put("orderID",theKey);
                    pendingOrdersDBF.child(pendingUID).setValue(addedData);

                    Map addToHistory = new HashMap();
                    addToHistory.put("orderID",theKey);
                    String uID = customersDBF.push().getKey();
                    customersDBF.child("order(s)").child(uID).setValue(addToHistory);

                    Intent orderPlaced = new Intent(getApplicationContext(),OrderPlaced.class);
                    orderPlaced.putExtra("user",user);
                    startActivity(orderPlaced);

                }

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

