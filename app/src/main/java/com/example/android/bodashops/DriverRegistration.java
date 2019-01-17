package com.example.android.bodashops;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class DriverRegistration extends AppCompatActivity {

    EditText mOwnerNameET, mOwnerIdET, mOwnerPhoneNoET;
    Button submitOwnerDetsBtn;

    String ownerIdNo, ownerPhoneNo, ownerName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_registration);

        mOwnerNameET = (EditText) findViewById(R.id.ownerNameET);
        mOwnerIdET = (EditText) findViewById(R.id.ownerIdET);
        mOwnerPhoneNoET = (EditText) findViewById(R.id.ownerPhoneNoET);
        submitOwnerDetsBtn = (Button) findViewById(R.id.submitOwnerDetsBtn);

        submitOwnerDetsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ownerPhoneNo = mOwnerPhoneNoET.getText().toString();
                ownerIdNo = mOwnerIdET.getText().toString();
                ownerName = mOwnerNameET.getText().toString();

                int length = ownerPhoneNo.length();

                if (length != 10){
                    mOwnerPhoneNoET.setError("Phone number must be 10 digits");
                    return;
                }
                Intent intent = new Intent(DriverRegistration.this, PhoneVerification.class);
                intent.putExtra(Config.OWNERPHONENOKEY,ownerPhoneNo);
                startActivity(intent);
            }
        });

    }
}
