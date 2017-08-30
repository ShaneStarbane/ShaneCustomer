package com.example.hendryshanedeguia.freshcartcustomer;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class AccountDetermining extends AppCompatActivity {

    //Declare
    private Button btnAlready, btnDont;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_determining);
        //Bind
        btnAlready =  (Button) findViewById(R.id.btnAlready);
        btnDont =  (Button) findViewById(R.id.btnDont);

        //setListeners

        btnAlready.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent nextPhase =  new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(nextPhase);
            }
        });
        btnDont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent nextPhase =  new Intent(getApplicationContext(),Register.class);
                startActivity(nextPhase);
            }
        });

    }
}
