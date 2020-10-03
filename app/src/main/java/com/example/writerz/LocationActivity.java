package com.example.writerz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.ResultReceiver;
import android.provider.Settings;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class LocationActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;
    ProgressBar progressBar;
    TextView textLatLong;
    TextView textAddress;
    private  ResultReceiver resultReceiver;
    Location location;
    DatabaseReference reference;
    FirebaseUser firebaseUser;
    String address;
    double latitude;
    double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        resultReceiver = new AddressResultReceiver(new Handler());

        textLatLong = findViewById(R.id.coordinates);
        progressBar = findViewById(R.id.progress_bar);
        textAddress = findViewById(R.id.address);

        findViewById(R.id.btn_location).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(
                            LocationActivity.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION}
                            , REQUEST_CODE_LOCATION_PERMISSION);
                }
                else {
                    LocationManager lm = (LocationManager)getApplication().getSystemService(Context.LOCATION_SERVICE);
                    boolean gps_enabled = false;
                    boolean network_enabled = false;

                    try {
                        gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
                    } catch(Exception ex) {}

                    try {
                        network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
                    } catch(Exception ex) {}

                    if(!gps_enabled && !network_enabled) {
                        AlertDialog.Builder alertDialog=new AlertDialog.Builder(LocationActivity.this);
                        alertDialog
                                .setMessage("To Continue, turn on device location,which uses Google's Location Service")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                                    }
                                })
                                .setNegativeButton("NO THANKS",null)
                                .show();}
                    else
                        getCurrentLocation();
                }
            }
        });

        findViewById(R.id.btn_upload).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!textAddress.getText().toString().equals("Address"))
                     upload();
                else
                    Toast.makeText(LocationActivity.this, "Please get Location first", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION  && grantResults.length>0 ){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                getCurrentLocation();
            else
                Toast.makeText(this, "Permission Denied!", Toast.LENGTH_SHORT).show();
        }
    }

    private void getCurrentLocation(){
        progressBar.setVisibility(View.VISIBLE);
        final LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000)
                .setFastestInterval(3000)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationServices.getFusedLocationProviderClient(LocationActivity.this)
                .requestLocationUpdates(locationRequest,new LocationCallback(){
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        super.onLocationResult(locationResult);
                        LocationServices.getFusedLocationProviderClient(LocationActivity.this)
                                .removeLocationUpdates(this);
                        if (locationResult != null && locationResult.getLocations().size()>0){
                            int latestLocationIndex = locationResult.getLocations().size() - 1;
                             latitude =
                                    locationResult.getLocations().get(latestLocationIndex).getLatitude();
                             longitude =
                                    locationResult.getLocations().get(latestLocationIndex).getLongitude();
                            textLatLong.setText(
                                    String.format("Latitude: %s\nLongitude: %s",latitude,longitude)
                            );
                            location = new Location("providerNA");
                            location.setLatitude(latitude);
                            location.setLongitude(longitude);
                            fetchAddressFromLatLong(location);
                        }
                        else {
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                }, Looper.getMainLooper());
    }

    private void fetchAddressFromLatLong(Location location){
        Intent intent = new Intent( this, FetchAddressIntentService.class);
        intent.putExtra(Constants.RECEIVER,resultReceiver);
        intent.putExtra(Constants.LOCATION_DATA_EXTRA,location);
        startService(intent);
    }

    private  class AddressResultReceiver extends ResultReceiver{
        AddressResultReceiver(Handler handler) {
            super(handler); }
        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            super.onReceiveResult(resultCode, resultData);
            if (resultCode == Constants.SUCCESS_RESULT){
                textAddress.setText(resultData.getString(Constants.RESULT_DATA_KEY));
                address = resultData.getString(Constants.RESULT_DATA_KEY);}
            else
                Toast.makeText(LocationActivity.this, resultData.getString(Constants.RESULT_DATA_KEY), Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
        }
    }

    private void upload(){

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getPhoneNumber());
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("lat",""+latitude);
        hashMap.put("lon",""+longitude);
       // hashMap.put("loc",address);

        reference.updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Intent intent = new Intent(LocationActivity.this,NameLocationActivity.class);
                    intent.putExtra("userType",getIntent().getStringExtra("userType"));
                    intent.putExtra("phoneNumber",getIntent().getStringExtra("phoneNumber"));
                    startActivity(intent);
                }
            }
        });
    }
}
