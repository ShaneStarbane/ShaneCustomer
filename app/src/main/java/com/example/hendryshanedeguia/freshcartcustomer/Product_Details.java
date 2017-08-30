package com.example.hendryshanedeguia.freshcartcustomer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;

public class Product_Details extends AppCompatActivity {

    public DatabaseReference mDatabaseRef;
    private ProductListAdapter adapter;
    private StorageReference mStoraRef;
    TextView tvName,tvDes,tvPrice,tvCurrency,tvProdID,tvProdCategory;
    ImageView ivPic;
    Button btnIncrease,btnDecrease,btnAdd,btnContinue;
    EditText etQuantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product__details);
         mDatabaseRef = FirebaseDatabase.getInstance().getReference("Orders");

         tvName = (TextView) findViewById(R.id.tvName);
         tvDes = (TextView) findViewById(R.id.tvDescription);
         tvPrice = (TextView) findViewById(R.id.tvPriceDetail);
         tvCurrency = (TextView) findViewById(R.id.tvCurrency);
         ivPic =  (ImageView) findViewById(R.id.ivPic);
         tvProdID = (TextView) findViewById(R.id.tvProdID);
         tvProdCategory = (TextView) findViewById(R.id.tvProdCategory);
         btnIncrease = (Button)findViewById(R.id.btnIncrease);
         btnDecrease = (Button)findViewById(R.id.btnDecrease);
        btnAdd = (Button)findViewById(R.id.btnAddToCart);
        btnContinue = (Button)findViewById(R.id.btnContinue);
         etQuantity = (EditText)findViewById(R.id.etQuantity);
         etQuantity.setText("0");

        Intent intent = getIntent();


        final String prodName = intent.getStringExtra("prodName");
        String prodDes = intent.getStringExtra("prodDes");
        final String prodPrice = intent.getStringExtra("prodPrice");
        String prodCurrency = intent.getStringExtra("prodCurrency");
        final String imageURL = intent.getStringExtra("imageURL");


        tvName.setText(prodName);
        tvDes.setText(prodDes);
        tvPrice.setText(prodPrice);
        tvCurrency.setText(prodCurrency);
        Glide.with(getApplicationContext()).load(imageURL).into(ivPic);
       // Toast.makeText(getApplicationContext(),imageURL,Toast.LENGTH_LONG).show();
       // tvName.setText(prodName);
        //tvName.setText(prodName);

        btnIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int quantity = Integer.parseInt(etQuantity.getText().toString());
                quantity = quantity + 1;
                etQuantity.setText(String.valueOf(quantity));
            }
        });
        btnDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int quantity = Integer.parseInt(etQuantity.getText().toString());
               if(quantity == 0) {
                   return;
               }
               quantity = quantity - 1;
                etQuantity.setText(String.valueOf(quantity));
            }

        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uploadID = mDatabaseRef.push().getKey();
                tvCurrency.setText(uploadID);
                 OrderInformation oi = new OrderInformation("1st Order Try "+uploadID,"50","09222188322","Secret","wwww.google.com","Shane the Virgin");
                //IndividualOrderInfo ioi = new IndividualOrderInfo(etQuantity.getText().toString(),prodName,prodPrice);
                    mDatabaseRef.child(uploadID).setValue(oi);

            }
        });
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String uploadID = mDatabaseRef.push().getKey();
                IndividualOrderInfo ioi = new IndividualOrderInfo(etQuantity.getText().toString(),prodName,prodPrice,uploadID,imageURL);
                mDatabaseRef.child(tvCurrency.getText().toString()).child("order(s)").setValue(ioi);
            }
        });

    }
}
