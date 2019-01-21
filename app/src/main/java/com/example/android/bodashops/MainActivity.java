package com.example.android.bodashops;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements ItemsFragment.OnFragmentInteractionListener,
        OrdersFragment.OnFragmentInteractionListener, NotificationsFragment.OnFragmentInteractionListener{


    private DrawerLayout mDrawerLayout;
    private BottomNavigationView mBottomNavigationView;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        NavigationView mNavigationView = findViewById(R.id.nav_view);
        mBottomNavigationView = findViewById(R.id.bottomNavigationView2);

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
                        menuItem.setChecked(true);

                        //TODO: update UI accordingly with new fragments


                        //close drawers
                        mDrawerLayout.closeDrawers();


                        return true;
                    }
                }
        );

        //home fragment
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragment = new OrdersFragment();
        fragmentTransaction.replace(R.id.frameLayout,fragment);
        //fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();



        //listen for Bottom navigationview events
        mBottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        switch (menuItem.getItemId()){
                            case R.id.orders:
                                loadFragments("orders");
                                return true;
                            case R.id.items:
                                loadFragments("items");
                                return true;
                            case R.id.notifications:
                                loadFragments("notifications");
                                return true;
                            default:
                                return false;
                        }
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



    private void loadFragments(String type) {

        switch (type){
            case "orders":
                fragment = new OrdersFragment();
                break;
            case "items":
                fragment = new ItemsFragment();
                break;
            case "notifications":
                fragment = new NotificationsFragment();
                break;
            default:
                fragment = new OrdersFragment();
        }

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.frameLayout,fragment);
        //fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
