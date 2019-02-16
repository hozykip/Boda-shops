package com.example.android.bodashops.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.android.bodashops.Config;
import com.example.android.bodashops.R;
import com.example.android.bodashops.VolleySingleton;
import com.example.android.bodashops.adapters.ProductAttributesAdapter;
import com.example.android.bodashops.model.ProductAttributesModel;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.snackbar.Snackbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProductDetailsActivity extends AppCompatActivity {

    AppCompatImageView mImageView;
    AppCompatTextView mProductName, mQty, mPrice;
    CollapsingToolbarLayout mCollapsingToolbarLayout;

    private ProgressBar bar;
    private CoordinatorLayout coordinatorLayout;
    private RecyclerView recyclerView;

    private StringRequest request;
    private ArrayList<ProductAttributesModel> productsList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        mImageView = (AppCompatImageView) findViewById(R.id.pa_thumbnail);
        mProductName = findViewById(R.id.pa_product_name);
        mQty = findViewById(R.id.pa_stock);
        mPrice = findViewById(R.id.pa_price);
        mCollapsingToolbarLayout = findViewById(R.id.collapsingtoolbar_id);
        bar = findViewById(R.id.pb_product_details);
        coordinatorLayout = findViewById(R.id.cd_layout_product_details);
        recyclerView = findViewById(R.id.recyclerView_product_details);

        Intent intent = getIntent();

        String title = intent.getStringExtra("product_name");
        String qty = intent.getStringExtra("product_qty");
        String id = intent.getStringExtra("product_id");
        String price = intent.getStringExtra("product_price");
        String img_name = intent.getStringExtra("product_img");
        String img_url = Config.IMG_BASE_URL + img_name;

        mCollapsingToolbarLayout.setTitle(title);

        RequestOptions options;
        options = new RequestOptions().placeholder(R.drawable.loading_shape).error(R.drawable.image_error);

        Glide.with(getApplicationContext()).load(img_url).apply(options).into(mImageView);
        mProductName.setText(title);
        mQty.setText("In stock: "+qty);
        mPrice.setText("Ksh. "+price);

        //FetchAttributes
        productsList = new ArrayList<>();
        getAttributes(id);
    }

    private void getAttributes(final String id)
    {
        request = new StringRequest(Request.Method.POST, Config.URL_PRODUCTS_ATTRIBUTES,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i < array.length(); i++){
                                JSONObject object = array.getJSONObject(i);
                                ProductAttributesModel product = new ProductAttributesModel(
                                        object.getString("productId"),
                                        object.getString("attribute"),
                                        object.getString("value")
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
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("id", id);

                return params;
            }
        };
        VolleySingleton.getInstance(ProductDetailsActivity.this).addToRequestQue(request);
    }

    private void setupRecyclerView(ArrayList<ProductAttributesModel> list){
        if (list.isEmpty()){
            Snackbar.make(coordinatorLayout,"No attributes!",Snackbar.LENGTH_LONG).show();
            bar.setVisibility(View.GONE);
        }
        RecyclerView.Adapter mAdapter = new ProductAttributesAdapter(ProductDetailsActivity.this,list,bar);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ProductDetailsActivity.this);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(mAdapter);

    }
}
