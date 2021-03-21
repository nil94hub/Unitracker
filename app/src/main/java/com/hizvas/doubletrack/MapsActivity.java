package com.hizvas.doubletrack;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Line;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.SquareCap;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.Duration;
import com.google.maps.model.TravelMode;
import com.mapbox.android.core.location.LocationEngine;

import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncher;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncherOptions;
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute;
import com.thebrownarrow.permissionhelper.PermissionResult;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static com.google.android.gms.maps.model.JointType.ROUND;
import static com.hizvas.doubletrack.LocationTrackerService.origin;
import static com.hizvas.doubletrack.LoginActivity.customnav;
import static com.hizvas.doubletrack.LoginActivity.locpermission;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapLoadedCallback, Animation.AnimationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener, DirectionCallback {

    LocationManager locationManager;
    //LocationEngine locationEngine;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    Marker currentMarker;
    Location location;
    String origin_address,dest_address;
    LatLng destination;
    public static GoogleMap mMap;
    LatLng previousLatLng;
    LatLng currentLatLng,originlatlng,destlatlng;
    private Polyline polyline1;
    private static final int RC_LOCATION_REQUEST = 1234;
    private int RC_LOCATION_ON_REQUEST = 1235;
    private LocationRequest locationRequest;
    private List<LatLng> polylinePoints = new ArrayList<>();
    private Marker mCurrLocationMarker;
    int nav = 1,fetchcurrentloc=1;
    TextView v,pickup_location,drop_location;
    Place  pickup, drop;
    private int PLACE_PICKER_REQUEST = 7896;
    private int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1234;
    ImageView currentloc;
    LinearLayout header;
    Point origin1,destination1;
    SupportMapFragment mapFragment;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        bool=1;
        gpscheck();
        RelativeLayout relativeLayout = (RelativeLayout)findViewById(R.id.header);
        pickup_location = (TextView) findViewById(R.id.pickup_location);
        drop_location = (TextView) findViewById(R.id.drop_location);
         header = (LinearLayout)findViewById(R.id.hedr);
        if (customnav==0){
            header.setVisibility(View.GONE);
        }
      //  header = (RelativeLayout)findViewById(R.id.header);
        currentloc = (ImageView)findViewById(R.id.current_location);

        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), getString(R.string.google_maps_key), Locale.US);
        }
      //  callLocationEngine();
      //  Toast.makeText(getApplicationContext(), ""+SessionManager.getMobile(), Toast.LENGTH_SHORT).show();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
         mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(Color.argb(255,0,0,0));
        toolbar.getOverflowIcon().setColorFilter(Color.WHITE , PorterDuff.Mode.SRC_ATOP);
       // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        checkLocationSettingsRequest();
        setUpLocationRequest();

        currentloc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickup_location.setText(origin_address);
            }
        });
        try {
            pickup_location.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!Places.isInitialized()) {
                        Places.initialize(getApplicationContext(), getString(R.string.google_maps_key), Locale.US);
                    }

                    List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS,Place.Field.LAT_LNG);
                   // Toast.makeText(MapsActivity.this, "okay", Toast.LENGTH_SHORT).show();
// Start the autocomplete intent.
                    Intent intent = new Autocomplete.IntentBuilder(
                            AutocompleteActivityMode.FULLSCREEN, fields)
                            .build(getApplicationContext());
                    startActivityForResult(intent, PLACE_PICKER_REQUEST);
                    //  Toast.makeText(MapsActivity.this, ""+t, Toast.LENGTH_SHORT).show();


                }
            });
            drop_location.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    /*  Intent intent =
                              new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                                      .build(getActivity());
                      startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);

                  */

                    List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS,Place.Field.LAT_LNG);

// Start the autocomplete intent.
                    Intent intent = new Autocomplete.IntentBuilder(
                            AutocompleteActivityMode.FULLSCREEN, fields)
                            .build(getApplicationContext());
                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
                }
            });

        }catch (Throwable t){
            Toast.makeText(MapsActivity.this, ""+t, Toast.LENGTH_SHORT).show();
        }

        v = (TextView)findViewById(R.id.tool_left_title);
        locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        if (LoginActivity.locpermission == 1){
            getApplicationContext().startService(new Intent(getApplicationContext(), LocationTrackerService.class));

        }
        /*mTrackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(getActivity(), "ok", Toast.LENGTH_SHORT).show();
                getActivity().startService(new Intent(getActivity(), LocationTrackerService.class));
            }
        });*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (MapsActivity.this.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && MapsActivity.this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    Activity#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for Activity#requestPermissions for more details.
                return;
            }
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 0, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                //fetch current loc
              //  Toast.makeText(MapsActivity.this, "current location", Toast.LENGTH_SHORT).show();
                SharedPreferences preferences2 = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
                String mobile =  preferences2.getString("mobile", null);
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                origin = new LatLng(latitude, longitude);
                Geocoder geoCoder = new Geocoder(getBaseContext(), Locale.getDefault());
                try {
                    List<Address> addresses = geoCoder.getFromLocation(latitude, longitude, 1);
                    origin_address = addresses.get(0).getAddressLine(0);
                    if (customnav==1 && fetchcurrentloc==1){
                        pickup_location.setText(origin_address);
                        fetchcurrentloc=0;
                    }
                  //  String add = "";
                    if (addresses.size() > 0) {
                        System.out.println(addresses.get(0).getLocality());
                       // pickup_location.setText(origin_address);
                   //     Toast.makeText(MapsActivity.this, ""+addresses.get(0).getLocality(), Toast.LENGTH_SHORT).show();
                        System.out.println(addresses.get(0).getCountryName());
                    }

                  //  showToastMessage(add);
                }
                catch (IOException e1) {
                    e1.printStackTrace();
                }
           //     BitmapDescriptor iconself = BitmapDescriptorFactory.fromResource(R.drawable.flag_brunei);

//To fetch and plot self location and marker om map
                if (customnav==1){
                if (origin!=null){
                    if(currentMarker!=null){
                        currentMarker.remove();
                        currentMarker.setPosition(origin);

                       // currentMarker.setIcon(iconself);
                        currentMarker.setTitle(mobile);

                        currentMarker = mMap.addMarker(new MarkerOptions().position(origin).title(mobile));
                       mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(origin,17 ));
                    }else{
                        //Toast.makeText(getApplicationContext(), "null", Toast.LENGTH_SHORT).show();


                        currentMarker = mMap.addMarker(new MarkerOptions().position(origin)
                                .title(mobile));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(origin,17 ));
//BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)
                     //   mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,18 ));
                    }}
                }

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
              //  Toast.makeText(MapsActivity.this, ""+status, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_LOCATION_ON_REQUEST && resultCode == Activity.RESULT_OK){
            Log.e("tag", "Resolution done");
            startLocationUpdates();
        }else  if (requestCode == 1000) {
            if (resultCode == RESULT_OK) {
                String result = data.getStringExtra("result");
              //  getcurrentlocation();

            }
            if (resultCode == RESULT_CANCELED) {
                //Write your code if there's no result
            }}
        /*if (requestCode == 1000) {
            if (resultCode == RESULT_OK) {
                String result = data.getStringExtra("result");
              //  getcurrentlocation();

            }
            if (resultCode == RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }*/ else if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(getApplicationContext(), "ok", Toast.LENGTH_SHORT).show();
                pickup = Autocomplete.getPlaceFromIntent(data);

                pickup_location.setText(pickup.getAddress());


            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                Status status = Autocomplete.getStatusFromIntent(data);
              //  Log.e(TAG, status.toString());
              //  Toast.makeText(getActivity(), status.getStatusMessage(), Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                drop = Autocomplete.getPlaceFromIntent(data);
                drop_location.setText(drop.getAddress());
             //   SessionManager.setTO(drop.getAddress());
              //  String to = drop.getAddress();
                //Toast.makeText(getActivity(), "" +to,Toast.LENGTH_SHORT).show();
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                Status status = Autocomplete.getStatusFromIntent(data);
              //  Toast.makeText(getActivity(), status.getStatusMessage(), Toast.LENGTH_LONG).show();

            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
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
                                status.startResolutionForResult(MapsActivity.this, 1000);
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
public void gpscheck(){
    if (!GPSEnable()) {
       // Toast.makeText(LoginActivity.this, "ok", Toast.LENGTH_SHORT).show();
        tunonGps();
    } else {
        // Toast.makeText(LoginActivity.this, "GPS disabled", Toast.LENGTH_SHORT).show();

    }
}

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.getUiSettings().setScrollGesturesEnabled(true);
        mMap.getUiSettings().setTiltGesturesEnabled(true);
        mMap.getUiSettings().setRotateGesturesEnabled(true);
        // Add a marker in Sydney and move the camera
        PolylineOptions greyOptions = new PolylineOptions();
        greyOptions.width(15);
        greyOptions.color(Color.BLACK);
        greyOptions.startCap(new SquareCap());
        greyOptions.endCap(new SquareCap());
        greyOptions.jointType(ROUND);

       // polyline1 = mMap.addPolyline(greyOptions);


        if (LoginActivity.locpermission == 1){
            fetchLocationUpdates();
        }

    }
    @Override
    protected void onResume() {
        super.onResume();
        if (nav == 1 && locpermission==1){
            getApplicationContext().startService(new Intent(getApplicationContext(), LocationTrackerService.class));
nav =0;
        }
        bool=1;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_settings:

                  if (customnav==1){
                    if (!pickup_location.getText().toString().equals("") && !drop_location.getText().toString().equals("")){
                       // Toast.makeText(getApplicationContext(), "Location required for fetching route", Toast.LENGTH_SHORT).show();
                        Geocoder coder = new Geocoder(getApplicationContext());
                        List<Address> fromaddress,toaddress;
                       //
                        try {
                            // May throw an IOException
                            fromaddress = coder.getFromLocationName(pickup_location.getText().toString(), 5);
                            toaddress = coder.getFromLocationName(drop_location.getText().toString(), 5);


                            Address location = fromaddress.get(0);
                            Address location1 = toaddress.get(0);
                            originlatlng = new LatLng(location.getLatitude(), location.getLongitude());
                            SessionManager.setolat(String.valueOf(originlatlng.latitude));
                            SessionManager.setolong(String.valueOf(originlatlng.longitude));
                             destlatlng = new LatLng(location1.getLatitude(), location1.getLongitude());
                            SessionManager.setdlat(String.valueOf(destlatlng.latitude));
                            SessionManager.setdlong(String.valueOf(destlatlng.longitude));
                            new Handler().postDelayed (this::requestDirection, 1000);
                         //  Toast.makeText(getApplicationContext(), ""+originlatlng.latitude, Toast.LENGTH_SHORT).show();
                       //     Toast.makeText(getApplicationContext(), ""+destlatlng, Toast.LENGTH_SHORT).show();

                        } catch (IOException ex) {

                            ex.printStackTrace();
                        }
                    }else {
                        Toast.makeText(getApplicationContext(), "Location required", Toast.LENGTH_SHORT).show();
                          }
                }else {
                      new Handler().postDelayed (this::requestDirection, 1000);
                  }
               // mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

               // startActivity(new Intent(getApplicationContext(),SettingsActivity.class));
                //new Handler().postDelayed (this::requestDirection, 2000);



              /*  SharedPreferences preferences3 = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
                String user =  preferences3.getString("user", null);
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference ref = database.getReference().child("location").child(user);
                SharedPreferences preferences4 = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
                String rd =  preferences4.getString("mobile", null);

                Map<String, Object> data = new HashMap<>();
                data.put("req","r");
                data.put("sender",rd);

                ref.setValue(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //
                       // Log.i("tag", "Location update saved");
                    }
                });*/


                return(true);
            case R.id.action_about:
                nav = 1;
               
                if (customnav==1){
                    if (!pickup_location.getText().toString().equals("") && !drop_location.getText().toString().equals("")){
                       // Toast.makeText(getApplicationContext(), ""+originlatlng.latitude, Toast.LENGTH_SHORT).show();
                        Geocoder coder = new Geocoder(getApplicationContext());
                        List<Address> fromaddress,toaddress;
                        //
                        try {
                            // May throw an IOException
                            fromaddress = coder.getFromLocationName(pickup_location.getText().toString(), 5);
                            toaddress = coder.getFromLocationName(drop_location.getText().toString(), 5);


                            Address location = fromaddress.get(0);
                            Address location1 = toaddress.get(0);
                            originlatlng = new LatLng(location.getLatitude(), location.getLongitude());
                            SessionManager.setolat(String.valueOf(originlatlng.latitude));
                            SessionManager.setolong(String.valueOf(originlatlng.longitude));
                            destlatlng = new LatLng(location1.getLatitude(), location1.getLongitude());
                            SessionManager.setdlat(String.valueOf(destlatlng.latitude));
                            SessionManager.setdlong(String.valueOf(destlatlng.longitude));
                            // new Handler().postDelayed (this::requestDirection, 1000);
                            //  Toast.makeText(getApplicationContext(), ""+originlatlng.latitude, Toast.LENGTH_SHORT).show();
                            //     Toast.makeText(getApplicationContext(), ""+destlatlng, Toast.LENGTH_SHORT).show();

                        } catch (IOException ex) {

                            ex.printStackTrace();
                        }
                        origin1 = Point.fromLngLat(Double.parseDouble(SessionManager.getolong()), Double.parseDouble(SessionManager.getolat()));
                        destination1 = Point.fromLngLat(Double.parseDouble(SessionManager.getdlong()), Double.parseDouble(SessionManager.getdlat()));

                      // fetchRoute(origin1,destination1);
                        String url = "https://www.google.com/maps/dir/?api=1&destination=" + drop_location.getText().toString()+ "&travelmode=driving";
                        //  String url = "https://www.google.com/maps/dir/"+pickup_location.getText().toString()+"/"+drop_location.getText().toString();
                        Intent intent1 = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(intent1);
                    }
                    else {
                      //  Toast.makeText(getApplicationContext(), "hobena", Toast.LENGTH_SHORT).show();
                    }
                }else {
                   // Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();
                    double olat = origin.latitude;
                    double olong = origin.longitude;

if (customnav==1){
    if (pickup_location.getText().toString()!=null){
     origin1 = Point.fromLngLat(originlatlng.latitude, originlatlng.longitude);}
    else {
        Toast.makeText(getApplicationContext(), "Location required", Toast.LENGTH_SHORT).show();
    }
}else {
    origin1 = Point.fromLngLat(olong, olat);
    double dlat = destination.latitude;
    double dlong = destination.longitude;
               /* if (customnav==1){
                   if(drop_location.getText().toString()!=null){
                    destination1 = Point.fromLngLat(destlatlng.latitude, destlatlng.longitude);}
                   else{

                   }
                }else*/

                    String url = "https://www.google.com/maps/dir/?api=1&destination=" + dlat+"+"+dlong+ "&travelmode=driving";
                    //  String url = "https://www.google.com/maps/dir/"+pickup_location.getText().toString()+"/"+drop_location.getText().toString();
                    Intent intent1 = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent1);
                    destination1 = Point.fromLngLat(dlong, dlat);
              /*  if (customnav==1){if (originlatlng!=null && destlatlng!=null){
                    fetchRoute(origin1,destination1);
                }else if (customnav==0){*/
                   // fetchRoute(origin1, destination1);


                }}

              //  mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
               // map.invalidate();


                return(true);
        }

        return(super.onOptionsItemSelected(item));
    }

    private void fetchLocationUpdates() {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();

        SharedPreferences preferences2 = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        String user =  SessionManager.getUser();

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
                Toast.makeText(MapsActivity.this, ""+databaseError, Toast.LENGTH_SHORT).show();
            }
        });
    }
    private LocationCallback locationUpdatesCallback;



static int alertstate = 0;
    @Override
    protected void onStop() {
        super.onStop();
   //     Toast.makeText(this, "stop", Toast.LENGTH_SHORT).show();
alertstate = 1;
       /* locationUpdatesCallback = new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {*/
                    SharedPreferences preferences2 = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
                    String rd = SessionManager.getMobile();
                    //   Toast.makeText(LocationTrackerService.this, ""+rd, Toast.LENGTH_SHORT).show();
if (locpermission==1) {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference().child("location").child(rd);


    Map<String, Object> data = new HashMap<>();
    //Location lastLocation = locationResult.getLastLocation();
    // Toast.makeText(getApplicationContext(), "gf", Toast.LENGTH_SHORT).show();
    // data.put("time", lastLocation.getTime());
    SharedPreferences preferenceslat = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
    double lat = Double.parseDouble(preferenceslat.getString("lat", null));
    double lng = Double.parseDouble(preferenceslat.getString("lng", null));
    data.put("latitude", lat);
    data.put("longitude", lng);


    data.put("time", 0);
    String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
    String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
    data.put("lsdate", currentDate);
    data.put("lstime", currentTime);
SessionManager.setlsDate(currentDate);
SessionManager.setlsTime(currentTime);
    ref.setValue(data).addOnSuccessListener(new OnSuccessListener<Void>() {
        @Override
        public void onSuccess(Void aVoid) {
            //
            Log.i("tag", "Location update saved");
        }
    });
    //}};
                /*    LocationNotification.notify(LocationTrackerService.this, "Location Tracking",
                            "Lat:" + lastLocation.getLatitude() + " - Lng:" + lastLocation.getLongitude());*/

    bool = 0;
    if (locpermission == 1) {
        getApplicationContext().stopService(new Intent(getApplicationContext(), LocationTrackerService.class));
    }
    //  Toast.makeText(this, "hi", Toast.LENGTH_SHORT).show();
}
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        mapFragment.onDestroy();


    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapFragment.onLowMemory();
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapFragment.onSaveInstanceState(outState);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //Toast.makeText(this, "keydown", Toast.LENGTH_SHORT).show();
        alertstate = 1;
        ActivityCompat.finishAffinity(this);
        return super.onKeyDown(keyCode, event);

    }
public static int bool = 1;


    private void updateMap(DataSnapshot dataSnapshot) {
        double deslatitude = 0, deslongitude = 0;
        long stat = 4;
        String lsdate="",lstime="";
      //  BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.avatar);
        Iterable<DataSnapshot> data = dataSnapshot.getChildren();
        for(DataSnapshot d: data){
            if(d.getKey().equals("latitude")){
                /*if (bool==1){
                    Toast.makeText(this, "User is online", Toast.LENGTH_LONG).show();
                    bool=0;
                }*/

                deslatitude = (Double) d.getValue();
            }else if(d.getKey().equals("longitude")){
                deslongitude = (Double) d.getValue();
                destination = new LatLng( deslatitude,deslongitude);
                Geocoder geocoder;
                List<Address> addresses;
                geocoder = new Geocoder(this, Locale.getDefault());

                try {
                    addresses = geocoder.getFromLocation(deslatitude, deslongitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                     dest_address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()

                } catch (IOException e) {
                    e.printStackTrace();
                }

               // String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()

            }
            else if (d.getKey().equals("time")){
                 stat = (Long)d.getValue();
              //  Toast.makeText(this, ""+stat, Toast.LENGTH_SHORT).show();
                if ( stat == 1){
                    v.setText("Online");
                   // Toast.makeText(getApplicationContext(), "User is online", Toast.LENGTH_SHORT).show();
                }}
                   // Toast.makeText(getApplicationContext(), "sdd", Toast.LENGTH_SHORT).show();
                //    v.setText("Offline");

                  else if (d.getKey().equals("lsdate")){
                        lsdate = (String) d.getValue();
                   } else if (d.getKey().equals("lstime")){
                        lstime = (String) d.getValue();

                       v.setText("Last seen "+lsdate+", "+lstime);

                   // Toast.makeText(getApplication(), "User is offline", Toast.LENGTH_SHORT).show();
                }




        }
        SharedPreferences preferences2 = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        String usermobile =  SessionManager.getUser();
       /* if (latLng!=null){
            if(currentMarker!=null){
                currentMarker.setPosition(latLng);
                currentMarker.setIcon(icon);
                currentMarker.setTitle(usermobile);
            }else{
                currentMarker = mMap.addMarker(new MarkerOptions().position(latLng)
                        .icon(icon).title(usermobile));
            }
        }*/

//To get opponent's location and set marker
        currentLatLng = new LatLng(deslatitude, deslongitude);
        if(previousLatLng ==null || previousLatLng != currentLatLng){
            // add marker line
            if(mMap!=null) {

                /*previousLatLng  = currentLatLng;
                polylinePoints.add(currentLatLng);
                polyline1.setPoints(polylinePoints);*/
                Log.w("tag", "Key:" + currentLatLng);

                if(mCurrLocationMarker!=null){
                    mCurrLocationMarker.setPosition(currentLatLng);
                }else{
                   // Toast.makeText(this, "ok", Toast.LENGTH_SHORT).show();
                    mCurrLocationMarker = mMap.addMarker(new MarkerOptions()
                            .position(currentLatLng)
                            .title(usermobile));
                }
              //  mMap.setMapTypeId(google.maps.MapTypeId.SATELLITE);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 14));
            }
            /*mMap.setMyLocationEnabled(true);
            currentMarker = mMap.addMarker(new MarkerOptions().position(latLng)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)).title("g"));*/
        }
    }
   /* @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_LOCATION_ON_REQUEST && resultCode == Activity.RESULT_OK){
            Log.e("tag", "Resolution done");
            startLocationUpdates();
        }
    }*/
    private void setUpLocationRequest() {
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(7000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void checkLocationSettingsRequest(){

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        SettingsClient locationClient = LocationServices.getSettingsClient( getApplicationContext());
        Task<LocationSettingsResponse> locationSettings = locationClient.checkLocationSettings(builder.build());

        locationSettings.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {
                if(!task.isSuccessful()){
                    Log.e("tag", "Exception" + task.getException().getMessage());
                    if(task.getException() instanceof ResolvableApiException){
                        // show permission request to user
                        try {
                            ResolvableApiException resolvable = (ResolvableApiException) task.getException();
                            resolvable.startResolutionForResult( MapsActivity.this,
                                    RC_LOCATION_ON_REQUEST);
                        } catch (IntentSender.SendIntentException sendEx) {
                            // Ignore the error.
                        }
                    }
                }else{
                    startLocationUpdates();
                }
            }
        });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == RC_LOCATION_REQUEST && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            startLocationUpdates();
        }
    }
    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission( getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission( getApplicationContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions( MapsActivity.this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION }, RC_LOCATION_REQUEST );
        }else {
            //startService(new Intent(this, LocationTrackerService.class));
        }
    }

    @Override
    public void onLocationChanged(Location location) {
      //  Toast.makeText(getApplicationContext(), "okay", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onMapLoaded() {

    }
    public void requestDirection() {

        if (customnav==1){
            GoogleDirection.withServerKey(getString(R.string.google_maps_key))
                    .from(originlatlng)
                    .to(destlatlng)
                    .transportMode(TransportMode.DRIVING)
                    .execute(MapsActivity.this);
        }else {
            GoogleDirection.withServerKey(getString(R.string.google_maps_key))
                    .from(origin)
                    .to(destination)
                    .transportMode(TransportMode.DRIVING)
                    .execute(MapsActivity.this);
        }

        // confirm.setEnabled(false);
    }
    @Override
    public void onDirectionSuccess(Direction direction, String rawBody) {
        if (getApplicationContext() != null) {
mMap.clear();
            if (direction.isOK()) {
                ArrayList<LatLng> directionPositionList = direction.getRouteList().get(0).getLegList().get(0).getDirectionPoint();
                mMap.addPolyline(DirectionConverter.createPolyline(getApplicationContext(), directionPositionList, 5, Color.BLACK));
                mMap.addMarker(new MarkerOptions().position(new LatLng(origin.latitude, origin.longitude)).title("").snippet(origin_address));

              // mMap.addMarker(new MarkerOptions().position(new LatLng(destination.latitude, destination.longitude)).title("Drop Location").snippet(dest_address));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(origin, 12));
                if (customnav==1){
                }else {
                String o = origin.latitude + "," + origin.longitude;
                String d = destination.latitude + "," + destination.longitude;
                SessionManager.setdlat(String.valueOf(destination.latitude));
                SessionManager.setdlong(String.valueOf(destination.longitude));}
               // getDurationForRoute(o,d);
               // Toast.makeText(MapsActivity.this, "fds", Toast.LENGTH_LONG).show();

               // Toast.makeText(getApplicationContext(), "distance "+(Double.valueOf(direction.getRouteList().get(0).getLegList().get(0).getDistance().getValue()) / 1000), Toast.LENGTH_SHORT).show();
                //calFare();
            } else {
            }
        }
    }
    public String getDurationForRoute(String origin, String destination)
    // - We need a context to access the API
    {
        GeoApiContext geoApiContext = new GeoApiContext.Builder()
                .apiKey(getString(R.string.google_maps_key))
                .build();

        // - Perform the actual request
        DirectionsResult directionsResult = null;
        try {
            directionsResult = DirectionsApi.newRequest(geoApiContext)
                    .mode(TravelMode.DRIVING)
                    .origin(origin)
                    .destination(destination)
                    .await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (com.google.maps.errors.ApiException e) {
            e.printStackTrace();
        }

        // - Parse the result
        DirectionsRoute route = directionsResult.routes[0];
        DirectionsLeg leg = route.legs[0];
        Duration duration = leg.duration;
        //Toast.makeText(getActivity(), "duration"+duration.inSeconds/60, Toast.LENGTH_SHORT).show();
       // SessionManager.setRideduration(String.valueOf(duration.inSeconds/60));
        long mins,hour;
        long time = duration.inSeconds/60;
        if (time>=60){
            mins=time%60;
            hour = (time-mins)/60;
            if (mins>0){
               // Toast.makeText(getApplicationContext(), ""+hour + " Hrs " + mins + " minutes", Toast.LENGTH_SHORT).show();
                // ride_duration.setText(SessionManager.getRideduration()+"minutes");
                if (hour>1) {
                  //  Toast.makeText(getApplicationContext(), ""+hour + " Hrs " + mins + " minutes", Toast.LENGTH_SHORT).show();
                    // ride_duration.setText(hour + " Hrs " + mins + " minutes");
                }
                else{
                   // Toast.makeText(getApplicationContext(), ""+hour + " Hr " + mins + " minutes", Toast.LENGTH_SHORT).show();

                    //  ride_duration.setText(hour + " Hr " + mins + " minutes");
                }
            }else if (mins==0){
               // ride_duration.setText(hour+" Hrs");
            //    Toast.makeText(getApplicationContext(), ""+(hour+" Hrs"), Toast.LENGTH_SHORT).show();

            }
        }else{
            Toast.makeText(getApplicationContext(), ""+duration.inSeconds/60+" minutes", Toast.LENGTH_SHORT).show();

            // ride_duration.setText(""+duration.inSeconds/60+" minutes");
        }
      /*  double olat = Double.parseDouble(SessionManager.getolat());
        double olong = Double.parseDouble(SessionManager.getolong());
*//*    double olat = 25.566556;
    double olong = 88.566545;*//*
        Point origin1 = Point.fromLngLat(olong, olat);
        double dlat = Double.parseDouble(SessionManager.getdlat());
        double dlong = Double.parseDouble(SessionManager.getdlong());
      *//*double dlat = 22.6792055;
      double dlong = 88.1975253;*//*
        Point destination1 = Point.fromLngLat(dlong, dlat);
        fetchRoute(origin1,destination1);*/
        return String.valueOf(duration.inSeconds);
    }
    @Override
    public void onDirectionFailure(Throwable t) {
        Toast.makeText(getApplicationContext(), ""+t, Toast.LENGTH_SHORT).show();
    }


    private void fetchRoute(Point origin, Point destination) {
        NavigationRoute.builder(getApplicationContext())
                .accessToken(Mapbox.getAccessToken())
                .origin(origin)
                .destination(destination)
                .build()
                .getRoute(new Callback<DirectionsResponse>() {
                    @Override
                    public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                        //Log.d("map", String.valueOf(origin));
                      //  Log.d("map", String.valueOf(response));
                          com.mapbox.api.directions.v5.models.DirectionsRoute directionsRoute = response.body().routes().get(0);
                      //  Toast.makeText(MapsActivity.this, ""+directionsRoute.distance(), Toast.LENGTH_SHORT).show();
                        startNavigation(directionsRoute);
                    }

                    @Override
                    public void onFailure(Call<DirectionsResponse> call, Throwable t) {
                        Toast.makeText(MapsActivity.this, ""+t, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void startNavigation(com.mapbox.api.directions.v5.models.DirectionsRoute directionsRoute) {
        NavigationLauncherOptions.Builder navigationLauncherOptions = NavigationLauncherOptions.builder();
        navigationLauncherOptions.shouldSimulateRoute(false);
        navigationLauncherOptions.enableOffRouteDetection(true);
        navigationLauncherOptions.snapToRoute(true);
        navigationLauncherOptions.directionsRoute(directionsRoute);

        NavigationLauncher.startNavigation(MapsActivity.this, navigationLauncherOptions.build());
    }
}
