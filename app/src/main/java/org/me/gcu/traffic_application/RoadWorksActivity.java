package org.me.gcu.traffic_application;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
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
import org.xmlpull.v1.XmlPullParserException;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class RoadWorksActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final int ERROR_DIALOG_REQUEST =9001;
    private RecyclerView mRecyclerView;
    private ItemRecycleAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private EditText search;
    private String urlSource = "https://trafficscotland.org/rss/feeds/plannedroadworks.aspx";
    private HandleXml obj;
    private ListView listView;
    private Calendar cal = Calendar.getInstance();
    private SimpleDateFormat sdf= new SimpleDateFormat("MMM-dd-yyyy");
    private String current_date = sdf.format(cal.getTime());
    private DatePicker datePicker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_road_works);

        search = (TextInputEditText)findViewById(R.id.inputDate);
        search.setText(current_date);
        mRecyclerView = findViewById(R.id.recView);
        obj = new HandleXml(this.urlSource);
        try {
            mAdapter = new ItemRecycleAdapter(obj.fetchXml());

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        mLayoutManager = new LinearLayoutManager(this);
        //mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        BottomNavigationView bottomnav = findViewById(R.id.bottom_navigation);
        bottomnav.setOnNavigationItemSelectedListener(navListener);



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
                            openActivity1();

                            break;

                        case R.id.nav_plan:
                            openActivity2();
                            break;

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
