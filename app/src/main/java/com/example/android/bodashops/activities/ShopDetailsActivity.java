package com.example.android.bodashops.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.example.android.bodashops.R;
import com.example.android.bodashops.adapters.RegistrationStepsAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ShopDetailsActivity extends AppCompatActivity {

    private final String[] views = {"Personal details", "Shop details"};
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_details);

        ListView mListView = (ListView) findViewById(R.id.myCustomList2);
        fab = (FloatingActionButton) findViewById(R.id.fab_goto_confirm_dets_activity);

        RegistrationStepsAdapter adapter = new RegistrationStepsAdapter(this, 1,1);
        adapter.addAll(views);

        mListView.setAdapter(adapter);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ShopDetailsActivity.this,ConfirmDetailsActivity.class));
            }
        });
    }
}
