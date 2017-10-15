package com.example.hendryshanedeguia.freshcartcustomer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Register extends AppCompatActivity {
    EditText etEmail,etConfirmEmail,etPassword,etConfirmPassword,etContactNum,etAddress,etUsername;
    Button btnSignUp,btnImage;
    ProgressDialog progressDialog;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    ImageView ivImage;
    private Uri imgUri;
    public static  final String Storage_Path ="Customer_images/";
    public static  final String Database_Path ="Customers";
    public static  final int Request_Code = 1234;
    FirebaseUser mUser;

    String cust_email,cust_password,confirmEmail,confirmPass,cust_address,cust_contact,cust_Username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        databaseReference=  FirebaseDatabase.getInstance().getReference(Database_Path);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etConfirmEmail = (EditText) findViewById(R.id.etConfirmEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etConfirmPassword = (EditText) findViewById(R.id.etConfirmPassword);
        etContactNum = (EditText) findViewById(R.id.etContactNum);
        etAddress = (EditText) findViewById(R.id.etAddress);
        etUsername = (EditText) findViewById(R.id.etUsername);
        btnSignUp = (Button) findViewById(R.id.btnSignUp);
        ivImage =  (ImageView) findViewById(R.id.ivImage);
        storageReference = FirebaseStorage.getInstance().getReference(Storage_Path);
        btnImage = (Button) findViewById(R.id.btnImage);

    }
    @SuppressWarnings("VisibleForTests")
    public void btnSignUp_Click(View v){
       // Toast.makeText(getApplicationContext(),"Hi...",Toast.LENGTH_SHORT).show();
        cust_email = etEmail.getText().toString().trim();
        cust_password = etPassword.getText().toString().trim();
        confirmEmail = etConfirmEmail.getText().toString().trim();
        confirmPass = etConfirmPassword.getText().toString().trim();
        cust_address = etAddress.getText().toString().trim();
        cust_contact = etContactNum.getText().toString().trim();
        cust_Username = etUsername.getText().toString().trim();
        String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(cust_email);
        //Checking if something is wrong
        if (TextUtils.isEmpty(cust_email)){
            Toast.makeText(getApplicationContext(),"Email should not be empty",Toast.LENGTH_LONG).show();
            return;
        }
        if(!matcher.matches()){
            Toast.makeText(getApplicationContext(), "Not a valid email address.( Only Alphanumeric , _ , . , and - characters are allowed). ", Toast.LENGTH_LONG).show();
            return;
        }
        if (!TextUtils.equals(cust_email,confirmEmail)){
            Toast.makeText(getApplicationContext(),"Emails do not match",Toast.LENGTH_LONG).show();
            return ;
        }
        if (TextUtils.isEmpty(cust_password)){
            Toast.makeText(getApplicationContext(),"Password should not be empty",Toast.LENGTH_LONG).show();
            return;
        }
        if (!TextUtils.equals(cust_password,confirmPass)){
            Toast.makeText(getApplicationContext(),"Passwords do not match",Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(cust_address)){
            Toast.makeText(getApplicationContext(),"Address should not be empty",Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(cust_contact)){
            Toast.makeText(getApplicationContext(),"Contact number should not be empty",Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(cust_Username)){
            Toast.makeText(getApplicationContext(),"Username number should not be empty",Toast.LENGTH_LONG).show();
            return;
        }

        if(imgUri == null) {
            Toast.makeText(getApplicationContext(), "Please select an image", Toast.LENGTH_LONG).show();
            return;

        }


            final ProgressDialog dialog = new ProgressDialog(this);
            dialog.setMessage("Registering user . . . ");
            dialog.show();
            String photoName = etUsername.getText().toString();
            StorageReference ref = storageReference.child(photoName);
            ref.putFile(imgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    dialog.dismiss();

                    final String cust_email = etEmail.getText().toString().trim();
                    String cust_password = etPassword.getText().toString().trim();
                    final String cust_address = etAddress.getText().toString().trim();
                    final String cust_contact = etContactNum.getText().toString().trim();
                    final String cust_Username = etUsername.getText().toString().trim();
                    final String imageURL = taskSnapshot.getDownloadUrl().toString();

                    firebaseAuth.createUserWithEmailAndPassword(cust_email,cust_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                FirebaseUser fbu = FirebaseAuth.getInstance().getCurrentUser();
                                String uID = fbu.getUid();
                                CustomerInformation  CustInfo =  new CustomerInformation(cust_email,cust_contact,cust_address,imageURL,cust_Username,uID,"0");

                                databaseReference.child(uID).setValue(CustInfo);
                                etUsername.setText("");
                                etPassword.setText("");
                                etConfirmPassword.setText("");
                                etEmail.setText("");
                                etConfirmEmail.setText("");
                                etAddress.setText("");
                                etContactNum.setText("");
                                ivImage.setImageBitmap(null);
                                Toast.makeText(getApplicationContext(), "Registration success!", Toast.LENGTH_LONG).show();
                            }
                            else{
                                Toast.makeText(getApplicationContext(), "Something went wrong @~@", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Something went wrong. Please try again.", Toast.LENGTH_LONG).show();

                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {

                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress =(100* taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            dialog.setMessage("Registering user " + (int)progress+"%");
                        }
                    });


    }
    public void btnImage_Click(View v){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select image"), Request_Code);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Request_Code && resultCode == RESULT_OK &&  data != null && data.getData() != null) {
            imgUri = data.getData();
            try{
                Bitmap bm = MediaStore.Images.Media.getBitmap(getContentResolver(),imgUri);
                 ivImage.setImageBitmap(bm);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void registerUser(){

      //  Toast.makeText(getApplicationContext(),"Hi...",Toast.LENGTH_LONG).show();
        String cust_email = etEmail.getText().toString().trim();
        String cust_password = etPassword.getText().toString().trim();
        String confirmEmail = etConfirmEmail.getText().toString().trim();
        String confirmPass =  etConfirmPassword.getText().toString().trim();
        String cust_address = etAddress.getText().toString().trim();
        String cust_contact = etContactNum.getText().toString().trim();
        String cust_Username = etUsername.getText().toString().trim();
        Toast.makeText(getApplicationContext(),"Hi...",Toast.LENGTH_SHORT).show();
        //Checking if something is wrong
        checkFields( cust_email, confirmEmail, cust_password, confirmPass, cust_address, cust_contact, cust_Username);
        //if everything went well
       progressDialog.setMessage("Registering User...");
       progressDialog.show();
        firebaseAuth.createUserWithEmailAndPassword(cust_email,cust_password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "User has been successfully registered!", Toast.LENGTH_SHORT).show();
                    saveCustomerInformation();
                    progressDialog.dismiss();
                }else{
                    Toast.makeText(getApplicationContext(), "Something went wrong. Please try again.", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }
        });


    }


    private void saveCustomerInformation(){
        /*
        String cust_email = etEmail.getText().toString().trim();
        String cust_contact = etContactNum.getText().toString().trim();
        String cust_address = etAddress.getText().toString().trim();

        CustomerInformation customerInformation = new CustomerInformation(cust_email,cust_contact,cust_address);
        FirebaseUser user = firebaseAuth.getCurrentUser();
        databaseReference.child("Customers").child(user.getUid()).setValue(customerInformation);
        */
    }
    public boolean checkFields(String cust_email,String confirmEmail,String cust_password,String confirmPass,String cust_address,String cust_contact,String cust_Username){
        boolean result = false;
        if (TextUtils.isEmpty(cust_email)){
            Toast.makeText(getApplicationContext(),"Email should not be empty",Toast.LENGTH_LONG).show();
            result = true;
        }
        if (TextUtils.isEmpty(cust_password)){
            Toast.makeText(getApplicationContext(),"Password should not be empty",Toast.LENGTH_LONG).show();
            result = true;
        }
        if (!TextUtils.equals(cust_password,confirmPass)){
            Toast.makeText(getApplicationContext(),"Passwords do not match",Toast.LENGTH_LONG).show();
            result = true;
        }
        if (!TextUtils.equals(cust_email,confirmEmail)){
            Toast.makeText(getApplicationContext(),"Emails do not match",Toast.LENGTH_LONG).show();
            result = true;
        }
        if (TextUtils.isEmpty(cust_address)){
            Toast.makeText(getApplicationContext(),"Address should not be empty",Toast.LENGTH_LONG).show();
            result = true;
        }
        if (TextUtils.isEmpty(cust_contact)){
            Toast.makeText(getApplicationContext(),"Contact number should not be empty",Toast.LENGTH_LONG).show();
            result = true;
        }
        if (TextUtils.isEmpty(cust_Username)){
            Toast.makeText(getApplicationContext(),"Username number should not be empty",Toast.LENGTH_LONG).show();
            result = true;
        }
         return  result;
    }
}
