package com.hizvas.doubletrack;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;

import com.thebrownarrow.permissionhelper.ActivityManagePermission;
import com.thebrownarrow.permissionhelper.PermissionResult;
import com.thebrownarrow.permissionhelper.PermissionUtils;

import java.util.concurrent.TimeUnit;

import static com.hizvas.doubletrack.MapsActivity.alertstate;

public class LoginActivity extends ActivityManagePermission implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    Button btncruise,btnaddcnct;
    ImageView btnFetch;
    TextView tv1,tv2;
    ImageView imv;
    SwipeRefreshLayout swipeRefreshLayout;
    EditText etUserMobile,etMobile;
    private static final int PICK_CONTACT = 1;
    private static int PICK = 1;
     CountryCodePicker ccp,ccpuser;
    private AdView mAdView;
    private InterstitialAd mInterstitialAd;
    public static int customnav=0;
    SharedPreferences.Editor editor;
    String Fp="";
    int count = 0;
    int newreq = 1;
    String permissionAsk[] = {PermissionUtils.Manifest_ACCESS_COARSE_LOCATION, PermissionUtils.Manifest_ACCESS_FINE_LOCATION, PermissionUtils.Manifest_READ_CONTACTS, PermissionUtils.Manifest_READ_SMS};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        SessionManager.initialize(getApplicationContext());

        Bindview();
        if (!GPSEnable()) {
          //  Toast.makeText(LoginActivity.this, "ok", Toast.LENGTH_SHORT).show();
            tunonGps();
        } else {
            // Toast.makeText(LoginActivity.this, "GPS disabled", Toast.LENGTH_SHORT).show();

        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            askCompactPermissions(permissionAsk, new PermissionResult() {
                @Override
                public void permissionGranted() {
                  //  AskPermission();
                        }

                @Override
                public void permissionDenied() {
                    Toast.makeText(getApplicationContext(), "Permission required for normal functioning of the app", Toast.LENGTH_SHORT).show();
                    finish();
                }

                @Override
                public void permissionForeverDenied() {

                }
            });
        }else {
           /* Toast.makeText(getApplicationContext(), "Permission required for normal functioning of the app", Toast.LENGTH_SHORT).show();
            finish();*/
            //startService(new Intent(this, LocationTrackerService.class));
        }
/*imv.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

        try {
          //  9163952351
            getApplicationContext().startService(new Intent(getApplicationContext(), LocationTrackerService.class));
            Toast.makeText(LoginActivity.this, ""+tv1.getText().toString(), Toast.LENGTH_SHORT).show();
          //  SmsManager smsManager = SmsManager.getDefault();
          //  smsManager.sendTextMessage(tv1.getText().toString(), null, ".", null, null);
            //Toast.makeText(getApplicationContext(), "SMS1 sent.", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
             Toast.makeText(getApplicationContext(), "SMS1 faild, please try again."+e, Toast.LENGTH_LONG).show();
           // e.printStackTrace();
        }
    }
});*/

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
      //  SharedPreferences preferences2 = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        String user =  SessionManager.getMobile();
        if (user==null){

        }else{
        DatabaseReference ref = database.getReference().child("location").child(user);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Log.i("tag", "New location updated:" + dataSnapshot.getKey());
                // Toast.makeText(MapsActivity.this, ""+dataSnapshot.getKey(), Toast.LENGTH_SHORT).show();
                updateMap(dataSnapshot);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(LoginActivity.this, ""+databaseError, Toast.LENGTH_SHORT).show();
            }
        });}

    }
    private void newRequest(){
        newreq=0;
        final Dialog myDialog;
        myDialog = new Dialog(this);
        myDialog.setContentView(R.layout.custompopup);
        Button btnselect, btncancel;
        TextView dg;
        dg = (TextView)myDialog.findViewById(R.id.tvdg);
        dg.setText(""+num+" wants to track you");
        btnselect = (Button) myDialog.findViewById(R.id.btnselectX);
        btncancel = (Button) myDialog.findViewById(R.id.btncancel);
        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mInterstitialAd.isLoaded()) {
                    //    Toast.makeText(getApplicationContext(), "ad", Toast.LENGTH_SHORT).show();
                    mInterstitialAd.show();
                }
               // startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                myDialog.dismiss();
            }
        });
        btnselect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Toast.makeText(LoginActivity.this, "ok", Toast.LENGTH_SHORT).show();
                if (mInterstitialAd.isLoaded()) {
                    //    Toast.makeText(getApplicationContext(), "ad", Toast.LENGTH_SHORT).show();
                    mInterstitialAd.show();
                }
                locpermission=1;
                SharedPreferences.Editor editor = LoginActivity.this.getSharedPreferences("MyPref", Context.MODE_PRIVATE).edit();
              //  editor.putString("mobile", ccp.getFullNumberWithPlus() );
                editor.putString("user", String.valueOf(num));
                startActivity(new Intent(getApplicationContext(), MapsActivity.class));
            }
        });

        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }
public static int locpermission = 0;
    String request = "",nm="";
    long num = 0;

    private void updateMap(DataSnapshot dataSnapshot) {


        //  BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.avatar);
        Iterable<DataSnapshot> data = dataSnapshot.getChildren();
        for(DataSnapshot d: data){
             if(d.getKey().equals("sender")){
              //  nm = String.valueOf(d.getValue());
                Toast.makeText(getApplicationContext(), ""+nm, Toast.LENGTH_SHORT).show();
            }
            else if(d.getKey().equals("req")){
                request = (String) d.getValue();
                if (request.equals("r")){
if(newreq==1){
    newRequest();
}



                    }
                Toast.makeText(getApplicationContext(), ""+request, Toast.LENGTH_SHORT).show();
            }



        }

    }


    @Override
    protected void onStart() {
        super.onStart();
/*if (alertstate==1){
    alertstate=0;
    final Dialog myDialog;
    myDialog = new Dialog(this);
    myDialog.setContentView(R.layout.custompopup);
    Button btnselect,btncancel;
    btnselect = (Button)myDialog.findViewById(R.id.btnselectX);
    btncancel = (Button)myDialog.findViewById(R.id.btncancel);
    btncancel.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (mInterstitialAd.isLoaded()){
                //    Toast.makeText(getApplicationContext(), "ad", Toast.LENGTH_SHORT).show();
                mInterstitialAd.show();
            }
            startActivity(new Intent(getApplicationContext(),LoginActivity.class));
        }
    });
    btnselect.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
           // Toast.makeText(LoginActivity.this, "ok", Toast.LENGTH_SHORT).show();
            if (mInterstitialAd.isLoaded()){
            //    Toast.makeText(getApplicationContext(), "ad", Toast.LENGTH_SHORT).show();
                mInterstitialAd.show();
            }
            startActivity(new Intent(getApplicationContext(),MapsActivity.class));
        }
    });

    myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    myDialog.show();

}*/
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ActivityCompat.finishAffinity(this);
    }

    public void Bindview(){
       // imv = (ImageView)findViewById(R.id.panic);
        tv1 = (TextView)findViewById(R.id.tv1);
        tv2 = (TextView)findViewById(R.id.tv2);
        btnaddcnct = (Button)findViewById(R.id.btnAddcnct);
        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipe_refresh);
        etUserMobile = (EditText)findViewById(R.id.etUserMobile);
      //  btnSubmit = (Button)findViewById(R.id.btnSubmit);
        etMobile = (EditText)findViewById(R.id.etMobile);
        btnFetch = (ImageView) findViewById(R.id.btnFetch);
        ccp = (CountryCodePicker) findViewById(R.id.ccp);
        ccpuser = (CountryCodePicker)findViewById(R.id.ccpuser);
        btncruise = (Button)findViewById(R.id.btncruise);
        btncruise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customnav=1;
                locpermission=0;
                startActivity(new Intent(getApplicationContext(),MapsActivity.class));
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
             //   Toast.makeText(LoginActivity.this, "to ad", Toast.LENGTH_SHORT).show();
            }
        });
        final LinearLayout llsnackbar = (LinearLayout)findViewById(R.id.llsnackbar);
ccp.registerCarrierNumberEditText(etMobile);
ccpuser.registerCarrierNumberEditText(etUserMobile);

        mInterstitialAd = new InterstitialAd(getApplicationContext());
        mInterstitialAd.setAdUnitId("ca-app-pub-8806098815345498/2910277797");
        //  mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        //mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        //ca-app-pub-3940256099942544/1033173712
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mInterstitialAd.setAdListener(new AdListener(){
            @Override
            public void onAdClosed() {
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }
        });
        // if (etMobile.length()!=0){
        btnaddcnct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customnav=0;
                // ccp.getFullNumber();
               // Toast.makeText(LoginActivity.this, "ok", Toast.LENGTH_SHORT).show();
             /*   if (mInterstitialAd.isLoaded()){
                  //  Toast.makeText(getApplicationContext(), "ad", Toast.LENGTH_SHORT).show();
                    mInterstitialAd.show();
                }*/
                if (ccp.isValidFullNumber()==true && ccpuser.isValidFullNumber()==true){
                //    Toast.makeText(LoginActivity.this, "ok", Toast.LENGTH_SHORT).show();
                    if (mInterstitialAd.isLoaded()){
                   //     Toast.makeText(getApplicationContext(), "ad", Toast.LENGTH_SHORT).show();
                        mInterstitialAd.show();
                    }
                    /*SharedPreferences.Editor editor = LoginActivity.this.getSharedPreferences("MyPref", Context.MODE_PRIVATE).edit();
                    editor.putString("mobile", ccp.getFullNumberWithPlus() );
                    editor.putString("user", ccpuser.getFullNumberWithPlus() );
                    editor.putString("etmobile", etMobile.getText().toString());
                    editor.putString("etuser", etUserMobile.getText().toString() );
                    editor.commit();*/
                    SessionManager.setMobile(ccp.getFullNumberWithPlus());
                    SessionManager.setEt_mobile(etMobile.getText().toString());
                    SessionManager.setUser(ccpuser.getFullNumberWithPlus());
                    SessionManager.setEt_user(etUserMobile.getText().toString() );
                 //   Toast.makeText(LoginActivity.this, ""+SessionManager.getEt_mobile()+SessionManager.getMobile()+SessionManager.getUser()+SessionManager.getEt_user(), Toast.LENGTH_SHORT).show();
                    SharedPreferences preferences2 = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
                    boolean login =  preferences2.getBoolean("login", false);
                    if (login == true){
                        customnav=0;
                        startActivity(new Intent(getApplicationContext(),MapsActivity.class));

                      //  SessionManager.setMobile("qwer");
                        locpermission=1;
                    }else {
                        customnav=0;
                        Intent intent = new Intent(getApplicationContext(),OtpActivity.class);
                        startActivity(intent);
                    }
                }
                else {
                    Snackbar.make(llsnackbar, "Mobile number validation failed", Snackbar.LENGTH_LONG)
                            .setAction("OK", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                }
                            })
                            .setActionTextColor(getResources().getColor(android.R.color.holo_red_light ))
                            .show();
                 //   TextView action = findViewById(android.support.design.R.id.snackbar_action); action.setTextColor(getApplicationContext().getResources().getColor(android.R.color.holo_red_dark));
// Changing action button text color

                }

               // Toast.makeText(LoginActivity.this, "ookat"+ccp.getFullNumberWithPlus().toString(), Toast.LENGTH_SHORT).show();
                   /* if (tv2.length()==0 && count==1){
                        Toast.makeText(LoginActivity.this, "2nd", Toast.LENGTH_SHORT).show();
                        tv2.setText(""+etUserMobile.getText().toString());
                        count=0;
                    }
                    if (tv1.length()==0 && count==0){
                        tv1.setText(""+etUserMobile.getText().toString());
                        etUserMobile.setText("");
                        count=1;
                        Toast.makeText(LoginActivity.this, ""+count, Toast.LENGTH_SHORT).show();
                    }
*/
                // Toast.makeText(LoginActivity.this, "ok", Toast.LENGTH_SHORT).show();
            }
        });

        //    }
        btnFetch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mInterstitialAd.isLoaded()){
                    //  Toast.makeText(getApplicationContext(), "ad", Toast.LENGTH_SHORT).show();
                    mInterstitialAd.show();
                }
                Intent i = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                startActivityForResult(i,PICK_CONTACT);
                PICK=1;
                // Toast.makeText(Login.this, "Click", Toast.LENGTH_SHORT).show();
            }
        });

        SharedPreferences preferences2 = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        boolean login =  preferences2.getBoolean("login", false);
 /*       SharedPreferences preferences3 = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        String mobile =  preferences3.getString("etmobile", null);
        String user =  preferences3.getString("etuser", null);*/
        if (login == true){
           // Toast.makeText(this, "ok"+SessionManager.getEt_mobile(), Toast.LENGTH_SHORT).show();
           etMobile.setText(""+SessionManager.getEt_mobile());
           etMobile.setEnabled(false);
           etUserMobile.setText(""+SessionManager.getEt_user());
           ccp.setEnabled(false);
        }
    }


    /*@Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            switch(requestCode){
                case PICK_CONTACT:
                    contactPicked(data);
                    break;
            }
        }
    }
    private void contactPicked(Intent data){
        Cursor cursor = null;
        try {
            Uri uri = data.getData();
            cursor = getContentResolver().query(uri,null,null,null,null);
            cursor.moveToFirst();
            int phindex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            String PHNO = cursor.getString(phindex);

           /* if (PHNO.length()>10){
                Fp = PHNO.substring(3,13);
            }
            else if (PHNO.length()==14){
                Fp = PHNO.substring(3,13);
            }*/
            if (PICK == 1){
                etUserMobile.setText(PHNO);
            }
        } catch (Exception e) {
            Toast.makeText(this, ""+e, Toast.LENGTH_SHORT).show();
        }
        finally {
            cursor.close();
        }
    }
    public void AskPermission() {
        // Toast.makeText(this, "Check", Toast.LENGTH_SHORT).show();
        askCompactPermissions(permissionAsk, new PermissionResult() {
            @Override
            public void permissionGranted() {
                if (!GPSEnable()) {
                    tunonGps();
                } else {
                    // Toast.makeText(LoginActivity.this, "GPS disabled", Toast.LENGTH_SHORT).show();
                    //  getCurrentlOcation();
                }

            }

            @Override
            public void permissionDenied() {

            }

            @Override
            public void permissionForeverDenied() {
                //  openSettingsApp(getApplicationContext());
                tunonGps();
            }
        });
    }
    public void tunonGps() {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API).addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this).build();
            mGoogleApiClient.connect();
            mLocationRequest = LocationRequest.create();
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            mLocationRequest.setInterval(30 * 1000);
            mLocationRequest.setFastestInterval(5 * 1000);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(mLocationRequest);

            // **************************
            builder.setAlwaysShow(true); // this is the key ingredient
            // **************************

            PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi
                    .checkLocationSettings(mGoogleApiClient, builder.build());
            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                @Override
                public void onResult(LocationSettingsResult result) {
                    final Status status = result.getStatus();
                    final LocationSettingsStates state = result
                            .getLocationSettingsStates();
                    switch (status.getStatusCode()) {
                        case LocationSettingsStatusCodes.SUCCESS:
                            // All location settings are satisfied. The client can
                            // initialize location
                            // requests here.
                            //  getCurrentlOcation();
                            break;
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            // Location settings are not satisfied. But could be
                            // fixed by showing the user
                            // a dialog.
                            try {
                                // Show the dialog by calling
                                // startResolutionForResult(),
                                // and setting the result in onActivityResult().
                                status.startResolutionForResult(LoginActivity.this, 1000);
                            } catch (IntentSender.SendIntentException e) {
                                // Ignore the error.
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            // Location settings are not satisfied. However, we have
                            // no way to fix the
                            // settings so we won't show the dialog.
                            break;
                    }
                }
            });
        }

    }
    public Boolean GPSEnable() {
        LocationManager locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            return true;
        }
        return false;
    }

    @Override
    public void onConnected( Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed( ConnectionResult connectionResult) {

    }

}
