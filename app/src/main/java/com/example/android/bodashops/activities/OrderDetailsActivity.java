package com.example.android.bodashops.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.paperdb.Paper;

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

public class OrderDetailsActivity extends AppCompatActivity {

    private TextView orderIdView, dateView, nameView, phoneView, locationView,
                        itemsCountView, totalView, statusView, deliveryTypeView;

    List<OrderDetailsModel> orderList;
    private MaterialButton completeButton;

    private ProgressBar detsProgress;
    private RecyclerView recyclerView;
    private ProgressDialog dialog;

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        Toolbar toolbar = findViewById(R.id.toolbarOrderDets);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        setTitle("Order details");

        Paper.init(this);

        orderList = new ArrayList<>();
        orderIdView = (TextView) findViewById(R.id.orderId);
        dateView = (TextView) findViewById(R.id.dateOfBooking);
        nameView = (TextView) findViewById(R.id.orderPerson);
        phoneView = (TextView) findViewById(R.id.phoneNoField);
        locationView = (TextView) findViewById(R.id.location);
        itemsCountView = (TextView) findViewById(R.id.itemsCount);
        totalView = (TextView) findViewById(R.id.orderTotalPrice);
        //statusView = (TextView) findViewById(R.id.orderStatus);
        deliveryTypeView = (TextView) findViewById(R.id.deliveryStyle);
        completeButton = (MaterialButton) findViewById(R.id.buttonCompleteOrder);
        intent = getIntent();
        String completed = intent.getStringExtra(Config.ORDERCOMPLETED);

        if (completed.equalsIgnoreCase("Completed")){
            completeButton.setVisibility(View.GONE);
        }

        detsProgress = findViewById(R.id.progressBarDets);

        //statusView.setVisibility(View.GONE);

        populateViews();
        completeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                completeOrder();
            }
        });
    }

    private void completeOrder() {

        dialog = ProgressDialog.show(this,"", "Completing order...", true);
        Intent intent = getIntent();

        final String $orderId = intent.getStringExtra(Config.ORDERID);
        final String $amount = intent.getStringExtra(Config.ORDERTOTALPRICE);
        final String $ownerId = Paper.book().read(Prevalent.SESSIONOWNERID);

        StringRequest request = new StringRequest(Request.Method.POST, Config.URLCOMPLETE_ORDERS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject jsonObject;

                        dialog.dismiss();

                        try {
                            jsonObject = new JSONObject(response);

                            boolean error = jsonObject.getBoolean("error");
                            String message = jsonObject.getString("message");

                            if (!error){
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            }else {
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("orderId", $orderId);
                hashMap.put("ownerId", $ownerId);
                hashMap.put("amount", $amount);
                return hashMap;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQue(request);
    }

    private void populateViews()
    {
        Intent intent = getIntent();

        String $orderId = intent.getStringExtra(Config.ORDERID);
        String $date = intent.getStringExtra(Config.ORDERTIME);
        String $name = intent.getStringExtra(Config.BUYERNAME);
        String $phone = intent.getStringExtra(Config.BUYERPHONE);
        String $location = intent.getStringExtra(Config.ORDERLOCATION);
        String $itemsCount = intent.getStringExtra(Config.ITEMSCOUNT);
        String $totalAmt = intent.getStringExtra(Config.ORDERTOTALPRICE);

        orderIdView.setText($orderId);
        dateView.setText($date);
        nameView.setText($name);
        phoneView.setText($phone);
        locationView.setText($location);
        itemsCountView.setText($itemsCount);
        totalView.setText($totalAmt);

        fetchOrderDetails($orderId);
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
            RecyclerView.Adapter mAdapter = new OrderDetailsAdapter(OrderDetailsActivity.this,orderList);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());

            recyclerView = findViewById(R.id.recyclerViewOrderDets);

            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(layoutManager);

            recyclerView.setAdapter(mAdapter);
        }
    }
}
