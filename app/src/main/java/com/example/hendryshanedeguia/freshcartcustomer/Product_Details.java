package com.example.hendryshanedeguia.freshcartcustomer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

public class Product_Details extends AppCompatActivity {


    public DatabaseReference tempDBF;
    public DatabaseReference IndividualTempDBF;
    public DatabaseReference customerDBF;


    private ProductListAdapter adapter;
    private StorageReference mStoraRef;
    TextView tvName,tvDes,tvPrice,tvCurrency,tvProdID,tvProdCategory,tvCartKeyProdDetails,tvTotalBill;
    ImageView ivPic;
    Button btnIncrease,btnDecrease,btnAdd,btnContinue;
    EditText etQuantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product__details);
        IndividualTempDBF = FirebaseDatabase.getInstance().getReference("Orders");
        tempDBF =  FirebaseDatabase.getInstance().getReference("Orders");
        Intent thisIntent = getIntent();
        String user = thisIntent.getStringExtra("user");
        customerDBF = FirebaseDatabase.getInstance().getReference().child("Customers").child(user);


         tvName = (TextView) findViewById(R.id.tvName);
         tvDes = (TextView) findViewById(R.id.tvDescription);
         tvPrice = (TextView) findViewById(R.id.tvPriceDetail);
         tvCurrency = (TextView) findViewById(R.id.tvCurrency);
         ivPic =  (ImageView) findViewById(R.id.ivPic);
         tvProdID = (TextView) findViewById(R.id.tvProdID);
         tvTotalBill = (TextView) findViewById(R.id.tvTotalBill);

         tvProdCategory = (TextView) findViewById(R.id.tvProdCategory);
         btnIncrease = (Button)findViewById(R.id.btnIncrease);
         btnDecrease = (Button)findViewById(R.id.btnDecrease);
         btnAdd = (Button)findViewById(R.id.btnAddToCart);
         btnContinue = (Button)findViewById(R.id.btnContinue);
         etQuantity = (EditText)findViewById(R.id.etQuantity);
         tvCartKeyProdDetails = (TextView) findViewById(R.id.tvCartKeyProdDetials);
         etQuantity.setText("0");


        Intent intent = getIntent();
        if(intent.hasExtra("cartKey")){
            String cartKey = intent.getStringExtra("cartKey");
            tvCartKeyProdDetails.setText(cartKey);
        }
        if(intent.hasExtra("currentBill")){
            String totalBill = intent.getStringExtra("currentBill");
            tvTotalBill.setText(totalBill);
        }


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
                String currentCartKey = tvCartKeyProdDetails.getText().toString();
               final String currentTotalBill = tvTotalBill.getText().toString();
                final NumberFormat formatter = new DecimalFormat("#0.00");
                //xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                if(TextUtils.equals(currentCartKey,"default")){
                    String uploadID = IndividualTempDBF.push().getKey();
                    tvCartKeyProdDetails.setText(uploadID);
                    double price = Double.parseDouble(tvPrice.getText().toString());
                    double quantity = Double.parseDouble(etQuantity.getText().toString());
                    double totalPrice = price * quantity;
                    String totalPriceConverted = String.valueOf(totalPrice);



                    if(!TextUtils.equals(currentTotalBill,"default")){
                        double totalBill = Double.parseDouble(tvTotalBill.getText().toString());
                        double newValue = totalPrice + totalBill;
                        tvTotalBill.setText(String.valueOf(formatter.format(newValue)));
                    }


                    if(TextUtils.equals(currentTotalBill,"default")){
                        double newValue = totalPrice;
                        tvTotalBill.setText(String.valueOf(formatter.format(newValue)));
                    }

                    //Store
                    OrderInformation oi = new OrderInformation(uploadID,tvTotalBill.getText().toString(),"null","null","null","Shane","null","null","null","0.00","0.00","0.00","null");
                    IndividualOrderInfo ioi = new IndividualOrderInfo(etQuantity.getText().toString(),prodName,prodPrice,uploadID,imageURL,totalPriceConverted);
                    tempDBF.child("All Orders").child(uploadID).setValue(oi);
                    IndividualTempDBF.child("All Orders").child(uploadID).child("order(s)").child(uploadID).setValue(ioi);
                    //removed the return;
                }



                //xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                String currentBill = tvTotalBill.getText().toString();
                if(!TextUtils.equals(currentCartKey,"default")){
                    String uploadID = IndividualTempDBF.push().getKey();
                    Double price = Double.parseDouble(tvPrice.getText().toString());
                    Double quantity = Double.parseDouble(etQuantity.getText().toString());

                    final Double totalPrice = price * quantity;
                    final String totalPriceConverted = String.valueOf(totalPrice);
                    //xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                    if(!TextUtils.equals(currentTotalBill,"default")){
                        double totalBill = Double.parseDouble(currentBill);
                        double newValue = totalPrice + totalBill;
                        tvTotalBill.setText(String.valueOf(formatter.format(newValue)));
                        Map currentCart = new HashMap();
                        currentCart.put("orderBill",tvTotalBill.getText().toString());
                        tempDBF.child("All Orders").child(tvCartKeyProdDetails.getText().toString()).updateChildren(currentCart);

                    }
                    //xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                    IndividualOrderInfo ioi = new IndividualOrderInfo(etQuantity.getText().toString(),prodName,prodPrice,uploadID,imageURL,totalPriceConverted);
                    IndividualTempDBF.child("All Orders").child(currentCartKey).child("order(s)").child(uploadID).setValue(ioi);


                }



                Intent nextPhase = new Intent(getApplicationContext(),MainActivity.class);
                Intent thisIntent = getIntent();
                String user = thisIntent.getStringExtra("user");
                //nextPhase.putExtra("currentBill",currentBill);
                nextPhase.putExtra("user",user);
                nextPhase.putExtra("cartKey",tvCartKeyProdDetails.getText(). toString());
                nextPhase.putExtra("currentBill",tvTotalBill.getText().toString());
                startActivity(nextPhase);
            }
        });


        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String currentBill = tvTotalBill.getText().toString();
                if(TextUtils.equals(tvCartKeyProdDetails.getText().toString(),"default")){
                    return;
                }
              else{
                    Intent nextPhase = new Intent(getApplicationContext(),MainActivity.class);
                   if(!TextUtils.equals(currentBill,"default")){
                        nextPhase.putExtra("currentBill",currentBill);
                    }
                    Intent thisIntent = getIntent();
                    String user = thisIntent.getStringExtra("user");
                    nextPhase.putExtra("user",user);
                    nextPhase.putExtra("cartKey",tvCartKeyProdDetails.getText().toString());
                    startActivity(nextPhase);
                }
            }
        });
    }
}
