package com.vimal.commrade;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements LocationListener {
    LocationManager locationManager;
    String address,FullName,Mobile,Gender;
    TextView name,loc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION
            },100);
        }
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{
                    Manifest.permission.SEND_SMS
            },100);
        }


        name=(TextView) findViewById(R.id.MainName);
        loc=(TextView)findViewById(R.id.Location) ;
        SessionManager sessionManager=new SessionManager(this);
        HashMap<String,String> userDetails=sessionManager.getUserDetailsFromSession();
        FullName=userDetails.get(SessionManager.KEY_NAME);
        Mobile=userDetails.get(SessionManager.KEY_MOBILE);
        Gender=userDetails.get(SessionManager.KEY_GENDER);
        Toast.makeText(this,FullName, Toast.LENGTH_SHORT).show();
        name.setText(FullName+"\n"+Mobile+"\n"+Gender);

    }
    public void logoutbtn(View view){
        SessionManager sessionManager=new SessionManager(this);
        sessionManager.Logout();
        Intent intent=new Intent(MainActivity.this,Login.class);
        startActivity(intent);
        finish();

    }

    public void locationbtn(View view){
        getLocation();


    }

    public void sms(View view){
        String Message=FullName+address;
        try{
            SmsManager smsManager=SmsManager.getDefault();
            smsManager.sendTextMessage("+917523075849",null,Message,null,null);
        }catch (Exception e){
            Toast.makeText(this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }

    @SuppressLint("MissingPermission")
    public void getLocation(){
        try{
            locationManager=(LocationManager)getApplicationContext().getSystemService(LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,10000,5,MainActivity.this);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
//        Toast.makeText(this, ""+location.getLatitude()+""+location.getLongitude(), Toast.LENGTH_SHORT).show();
        try{
            Geocoder geocoder=new Geocoder(MainActivity.this, Locale.getDefault());
            List<Address> addresses=geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
            address=addresses.get(0).getAddressLine(0);
            loc.setText(address);

        }catch (Exception e){
            e.printStackTrace();

        }

    }
}