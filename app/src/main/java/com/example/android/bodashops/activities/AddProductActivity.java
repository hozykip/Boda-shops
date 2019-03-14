package com.example.android.bodashops.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
import com.example.android.bodashops.VolleySingleton;
import com.jaredrummler.materialspinner.MaterialSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AddProductActivity extends AppCompatActivity implements View.OnClickListener{

    //Spinner spinner;
    private MaterialSpinner spinner;
    EditText mPrice, mProductName,mQty;
    Button mNext, mClear;

    private String name;
    private String quantity, price, typeId;


    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        setUI();
    }

    private void setUI()
    {
        Toolbar toolbar = findViewById(R.id.toolbarAddProductAct);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        spinner = (MaterialSpinner) findViewById(R.id.AddProduct_spinner_types);
        mPrice = findViewById(R.id.AddProduct_priceEt);
        mProductName = findViewById(R.id.AddProduct_nameET);
        mQty = findViewById(R.id.AddProduct_qtyET);
        mNext = findViewById(R.id.AddProduct_NextBtn);
        mClear = findViewById(R.id.AddProduct_clearBtn);

        getTypes();


        mNext.setOnClickListener(this);
        mClear.setOnClickListener(this);
    }

    private void getTypes()
    {
        final ArrayList<String> producttypes = new ArrayList<>();
        JsonArrayRequest jsonArrayRequest;
        RequestQueue requestQueue;
        jsonArrayRequest = new JsonArrayRequest(Config.GET_TYPES_URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        for (int i = 0; i < response.length(); i++){
                            try {
                                JSONObject object = response.getJSONObject(i);
                                String type = object.getString("type");

                                producttypes.add(type);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        setTypes(producttypes);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQue(jsonArrayRequest);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void setTypes(ArrayList<String> productTypes)
    {
        spinner.setItems(productTypes);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId()){
            case R.id.AddProduct_NextBtn:
                next();
                break;
            case R.id.AddProduct_clearBtn:
                clear();
        }
    }

    private void clear()
    {
        mProductName.setText("");
        mQty.setText("");
        mPrice.setText("");
        spinner.refreshDrawableState();
    }

    private void next()
    {
        if (getValues())
        {
            Intent intent = new Intent(AddProductActivity.this, ProductImageActivity.class);
            intent.putExtra(Config.PRODNAMEKEY,name);
            intent.putExtra(Config.QTYKEY, quantity);
            intent.putExtra(Config.TYPEKEY,typeId);
            intent.putExtra(Config.PRICEKEY,price);

            //intent to camera activity
            startActivity(intent);
        }
    }

    private boolean getValues()
    {
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

        price = strprice;
        quantity = strqty;
        typeId = String.valueOf(spinner.getSelectedIndex());
        return true;
    }
}
