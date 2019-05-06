package com.example.android.bodashops.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.android.bodashops.R;
import com.example.android.bodashops.SessionManager;
import com.example.android.bodashops.fragments.AllOrdersFragment;
import com.example.android.bodashops.fragments.PendingOrdersFragment;
import com.example.android.bodashops.fragments.PersonalAccountFragment;
import com.example.android.bodashops.fragments.ShopAccountFragment;
import com.example.android.bodashops.fragments.TodaysOrdersFragment;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class Shop extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private Intent myIntent;

    private ViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        final NavigationView mNavigationView = findViewById(R.id.nav_view);
        pager = (ViewPager) findViewById(R.id.pager);

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

        setUI();
    }

    private void setUI() {
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(new Shop.ViewPagerAdapter(getSupportFragmentManager()));
        setupViewPager(pager);
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

    private void setupViewPager(ViewPager viewPager) {
        Shop.ViewPagerAdapter adapter = new Shop.ViewPagerAdapter(getSupportFragmentManager());

        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        // tab titles
        private String[] tabTitles = new String[]{"Your Account", "Shop"};

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
                    return new PersonalAccountFragment();
                case 1:
                    return new ShopAccountFragment();
                default:
                    return null; // shouldn't happen
            }
        }

        @Override
        public int getCount() {
            return tabTitles.length;
        }
    }

}
