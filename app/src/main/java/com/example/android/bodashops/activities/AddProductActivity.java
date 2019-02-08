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

import com.example.android.bodashops.Config;
import com.example.android.bodashops.R;

import java.util.ArrayList;

public class AddProductActivity extends AppCompatActivity implements View.OnClickListener{

    Spinner spinner;
    EditText mPrice, mProductName,mQty;
    Button mNext, mClear;

    private String name;
    private String quantity, price, typeId;

    ArrayList<String> types ;
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

        spinner = (Spinner) findViewById(R.id.AddProduct_spinner_types);
        mPrice = findViewById(R.id.AddProduct_priceEt);
        mProductName = findViewById(R.id.AddProduct_nameET);
        mQty = findViewById(R.id.AddProduct_qtyET);
        mNext = findViewById(R.id.AddProduct_NextBtn);
        mClear = findViewById(R.id.AddProduct_clearBtn);

        mNext.setOnClickListener(this);
        mClear.setOnClickListener(this);
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
        typeId = String.valueOf(spinner.getSelectedItemPosition());
        return true;
    }
}
