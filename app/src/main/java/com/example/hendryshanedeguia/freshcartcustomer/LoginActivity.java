package com.example.hendryshanedeguia.freshcartcustomer;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    Button btnLogin;
    EditText etUseremail, etUserPass;
    FirebaseAuth fba;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        etUseremail = (EditText) findViewById(R.id.etUserEmail);
        etUserPass = (EditText) findViewById(R.id.etUserPass);
        btnLogin =  (Button) findViewById(R.id.btnLogin);
        fba = FirebaseAuth.getInstance();

        //Change this after
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String  email = etUseremail.getText().toString().trim();
                String  password = etUserPass.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    //If email is empty, it sends out this toast message to guide the user
                    Toast.makeText(getApplicationContext(), "Email must not be empty", Toast.LENGTH_SHORT).show();
                    //Stops stops the function
                    return;

                }
                if(TextUtils.isEmpty(password)){
                    //If password is empty, it sends out this toast message to guide the user
                    Toast.makeText(getApplicationContext(), "Password must not be empty", Toast.LENGTH_SHORT).show();
                    //Stops stops the function
                    return;
                }

                fba.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isComplete()){
                            FirebaseUser fbu = FirebaseAuth.getInstance().getCurrentUser();
                            String userID =fbu.getUid();
                            Intent nextPhase =  new Intent(LoginActivity.this,MainActivity.class);
                            nextPhase.putExtra("user",userID);
                            startActivity(nextPhase);
                        }else{
                            Toast.makeText(LoginActivity.this, "Oops! Something went wrong. Please try again!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}
