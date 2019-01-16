package com.example.android.bodashops;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ItemsActivity extends AppCompatActivity {

    /* - _ */
    TextView allProductsTvLink, addNewProductTvLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items);

        allProductsTvLink = (TextView) findViewById(R.id.allProductsLinkTv);
        addNewProductTvLink = (TextView) findViewById(R.id.addNewProductLinkTv);

        allProductsTvLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ItemsActivity.this, AllProducts.class);
                startActivity(intent);
            }
        });

    }
}
