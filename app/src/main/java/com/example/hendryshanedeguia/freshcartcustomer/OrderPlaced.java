package com.example.hendryshanedeguia.freshcartcustomer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class OrderPlaced extends AppCompatActivity {
    Button btnOkay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_placed);
        btnOkay = (Button) findViewById(R.id.btnOkay);

       btnOkay.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent thisIntent = getIntent();
               String user = thisIntent.getStringExtra("user");

               Intent orderPlaced = new Intent(getApplicationContext(),MainActivity.class);
               orderPlaced.putExtra("user",user);
               startActivity(orderPlaced);
           }
       });
    }
}
