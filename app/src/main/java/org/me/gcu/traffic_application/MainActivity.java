// Student Name: Darren Lally
// Student ID: S1622370
package org.me.gcu.traffic_application;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;

import org.me.gcu.traffic_application.Adapter.ItemRecycleAdapter;
import org.me.gcu.traffic_application.Classes.Item;
import org.xmlpull.v1.XmlPullParserException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final int ERROR_DIALOG_REQUEST =9001;
    private RecyclerView mRecyclerView;
    private ItemRecycleAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private EditText search;
    private String urlSource = "https://trafficscotland.org/rss/feeds/plannedroadworks.aspx";
    private HandleXml obj;
    private HandleXml obj2;
    private HandleXml obj3;
    private TextView heading;
    private Calendar cal = Calendar.getInstance();
    private SimpleDateFormat sdf= new SimpleDateFormat("EEE, dd MMM");
    private String current_date = sdf.format(cal.getTime());

    DatePickerDialog.OnDateSetListener setListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        search = (TextInputEditText)findViewById(R.id.inputDate);
        search.setText(current_date);
        heading = (TextView)findViewById(R.id.heading);
        heading.setText("Planned Roadworks");

        if(isMapOk()){
            Log.i("map", "Map is ok");
        }else{
            Log.i("map", "Map isnt ok");

        }
        init();
        BottomNavigationView bottomnav = findViewById(R.id.bottom_navigation);
        bottomnav.setOnNavigationItemSelectedListener(navListener);


        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this,android.R.style.Theme_Holo_Light_Dialog_MinWidth,setListener,year,month,day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });

        setListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(android.widget.DatePicker view, int year, int month, int day) {

                Calendar calendar2 = Calendar.getInstance();
                calendar2.set(year, month, day);
                SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM");
                String dateString = sdf.format(calendar2.getTime());
                search.setText(dateString);
                mAdapter.getFilter().filter(dateString);


            }
        };
    }

    public void init(){
        mRecyclerView = findViewById(R.id.recView);
        obj = new HandleXml(this.urlSource);
        obj2 = new HandleXml("https://trafficscotland.org/rss/feeds/roadworks.aspx");
        obj3 = new HandleXml("https://trafficscotland.org/rss/feeds/currentincidents.aspx");
        try {
            mAdapter = new ItemRecycleAdapter(obj.fetchXml(),obj2.fetchXml());
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

    }

    public boolean isMapOk(){
        Log.d(TAG,"Is services okay: checking google version");
        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MainActivity.this);

        if(available == ConnectionResult.SUCCESS){
            return true;
        }else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            Log.d(TAG,"An error occurred: Fixable");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(MainActivity.this,available, ERROR_DIALOG_REQUEST);
            dialog.show();
        }else {
            Log.d(TAG,"You can't make map requests");
        }
        return false;
    }


    private BottomNavigationView.OnNavigationItemSelectedListener navListener=
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    switch (menuItem.getItemId()){
                        case R.id.nav_home:
                            try {
                                ArrayList<Item> newlist1 = new ArrayList<>(obj.fetchXml());
                                mAdapter.updateData(newlist1);
                            } catch (XmlPullParserException e) {
                                e.printStackTrace();
                            }
                            return true;

                        case R.id.nav_plan:

                            try {
                                ArrayList<Item> newlist = new ArrayList<>(obj2.fetchXml());
                                mAdapter.updateData(newlist);
                                heading.setText("Roadworks");

                            } catch (XmlPullParserException e) {
                                e.printStackTrace();
                            }

                            return true;

                        case R.id.nav_live:
                            try {
                                ArrayList<Item> newlist = new ArrayList<>(obj3.fetchXml());
                                mAdapter.updateData(newlist);
                                heading.setText("Current Incidents - No Data");

                            } catch (XmlPullParserException e) {
                                e.printStackTrace();
                            }
                            return true;


                        case R.id.nav_map:
                            Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                            startActivity(intent);

                            return true;


                    }
                    return false;
                }

            };
}
