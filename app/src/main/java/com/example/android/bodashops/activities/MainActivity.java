package com.example.android.bodashops.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import io.paperdb.Paper;

import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.android.bodashops.Config;
import com.example.android.bodashops.Prevalent;
import com.example.android.bodashops.SessionManager;
import com.example.android.bodashops.VolleySingleton;
import com.example.android.bodashops.fragments.AllOrdersFragment;
import com.example.android.bodashops.fragments.NotificationsFragment;
import com.example.android.bodashops.R;
import com.example.android.bodashops.fragments.PendingOrdersFragment;
import com.example.android.bodashops.fragments.TodaysOrdersFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements
        NotificationsFragment.OnFragmentInteractionListener {


    private DrawerLayout mDrawerLayout;

    private ViewPager pager;
    //private PagerSlidingTabStrip tabs;
    TabLayout tabs;

    private Intent myIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Paper.init(this);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        final NavigationView mNavigationView = findViewById(R.id.nav_view);

        Toast.makeText(getApplicationContext(),"Activity.onCreate()", Toast.LENGTH_SHORT).show();
        //set the toolbar as the actionbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //nav drawer btn
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);

        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(new MainActivity.ViewPagerAdapter(getSupportFragmentManager()));
        setupViewPager(pager);

        // Bind the tabs to the ViewPager
        tabs = (TabLayout) findViewById(R.id.tabsHome);

        tabs.setupWithViewPager(pager);

        //handle navigation click events
        mNavigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                        //set item as selected
                        //menuItem.setChecked(true);
                        int itemId = menuItem.getItemId();
                        mNavigationView.setCheckedItem(itemId);

                        switch (itemId)
                        {
                            case R.id.itemsmenu:
                                myIntent = new Intent(MainActivity.this, ItemsActivity.class);
                                startActivity(myIntent);
                                break;
                            /*case R.id.ordersmenu:
                                myIntent = new Intent(MainActivity.this, ItemsActivity.class);
                                startActivity(myIntent);
                                break;*/
                            case R.id.logoutmenu:
                                logoutOwner();
                                break;
                        }

                        //close drawers
                        mDrawerLayout.closeDrawers();


                        return true;
                    }
                }
        );
    }

    private void logoutOwner() {
        final ProgressDialog progressDialog = ProgressDialog.show(this,"",
                "Logging out "+ Paper.book().read(Prevalent.SESSIONFIRSTNAME), true);

        //Server logout
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.OWNERLOGOUT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject object = null;

                        try {
                            object = new JSONObject(response);

                            boolean error = object.getBoolean("error");
                            String message = object.getString("message");

                            if (!error){
                                SessionManager sessionManager = new SessionManager(MainActivity.this);
                                sessionManager.removeSession();
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), "Logged out successfully", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                            }else {
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "JSON error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(MainActivity.this,error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("ownerId", (String) Paper.book().read(Prevalent.SESSIONOWNERID));
                return params;
            }
        };
        VolleySingleton.getInstance(MainActivity.this).addToRequestQue(stringRequest);
    }

    private void setupViewPager(ViewPager viewPager) {
        MainActivity.ViewPagerAdapter adapter = new MainActivity.ViewPagerAdapter(getSupportFragmentManager());

        viewPager.setAdapter(adapter);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        // tab titles
        private String[] tabTitles = new String[]{"New", "Today", "All"};

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        // overriding getPageTitle()
        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new PendingOrdersFragment();
                case 1:
                    return new TodaysOrdersFragment();
                case 2:
                    return new AllOrdersFragment();
                default:
                    return null; // shouldn't happen
            }
        }

        @Override
        public int getCount() {
            return tabTitles.length;
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
