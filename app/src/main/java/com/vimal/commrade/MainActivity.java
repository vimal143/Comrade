package com.vimal.commrade;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static android.provider.ContactsContract.CommonDataKinds.Website.URL;

/**
 * @author Vimal.Pandey
 */

public class MainActivity extends AppCompatActivity implements LocationListener {
    LocationManager locationManager;
    String address,FullName,Mobile,Gender;
    Double latittude,longitude;
    TextView name;
    Boolean GPSStatus;
    DatabaseHandler database;
    ArrayList<String> SmsRecieverName,SmsRecieverMobile;


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


        name=findViewById(R.id.MainName);
       // loc=(TextView)findViewById(R.id.Location) ;
        SessionManager sessionManager=new SessionManager(this);
        HashMap<String,String> userDetails=sessionManager.getUserDetailsFromSession();
        FullName=userDetails.get(SessionManager.KEY_NAME);
        Mobile=userDetails.get(SessionManager.KEY_MOBILE);
        Gender=userDetails.get(SessionManager.KEY_GENDER);
        Toast.makeText(this,"Logged in as "+FullName, Toast.LENGTH_SHORT).show();
       name.setText("Hello"+" "+FullName);
        database=new DatabaseHandler(MainActivity.this);

       SmsRecieverName=new ArrayList<>();
       SmsRecieverMobile=new ArrayList<>();
       FetchMemberInfo();



    }

    public void logoutbtn(View view){
        SessionManager sessionManager=new SessionManager(this);
        sessionManager.Logout();
        Intent intent=new Intent(MainActivity.this,Login.class);
        startActivity(intent);
        finish();

    }

    public void locationbtn(View view){
        CheckGPSStatus();
        getLocation();
        sms();



    }
    public void addUserCard(View view){
        Intent i=new Intent(MainActivity.this,AddMember.class);
        startActivity(i);
    }

    public void sms(){
        //Added on 23/08/2020 By Vimal)
        String Message=FullName+" "+address;
        try{
            SmsManager smsManager=SmsManager.getDefault();
            for(int i=0;i<SmsRecieverMobile.size();i++){
                String msgNumber="+91"+SmsRecieverMobile.get(i).toString();
                Toast.makeText(this, msgNumber, Toast.LENGTH_SHORT).show();
                smsManager.sendTextMessage(msgNumber,null,Message,null,null);
                Toast.makeText(this, "SMS alert Sent "+msgNumber, Toast.LENGTH_SHORT).show();
            }




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
        // 22/08/2020
//        Toast.makeText(this, ""+location.getLatitude()+""+location.getLongitude(), Toast.LENGTH_SHORT).show();
        try{
            latittude=location.getLatitude();
            longitude=location.getLongitude();
            Geocoder geocoder=new Geocoder(MainActivity.this, Locale.getDefault());
            List<Address> addresses=geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
            address=addresses.get(0).getAddressLine(0);

            //loc.setText(address);

        }catch (Exception e){
            e.printStackTrace();

        }

    }

    public void CheckGPSStatus(){
        //Added on 25/08/2020 By Vimal Pandey
        locationManager=(LocationManager)getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        assert locationManager!=null;
        GPSStatus=locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if(!GPSStatus){
            Intent i=new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(i);
        }
    }


    void FetchMemberInfo(){
        Cursor cursor=database.readData();
        if(cursor.getCount()==0){
            Toast.makeText(this, "No Member Found", Toast.LENGTH_SHORT).show();
        }
        else{
            while(cursor.moveToNext()){
                SmsRecieverName.add(cursor.getString(1));
                SmsRecieverMobile.add(cursor.getString(3));
            }

            }

    }


}