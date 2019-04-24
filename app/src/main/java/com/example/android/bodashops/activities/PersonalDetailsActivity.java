package com.example.android.bodashops.activities;

import androidx.appcompat.app.AppCompatActivity;

import com.anton46.stepsview.StepsView;
import com.example.android.bodashops.Config;
import com.example.android.bodashops.R;
import com.example.android.bodashops.adapters.RegistrationStepsAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.scottyab.showhidepasswordedittext.ShowHidePasswordEditText;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class PersonalDetailsActivity extends AppCompatActivity {

    private FloatingActionButton fab;

    private TextInputEditText fNameEditText, lNameEditText, phoneEditText, emailEditText, idEditText;
    private ShowHidePasswordEditText passwordEditText;

    private String $fName, $lName, $phone, $email, $password, $id;

    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    private final String[] views = {"Personal details"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_details);

        preferences = getApplication().getSharedPreferences(Config.PREF_NAME, Config.PRIVATE_MODE);

        ListView mListView = (ListView) findViewById(R.id.myCustomList);
        fNameEditText = (TextInputEditText) findViewById(R.id.firstNameEditText);
        lNameEditText = (TextInputEditText) findViewById(R.id.lastNameEditText);
        phoneEditText = (TextInputEditText) findViewById(R.id.phoneEditText);
        emailEditText = (TextInputEditText) findViewById(R.id.emailEditText);
        passwordEditText = (ShowHidePasswordEditText) findViewById(R.id.passwordEditText);
        idEditText = (TextInputEditText) findViewById(R.id.idEditText);

        RegistrationStepsAdapter adapter = new RegistrationStepsAdapter(this, 0,0);
        adapter.addAll(views);

        mListView.setAdapter(adapter);

        fab = findViewById(R.id.fab_to_shop_details);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchData();
            }
        });

    }

    private void fetchData() {
        $fName = fNameEditText.getText().toString();
        $lName = lNameEditText.getText().toString();
        $phone = phoneEditText.getText().toString();
        $email = emailEditText.getText().toString();
        $password = passwordEditText.getText().toString();
        $id = idEditText.getText().toString();

        if (validateData()){
            storeData();
        }
    }

    private void storeData() {
        editor = preferences.edit();
        String $mobile = "+254"+$phone;
        editor.putString(Config.FNAMEKEY, $fName);
        editor.putString(Config.LNAMEKEY, $lName);
        editor.putString(Config.MOBILEKEY, $mobile);
        editor.putString(Config.EMAILKEY, $email);
        editor.putString(Config.PASSWORDKEY, $password);
        editor.putString(Config.IDKEY, $id);
        editor.apply();

        routeToShopDetails();
    }

    private void routeToShopDetails() {
        startActivity(new Intent(PersonalDetailsActivity.this, ShopDetailsActivity.class));
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
            Toast.makeText(getApplicationContext(),"Id Number is required",Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty($phone)){
            Toast.makeText(getApplicationContext(),"Phone number is required",Toast.LENGTH_SHORT).show();
            return false;
        }
        if ($phone.length() != 9){
            Toast.makeText(getApplicationContext(),"Phone number should be 9 digits long",Toast.LENGTH_SHORT).show();
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
        return true;
    }


}
