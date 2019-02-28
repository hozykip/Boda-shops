package com.example.android.bodashops.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

//import com.afollestad.materialdialogs.MaterialDialog;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
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
    private FloatingActionButton fab_delete, fab_edit;

    private ProgressBar bar;
    private CoordinatorLayout coordinatorLayout;
    private RecyclerView recyclerView;

    private StringRequest request;
    private ArrayList<ProductAttributesModel> productsList;

    private String id, title, qty, price, img_name, img_url;
    ProgressDialog progressDialog;
    StringRequest stringRequest;
    Boolean attributesloaded = false;


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
        fab_edit = findViewById(R.id.fab_product_details_edit);
        fab_delete = findViewById(R.id.fab_product_details_delete);

        Intent intent = getIntent();

        title = intent.getStringExtra("product_name");
        qty = intent.getStringExtra("product_qty");
        id = intent.getStringExtra("product_id");
        price = intent.getStringExtra("product_price");
        img_name = intent.getStringExtra("product_img");
        img_url = Config.IMG_BASE_URL + img_name;

        fab_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductDetailsActivity.this, EditProductActivity.class);
                intent.putExtra(Config.PRODIDKEY,id);
                intent.putExtra(Config.PRODNAMEKEY,title);
                intent.putExtra(Config.PRICEKEY,price);
                intent.putExtra(Config.QTYKEY,qty);
                intent.putExtra(Config.IMG_BASE_URL,img_name);

                startActivity(intent);
            }
        });

        fab_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ProductDetailsActivity.this);
                builder.setTitle("Warning!");
                builder.setIcon(R.drawable.ic_warning_black_24dp);
                builder.setMessage("Are you sure you want to delete product "+title+"?");
                builder.setCancelable(false);
                // Add the buttons
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked delete button
                        progressDialog = ProgressDialog.show(ProductDetailsActivity.this, "",
                                "Deleting. Please wait...", true);
                        deleteProduct();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
                // Set other dialog properties
                // Create the AlertDialog
                AlertDialog dialog = builder.create();
                dialog.show();
                dialog.getButton(dialog.BUTTON_NEGATIVE).setTextColor(Color.BLUE);
                //dialog.getButton(dialog.BUTTON_NEGATIVE).setTextColor(Color.BLUE);
            }
        });

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

    private void deleteProduct()
    {
        stringRequest = new StringRequest(Request.Method.POST, Config.DELETE_PRODUCT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject(response);

                            String operation = object.getString("operation");
                            String message = object.getString("message");

                            if (operation.contains("success"))
                            {
                                progressDialog.dismiss();
                                Toast toast = Toast.makeText(getApplicationContext(),message, Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();

                                startActivity(new Intent(ProductDetailsActivity.this, ItemsActivity.class));
                                finish();

                            }else if(operation.contains("fail"))
                            {
                                progressDialog.dismiss();

                                AlertDialog.Builder builder = new AlertDialog.Builder(ProductDetailsActivity.this);
                                builder.setTitle("Operation failed!");
                                builder.setIcon(R.drawable.ic_error_outline_black_24dp);
                                builder.setMessage(message+"\nRetry?");
                                builder.setCancelable(false);
                                builder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        progressDialog = ProgressDialog.show(ProductDetailsActivity.this, "",
                                                "Deleting. Please wait...", true);
                                        deleteProduct();
                                    }
                                });
                                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        //nothing
                                    }
                                });

                                AlertDialog dialog = builder.create();
                                dialog.show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ProductDetailsActivity.this,error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("prodId",id);

                return params;
            }
        };

        VolleySingleton.getInstance(ProductDetailsActivity.this).addToRequestQue(stringRequest);

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
                Toast.makeText(ProductDetailsActivity.this,error.getMessage(),Toast.LENGTH_LONG).show();
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
