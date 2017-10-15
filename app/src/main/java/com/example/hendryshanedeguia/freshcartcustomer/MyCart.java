package com.example.hendryshanedeguia.freshcartcustomer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
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

public class MyCart extends AppCompatActivity {
    DatabaseReference mDatabaseReference,billDBF;
    ListView lvOrders;
    TextView tvCartKey,tvNothingMessage,tvSubTotalLabel;
    Button btnCheckOut;
    ProgressDialog progressDialog;
    private List<IndividualOrderInfo> orderList;
    private OrderListAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_cart);
        final Intent intent = getIntent();

        orderList =  new ArrayList<>();
        tvCartKey = (TextView) findViewById(R.id.tvCartKey);
        tvSubTotalLabel = (TextView) findViewById(R.id.tvSubTotalLabel);
        lvOrders = (ListView) findViewById(R.id.lvOrders);
        btnCheckOut = (Button) findViewById(R.id.btnCheckOut);
        tvNothingMessage = (TextView) findViewById(R.id.tvNothingMessage);
            final String cartKey = intent.getStringExtra("cartKey");
            tvCartKey.setText(cartKey);

        billDBF = FirebaseDatabase.getInstance().getReference("Orders").child("All Orders").child(cartKey);
        billDBF.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String theBill = dataSnapshot.child("orderGross").getValue().toString();
                tvSubTotalLabel.setText(theBill);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




            final String currentCartKey = tvCartKey.getText().toString();
            if (!TextUtils.equals(currentCartKey, "default")) {
                tvNothingMessage.setText("");
              mDatabaseReference = FirebaseDatabase.getInstance().getReference("Orders").child("All Orders").child(cartKey).child("order(s)");



              mDatabaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            IndividualOrderInfo ioi = snapshot.getValue(IndividualOrderInfo.class);
                            orderList.add(ioi);
                        }

                        adapter = new OrderListAdapter(MyCart.this, R.layout.order_items, orderList);
                        lvOrders.setAdapter(adapter);
                    }


                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                btnCheckOut.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(!TextUtils.equals(tvSubTotalLabel.getText().toString(),"default")) {

                            Intent thisIntent = getIntent();
                            String user = thisIntent.getStringExtra("user");

                            String cartKey = intent.getStringExtra("cartKey");
                            Intent checkOutPhase = new Intent(getApplicationContext(), CheckOut.class);
                            checkOutPhase.putExtra("cartKey", cartKey);
                            checkOutPhase.putExtra("user", user);
                            if (intent.hasExtra("currentBill")) {
                                String theBill = intent.getStringExtra("currentBill");
                                checkOutPhase.putExtra("currentBill", theBill);
                            }
                            startActivity(checkOutPhase);
                        }else {
                            Toast.makeText(getApplicationContext(), "No orders to check out.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }

//Change1
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){

                Intent thisIntent = getIntent();
                String user = thisIntent.getStringExtra("user");
                String cartKey = tvCartKey.getText().toString();
                Intent back = new Intent(getApplicationContext(),MainActivity.class);
                back.putExtra("user",user);
                back.putExtra("cartKey",cartKey);

            startActivity(back);
        }
        return super.onKeyDown(keyCode, event);
    }
}

