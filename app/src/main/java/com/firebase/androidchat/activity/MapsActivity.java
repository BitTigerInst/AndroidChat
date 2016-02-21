package com.firebase.androidchat.activity;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.firebase.androidchat.ChatApplication;
import com.firebase.androidchat.R;
import com.firebase.androidchat.bean.User;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.GenericTypeIndicator;
import com.firebase.client.ValueEventListener;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.LocationCallback;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;
import java.util.HashMap;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final int MY_PERMISSIONS_FINE_LOCATION = 1;
    private static final int MY_PERMISSIONS_COARSE_LOCATION = 0;
    private GoogleMap mMap;
    private String mChannelName;
    private Firebase mFirebase;
    private Firebase mFirebaseUserList;
    private GeoFire mFirebaseUserLocation;
    private String mUserName;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private Firebase mFirebaseUser;
    private ArrayList<String> userList;
    private ArrayList<Marker> markers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setupChannelname();
        setupUsername();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        // Setup our Firebase mFirebaseChat
        mFirebase = new Firebase(ChatApplication.FIREBASE_URL);
        mFirebaseUser = mFirebase.child("user");
        mFirebaseUserLocation = new GeoFire(mFirebase.child("location"));
        mFirebaseUserList = mFirebase.child("channel").child(mChannelName.replace(".", ",")).child("user");
        getUserList();
        markers = new ArrayList<>();
    }

    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

//         Add a marker in Sydney and move the camera

    }

    private void showUserMarker(){
        for(final String user: userList) {
            mFirebaseUserLocation.getLocation(user.replace(".", ","), new LocationCallback() {
                @Override
                public void onLocationResult(String key, GeoLocation location) {
                    if (location != null) {
                        System.out.println(String.format("The location for key %s is [%f,%f]", key, location.latitude, location.longitude));

                        LatLng userLocation = new LatLng(location.latitude, location.longitude);
                        Marker curMark = mMap.addMarker(new MarkerOptions().position(userLocation).title(user));
                        markers.add(curMark);
                        if(userList.size() == markers.size()){
                            cameraZoomIn();
                        }
                    } else {
                        System.out.println(String.format("There is no location for key %s in GeoFire", key));
                    }
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                    System.err.println("There was an error getting the GeoFire location: " + firebaseError);
                }
            });
        }
    }

    private void setupChannelname() {
        SharedPreferences prefs = getApplication().getSharedPreferences("ChatPrefs", 0);
        mChannelName = prefs.getString("channel", null);
    }

    private void setupUsername() {
        SharedPreferences prefs = getApplication().getSharedPreferences("ChatPrefs", 0);
        mUserName = prefs.getString("username", null);
    }

    @Override
    public void onConnected(Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    MY_PERMISSIONS_COARSE_LOCATION);
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_FINE_LOCATION);
        }
        mMap.setMyLocationEnabled(true);
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            double mLatitude = Double.parseDouble(String.valueOf(mLastLocation.getLatitude()));
            double mLongitude = Double.parseDouble(String.valueOf(mLastLocation.getLongitude()));
            mFirebaseUserLocation.setLocation(mUserName.replace(".", ","), new GeoLocation(mLatitude, mLongitude));
            LatLng myLocation = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            Marker curMark = mMap.addMarker(new MarkerOptions().position(myLocation).title(mUserName));
            markers.add(curMark);
            cameraZoomIn();
//            mMap.moveCamera(CameraUpdateFactory.newLatLng(myLocation));
//            mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
        }

        showUserMarker();
    }

    private void cameraZoomIn() {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (Marker marker : markers) {
            builder.include(marker.getPosition());
        }
        LatLngBounds bounds = builder.build();
        int padding = 15; // offset from edges of the map in pixels
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        mMap.animateCamera(cu);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    private void getUserList(){
        userList = new ArrayList<>();
        mFirebaseUserList.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                GenericTypeIndicator<HashMap<String, User>> t = new GenericTypeIndicator<HashMap<String, User>>() {
                };
                HashMap<String, User> map = snapshot.getValue(t);
                if(map == null)
                    return;
                for (User u : map.values()) {
                    userList.add(u.getName());
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });
    }
}
