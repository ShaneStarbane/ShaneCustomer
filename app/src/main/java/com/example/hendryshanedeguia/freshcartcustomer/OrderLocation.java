package com.example.hendryshanedeguia.freshcartcustomer;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.TextView;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class OrderLocation extends FragmentActivity implements OnMapReadyCallback , GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    private GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    LocationRequest mLocationRequest;
    DatabaseReference custLocationBDF,driverLocationDBF;
    TextView tvDriverloc,tvDriverloc2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_location);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        tvDriverloc = (TextView) findViewById(R.id.tvDriverloc);
        tvDriverloc2 = (TextView) findViewById(R.id.tvDriverloc2);

           }




    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        googleMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                        this, R.raw.style_json));
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        buildGoogleApiClient();
        mMap.setMyLocationEnabled(true);

    }
    protected synchronized void buildGoogleApiClient(){
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }
    @Override
    public void onConnected(@Nullable Bundle bundle) {

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(2500);
        mLocationRequest.setFastestInterval(2500);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(final Location location) {
        mLastLocation = location;
        Intent thisIntent =  getIntent();
        String driverID = thisIntent.getStringExtra("driverID");
        driverLocationDBF = FirebaseDatabase.getInstance().getReference("Drivers").child(driverID).child("driverLocation").child("l");
        driverLocationDBF.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                double driverLat = Double.parseDouble(dataSnapshot.child("0").getValue().toString());
                double driverLong = Double.parseDouble(dataSnapshot.child("1").getValue().toString());




                double Lat = Double.parseDouble("10.2935887");
                double Long = Double.parseDouble("123.8608929");
                LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());
                LatLng latLng2 = new LatLng(driverLat,driverLong);

                //mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(15));

                custLocationBDF = FirebaseDatabase.getInstance().getReference("Customers").child("qnlHdvPLuUT9IXyMGWZlqQMzKC92").child("custLocation");

                GeoFire geoFire = new GeoFire(custLocationBDF);
                geoFire.setLocation("qnlHdvPLuUT9IXyMGWZlqQMzKC92",new GeoLocation(location.getLatitude(),location.getLongitude()));
                getDirectionsUrl(latLng,latLng2);
                String url = getDirectionsUrl(latLng,latLng2);
                DownloadTask downloadTask = new DownloadTask(mMap);
                // Start downloading json data from Google Directions API
                downloadTask.execute(url);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });



    }





    private String getDirectionsUrl(LatLng origin, LatLng dest) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Sensor enabled
        String sensor = "sensor=false";
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor;
        // Output format
        String output = "json";
        // Building the url to the web service
        return "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            Intent thisIntent =  getIntent();
            String orderGross = thisIntent.getStringExtra("orderGross");
            String orderVAT = thisIntent.getStringExtra("orderVAT");
            String orderPromo = thisIntent.getStringExtra("promo");
            String orderBill = thisIntent.getStringExtra("orderBill");
            String orderDiscount = thisIntent.getStringExtra("discount");
            String cashOnHand =thisIntent.getStringExtra("cashOnHand");
            String change = "0.00";
            String driverID = thisIntent.getStringExtra("driverID");
            String user = thisIntent.getStringExtra("user");
            String orderID = thisIntent.getStringExtra("orderID");
            String orderStatus = thisIntent.getStringExtra("orderStatus");


            Intent x = new Intent(getApplicationContext(),MyOrder.class);
            x.putExtra("user",user);
            x.putExtra("orderGross",orderGross);
            x.putExtra("orderVAT",orderVAT);
            x.putExtra("orderPromo",orderPromo);
            x.putExtra("orderBill",orderBill);
            x.putExtra("orderDiscount",orderDiscount);
            x.putExtra("cashOnHand",cashOnHand);
            x.putExtra("orderID",orderID);
            x.putExtra("change",change);
            x.putExtra("orderStatus",orderStatus);
            x.putExtra("driverID",driverID);

            startActivity(x);

        }
        return super.onKeyDown(keyCode, event);
    }
}
