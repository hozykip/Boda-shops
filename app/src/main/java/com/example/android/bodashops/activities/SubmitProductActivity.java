package com.example.android.bodashops.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.android.bodashops.Config;
import com.example.android.bodashops.R;
import com.example.android.bodashops.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SubmitProductActivity extends AppCompatActivity {

    LinearLayout parentLinearLayout;
    private Button submitBtn;
    private ArrayList attributes;

    private String productName, imgBitmapStr;
    private String productQty, productPrice, productType;

    private JSONArray jsonArrayAttributes;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_product);

        parentLinearLayout = findViewById(R.id.parentLinearLayout);
        submitBtn = findViewById(R.id.submitProductBtn);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getProductDetails();
            }
        });

        toolbar = (Toolbar) findViewById(R.id.toolbarSubmitActivity);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Submit products");

    }

    private void getProductDetails()
    {
        ArrayList allAttr = getItems();
        if (!allAttr.isEmpty())
        {
            //product main details
            Intent intent = getIntent();
            productName = intent.getStringExtra(Config.PRODNAMEKEY);
            productPrice = intent.getStringExtra(Config.PRICEKEY);
            productQty = intent.getStringExtra(Config.QTYKEY);
            productType = intent.getStringExtra(Config.TYPEKEY);
            imgBitmapStr = intent.getStringExtra(Config.IMAGEBITMAPSTRING);

            //product attributes
            jsonArrayAttributes = new JSONArray(allAttr);

            uploadItem();
        }
    }

    private ArrayList getItems()
    {
        attributes = new ArrayList();
        for (int i = 0; i < parentLinearLayout.getChildCount() -3; i++)
        {
            View innerLayout = parentLinearLayout.getChildAt(i);

            EditText attrET = innerLayout.findViewById(R.id.attributeField);
            EditText valET = innerLayout.findViewById(R.id.valueField);

            String attr = attrET.getText().toString();
            String val = valET.getText().toString().trim();

            if (attr.isEmpty() || attr == "" || val.isEmpty() || val == "" )
            {
                attributes.clear();
                attrET.setError("Fill all fields");
                valET.setError("Fill all fields");
            }else {
                JSONObject object = new JSONObject();
                try {
                    object.put("key",attr);
                    object.put("value", val);

                    attributes.add(object);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(SubmitProductActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        }
        return attributes;
    }

    public void addRowDetailsField(View view) {
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.fieldrow,null);

        parentLinearLayout.addView(rowView,parentLinearLayout.getChildCount() - 3);
    }

    public void deleteRowField(View view) {
        parentLinearLayout.removeView((View) view.getParent());
    }

    private void uploadItem()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.URL_UPLOAD_PRODUCT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(SubmitProductActivity.this,response,Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SubmitProductActivity.this,error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                productType="1";
                params.put("prodName",productName);
                params.put("prodType",productType);
                params.put("prodQty",productQty);
                params.put("prodPrice",productPrice);
                params.put("productImageStringBitmap", imgBitmapStr);
                params.put("productAttributes", jsonArrayAttributes.toString());

                return params;
            }
        };
        VolleySingleton.getInstance(SubmitProductActivity.this).addToRequestQue(stringRequest);
    }
}
