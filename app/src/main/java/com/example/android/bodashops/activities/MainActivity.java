package com.example.android.bodashops.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.view.MenuItem;
import android.widget.Toast;

import com.example.android.bodashops.fragments.NotificationsFragment;
import com.example.android.bodashops.R;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements ItemsFragment.OnFragmentInteractionListener,
        NotificationsFragment.OnFragmentInteractionListener {


    private DrawerLayout mDrawerLayout;

    private Intent myIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                            case R.id.ordersmenu:
                                myIntent = new Intent(MainActivity.this, OrdersActivity.class);
                                startActivity(myIntent);
                                break;
                            case R.id.notificationsmenu:
                                myIntent = new Intent(MainActivity.this, OrdersActivity.class);
                                startActivity(myIntent);
                                break;
                        }

                        //close drawers
                        mDrawerLayout.closeDrawers();


                        return true;
                    }
                }
        );
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

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
