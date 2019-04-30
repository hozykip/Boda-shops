package com.example.android.bodashops.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.paperdb.Paper;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.TextureView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import com.example.android.bodashops.VolleySingleton;
import com.example.android.bodashops.adapters.OrderDetailsAdapter;
import com.example.android.bodashops.model.OrderDetailsModel;
import com.google.android.material.button.MaterialButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AccountOrderDetails extends AppCompatActivity {

    private TextView orderIdView, dateView, nameView, phoneView, locationView,
            itemsCountView, totalView, statusView, deliveryTypeView;
    private ProgressBar detsProgress;
    private LinearLayout holder;

    List<OrderDetailsModel> orderList;
    private RecyclerView recyclerView;

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_order_details);

        Toolbar toolbar = findViewById(R.id.toolbarOrderDets);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        setTitle("Order details");

        orderList = new ArrayList<>();

        orderIdView = (TextView) findViewById(R.id.orderId);
        dateView = (TextView) findViewById(R.id.dateOfBooking);
        nameView = (TextView) findViewById(R.id.orderPerson);
        phoneView = (TextView) findViewById(R.id.phoneNoField);
        locationView = (TextView) findViewById(R.id.location);
        itemsCountView = (TextView) findViewById(R.id.itemsCount);
        totalView = (TextView) findViewById(R.id.orderTotalPrice);
        deliveryTypeView = (TextView) findViewById(R.id.deliveryStyle);
        detsProgress = findViewById(R.id.progressBarDets);
        holder = findViewById(R.id.holderView);

        intent = getIntent();
        setUI();
    }

    private void setUI() {
        String orderId = intent.getStringExtra(Config.ORDERID);

        orderIdView.setText(orderId);
        populateViews(orderId);
        fetchOrderDetails(orderId);
    }

    private void populateViews(final String orderId) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.URL_ORDERS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONArray array;
                try {
                    array = new JSONArray(response);

                    for (int i = 0; i<array.length();i++)
                    {
                        JSONObject object = array.getJSONObject(i);

                        String id = object.getString("orderId");
                        String orderTime = object.getString("orderTime");

                        //get buyer details
                        JSONArray buyerDetails = new JSONArray(object.getString("buyerDets"));

                        JSONObject buyerObj = buyerDetails.getJSONObject(0);
                        String fName = buyerObj.getString("fName");
                        String lName = buyerObj.getString("lName");
                        String phone = "+254"+buyerObj.getString("phone");
                        String buyerLocation = buyerObj.getString("buyerLocation");

                        //get orderdetails
                        int totalOrderprice = 0;
                        JSONArray orderDetails = new JSONArray(object.getString("orderDetails"));

                        for (int j=0; j<orderDetails.length(); j++)
                        {
                            JSONObject orderObj = orderDetails.getJSONObject(j);
                            int price = Integer.parseInt(orderObj.getString("price"));
                            totalOrderprice += price;
                        }
                        String orderItemsCount = String.valueOf(orderDetails.length());
                        String orderPrice = String.valueOf(totalOrderprice);

                        String buyerName = fName+" "+lName;

                        if(id.equalsIgnoreCase(orderId)){
                            dateView.setText(orderTime);
                            nameView.setText(buyerName);
                            phoneView.setText(phone);
                            locationView.setText(buyerLocation);
                            itemsCountView.setText(orderItemsCount);
                            totalView.setText(String.valueOf(totalOrderprice));
                            holder.setVisibility(View.VISIBLE);
                            break;
                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "JSONException error in all orders", Toast.LENGTH_SHORT).show();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Volley error in all orders:\n"+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        })
        {
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("all","all");
                params.put("shopId",(String) Paper.book().read(Prevalent.SESSIONSHOPID));

                return params;
            }
        };

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQue(stringRequest);
    }

    private void fetchOrderDetails(final String id)
    {
        StringRequest request = new StringRequest(Request.Method.POST, Config.URL_ORDERS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(getApplicationContext(),"res: "+response,Toast.LENGTH_LONG).show();
                        try {

                            JSONObject object = new JSONObject(response);

                            String operation = object.getString("operation");
                            String message = object.getString("message");


                            if(operation.equalsIgnoreCase("succes"))
                            {
                                JSONArray array = object.getJSONArray("productDets");

                                if (array.length() > 0)
                                {
                                    for (int i = 0; i < array.length(); i++)
                                    {
                                        JSONObject productObj = array.getJSONObject(i);
                                        String prodId = productObj.getString("productId");
                                        String qty = productObj.getString("qty");
                                        String totalPrice = productObj.getString("totalPrice");
                                        String img = productObj.getString("img");
                                        String productName = productObj.getString("productName");



                                        OrderDetailsModel model = new OrderDetailsModel(
                                                img,productName,qty,totalPrice
                                        );
                                        orderList.add(model);
                                    }

                                    setupRecyclerView(orderList);
                                }else {
                                    Toast.makeText(getApplicationContext(),"No order details",Toast.LENGTH_LONG).show();
                                }
                            }else if(operation.equalsIgnoreCase("fail")){
                                Toast.makeText(getApplicationContext(),"Error: "+message,Toast.LENGTH_LONG).show();
                            }else{
                                Toast.makeText(getApplicationContext(),"Response error",Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        detsProgress.setVisibility(View.GONE);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        detsProgress.setVisibility(View.GONE);
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("orderId", id);
                params.put("getOrderDetails", "getDets");
                params.put("shopId",(String) Paper.book().read(Prevalent.SESSIONSHOPID));

                return params;
            }
        };

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQue(request);
    }

    private void setupRecyclerView(List<OrderDetailsModel> orderList)
    {

        if (orderList.isEmpty()){
            Toast toast = Toast.makeText(getApplicationContext(),"No order details!",Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();
        }else {
            RecyclerView.Adapter mAdapter = new OrderDetailsAdapter(AccountOrderDetails.this,orderList);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());

            recyclerView = findViewById(R.id.recyclerViewOrderDets);

            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(layoutManager);

            recyclerView.setAdapter(mAdapter);
        }
    }
}
