package com.example.android.bodashops.fragments;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.android.bodashops.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import io.paperdb.Paper;

public class PersonalAccountFragment extends Fragment {

    private StringRequest stringRequest;
    private TextView nameTv, phoneTv, emailTv;

    public PersonalAccountFragment() {
        // Required empty public constructor
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Paper.init(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View parentView = inflater.inflate(R.layout.fragment_personal_account, container, false);;

        nameTv = parentView.findViewById(R.id.nameTv);
        phoneTv = parentView.findViewById(R.id.phoneTv);
        emailTv = parentView.findViewById(R.id.emailTv);
        displayPersonalInfo();
        return parentView;
    }

    private void displayPersonalInfo(){

        String Fname =(String) Paper.book().read(Prevalent.SESSIONFIRSTNAME);
        String Lname =(String) Paper.book().read(Prevalent.SESSIONLASTNAME);
        nameTv.setText(Fname+ " "+Lname);
        phoneTv.setText((String) Paper.book().read(Prevalent.SESSIONPHONE));
        emailTv.setText((String) Paper.book().read(Prevalent.SESSIONEMAIL));
    }

}
