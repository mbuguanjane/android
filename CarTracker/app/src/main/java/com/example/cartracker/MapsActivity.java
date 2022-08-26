package com.example.cartracker;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
    ArrayList<LatLng> points=new ArrayList<>();
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Current Location");

        ValueEventListener listener=databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    Double latitude = Double.parseDouble(dataSnapshot.child("latitude").getValue().toString());
                    Double longitude = Double.parseDouble(dataSnapshot.child("longitude").getValue().toString());
                    String gpstime = dataSnapshot.child("gpstime").getValue().toString();
                    String gpsdate = dataSnapshot.child("gpsdate").getValue().toString();
                    LatLng location = new LatLng(latitude, longitude);
                    System.out.println("Success DB " + latitude);
                    points.add(location);


                    MarkerOptions markerOptions = new MarkerOptions();

                    // Setting latitude and longitude of the marker position
                    markerOptions.position(location);

                    // Setting titile of the infowindow of the marker
                    markerOptions.title("Date->" + gpsdate + " Time-> " + gpstime);
                    Location temp = new Location(LocationManager.GPS_PROVIDER);
                    temp.setLatitude(latitude);
                    temp.setLongitude(longitude);
                    String Address = convertLocationToAddress(temp);
                    // Setting the content of the infowindow of the marker
                    markerOptions.snippet("Address-> " + Address);

                    // Instantiating the class PolylineOptions to plot polyline in the map
                    PolylineOptions polylineOptions = new PolylineOptions();

                    // Setting the color of the polyline
                    polylineOptions.color(Color.RED);

                    // Setting the width of the polyline
                    polylineOptions.width(3);

                    // Adding the taped point to the ArrayList


                    // Setting points of polyline
                    polylineOptions.addAll(points);

                    // Adding the polyline to the map
                    mMap.addPolyline(polylineOptions);

                    // Adding the marker to the map
                    mMap.addMarker(markerOptions);
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(points.get(0), 16f));
                }catch(Exception ex)
                {
                    Toast.makeText(MapsActivity.this,"Error occurred"+ex.getMessage(),Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                        System.out.println("Error "+databaseError);
            }
        });


    }
    private String convertLocationToAddress(Location location) {
        String addressText;
        String errorMessage = "";

        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

        List<Address> addresses = null;

        try {
            addresses = geocoder.getFromLocation(
                    location.getLatitude(),
                    location.getLongitude(),
                    1
            );
        } catch (IOException ioException) {
            // Network or other I/O issues

        } catch (IllegalArgumentException illegalArgumentException) {
            // Invalid long / lat

        }

        // No address was found
        if (addresses == null || addresses.size() == 0) {
            if (errorMessage.isEmpty()) {


            }
            addressText = errorMessage;

        } else {
            Address address = addresses.get(0);
            ArrayList<String> addressFragments = new ArrayList<>();

            // Fetch the address lines, join them, and return to thread
            for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                addressFragments.add(address.getAddressLine(i));
            }

            addressText =
                    TextUtils.join(System.getProperty("line.separator"),
                            addressFragments);
        }

        return addressText;

    }
}