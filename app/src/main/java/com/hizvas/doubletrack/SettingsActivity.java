package com.hizvas.doubletrack;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;

import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import static com.hizvas.doubletrack.MapsActivity.mMap;

public class SettingsActivity extends AppCompatActivity {
    Spinner spinner;
    Switch status, zoom;
int spin=0;
int item=0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        spinner = (Spinner) findViewById(R.id.spinner);
        status = (Switch)findViewById(R.id.onoff);
        zoom = (Switch)findViewById(R.id.zoom);
        ArrayList categories = new ArrayList();
        categories.add("Normal");
        categories.add("Hybrid");
        categories.add("Satelite");
        categories.add("Terrain");
      //  spinner.setselec(spin);
        spinner.setSelection(spin);
        SharedPreferences preferences2 = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        String zm =  preferences2.getString("zoom", null);
if (zm.equals("t")){ //zoom.setChecked(true);
    Toast.makeText(this, ""+zm, Toast.LENGTH_SHORT).show();
}
else {
    zoom.setChecked(false);
    Toast.makeText(this, ""+zm.toString(), Toast.LENGTH_SHORT).show();
}
zoom.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (b==true){
            mMap.getUiSettings().setZoomControlsEnabled(true);
            SharedPreferences preferences = getPreferences(MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();  // Put the values from the UI
            editor.putString("zoom","t");
         //   EditText txtName = (EditText)findViewById(R.id.txtName);
            // mMap.getUiSettings().setCompassEnabled(true);
            //mMap.getUiSettings().setMyLocationButtonEnabled(true);
            //  mMap.setMyLocationEnabled(true);
            //  mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            //mMap.invalidate();
            // googleMap.getUiSettings().setMapToolbarEnabled(true);
            mMap.getUiSettings().setZoomGesturesEnabled(true);
        }else if (b==false){
            mMap.getUiSettings().setZoomControlsEnabled(false);
            SharedPreferences preferences = getPreferences(MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();  // Put the values from the UI
            editor.putString("zoom","f");
            // mMap.getUiSettings().setCompassEnabled(true);
            //mMap.getUiSettings().setMyLocationButtonEnabled(true);
            //  mMap.setMyLocationEnabled(true);
            //  mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            //mMap.invalidate();
            // googleMap.getUiSettings().setMapToolbarEnabled(true);
            mMap.getUiSettings().setZoomGesturesEnabled(true);
        }
    }
});
        // Creating adapter for spinner
        ArrayAdapter dataAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
        // Spinner click listener
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                spinner.setSelection(i);
               //  item = adapterView.getItemAtPosition(i).toString();
                item = i;
if (i==2){
    mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
}
else if (i==0){
    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
}
else if(i==1){
    mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
}
else if (i==3){
    mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);

}



            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        // Spinner Drop down elements



    }
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
         spin = savedInstanceState.getInt("spinner");
    }
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is
        // killed and restarted.
      //  savedInstanceState.putBoolean("zoom",true );
      //  savedInstanceState.putDouble("myDouble", 1.9);
     //   savedInstanceState.putInt("MyInt", 1);
        savedInstanceState.putInt("spinner", item);
        // etc.
    }

    @Override
    protected void onResume() {
        super.onResume();
        spinner.setSelection(spin);
        SharedPreferences preferences2 = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        String zm =  preferences2.getString("zoom", null);
        if (zm.equals("t")){ zoom.setChecked(true);
            Toast.makeText(this, ""+zm, Toast.LENGTH_SHORT).show();
        }
        else {
            zoom.setChecked(false);
            Toast.makeText(this, ""+zm, Toast.LENGTH_SHORT).show();
        }
    }
}
