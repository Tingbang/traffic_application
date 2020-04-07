package org.me.gcu.traffic_application;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;

import org.me.gcu.traffic_application.Adapter.ItemRecycleAdapter;
import org.me.gcu.traffic_application.models.DatePicker;
import org.me.gcu.traffic_application.models.Item;
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
    private ListView listView;
    private Calendar cal = Calendar.getInstance();
    private SimpleDateFormat sdf= new SimpleDateFormat("EEE, dd MMM");
    private String current_date = sdf.format(cal.getTime());
    private DatePicker datePicker;

    DatePickerDialog.OnDateSetListener setListener;

    private ArrayList<Item> test;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        search = (TextInputEditText)findViewById(R.id.inputDate);
        search.setText(current_date);
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
                //month=month+1;
                String date = String.format("%02d/%02d/%d ", day,month,year);

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
        this.obj2 = new HandleXml("https://trafficscotland.org/rss/feeds/plannedroadworks.aspx");
        try {
            mAdapter = new ItemRecycleAdapter(obj.fetchXml());
            //this.test =(obj.fetchXml());
            //System.out.println(obj.getTitle());
            System.out.println("TESTING12CHECK");

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);


    }

    public void openActivity1() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }

    public void openActivity2() {
        Intent intent = new Intent(this, RoadWorksActivity.class);
        startActivity(intent);

    }





    private BottomNavigationView.OnNavigationItemSelectedListener navListener=
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment selectedFragment = null;
                    switch (menuItem.getItemId()){
                        case R.id.nav_home:
                            ArrayList<Item> newlist1 = new ArrayList<>();
                            try {
                                newlist1 = obj.fetchXml();
                            } catch (XmlPullParserException e) {
                                e.printStackTrace();
                            }

                            mAdapter.RemoveData(newlist1);
                            //mAdapter = new ItemRecycleAdapter(newlist);
                            mAdapter.updateData(newlist1);
                            return true;

                        case R.id.nav_plan:
                            ArrayList<Item> newlist = new ArrayList<>();
                            try {
                                newlist = obj2.fetchXml();
                            } catch (XmlPullParserException e) {
                                e.printStackTrace();
                            }

                            //mAdapter.RemoveData(newlist);
                            //mAdapter = new ItemRecycleAdapter(newlist);
                            mAdapter.updateData(newlist);
                            return true;

                        case R.id.nav_map:
                            //Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                            //selectedFragment.startActivity();
                            //selectedFragment = new MapFragment();
                            break;

                    }
                    // getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectedFragment).commit();
                    return false;
                }

            };
}
