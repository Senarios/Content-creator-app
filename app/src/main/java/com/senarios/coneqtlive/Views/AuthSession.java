package com.senarios.coneqtlive.Views;

import android.content.Context;
import android.content.SharedPreferences;

public class AuthSession {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String PREF_NAME = "session";
    String SESSION_KEY = "session_user";

    public AuthSession(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveSession(UserSession user) {
        String email = user.getEmail();
        editor.putString(SESSION_KEY, email).commit();
    }

    public String getSession() {
        return sharedPreferences.getString(SESSION_KEY, "abc");
    }

    public void removeSession() {
        editor.putString(SESSION_KEY, "abc");
        editor.commit();
    }
}
