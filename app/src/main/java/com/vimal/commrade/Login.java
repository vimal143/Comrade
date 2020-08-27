package com.vimal.commrade;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {
    TextInputLayout Mobile;
    String mobileNo;
    ProgressBar progressbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Mobile = findViewById(R.id.mobileNumberLogin);
        progressbar = findViewById(R.id.progressBarLogin);
    }

    public void redirectToRegister(View view) {
        Intent register = new Intent(Login.this, Signup.class);
        startActivity(register);

    }

    public void redirectToOtp(View view) {


        if (!isConnected(this)) {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
        if (!validateMobileNumber()) {
            return;
        }
        progressbar.setVisibility(View.VISIBLE);

        String enteredNo = Mobile.getEditText().getText().toString().trim();
        mobileNo = "+91" + enteredNo;
        Query checkUser = FirebaseDatabase.getInstance().getReference("UserDetails").orderByChild("mobile").equalTo(mobileNo);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Intent otp = new Intent(Login.this, otpVerification.class);
                    otp.putExtra("mobile", mobileNo);
                    otp.putExtra("flag", "Login");
                    startActivity(otp);
                } else {
                    progressbar.setVisibility(View.GONE);
                    Toast.makeText(Login.this, "User Does not Exist", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressbar.setVisibility(View.GONE);
                Toast.makeText(Login.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });


    }

    private boolean isConnected(Login login) {
        ConnectivityManager connectivityManager = (ConnectivityManager) login.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wificon = connectivityManager.getNetworkInfo(connectivityManager.TYPE_WIFI);
        NetworkInfo mobileconn = connectivityManager.getNetworkInfo(connectivityManager.TYPE_MOBILE);
        return ((wificon != null && wificon.isConnected()) || (mobileconn != null && mobileconn.isConnected()));
    }

    private boolean validateMobileNumber() {
        String number = Mobile.getEditText().getText().toString().trim();
        if (number.isEmpty()) {
            Mobile.setError("Field can not be  Empty");
            return false;
        } else if (number.length() != 10) {
            Mobile.setError("Invalid Mobile Number");
            return false;
        } else {
            Mobile.setError(null);
            Mobile.setErrorEnabled(false);
            return true;
        }
    }

}