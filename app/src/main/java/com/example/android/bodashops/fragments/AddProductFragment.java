package com.example.android.bodashops.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.android.bodashops.Config;
import com.example.android.bodashops.ItemsHelper;
import com.example.android.bodashops.R;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.FragmentManager;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddProductFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddProductFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddProductFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String userChosenTask;
    private int REQUESTCAMERA = 0, SELECTFILE=1;

    private JsonArrayRequest jsonArrayRequest;
    private RequestQueue requestQueue;

    Spinner spinner;
    EditText mPrice, mProductName,mQty;
    Button mNext, mClear;

    private String name;
    private int quantity, price, typeId;


    ArrayList<String> types ;
    ArrayAdapter<String> adapter;

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public AddProductFragment() {

    }

    public static AddProductFragment newInstance(String param1, String param2) {
        AddProductFragment fragment = new AddProductFragment();
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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getActivity().setTitle("Add Item");

        View view = inflater.inflate(R.layout.fragment_add_product, container, false);

        setUI(view);

        return view;
    }

    private void loadSpinner() {
        types = new ArrayList<>();
        jsonArrayRequest = new JsonArrayRequest(Config.GET_TYPES_URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        for (int i = 0; i < response.length(); i++){
                            try {
                                JSONObject object = response.getJSONObject(i);
                                int index = Integer.parseInt(object.getString("typeId"));
                                String type = object.getString("type");

                                types.add(type);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        //populate spinner
                        adapter = new ArrayAdapter<String>(getContext(),R.layout.support_simple_spinner_dropdown_item,types);
                        spinner.setAdapter(adapter);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        //notify error
                    }
                });

        requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(jsonArrayRequest);
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onStart() {
        super.onStart();
        loadSpinner();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.AddProduct_NextBtn:
                next();
                break;
            case R.id.AddProduct_clearBtn:
                clear();
        }
    }

    private boolean getValues() {

        name = mProductName.getText().toString().trim();
        String strprice = mPrice.getText().toString().trim();
        String strqty = mQty.getText().toString().trim();

        if (name.isEmpty()){
            mProductName.setError("Name can't be empty!");
            mProductName.requestFocus();
            return false;
        }
        if (strqty.isEmpty()){
            mQty.setError("Quantity is required!");
            mQty.requestFocus();
            return false;
        }
        if (strprice.isEmpty()){
            mPrice.setError("Price is required!");
            mPrice.requestFocus();
            return false;
        }

        price = Integer.parseInt(strprice);
        quantity = Integer.parseInt(mQty.getText().toString().trim());
        typeId = spinner.getSelectedItemPosition();
        return true;
    }

    private void next() {
        if (getValues()){
            ProductImageFragment fragment = new ProductImageFragment();
            FragmentManager manager = getFragmentManager();
            manager.beginTransaction()
                    .replace(R.id.frameLayout, fragment)
                    .commit();
        }

    }

    private void clear() {
        mProductName.setText("");
        mQty.setText("");
        mPrice.setText("");
        spinner.refreshDrawableState();

    }

    private void setUI(View view) {
        spinner = (Spinner) view.findViewById(R.id.AddProduct_spinner_types);
        mPrice = view.findViewById(R.id.AddProduct_priceEt);
        mProductName = view.findViewById(R.id.AddProduct_nameET);
        mQty = view.findViewById(R.id.AddProduct_qtyET);
        mNext = view.findViewById(R.id.AddProduct_NextBtn);
        mClear = view.findViewById(R.id.AddProduct_clearBtn);

        mNext.setOnClickListener(this);
        mClear.setOnClickListener(this);

    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
