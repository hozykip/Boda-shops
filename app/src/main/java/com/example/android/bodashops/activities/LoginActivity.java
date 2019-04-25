package com.example.android.bodashops.activities;

import androidx.appcompat.app.AppCompatActivity;
import io.paperdb.Paper;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.android.bodashops.Config;
import com.example.android.bodashops.Prevalent;
import com.example.android.bodashops.R;
import com.example.android.bodashops.SessionManager;
import com.example.android.bodashops.VolleySingleton;
import com.gc.materialdesign.views.ButtonFlat;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.scottyab.showhidepasswordedittext.ShowHidePasswordEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private ButtonFlat toRegistration, forgotPassBtn;
    private TextInputEditText phoneField;
    private ShowHidePasswordEditText passwordField;
    private CheckBox rememberCheckBox;
    private MaterialButton loginBtn;

    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        toRegistration = (ButtonFlat) findViewById(R.id.toRegistration);
        phoneField = (TextInputEditText) findViewById(R.id.phoneNumberInput);
        passwordField = (ShowHidePasswordEditText) findViewById(R.id.passwordInput);
        rememberCheckBox = (CheckBox) findViewById(R.id.rememberCheckBox);
        forgotPassBtn = (ButtonFlat) findViewById(R.id.forgotPassButton);
        loginBtn = (MaterialButton) findViewById(R.id.loginButton);

        sessionManager = new SessionManager(this);
        Paper.init(this);

        //checkSessions();
        loginBtn.setOnClickListener(this);
        toRegistration.setOnClickListener(this);
        forgotPassBtn.setOnClickListener(this);
    }

    private void checkSessions() {
        boolean session = sessionManager.checkSession();
        if (session){
            Toast.makeText(getApplicationContext(), "session exists... redirect", Toast.LENGTH_SHORT).show();
        }
        String phone = Paper.book().read(Prevalent.ownerPhoneKey);
        String password = Paper.book().read(Prevalent.ownerPasswordKey);

        if (phone != "" && password != ""){
            phoneField.setText(phone);
            passwordField.setText(password);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.loginButton:
                login();
                break;
            case R.id.toRegistration:
                gotoRegistration();
                break;
            case R.id.forgotPassButton:
                forgotPass();
                break;
            default:
                break;
        }
    }

    private void login() {

        String phone = phoneField.getText().toString().trim();
        String password = passwordField.getText().toString().trim();

        if (validate(phone, password)){
            String mobile = "+254"+phone;
            grantAccess(mobile, password);
        }else{
            Toast.makeText(getApplicationContext(), "Invalid data", Toast.LENGTH_SHORT).show();
        }
    }
    private void gotoRegistration() {
        startActivity(new Intent(LoginActivity.this, PersonalDetailsActivity.class));
    }
    private void forgotPass() {
        Toast.makeText(getApplicationContext(), "Forgot password", Toast.LENGTH_SHORT).show();
    }

    private boolean validate(String phone, String password) {
        if (phone.length() != 9){
            phoneField.setError("Must be 9 digits long");
            return false;
        }
        if(password.length() < 8){
            phoneField.setError("Must be at least 8 digits long");
            return false;
        }
        return true;
    }

    private void grantAccess(final String phone, final String password) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.OWNERANDLOGIN_URL,
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
                                JSONObject data = object.getJSONObject("data");
                                String ownerId = data.getString("ownerId");
                                String firstName = data.getString("ownerFirstName");
                                String lastName = data.getString("ownerLastName");
                                String phoneNo = data.getString("ownerPhone");
                                String id = data.getString("idNo");
                                String email = data.getString("email");

                                sessionManager.createSession(phoneNo,ownerId,firstName,lastName,email,id);
                                Toast.makeText(getApplicationContext(), "Session created", Toast.LENGTH_SHORT).show();
                                rememberOwner();
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            }else {
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    private void rememberOwner() {
                        if (rememberCheckBox.isChecked()){
                            Paper.book().write(Prevalent.ownerPhoneKey,phone);
                            Paper.book().write(Prevalent.ownerPasswordKey,password);
                        }else {
                            Paper.book().write(Prevalent.ownerPhoneKey,"");
                            Paper.book().write(Prevalent.ownerPasswordKey,"");
                        }
                        Toast.makeText(getApplicationContext(), "Remembered you", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(LoginActivity.this,error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("phone", phone);
                params.put("password", password);

                return params;
            }
        };
        VolleySingleton.getInstance(LoginActivity.this).addToRequestQue(stringRequest);
    }
}
