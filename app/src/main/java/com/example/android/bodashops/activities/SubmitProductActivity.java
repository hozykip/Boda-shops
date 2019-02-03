package com.example.android.bodashops.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import androidx.appcompat.widget.Toolbar;

import com.example.android.bodashops.R;

public class SubmitProductActivity extends AppCompatActivity {

    LinearLayout parentLinearLayout;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_product);

        parentLinearLayout = findViewById(R.id.parentLinearLayout);

        toolbar = (Toolbar) findViewById(R.id.toolbarSubmitActivity);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Submit products");
    }

    public void addRowDetailsField(View view) {
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.fieldrow,null);

        parentLinearLayout.addView(rowView,parentLinearLayout.getChildCount() - 3);
    }

    public void deleteRowField(View view) {
        parentLinearLayout.removeView((View) view.getParent());
    }
}
