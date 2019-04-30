package com.example.android.bodashops.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.android.bodashops.R;
import com.example.android.bodashops.SessionManager;
import com.google.android.material.navigation.NavigationView;

public class Shop extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private Intent myIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        final NavigationView mNavigationView = findViewById(R.id.nav_view);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);

        mNavigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        int itemId = menuItem.getItemId();
                        mNavigationView.setCheckedItem(itemId);

                        switch (itemId)
                        {
                            case R.id.homeLink:
                                myIntent = new Intent(Shop.this, MainActivity.class);
                                startActivity(myIntent);
                                break;
                            case R.id.itemsmenu:
                                myIntent = new Intent(Shop.this, ItemsActivity.class);
                                startActivity(myIntent);
                                break;
                            case R.id.myAccount:
                                startActivity(new Intent(Shop.this, AccountsActivity.class));
                                break;
                            case R.id.logoutmenu:
                                SessionManager.logoutOwner(Shop.this);
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
}
