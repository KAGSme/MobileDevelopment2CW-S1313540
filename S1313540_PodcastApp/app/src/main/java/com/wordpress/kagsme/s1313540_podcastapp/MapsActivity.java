package com.wordpress.kagsme.s1313540_podcastapp;

import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.icu.text.DecimalFormat;
import android.location.Location;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.Manifest;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mMap;
    private String distance;
    private String estimateTime;

    private TextView distanceView;
    private TextView estimateView;
    private Button calcBtn;

    private Spinner speedSpinner;
    private float speed;

    private Marker destination;
    private Marker myLocation;

    private LatLng myLatLing;
    private LatLng desLatLng;

    private GoogleApiClient mGoogleClient;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        if(mGoogleClient == null)mGoogleClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();

        distanceView = (TextView)findViewById(R.id.Distance);
        estimateView = (TextView)findViewById(R.id.EstimatedTime);

        calcBtn = (Button)findViewById(R.id.CalculateDistance);

        toolbar = (Toolbar)findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        calcBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBtnClick(v);
            }
        });

        speed = 5;
        speedSpinner = (Spinner)findViewById(R.id.speedSpinner);
        speedSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch(position)
                {
                    case 0:
                        speed = 5;
                        break;
                    case 1:
                        speed = 10;
                        break;
                    case 2:
                        speed = 30;
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                speed = 5;
            }
        });

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);
    }

    @Override
    protected void onStart(){
        super.onStart();
        mGoogleClient.connect();
    }

    @Override
    protected void onResume(){
        super.onResume();
    }

    @Override
    protected void onStop(){
        super.onStop();
        mGoogleClient.disconnect();
    }

    @TargetApi(23)
    @Override
    public void onConnected(Bundle connectionHint){
        Log.e("s1313540", "Getting Loc1");
        Location currentLocation = null;
        if(!(Build.VERSION.SDK_INT < 23)) {
            if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                Log.e("s1313540", "Getting Loc2");
                currentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleClient);
            }
        }
        if (currentLocation != null){
            myLatLing = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        }

        GetAndDisplayLastLoc();
    }

    @Override
    public void onConnectionSuspended(int cause){

    }

    @Override
    public void onConnectionFailed(ConnectionResult result){

    }


    private Polyline pLine;

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if(destination != null)destination.remove();
                destination = mMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title("DESTINATION")
                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.destination_marker))
                );
                desLatLng = latLng;

                if(myLocation != null && destination != null){
                    if(pLine!=null) pLine.remove();
                    pLine = mMap.addPolyline(new PolylineOptions()
                            .add(myLatLing)
                            .add(desLatLng)
                    );
                }
            }
        });

        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }

    public void GetAndDisplayLastLoc(){
        if(myLatLing != null)
        {
            myLocation = mMap.addMarker(new MarkerOptions()
                    .position(myLatLing)
                    .title("YOU")
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.you_marker))
            );
            mMap.moveCamera(CameraUpdateFactory.newLatLng(myLatLing));
            mMap.setMinZoomPreference(12);
        }
        else Log.e("s1313540", "My Loc is null");
    }

    public void onBtnClick(View v){
        switch (v.getId()){
            case R.id.CalculateDistance:
                CalculateDistance();
                break;
        }
    }

    public void CalculateDistance(){
        if(myLocation != null && destination != null)
        {
            float[] result = new float[1];
            Location.distanceBetween(myLocation.getPosition().longitude, myLocation.getPosition().latitude, destination.getPosition().longitude, destination.getPosition().latitude, result);
            distance = String.format("%.02f", result[0]/1000) ;
            distanceView.setText(String.format(getResources().getString(R.string.DistanceText), distance));
            Log.e("s1313540","distance: " + result[0]);

            estimateTime = String.format("%.02f", (result[0]/1000)/speed);
            estimateView.setText(String.format(getResources().getString(R.string.EstimateTimeText), estimateTime));
        }
        else Toast.makeText(this, "Missing either Your location or Destination!", Toast.LENGTH_LONG).show();
    }
}
