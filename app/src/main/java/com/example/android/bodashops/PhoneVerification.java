package com.example.android.bodashops;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class PhoneVerification extends AppCompatActivity {

    Button mOkPhoneNoBtn, mChangePhoneNo;
    TextView mPhoneForVerificationTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_verification);

        mOkPhoneNoBtn = (Button) findViewById(R.id.okPhoneNo);
        mChangePhoneNo = (Button) findViewById(R.id.changePhoneNo);
        mPhoneForVerificationTv = (TextView) findViewById(R.id.phoneForVerificationTv);

        //fetch the owner phone number to be verified
        Intent intent = getIntent();
        String ownerPhoneNo = intent.getStringExtra(Config.OWNERPHONENOKEY);

        mPhoneForVerificationTv.setText(ownerPhoneNo);

        mOkPhoneNoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PhoneVerification.this, VerifyingPhone.class));
            }
        });
    }
}
