package com.example.android.bodashops.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.android.bodashops.Config;
import com.example.android.bodashops.R;
import com.example.android.bodashops.VolleySingleton;
import com.example.android.bodashops.adapters.RegistrationStepsAdapter;
import com.gc.materialdesign.views.Card;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.scottyab.showhidepasswordedittext.ShowHidePasswordEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ConfirmDetailsActivity extends AppCompatActivity {
    private final String[] views = {"Personal details", "Shop details", "Confirm your details"};
    private FloatingActionButton fab;

    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    private TextInputEditText   fNameEditText, lNameEditText, phoneEditText, emailEditText,
                                shopNameEditText, locationEditText, descriptionEditText, idEditText;
    private ShowHidePasswordEditText passwordEditText;
    private String  $fName, $lName, $phone, $email, $password,
                    $shopName, $location, $description, $id;
    private Card card;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_details);

        preferences = getApplication().getSharedPreferences(Config.PREF_NAME, Config.PRIVATE_MODE);

        ListView mListView = (ListView) findViewById(R.id.myCustomList3);
        fab = (FloatingActionButton) findViewById(R.id.fab_goto_phone_verification_activity);
        fNameEditText = (TextInputEditText) findViewById(R.id.firstNameConfirm);
        lNameEditText = (TextInputEditText) findViewById(R.id.lastNameConfirm);
        phoneEditText = (TextInputEditText) findViewById(R.id.phoneConfirm);
        emailEditText = (TextInputEditText) findViewById(R.id.emailConfirm);
        passwordEditText = (ShowHidePasswordEditText) findViewById(R.id.passwordConfirm);
        shopNameEditText = (TextInputEditText) findViewById(R.id.shopNameConfirm);
        locationEditText = (TextInputEditText) findViewById(R.id.locationConfirm);
        descriptionEditText = (TextInputEditText) findViewById(R.id.shopDescriptionConfirm);
        idEditText = (TextInputEditText) findViewById(R.id.idConfirm);

        card = (Card) findViewById(R.id.processingCard);

        RegistrationStepsAdapter adapter = new RegistrationStepsAdapter(this, 1,2);
        adapter.addAll(views);

        mListView.setAdapter(adapter);

        displayData();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //fetchData();
                card.setVisibility(View.VISIBLE);
                createAccount(fetchAccountDetails());
            }
        });

        //fetchSavedData();
    }

    private void fetchData() {
        $fName = fNameEditText.getText().toString();
        $lName = lNameEditText.getText().toString();
        $phone = phoneEditText.getText().toString();
        $email = emailEditText.getText().toString();
        $password = passwordEditText.getText().toString();
        $shopName = shopNameEditText.getText().toString();
        $location = locationEditText.getText().toString();
        $description = descriptionEditText.getText().toString();
        $id = idEditText.getText().toString();

        if (validateData()){
            storeData();
        }
    }

    private void storeData() {
        editor = preferences.edit();
        editor.putString(Config.FNAMEKEY, $fName);
        editor.putString(Config.LNAMEKEY, $lName);
        editor.putString(Config.MOBILEKEY, $phone);
        editor.putString(Config.EMAILKEY, $email);
        editor.putString(Config.PASSWORDKEY, $password);
        editor.putString(Config.SHOPNAMEKEY, $shopName);
        editor.putString(Config.SHOPLOCATIONKEY, $location);
        editor.putString(Config.SHOPDESCRIPTIONKEY, $description);
        editor.putString(Config.IDKEY, $id);
        editor.apply();

        routeToPhoneVerification();
    }

    private void routeToPhoneVerification() {
        startActivity(new Intent(ConfirmDetailsActivity.this, PhoneVerificationActivity.class));
    }

    private void fetchSavedData() {
        String defaultStr = "Nothing";
        $fName = preferences.getString(Config.FNAMEKEY,defaultStr);
        $lName = preferences.getString(Config.LNAMEKEY,defaultStr);
        $phone = preferences.getString(Config.MOBILEKEY,defaultStr);
        $email = preferences.getString(Config.EMAILKEY,defaultStr);
        $password = preferences.getString(Config.PASSWORDKEY,defaultStr);
        $shopName = preferences.getString(Config.SHOPNAMEKEY,defaultStr);
        $location = preferences.getString(Config.SHOPLOCATIONKEY,defaultStr);
        $description = preferences.getString(Config.SHOPDESCRIPTIONKEY,defaultStr);
        $id = preferences.getString(Config.IDKEY,defaultStr);

        displayData();
    }

    private void displayData() {
        /*fNameEditText.setText($fName);
        lNameEditText.setText($lName);
        phoneEditText.setText($phone);
        emailEditText.setText($email);
        passwordEditText.setText($password);
        shopNameEditText.setText($shopName);
        locationEditText.setText($location);
        descriptionEditText.setText($description);
        idEditText.setText($id);*/

        fNameEditText.setText(preferences.getString(Config.FNAMEKEY, null));
        lNameEditText.setText(preferences.getString(Config.LNAMEKEY, null));
        phoneEditText.setText(preferences.getString(Config.MOBILEKEY, null));
        emailEditText.setText(preferences.getString(Config.EMAILKEY, null));
        passwordEditText.setText(preferences.getString(Config.PASSWORDKEY, null));
        shopNameEditText.setText(preferences.getString(Config.SHOPNAMEKEY, null));
        locationEditText.setText(preferences.getString(Config.SHOPLOCATIONKEY, null));
        descriptionEditText.setText(preferences.getString(Config.SHOPDESCRIPTIONKEY, null));
        idEditText.setText(preferences.getString(Config.IDKEY, null));
    }

    private void createAccount(final HashMap hashMap){


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.OWNERANDSHOPREGISTRATION_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject object = null;

                        card.setVisibility(View.GONE);

                        try {
                            object = new JSONObject(response);

                            boolean error = object.getBoolean("error");
                            String message = object.getString("message");

                            if (!error){
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(ConfirmDetailsActivity.this, LoginActivity.class));
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
                        card.setVisibility(View.GONE);
                        Toast.makeText(ConfirmDetailsActivity.this,error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = hashMap;

                return params;
            }
        };
        VolleySingleton.getInstance(ConfirmDetailsActivity.this).addToRequestQue(stringRequest);
    }

    private HashMap fetchAccountDetails()
    {
        HashMap<String, String> accountDetails = new HashMap<>();

        accountDetails.put("firstname", preferences.getString(Config.FNAMEKEY, null));
        accountDetails.put("lastname", preferences.getString(Config.LNAMEKEY, null));
        accountDetails.put("password", preferences.getString(Config.PASSWORDKEY, null));
        accountDetails.put("email", preferences.getString(Config.EMAILKEY, null));
        accountDetails.put("phone", preferences.getString(Config.MOBILEKEY, null));
        accountDetails.put("shopname", preferences.getString(Config.SHOPNAMEKEY, null));
        accountDetails.put("location", preferences.getString(Config.SHOPLOCATIONKEY, null));
        accountDetails.put("description", preferences.getString(Config.SHOPDESCRIPTIONKEY, null));
        accountDetails.put("id", preferences.getString(Config.IDKEY, null));

        return accountDetails;
    }

    private boolean validateData(){
        if (TextUtils.isEmpty($fName)){
            Toast.makeText(getApplicationContext(),"First name is required",Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty($lName)){
            Toast.makeText(getApplicationContext(),"Last name is required",Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty($id)){
            Toast.makeText(getApplicationContext(),"ID number is required",Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty($phone)){
            Toast.makeText(getApplicationContext(),"Phone number is required",Toast.LENGTH_SHORT).show();
            return false;
        }
        if ($phone.length() != 13){
            Toast.makeText(getApplicationContext(),"Check the phone number, must begin with country code",Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty($email)){
            Toast.makeText(getApplicationContext(),"Email address is required",Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher($email).matches()){
            emailEditText.setError("Enter a valid email address");
            return false;
        }
        if (TextUtils.isEmpty($password)){
            Toast.makeText(getApplicationContext(),"Password is required",Toast.LENGTH_SHORT).show();
            return false;
        }
        if ($password.length() < 8){
            Toast.makeText(getApplicationContext(),"Password should be at least 8 characters long",Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty($shopName)){
            Toast.makeText(getApplicationContext(),"Shop name is required",Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty($location)){
            Toast.makeText(getApplicationContext(),"Location is required",Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty($description)){
            Toast.makeText(getApplicationContext(),"Shop description is required",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
