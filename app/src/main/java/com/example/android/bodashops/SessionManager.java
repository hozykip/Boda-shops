package com.example.android.bodashops;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.android.bodashops.activities.LoginActivity;
import com.example.android.bodashops.activities.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import io.paperdb.Paper;

public class SessionManager {
    private SharedPreferences preferences;
    private  SharedPreferences.Editor editor;
    private Context context;

    public SessionManager(Context ctx){
        this.context = ctx;
        preferences = ctx.getSharedPreferences(Config.SESSIONPREF_NAME, Config.PRIVATE_MODE);
        editor = preferences.edit();
        Paper.init(ctx);
    }

    public boolean checkSession(){
        boolean isLoggedIn = preferences.getBoolean(Config.SESSIONEXISTS_KEY, false);
        return isLoggedIn;
    }

    public void createSession(String phone, String ownerId, String firstname, String lastname, String email, String idNo, String shopId){
        if (!checkSession()){
            Paper.book().write(Prevalent.SESSIONFIRSTNAME, firstname);
            Paper.book().write(Prevalent.SESSIONLASTNAME, lastname);
            Paper.book().write(Prevalent.SESSIONEMAIL, email);
            Paper.book().write(Prevalent.SESSIONIDNO, idNo);
            Paper.book().write(Prevalent.SESSIONOWNERID, ownerId);
            Paper.book().write(Prevalent.SESSIONPHONE, phone);
            Paper.book().write(Prevalent.SESSIONSHOPID, shopId);

            editor.putBoolean(Config.SESSIONEXISTS_KEY, true);
            editor.apply();
        }
    }
    public void removeSession(){
        if (checkSession()){
            //remove session
            Paper.book().delete(Prevalent.SESSIONFIRSTNAME);
            Paper.book().delete(Prevalent.SESSIONLASTNAME);
            Paper.book().delete(Prevalent.SESSIONEMAIL);
            Paper.book().delete(Prevalent.SESSIONIDNO);
            Paper.book().delete(Prevalent.SESSIONOWNERID);
            Paper.book().delete(Prevalent.SESSIONPHONE);

            editor.putBoolean(Config.SESSIONEXISTS_KEY, false);
            editor.apply();
        }
    }

    public static void logoutOwner(final Context context) {
        final ProgressDialog progressDialog = ProgressDialog.show(context,"",
                "Logging out "+ Paper.book().read(Prevalent.SESSIONFIRSTNAME), true);

        //Server logout
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.OWNERLOGOUT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject object = null;

                        try {
                            object = new JSONObject(response);

                            boolean error = object.getBoolean("error");
                            String message = object.getString("message");

                            if (!error){
                                /*SessionManager sessionManager = new SessionManager(context);
                                sessionManager.removeSession();*/
                                new SessionManager(context).removeSession();
                                progressDialog.dismiss();
                                Toast.makeText(context, "Logged out successfully", Toast.LENGTH_SHORT).show();
                                context.startActivity(new Intent(context, LoginActivity.class));
                            }else {
                                progressDialog.dismiss();
                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            progressDialog.dismiss();
                            Toast.makeText(context, "JSON error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(context,error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("ownerId", (String) Paper.book().read(Prevalent.SESSIONOWNERID));
                return params;
            }
        };
        VolleySingleton.getInstance(context).addToRequestQue(stringRequest);
    }
}
