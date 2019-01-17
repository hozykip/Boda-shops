package com.example.android.bodashops;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    CardView homeLinkTv, itemsLinkTv, ordersLinkTv, accsLinkTv, notificationsLinkTv, historyLinkTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        homeLinkTv = (CardView) findViewById(R.id.homeLink);
        itemsLinkTv = (CardView) findViewById(R.id.itemsLink);
        ordersLinkTv = (CardView) findViewById(R.id.ordersLink);
        accsLinkTv = (CardView) findViewById(R.id.accountsLink);
        notificationsLinkTv = (CardView) findViewById(R.id.notificationsLink);
        historyLinkTv = (CardView) findViewById(R.id.historyLink);

        itemsLinkTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ItemsActivity.class);
                startActivity(intent);
            }
        });
        ordersLinkTv.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                startActivity(new Intent(MainActivity.this, DriverRegistration.class));
            }
        });
    }
}
