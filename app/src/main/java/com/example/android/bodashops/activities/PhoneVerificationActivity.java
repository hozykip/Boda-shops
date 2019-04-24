package com.example.android.bodashops.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bigbangbutton.editcodeview.EditCodeView;
import com.example.android.bodashops.Config;
import com.example.android.bodashops.R;
import com.example.android.bodashops.VolleySingleton;
import com.example.android.bodashops.adapters.RegistrationStepsAdapter;
import com.gc.materialdesign.views.ButtonFlat;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class PhoneVerificationActivity extends AppCompatActivity {

    private final String[] views = {"Personal details", "Shop details", "Confirm your details", "Verifying phone: 0702127533"};

    private ButtonFlat resendBtn;
    private EditCodeView codeView;
    private ProgressBar progressBar;
    private String mVerificationId;

    SharedPreferences pref;
    //firebase auth object
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_verification);

        ListView mListView = (ListView) findViewById(R.id.myCustomList4);
        progressBar = findViewById(R.id.progressBarVerification);
        pref = getApplication().getSharedPreferences(Config.PREF_NAME, Config.PRIVATE_MODE);

        mAuth = FirebaseAuth.getInstance();

        RegistrationStepsAdapter adapter = new RegistrationStepsAdapter(this, 1,3);
        adapter.addAll(views);

        mListView.setAdapter(adapter);

        String phonenumber = pref.getString(Config.MOBILEKEY, null);

        if (phonenumber == null) {
            Toast.makeText(getApplicationContext(), "No Number!", Toast.LENGTH_SHORT).show();
        } else {
            sendVerificationCode(phonenumber);
        }
    }

    private void sendVerificationCode(String mobile) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                mobile,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallbacks);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            //Getting the code sent by SMS
            String code = phoneAuthCredential.getSmsCode();

            //sometime the code is not detected automatically
            //in this case the code will be null
            //so user has to manually enter the code
            if (code != null) {
                progressBar.setVisibility(View.GONE);
                codeView.setCode(code);
                //verifying the code
                verifyVerificationCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(PhoneVerificationActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            //storing the verification id that is sent to the user
            mVerificationId = s;
        }

    };

    private void verifyVerificationCode(String code) {
        //creating the credential
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);

        //signing the user
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(PhoneVerificationActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //verification successful we will start the profile activity
                            createAccount(fetchAccountDetails());

                        }
                        else {

                            //verification unsuccessful.. display an error message

                            String message = "Invalid code entered...";

                            /*if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                message = "Invalid code entered...";
                            }*/

                            Snackbar snackbar = Snackbar.make(findViewById(R.id.parent), message, Snackbar.LENGTH_LONG);
                            snackbar.setAction("Dismiss", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            });
                            snackbar.show();
                        }
                    }
                });
    }
    private HashMap fetchAccountDetails()
    {
        HashMap<String, String> accountDetails = new HashMap<>();

        accountDetails.put("firstname", pref.getString(Config.FNAMEKEY, null));
        accountDetails.put("lastname", pref.getString(Config.LNAMEKEY, null));
        accountDetails.put("password", pref.getString(Config.PASSWORDKEY, null));
        accountDetails.put("email", pref.getString(Config.EMAILKEY, null));
        accountDetails.put("phone", pref.getString(Config.MOBILEKEY, null));
        accountDetails.put("shopname", pref.getString(Config.SHOPNAMEKEY, null));
        accountDetails.put("location", pref.getString(Config.SHOPLOCATIONKEY, null));
        accountDetails.put("description", pref.getString(Config.SHOPDESCRIPTIONKEY, null));

        return accountDetails;
    }

    private void createAccount(final HashMap hashMap){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.OWNERANDSHOPREGISTRATION_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject object = null;

                        try {
                            object = new JSONObject(response);

                            boolean error = object.getBoolean("error");
                            String message = object.getString("message");

                            if (!error){
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(PhoneVerificationActivity.this, LoginActivity.class));
                            }else {
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(PhoneVerificationActivity.this,error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = hashMap;

                return params;
            }
        };
        VolleySingleton.getInstance(PhoneVerificationActivity.this).addToRequestQue(stringRequest);
    }
}
