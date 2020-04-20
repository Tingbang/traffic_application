// Student Name: Darren Lally
// Student ID: S1622370
package org.me.gcu.traffic_application;
import android.Manifest;
import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.maps.android.clustering.ClusterManager;

import org.me.gcu.traffic_application.Adapter.CustomInfoWindowAdapter;
import org.me.gcu.traffic_application.Classes.Item;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private boolean mLocationPermissionGranted = false;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private ArrayList<LatLng> latlonLIST = new ArrayList<>();
    private ArrayList<Item> dataLIST = new ArrayList<>();
    private ArrayList<Item> plannedLIST = new ArrayList<>();
    private ClusterManager mClusterManager;
    private Button btnPlan;
    private Button btnClear;
    private Button btnRoad;
    private HandleXml handleXML = new HandleXml("https://trafficscotland.org/rss/feeds/roadworks.aspx");
    private HandleXml plannedXML = new HandleXml("https://trafficscotland.org/rss/feeds/plannedroadworks.aspx");
    private EditText mSearchText;
    private FusedLocationProviderClient fusedLocationProviderClient;



    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        mSearchText =(EditText)findViewById(R.id.input_search);
        btnPlan = (Button)findViewById(R.id.btnPlan);
        btnClear = (Button)findViewById(R.id.btnClear);
        btnRoad = (Button)findViewById(R.id.btnRoadworks);

        BottomNavigationView bottomnav = findViewById(R.id.bottom_navigation);
        bottomnav.setOnNavigationItemSelectedListener(navListener);

        try {
            dataLIST = (handleXML.fetchXml());
            plannedLIST =(plannedXML.fetchXml());
            for(Item item : dataLIST){
                latlonLIST.add(item.getCoordinates());
            }

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        getLocationPermission();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(MapsActivity.this);
        BottomNavigationView bottomnav1 = findViewById(R.id.bottom_navigation);
        bottomnav1.setOnNavigationItemSelectedListener(navListener);


    }

    private void init(){
        mSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if(actionId == EditorInfo.IME_ACTION_SEARCH || actionId ==EditorInfo.IME_ACTION_DONE || event.getAction() == KeyEvent.ACTION_DOWN || event.getAction() == KeyEvent.KEYCODE_ENTER){
                        //Search Method
                    geoLocate();
                }
                return false;
            }
        });

    }

    private void geoLocate() {
        String searchString = mSearchText.getText().toString();
        Geocoder geocoder = new Geocoder(MapsActivity.this, Locale.getDefault());

        List<Address> list = new ArrayList<>();

        try{
            list = geocoder.getFromLocationName(searchString, 1);

        }catch (IOException e){
        }

        if(list.size()>0){
            Address address = list.get(0);
            moveCamerea(new LatLng(address.getLatitude(), address.getLongitude()), 9.5f);
            mMap.addCircle(new CircleOptions().center(new LatLng(address.getLatitude(),address.getLongitude()))
            .radius(10000)
            .strokeWidth(0f)
            .fillColor(0x550000FF));
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mClusterManager = new ClusterManager(this, mMap);

        if(mLocationPermissionGranted){
            getDeviceLocation();
            mMap.setMyLocationEnabled(true);
            init();

        }

        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(MapsActivity.this));
        String[] arry;
        for(int x = 0; x < dataLIST.size(); x++){
            MarkerOptions markerOptions= new MarkerOptions()
                    .position(new LatLng(dataLIST.get(x).getLat(),dataLIST.get(x).getLon()))
                    .title(dataLIST.get(x).getTitle())
                    .snippet(dataLIST.get(x).getDescription());
            mMap.addMarker(markerOptions);

        }

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.clear();

            }
        });

        btnPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.clear();
                for(int x = 0; x < plannedLIST.size(); x++){
                    MarkerOptions markerOptions= new MarkerOptions()
                            .position(new LatLng(plannedLIST.get(x).getLat(),plannedLIST.get(x).getLon()))
                            .title(plannedLIST.get(x).getTitle())
                            .snippet(plannedLIST.get(x).getDescription());
                    mMap.addMarker(markerOptions);

                }
            }
        });



        btnRoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.clear();

                for(int x = 0; x < dataLIST.size(); x++){
                    MarkerOptions markerOptions= new MarkerOptions()
                            .position(new LatLng(dataLIST.get(x).getLat(),dataLIST.get(x).getLon()))
                            .title(dataLIST.get(x).getTitle())
                            .snippet(dataLIST.get(x).getDescription());
                    mMap.addMarker(markerOptions);
                }
            }
        });


    }
    private void moveCamerea(LatLng latlng, float zoom){
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, zoom));
    }

    private void getDeviceLocation(){
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try{
            if(mLocationPermissionGranted){
                Task location = fusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            Log.d("oncomplete", "FOUND LOCATION");
                            moveCamerea(new LatLng(55.86,-4.25), 9.5f);
                        }else{
                            Log.d("oncomplete", " LOCATION is null");

                        }
                    }
                });
            }
        }catch (SecurityException e){
            Log.e("e", "security exception" + e.getMessage());
        }

    }


    private void getLocationPermission(){
        String[] permissions ={Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

        if(ContextCompat.checkSelfPermission(this.getApplicationContext(), FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this.getApplicationContext(), COARSE_LOCATION)== PackageManager.PERMISSION_GRANTED){
                mLocationPermissionGranted = true;
            }else{
                ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
            }
        }else{
            ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode){
            case LOCATION_PERMISSION_REQUEST_CODE:
                if(grantResults.length > 0 ){
                    for(int i =0; i<grantResults.length; i++){
                       if(grantResults[i] !=PackageManager.PERMISSION_GRANTED) {
                           mLocationPermissionGranted =false;
                           return;
                       }
                    }
                    mLocationPermissionGranted =true;
                }
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener=
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment selectedFragment = null;
                    switch (menuItem.getItemId()){

                        case R.id.nav_home:
                            Intent intent = new Intent(MapsActivity.this, MainActivity.class);
                            startActivity(intent);
                            return true;

                        case R.id.nav_plan:

                            break;

                        case R.id.nav_map:

                            break;
                        default:
                            return true;


                    }
                    return true;

                }

            };



}
