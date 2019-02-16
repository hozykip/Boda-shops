package com.example.android.bodashops.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.android.bodashops.Config;
import com.example.android.bodashops.R;
import com.example.android.bodashops.adapters.ProductsAdapter;
import com.example.android.bodashops.model.ProductModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ItemsActivity extends AppCompatActivity {

    private String JSON_URL = Config.URL_PRODUCTS;
    private ArrayList<ProductModel> productsList;
    private RecyclerView recyclerView;
    private JsonArrayRequest request;
    private RequestQueue requestQueue;
    public ProgressBar bar;
    private CoordinatorLayout coordinatorLayout;
    private FloatingActionButton mFloatingActionButtonAddItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items);

        Toolbar toolbar = findViewById(R.id.toolbarItemsActivity);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        setTitle("Items");

        mFloatingActionButtonAddItem = findViewById(R.id.floating_action_bar_add_item);
        productsList = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerView_items);
        bar = findViewById(R.id.items_loading_progressbar);
        coordinatorLayout = findViewById(R.id.items_coordinator_layout);

        jsonRequest();
        mFloatingActionButtonAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ItemsActivity.this, AddProductActivity.class));
            }
        });
    }
    private void jsonRequest() {

        request = new JsonArrayRequest(JSON_URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        try {
                            for (int i = 0; i < response.length(); i++){
                                JSONObject object = response.getJSONObject(i);

                                ProductModel product = new ProductModel(
                                        object.getString("productId"),
                                        object.getString("productName"),
                                        object.getString("price"),
                                        object.getString("quantity"),
                                        object.getString("image")
                                );

                                productsList.add(product);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        setupRecyclerView(productsList);


                    }
                }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        requestQueue = Volley.newRequestQueue(ItemsActivity.this);
        requestQueue.add(request);

    }
    private void setupRecyclerView(ArrayList<ProductModel> list){
        if (list.isEmpty()){
            Snackbar.make(coordinatorLayout,"No items!",Snackbar.LENGTH_LONG).show();
            bar.setVisibility(View.GONE);
        }
        RecyclerView.Adapter mAdapter = new ProductsAdapter(ItemsActivity.this,list,bar);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ItemsActivity.this);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(mAdapter);

    }
}
