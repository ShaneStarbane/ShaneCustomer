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
    DatabaseReference mDatabaseReference;
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

        if(intent.hasExtra("cartKey")) {
            final String cartKey = intent.getStringExtra("cartKey");
            tvCartKey.setText(cartKey);
            if(intent.hasExtra("currentBill")){
                String theBill = intent.getStringExtra("currentBill");
                tvSubTotalLabel.setText(theBill);
            }


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

                            Toast.makeText(getApplicationContext(), "No orders to check out.", Toast.LENGTH_SHORT).show();

                    }
                });
            }
        }
    }
//Change1
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        String currentBill= tvSubTotalLabel.getText().toString();
        if(keyCode == KeyEvent.KEYCODE_BACK){
            if(!TextUtils.equals(currentBill,"default")){
                Intent back = new Intent(MyCart.this,MainActivity.class);
                back.putExtra("currentBill",currentBill);
                back.putExtra("cartKey",tvCartKey.getText().toString());
                startActivity(back);
            }

        }
        return super.onKeyDown(keyCode, event);
    }
}

