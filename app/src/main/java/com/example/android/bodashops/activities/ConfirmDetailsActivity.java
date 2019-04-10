package com.example.android.bodashops.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import com.example.android.bodashops.R;
import com.example.android.bodashops.adapters.RegistrationStepsAdapter;

public class ConfirmDetailsActivity extends AppCompatActivity {
    private final String[] views = {"Personal details", "Shop details", "Confirm your details"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_details);

        ListView mListView = (ListView) findViewById(R.id.myCustomList3);

        RegistrationStepsAdapter adapter = new RegistrationStepsAdapter(this, 1,2);
        adapter.addAll(views);

        mListView.setAdapter(adapter);
    }
}
