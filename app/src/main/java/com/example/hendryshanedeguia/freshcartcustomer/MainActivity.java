package com.example.hendryshanedeguia.freshcartcustomer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.google.firebase.database.FirebaseDatabase.getInstance;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public DatabaseReference mDatabaseRef;
    private List<ProductInformation> productList;
    public ListView lv;
    private ProductListAdapter adapter;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //Initializing
        mDatabaseRef = getInstance().getReference("Products").child("Bakery");
        productList =  new ArrayList<>();
        progressDialog = new ProgressDialog(this);
        lv = (ListView) findViewById(R.id.lv);
        progressDialog.setMessage("Loading items");
        progressDialog.show();
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progressDialog.dismiss();

                for(DataSnapshot snapshot: dataSnapshot.getChildren()){

                    ProductInformation PI = snapshot.getValue(ProductInformation.class);
                    productList.add(PI);
                }

                adapter = new ProductListAdapter(MainActivity.this,R.layout.product_layout,productList);
                lv.setAdapter(adapter);
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                TextView tvName = (TextView)  view.findViewById(R.id.tvName);
                TextView tvDes = (TextView)  view.findViewById(R.id.tvDescription);
                TextView tvPrice = (TextView)  view.findViewById(R.id.tvPrice);
                TextView tvCurrency = (TextView)  view.findViewById(R.id.tvCurrency);
                TextView tvProdID = (TextView)  view.findViewById(R.id.tvProdID);
                TextView tvProdCategory = (TextView)  view.findViewById(R.id.tvProdCategory);
                TextView tvImageURL = (TextView)  view.findViewById(R.id.tvImageUrl);

                String prodName = tvName.getText().toString();
                String prodDes = tvDes.getText().toString();
                String prodPrice = tvPrice.getText().toString();
                String prodCurrency = tvCurrency.getText().toString();
                String prodID = tvProdID.getText().toString();
                String prodCategory = tvProdCategory.getText().toString();
                String imageURL = tvImageURL.getText().toString();
                Intent prodDetails =  new Intent(getApplicationContext(),Product_Details.class);
                Intent thisIntent = getIntent();
                if(thisIntent.hasExtra("cartKey")){
                    String theKey = thisIntent.getStringExtra("cartKey");
                    prodDetails.putExtra("cartKey",theKey);
                }
                if(thisIntent.hasExtra("currentBill")){
                    String theBill = thisIntent.getStringExtra("currentBill");
                    prodDetails.putExtra("currentBill",theBill);
                }
                prodDetails.putExtra("prodName",prodName);
                prodDetails.putExtra("prodDes",prodDes);
                prodDetails.putExtra("prodPrice",prodPrice);
                prodDetails.putExtra("prodCurrency",prodCurrency);
                prodDetails.putExtra("prodID",prodID);
                prodDetails.putExtra("prodCategory",prodCategory);
                prodDetails.putExtra("imageURL",imageURL);


                startActivity(prodDetails);



            }
        });

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.myCart) {
            Intent intent = getIntent();
            if(intent.hasExtra("cartKey")) {
                String cartKey = intent.getStringExtra("cartKey");
                Intent myCartPhase = new Intent(getApplicationContext(),MyCart.class);
                myCartPhase.putExtra("cartKey",cartKey);
                if(intent.hasExtra("currentBill")){
                    String theBill = intent.getStringExtra("currentBill");
                    myCartPhase.putExtra("currentBill",theBill);
                }
                startActivity(myCartPhase);
            }

            else{
                Intent myCartPhase = new Intent(getApplicationContext(),MyCart.class);
                startActivity(myCartPhase);
            }
        }
        if (id == R.id.checkOut) {
            Intent intent = getIntent();
            if(intent.hasExtra("cartKey")) {
                String cartKey = intent.getStringExtra("cartKey");
                Intent checkOutPhase = new Intent(getApplicationContext(),CheckOut.class);
                checkOutPhase.putExtra("cartKey",cartKey);
                if(intent.hasExtra("currentBill")){
                    String theBill = intent.getStringExtra("currentBill");
                    checkOutPhase.putExtra("currentBill",theBill);
                }
                startActivity(checkOutPhase);
            }

            else{
                Intent checkOutPhase = new Intent(getApplicationContext(),CheckOut.class);
                startActivity(checkOutPhase);
            }
        }
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public  void  changeChild(String childName){
        adapter.clear();
        lv.setAdapter(null);
        mDatabaseRef = getInstance().getReference("Products").child(childName);
        progressDialog.setMessage("Loading items");
        progressDialog.show();
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progressDialog.dismiss();

                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    ProductInformation PI = snapshot.getValue(ProductInformation.class);
                    productList.add(PI);

                }
                adapter = new ProductListAdapter(MainActivity.this,R.layout.product_layout,productList);
                lv.setAdapter(adapter);
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.Bakery) {
           changeChild(item.toString());

        } else if (id == R.id.Beverages) {
            changeChild(item.toString());
        } else if (id == R.id.Breakfast) {
            changeChild(item.toString());
        } else if (id == R.id.cannedGoods) {
            changeChild(item.toString());
        } else if (id == R.id.dairyAndEgss) {
            changeChild(item.toString());
        } else if (id == R.id.Deli) {
            changeChild(item.toString());
        }else if (id == R.id.Frozen) {
            changeChild(item.toString());
        }else if (id == R.id.Household) {
            changeChild(item.toString());
        }else if (id == R.id.International) {
            changeChild(item.toString());
        }else if (id == R.id.meatAndSeafood) {
            changeChild(item.toString());
        }else if (id == R.id.Pantry) {
            changeChild(item.toString());
        }else if (id == R.id.personalCare) {
            changeChild(item.toString());
        }else if (id == R.id.Pets) {
            changeChild(item.toString());
        }else if (id == R.id.Produce) {
            changeChild(item.toString());
        }else if (id == R.id.Snacks) {
            changeChild(item.toString());
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
