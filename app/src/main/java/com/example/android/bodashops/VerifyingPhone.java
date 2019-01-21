package com.example.android.bodashops;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

public class VerifyingPhone extends AppCompatActivity {

    Boolean verificationFlag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verifying_phone);

        //fetch the phone number to be verified
        Intent intent = getIntent();
        String phoneNo = intent.getStringExtra(Config.OWNERPHONENOKEY);

        sendVerification(phoneNo);

    }

    private void sendVerification(String phoneNo) {
        /*try {
            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    phoneNo,
                    60,
                    TimeUnit.SECONDS,
                    mCallBacks
            );
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
        }

        //verification flag

        verificationFlag = true;
        onSaveInstanceState(verificationFlag);*/
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallBacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

        }

        @Override
        public void onVerificationFailed(FirebaseException e) {

        }
    };
}
