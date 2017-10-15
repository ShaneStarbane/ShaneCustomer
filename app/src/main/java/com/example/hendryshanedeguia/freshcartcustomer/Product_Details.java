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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
    public  DatabaseReference billDBF;
    public  DatabaseReference numberOfOrders;


    private ProductListAdapter adapter;
    private StorageReference mStoraRef;
    TextView tvName,tvDes,tvPrice,tvCurrency,tvProdID,tvProdCategory,tvCartKeyProdDetails,tvTotalBill,tvCurrentNumberOfItems;
    ImageView ivPic;
    Button btnIncrease,btnDecrease,btnAdd,btnContinue;
    EditText etQuantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product__details);
        IndividualTempDBF = FirebaseDatabase.getInstance().getReference("Orders");

        Intent thisIntent = getIntent();
        String user = thisIntent.getStringExtra("user");
        String category = thisIntent.getStringExtra("prodCategory");
        String prodID = thisIntent.getStringExtra("prodID");
        tempDBF =  FirebaseDatabase.getInstance().getReference("Orders");
        customerDBF = FirebaseDatabase.getInstance().getReference().child("Customers").child(user);
        final Intent intent = getIntent();

        tvCurrentNumberOfItems = (TextView) findViewById(R.id.tvCurrentNumberOfItems);
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
        if(intent.hasExtra("cartKey")){
            String cartKey = intent.getStringExtra("cartKey");
            tvCartKeyProdDetails.setText(cartKey);

            billDBF =  FirebaseDatabase.getInstance().getReference("Orders").child("All Orders").child(cartKey);
            billDBF.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String theBill  = dataSnapshot.child("orderGross").getValue().toString();
                    tvTotalBill.setText(theBill);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
        numberOfOrders = FirebaseDatabase.getInstance().getReference("Orders").child("All Orders").child(tvCartKeyProdDetails.getText().toString());
        numberOfOrders.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String numOfItems = dataSnapshot.child("numberOfItems").getValue().toString();
                tvCurrentNumberOfItems.setText(numOfItems);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

         etQuantity.setText("0");

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
                Intent thisIntent = getIntent();
                String user = thisIntent.getStringExtra("user");
                String category = thisIntent.getStringExtra("prodCategory");
                String prodID = thisIntent.getStringExtra("prodID");

                FirebaseUser fbu = FirebaseAuth.getInstance().getCurrentUser();
                String userID =fbu.getUid();
                final String currentCartKey = tvCartKeyProdDetails.getText().toString();
                final NumberFormat formatter = new DecimalFormat("#0.00");
                //xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                if(TextUtils.equals(currentCartKey,"default")){

                    String uploadID = IndividualTempDBF.push().getKey();
                    tvCartKeyProdDetails.setText(uploadID);
                    double price = Double.parseDouble(tvPrice.getText().toString());
                    double quantity = Double.parseDouble(etQuantity.getText().toString());
                    double totalPrice = price * quantity;
                    String totalPriceConverted = String.valueOf(totalPrice);
                        double newValue = totalPrice;
                        tvTotalBill.setText(String.valueOf(formatter.format(newValue)));
                        double priceVAT = newValue* .12;

                    //Store
                    OrderInformation oi = new OrderInformation(uploadID,String.valueOf(newValue),"null","null","null","Shane","null","null","null","0.00","0.00",String.valueOf(formatter.format(priceVAT)),"null","null",userID,"null","1");
                    IndividualOrderInfo ioi = new IndividualOrderInfo(etQuantity.getText().toString(),prodName,prodPrice,uploadID,imageURL,totalPriceConverted,category,prodID);
                    tempDBF.child("All Orders").child(uploadID).setValue(oi);
                    IndividualTempDBF.child("All Orders").child(uploadID).child("order(s)").child(uploadID).setValue(ioi);
                }
                //xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                if(!TextUtils.equals(currentCartKey,"default")){
                    String uploadID = IndividualTempDBF.push().getKey();
                    Double price = Double.parseDouble(tvPrice.getText().toString());
                    Double quantity = Double.parseDouble(etQuantity.getText().toString());



                    final Double totalPrice = price * quantity;
                    final String totalPriceConverted = String.valueOf(totalPrice);

                        double totalBill = Double.parseDouble(tvTotalBill.getText().toString());
                        double newValue = totalPrice + totalBill;
                        double newVAT = newValue *.12;

                        double totalNumItems = Double.parseDouble(tvCurrentNumberOfItems.getText().toString());
                        double newValueItems = totalNumItems + 1;
                        String newValueString = String.valueOf(newValueItems);
                        tvTotalBill.setText(String.valueOf(formatter.format(newValue)));
                        Map currentCart = new HashMap();
                        currentCart.put("orderGross",tvTotalBill.getText().toString());
                        currentCart.put("orderVAT",String.valueOf(formatter.format(newVAT)));
                        currentCart.put("numberOfItems",newValueString);
                        billDBF.updateChildren(currentCart);

                    //xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                    IndividualOrderInfo ioi = new IndividualOrderInfo(etQuantity.getText().toString(),prodName,prodPrice,uploadID,imageURL,totalPriceConverted,category,prodID);

                    IndividualTempDBF.child("All Orders").child(currentCartKey).child("order(s)").child(uploadID).setValue(ioi);

                }


                Intent nextPhase = new Intent(getApplicationContext(),MyCart.class);
                nextPhase.putExtra("user",user);
                nextPhase.putExtra("cartKey",tvCartKeyProdDetails.getText(). toString());
                nextPhase.putExtra("prodCategory",category);
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
