package com.example.android.bodashops.activities;

import androidx.appcompat.app.AppCompatActivity;

import com.anton46.stepsview.StepsView;
import com.example.android.bodashops.R;
import com.example.android.bodashops.adapters.RegistrationStepsAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;


public class PersonalDetailsActivity extends AppCompatActivity {

    private FloatingActionButton fab;

    private final String[] views = {"Personal details"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_details);

        ListView mListView = (ListView) findViewById(R.id.myCustomList);

        RegistrationStepsAdapter adapter = new RegistrationStepsAdapter(this, 0,0);
        adapter.addAll(views);

        mListView.setAdapter(adapter);

        fab = findViewById(R.id.fab_to_shop_details);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PersonalDetailsActivity.this, ShopDetailsActivity.class));
            }
        });

    }



}
