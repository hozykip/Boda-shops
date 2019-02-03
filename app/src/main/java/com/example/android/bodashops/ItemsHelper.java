package com.example.android.bodashops;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ItemsHelper {
    private static JsonArrayRequest jsonArrayRequest;
    private static ArrayList<String> types;
    private static RequestQueue requestQueue;

    public static ArrayList<String> getTypes(Context context){
        types = new ArrayList<>();
        jsonArrayRequest = new JsonArrayRequest(Config.GET_TYPES_URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        for (int i = 0; i < response.length(); i++){
                            try {
                                JSONObject object = response.getJSONObject(i);
                                int index = Integer.parseInt(object.getString("typeId"));
                                String type = object.getString("type");

                                types.add(type);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

        requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(jsonArrayRequest);
        return types;
    }
}
