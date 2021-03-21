package com.hizvas.doubletrack;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.hizvas.doubletrack.MapsActivity.bool;

public class LocationTrackerService extends Service {
    private FusedLocationProviderClient mLocationProviderClient;
    private LocationCallback locationUpdatesCallback;
    private LocationRequest locationRequest;
    SharedPreferences pref;
    public static LatLng origin,navorigin,navdestination;
    public LocationTrackerService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
      /*  if (bool==1){
           // data.put("time", 1);
            SharedPreferences preferences2 = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
            String rd =  preferences2.getString("mobile", null);
            String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
            String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference ref = database.getReference().child("location").child(rd);
            Map<String, Object> data = new HashMap<>();
            data.put("lsdate",currentDate);
            data.put("lstime",currentTime);
            ref.setValue(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    //
                    Log.i("tag", "Location update saved");
                }
            });
        }*/
        setUpLocationRequest();
    }


    private void setUpLocationRequest() {
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(10000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @SuppressLint("MissingPermission")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String keyValue = intent.getStringExtra("key");
        if(keyValue!=null && keyValue.equals("stop")){
            stopSelf();
        }else {
            setUpLocationUpdatesCallback();
            mLocationProviderClient.requestLocationUpdates(locationRequest, locationUpdatesCallback, null);
        }
        //  setUpLocationUpdatesCallback();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        //  LocationNotification.cancel(this);

        mLocationProviderClient.removeLocationUpdates(locationUpdatesCallback);
    }


    private void setUpLocationUpdatesCallback() {
        locationUpdatesCallback = new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if(locationResult!=null){
                    SharedPreferences preferences2 = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
                    String rd =  SessionManager.getMobile();
                    //   Toast.makeText(LocationTrackerService.this, ""+rd, Toast.LENGTH_SHORT).show();
                    Location lastLocation = locationResult.getLastLocation();
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference ref = database.getReference().child("location").child(rd);


                    Map<String, Object> data = new HashMap<>();
                    data.put("latitude", lastLocation.getLatitude());
                    data.put("longitude", lastLocation.getLongitude());
                    origin = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
                    SessionManager.setolat(String.valueOf(lastLocation.getLatitude()));
                    SessionManager.setolong(String.valueOf(lastLocation.getLongitude()));
                   // destination = new LatLng()
                    SharedPreferences.Editor editorlat = LocationTrackerService.this.getSharedPreferences("MyPref", Context.MODE_PRIVATE).edit();
                    editorlat.putString("lat", String.valueOf(lastLocation.getLatitude()));
                    editorlat.putString("lng", String.valueOf(lastLocation.getLongitude()) );
                    editorlat.commit();
                   // Toast.makeText(LocationTrackerService.this, ""+bool, Toast.LENGTH_SHORT).show();
                   // data.put("time", lastLocation.getTime());
                    if (bool == 0){
                        data.put("time", 0);
                        //  String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                        //    String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
                        data.put("lsdate",SessionManager.getlsDate());
                        data.put("lstime",SessionManager.getlsTime());
                    }else {
                        data.put("time", 1);
                      /*  String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                        String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
                        data.put("lsdate",currentDate);
                        data.put("lstime",currentTime);*/
                    }

                    ref.setValue(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            //
                            Log.i("tag", "Location update saved");
                        }
                    });
                /*    LocationNotification.notify(LocationTrackerService.this, "Location Tracking",
                            "Lat:" + lastLocation.getLatitude() + " - Lng:" + lastLocation.getLongitude());*/
                }else{
                    Log.i("tag", "Location null");
                }
            }
        };
    }

}