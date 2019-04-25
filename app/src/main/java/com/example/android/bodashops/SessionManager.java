package com.example.android.bodashops;

import android.content.Context;
import android.content.SharedPreferences;

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

    public void createSession(String phone, String ownerId, String firstname, String lastname, String email, String idNo){
        if (!checkSession()){
            Paper.book().write(Prevalent.SESSIONFIRSTNAME, firstname);
            Paper.book().write(Prevalent.SESSIONLASTNAME, lastname);
            Paper.book().write(Prevalent.SESSIONEMAIL, email);
            Paper.book().write(Prevalent.SESSIONIDNO, idNo);
            Paper.book().write(Prevalent.SESSIONOWNERID, ownerId);
            Paper.book().write(Prevalent.SESSIONPHONE, phone);

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
}
