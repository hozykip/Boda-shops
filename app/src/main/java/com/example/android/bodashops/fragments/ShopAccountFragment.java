package com.example.android.bodashops.fragments;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import io.paperdb.Paper;

public class ShopAccountFragment extends Fragment {

    TextView nameTv, locationTv, descTv, catTv;


    public ShopAccountFragment() {
        // Required empty public constructor
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Paper.init(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_shop_account, container, false);
        nameTv = v.findViewById(R.id.nameTv);
        locationTv = v.findViewById(R.id.locationTv);
        descTv = v.findViewById(R.id.descrptionTv);
        catTv = v.findViewById(R.id.catTv);
        fetchOrders();
        return v;
    }

    private void fetchOrders()
    {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.SHOP_INFO_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject object = new JSONObject(response);

                    boolean error = object.getBoolean("error");
                    String message = object.getString("message");

                    if (!error){
                        JSONObject data = object.getJSONObject("data");

                        String shopName = data.getString("shopName");
                        String shopLocation = data.getString("shopLocation");
                        String description = data.getString("description");
                        String category = data.getString("category");

                        nameTv.setText(shopName);
                        locationTv.setText(shopLocation);
                        descTv.setText(description);
                        catTv.setText(category);

                    }else {
                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "JSONException error", Toast.LENGTH_SHORT).show();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Volley error:\n"+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        })
        {
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("shopId",(String) Paper.book().read(Prevalent.SESSIONSHOPID));
                return params;
            }
        };

        VolleySingleton.getInstance(getContext()).addToRequestQue(stringRequest);
    }

}
