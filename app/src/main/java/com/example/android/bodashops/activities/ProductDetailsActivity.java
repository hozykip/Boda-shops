package com.example.android.bodashops.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.android.bodashops.Config;
import com.example.android.bodashops.R;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class ProductDetailsActivity extends AppCompatActivity {

    AppCompatImageView mImageView;
    AppCompatTextView mProductName, mQty, mPrice;
    CollapsingToolbarLayout mCollapsingToolbarLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        mImageView = (AppCompatImageView) findViewById(R.id.pa_thumbnail);
        mProductName = findViewById(R.id.pa_product_name);
        mQty = findViewById(R.id.pa_stock);
        mPrice = findViewById(R.id.pa_price);
        mCollapsingToolbarLayout = findViewById(R.id.collapsingtoolbar_id);

        Intent intent = getIntent();

        String title = intent.getStringExtra("product_name");
        String qty = intent.getStringExtra("product_qty");
        String id = intent.getStringExtra("product_id");
        String price = intent.getStringExtra("product_price");
        String img_name = intent.getStringExtra("product_img");
        String img_url = Config.IMG_BASE_URL + img_name;

        mCollapsingToolbarLayout.setTitle(title);

        RequestOptions options;
        options = new RequestOptions().placeholder(R.drawable.loading_shape).error(R.drawable.image_error);

        Glide.with(getApplicationContext()).load(img_url).apply(options).into(mImageView);
        mProductName.setText(title);
        mQty.setText("In stock: "+qty);
        mPrice.setText("Ksh. "+price);

    }
}
