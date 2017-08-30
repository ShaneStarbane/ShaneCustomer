package com.example.hendryshanedeguia.freshcartcustomer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class MyCart extends AppCompatActivity {
    DatabaseReference mDatabaseReference;
    ListView lvOrders;
    TextView tvCartKey;
    Button btnCheckOut;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_cart);
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("Orders").child("");

        lvOrders = (ListView) findViewById(R.id.lvOrders);
        tvCartKey = (TextView) findViewById(R.id.tvCartKey);
        btnCheckOut = (Button) findViewById(R.id.btnCheckOut);






    }
}
