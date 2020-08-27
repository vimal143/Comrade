package com.vimal.commrade;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Signup extends AppCompatActivity {
    ProgressBar progressBarSignup;
    TextInputLayout Name, Mobile;
    RadioGroup radioGroup;
    RadioButton selection;
    String FullName, mobileNo, _Gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        //Getting Entered Data
        Name = findViewById(R.id.fullName);
        Mobile = findViewById(R.id.MobileNumber);
        radioGroup = findViewById(R.id.grp_radio);
        progressBarSignup = findViewById(R.id.progressBarSignup);

    }

    public void loginActivityRedirect(View view) {
        Intent intent = new Intent(Signup.this, Login.class);
        startActivity(intent);
    }

    public void redirectToOtp(View view) {

        if (!isConnected(this)) {
            //Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
            AlertDialog dialog = new AlertDialog.Builder(Signup.this)
                    .setTitle(Html.fromHtml("<font color='#2147F2'>Error</font>"))
                    .setMessage(Html.fromHtml("<font color='#2147F2'>No Internet Connection</font>"))
                    .setNegativeButton("Ok", null).show();
            Button buttonNegative = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
            buttonNegative.setTextColor(ContextCompat.getColor(Signup.this, R.color.colorPrimary));
            return;
        }
        if (!validatefullName() && !validateMobileNumber()) {
            return;
        }
        progressBarSignup.setVisibility(View.VISIBLE);
        FullName = Name.getEditText().getText().toString();
        String enteredNo = Mobile.getEditText().getText().toString().trim();
        mobileNo = "+91" + enteredNo;

        selection = findViewById(radioGroup.getCheckedRadioButtonId());
        _Gender = selection.getText().toString();
        Query checkUser = FirebaseDatabase.getInstance().getReference("UserDetails").orderByChild("mobile").equalTo(mobileNo);
//        Log.d("mobileee", "redirectToOtp: "+checkUser);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    progressBarSignup.setVisibility(View.GONE);
//                    Toast.makeText(Signup.this, "User Already Exist", Toast.LENGTH_SHORT).show();
                    AlertDialog dialog = new AlertDialog.Builder(Signup.this)
                            .setTitle(Html.fromHtml("<font color='#2147F2'>Error</font>"))
                            .setMessage(Html.fromHtml("<font color='#2147F2'>User Already Exist</font>"))
                            .setNegativeButton("Ok", null).show();
                    Button buttonNegative = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
                    buttonNegative.setTextColor(ContextCompat.getColor(Signup.this, R.color.colorPrimary));
                } else {
                    Intent otp = new Intent(Signup.this, otpVerification.class);
                    otp.putExtra("name", FullName);
                    otp.putExtra("mobile", mobileNo);
                    otp.putExtra("gender", _Gender);
                    otp.putExtra("flag", "SignUp");
                    startActivity(otp);
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressBarSignup.setVisibility(View.GONE);
                Toast.makeText(Signup.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }


    private boolean isConnected(Signup signup) {
        ConnectivityManager connectivityManager = (ConnectivityManager) signup.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wificon = connectivityManager.getNetworkInfo(connectivityManager.TYPE_WIFI);
        NetworkInfo mobileconn = connectivityManager.getNetworkInfo(connectivityManager.TYPE_MOBILE);
        if (((wificon != null && wificon.isConnected()) || (mobileconn != null && mobileconn.isConnected()))) {
            return true;
        } else {
            return false;
        }

    }

    private boolean validatefullName() {
        String fullName = Name.getEditText().getText().toString().trim();
        if (fullName.isEmpty()) {
            Name.setError("Field can not be Empty");
            return false;
        } else {
            Name.setError(null);
            Name.setErrorEnabled(false);
            return true;
        }
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