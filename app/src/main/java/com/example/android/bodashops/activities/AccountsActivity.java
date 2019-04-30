package com.example.android.bodashops.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.paperdb.Paper;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.android.bodashops.Config;
import com.example.android.bodashops.Prevalent;
import com.example.android.bodashops.R;
import com.example.android.bodashops.SessionManager;
import com.example.android.bodashops.VolleySingleton;
import com.example.android.bodashops.adapters.AccountInfoAdapter;
import com.example.android.bodashops.model.AccountModel;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AccountsActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private Intent myIntent;
    private ArrayList<AccountModel> list;

    private TextView totalView, ordersView;
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accounts);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        totalView = findViewById(R.id.totalView);
        ordersView = findViewById(R.id.ordersView);
        recyclerView = findViewById(R.id.recyclerView_accounts);
        final NavigationView mNavigationView = findViewById(R.id.nav_view);

        Paper.init(this);
        list = new ArrayList<>();

        Toast.makeText(getApplicationContext(),"Activity.onCreate()", Toast.LENGTH_SHORT).show();
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
                                myIntent = new Intent(AccountsActivity.this, MainActivity.class);
                                startActivity(myIntent);
                                break;
                            case R.id.itemsmenu:
                                myIntent = new Intent(AccountsActivity.this, ItemsActivity.class);
                                startActivity(myIntent);
                                break;
                            case R.id.shopDetails:
                                myIntent = new Intent(AccountsActivity.this, Shop.class);
                                startActivity(myIntent);
                                break;
                            case R.id.logoutmenu:
                                SessionManager.logoutOwner(AccountsActivity.this);
                                break;
                        }

                        //close drawers
                        mDrawerLayout.closeDrawers();


                        return true;
                    }
                }
        );
        getAccountSummary();
        getAccountPreviews();
    }

    private void getAccountSummary() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.ACCOUNT_SUMMARY_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject object = null;

                        try {
                            object = new JSONObject(response);

                            boolean error = object.getBoolean("error");

                            if (!error){

                                JSONObject data = object.getJSONObject("data");
                                int orders = data.getInt("orders");
                                int totalAmount = data.getInt("totalAmount");
                                String total = "Ksh. "+totalAmount;
                                String orderNumber;
                                if (orders == 1){
                                    orderNumber = orders+" order";
                                }else {
                                    orderNumber = orders+" orders";
                                }

                                ordersView.setText(orderNumber);
                                totalView.setText(total);

                            }else {
                                Toast.makeText(getApplicationContext(), "Error fetching todays summary", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(AccountsActivity.this,error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("shopId", (String) Paper.book().read(Prevalent.SESSIONSHOPID));
                return params;
            }
        };
        VolleySingleton.getInstance(AccountsActivity.this).addToRequestQue(stringRequest);
    }

    private void getAccountPreviews() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.ACCOUNT_INFO_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject object = null;

                        try {
                            object = new JSONObject(response);

                            boolean error = object.getBoolean("error");
                            String message = object.getString("message");

                            if (!error){
                                
                                JSONArray data = object.getJSONArray("data");
                                
                                if (data.length() > 0){
                                    for (int i = 0; i < data.length(); i++){
                                        JSONObject record = data.getJSONObject(i);
                                        String accId = record.getString("accId");
                                        String ownerId = record.getString("ownerId");
                                        String orderId = record.getString("orderId");
                                        String shopId = record.getString("shopId");
                                        String amount = record.getString("amount");
                                        String date = record.getString("date");

                                        AccountModel account = new AccountModel(orderId,ownerId,amount, date,accId,shopId);

                                        list.add(account);
                                    }

                                    setupRecyclerView(list);
                                }else {
                                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                }
                            }else {
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(AccountsActivity.this,error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("shopId", (String) Paper.book().read(Prevalent.SESSIONSHOPID));
                return params;
            }
        };
        VolleySingleton.getInstance(AccountsActivity.this).addToRequestQue(stringRequest);
    }

    private void setupRecyclerView(ArrayList<AccountModel> list) {
        if (list.isEmpty()){
            Toast toast = Toast.makeText(getApplicationContext(),"No accounts!",Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();
        }

        RecyclerView.Adapter mAdapter = new AccountInfoAdapter(getApplication(),list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAdapter);
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
