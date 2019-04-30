package com.example.android.bodashops.fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.paperdb.Paper;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.android.bodashops.activities.ItemsActivity;
import com.example.android.bodashops.adapters.OrdersAdapter;
import com.example.android.bodashops.model.OrdersModel;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PendingOrdersFragment extends Fragment
{

    private StringRequest stringRequest;
    private ArrayList<OrdersModel> ordersList;
    private RecyclerView recyclerView;
    private ProgressDialog dialog;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private String orderId, buyerId, orderLocation, orderStatus, orderTime, fName, lName, phone, buyerLocation,
            registeredOn, qty, orderPrice, orderItemsCount;

    public PendingOrdersFragment() {
        // Required empty public constructor
    }

    public static PendingOrdersFragment newInstance(String param1, String param2) {
        PendingOrdersFragment fragment = new PendingOrdersFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        ordersList = new ArrayList<>();
        Paper.init(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View parentView = inflater.inflate(R.layout.fragment_pending_orders, container, false);
        recyclerView = parentView.findViewById(R.id.recycler_pending);
        ordersList.clear();
        fetchOrders();
        return parentView;

    }

    private void fetchOrders()
    {
        dialog = ProgressDialog.show(getContext(), "","Fetching orders. Please wait...", true);

        stringRequest = new StringRequest(Request.Method.POST, Config.URL_ORDERS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                //Toast.makeText(getContext(),response,Toast.LENGTH_LONG).show();

                JSONArray array;
                try {
                    array = new JSONArray(response);

                    if (array.length() == 0){
                        dialog.dismiss();
                    }

                    for (int i = 0; i<array.length();i++)
                    {
                        JSONObject object = array.getJSONObject(i);

                        orderId = object.getString("orderId");
                        buyerId = object.getString("buyerId");
                        orderLocation = object.getString("location");
                        orderStatus = object.getString("orderCompletion");
                        orderTime = object.getString("orderTime");

                        //get buyer details
                        JSONArray buyerDetails = new JSONArray(object.getString("buyerDets"));

                        JSONObject buyerObj = buyerDetails.getJSONObject(0);
                        fName = buyerObj.getString("fName");
                        lName = buyerObj.getString("lName");
                        phone = "+254"+buyerObj.getString("phone");
                        buyerLocation = buyerObj.getString("buyerLocation");
                        registeredOn = buyerObj.getString("registeredOn");

                        //get orderdetails
                        int totalOrderprice = 0;
                        JSONArray orderDetails = new JSONArray(object.getString("orderDetails"));

                        for (int j=0; j<orderDetails.length(); j++)
                        {
                            JSONObject orderObj = orderDetails.getJSONObject(j);
                            int price = Integer.parseInt(orderObj.getString("price"));
                            totalOrderprice += price;
                        }
                        orderItemsCount = String.valueOf(orderDetails.length());
                        orderPrice = String.valueOf(totalOrderprice);

                        String buyerName = fName+" "+lName;

                        //create model object
                        OrdersModel model = new OrdersModel(
                                orderId,buyerId,orderStatus,orderTime,orderPrice,buyerName,phone,orderItemsCount,orderLocation
                        );
                        //populate list
                        ordersList.add(model);

                        if (i == (array.length() - 1))
                        {
                            dialog.dismiss();
                            //Toast.makeText(getContext(),"The last",Toast.LENGTH_LONG).show();
                        }


                    }

                    setupRecyclerView(ordersList);

                } catch (JSONException e) {
                    e.printStackTrace();
                    dialog.dismiss();
                    Toast.makeText(getActivity(), "JSON Error", Toast.LENGTH_SHORT).show();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                Toast.makeText(getActivity(), "Volley error in pending:\n "+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        })
        {
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("pending","pending");
                params.put("shopId",(String) Paper.book().read(Prevalent.SESSIONSHOPID));

                return params;
            }
        };

        VolleySingleton.getInstance(getContext()).addToRequestQue(stringRequest);
    }

    private void setupRecyclerView(ArrayList<OrdersModel> list)
    {
        if (list.isEmpty()){
            //Snackbar.make(coordinatorLayout,"No items!",Snackbar.LENGTH_LONG).show();
            Toast toast = Toast.makeText(getContext(),"No Orders!",Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();
            //bar.setVisibility(View.GONE);
        }
        RecyclerView.Adapter mAdapter = new OrdersAdapter(getContext(),list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(mAdapter);
    }

}
