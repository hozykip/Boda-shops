package com.example.android.bodashops.fragments;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.paperdb.Paper;

import android.text.TextUtils;
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
import com.example.android.bodashops.adapters.OrdersAdapter;
import com.example.android.bodashops.model.OrdersModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TodaysOrdersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TodaysOrdersFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private RecyclerView recyclerView;
    private ArrayList<OrdersModel> ordersList;
    private StringRequest stringRequest;

    private String orderId, buyerId, orderLocation, orderStatus, orderTime, fName, lName, phone, buyerLocation,
            registeredOn, qty, orderPrice, orderItemsCount;

    private ViewGroup parentContainer;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public TodaysOrdersFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TodaysOrdersFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TodaysOrdersFragment newInstance(String param1, String param2) {
        TodaysOrdersFragment fragment = new TodaysOrdersFragment();
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
        View parentView = inflater.inflate(R.layout.fragment_todays_orders, container, false);

        parentContainer = container;

        recyclerView = parentView.findViewById(R.id.todaysOrdersRecyclerview);
        ordersList.clear();
        fetchOrders();
        return parentView;
    }

    private void fetchOrders()
    {

        stringRequest = new StringRequest(Request.Method.POST, Config.URL_ORDERS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject obj = null;

                try{
                    obj = new JSONObject(response);

                    String outcome = obj.getString("operation");
                    String message = obj.getString("message");

                    if (TextUtils.equals(message,"Order dets!")){

                        //fetch the array of orders
                        JSONArray arr = obj.getJSONArray("orderDetails");

                        //loop through the array fetching details
                        for(int j = 0; j<arr.length(); j++)
                        {
                            JSONObject obj2 = arr.getJSONObject(j);

                            orderId = obj2.getString("orderId");
                            buyerId = obj2.getString("buyerId");
                            orderLocation = obj2.getString("location");
                            orderStatus = obj2.getString("orderCompletion");
                            orderTime = obj2.getString("orderTime");

                            //get buyer dets
                            JSONArray buyerDets = new JSONArray(obj2.getString("buyerDets"));
                            JSONObject buyerObj = buyerDets.getJSONObject(0);
                            fName = buyerObj.getString("fName");
                            lName = buyerObj.getString("lName");
                            phone = "+254"+buyerObj.getString("phone");
                            buyerLocation = buyerObj.getString("buyerLocation");
                            registeredOn = buyerObj.getString("registeredOn");

                            //get orderdetails
                            int totalOrderprice = 0;
                            JSONArray orderDetails = new JSONArray(obj2.getString("orderDetails"));

                            for (int k=0; k<orderDetails.length(); k++)
                            {
                                JSONObject orderObj = orderDetails.getJSONObject(k);
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
                        }

                        setupRecyclerView(ordersList);

                    }else {
                        Toast.makeText(getContext(), message,Toast.LENGTH_LONG).show();
                    }

                }catch (JSONException e){
                    Toast.makeText(getContext(), e.getMessage(),Toast.LENGTH_LONG).show();
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "VolleyError error in today's orders: \n"+error.getCause(), Toast.LENGTH_SHORT).show();
            }
        })
        {
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("today","all");
                params.put("shopId",(String) Paper.book().read(Prevalent.SESSIONSHOPID));

                return params;
            }
        };

        VolleySingleton.getInstance(getContext()).addToRequestQue(stringRequest);
    }

    private void setupRecyclerView(ArrayList<OrdersModel> list)
    {
        if (list.isEmpty()){

        }
        RecyclerView.Adapter mAdapter = new OrdersAdapter(getContext(),list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(mAdapter);
    }

    private void showNoOrdersView() {
        Toast toast = Toast.makeText(getContext(),"No Orders Today!",Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();
        getLayoutInflater().inflate(R.layout.no_orders_today,parentContainer,false);
    }

}
