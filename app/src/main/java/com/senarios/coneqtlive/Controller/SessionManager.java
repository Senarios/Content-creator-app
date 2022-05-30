package com.senarios.coneqtlive.Controller;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.HashMap;


public class SessionManager {
    SharedPreferences preferences;
    public SharedPreferences.Editor editor;
    Context getContext;

    int PRIVATE_MODE = 0;
    private Intent startMainActivity;
    public SessionManager(final Context context, String className) {
        this.getContext = context;
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = preferences.edit();
        editor.apply();
        if (this.isLoggedIn()) {
            if (className.equals("SplashActivity")) {
                new Thread() {
                    @Override
                    public void run() {
                        super.run();

                        try {
                            sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } finally {
                            //redirect to main screen
//                            startMainActivity = new Intent(context, HomeActivity.class);
                        }
                        startMainActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    }

                }.start();
            }
        }
    }
    //CREATE LOGIN SESSION
    public void createLoginSession(String email) {
        editor.putBoolean("isLoggedIn", true);
        editor.putString("Email", email);
        editor.apply();
    }
    // LOGIN SESSION CHECK
    public boolean isLoggedIn() {
        return preferences.getBoolean("isLoggedIn", false);
    }

    public void setUserInfo(HashMap<String, String> userInfo) {
        editor.putString("Email", userInfo.get("CustomerEmail"));
        editor.putString("CustomerName", userInfo.get("CustomerName"));
        editor.putString("CustomerMobile", userInfo.get("CustomerMobile"));
        editor.putString("CustomerAddress", userInfo.get("CustomerAddress"));
        editor.putString("CustomerIdKey",userInfo.get("CustomerIdKey"));
        editor.putString("CustomerPassword",userInfo.get("CustomerPassword"));
        editor.putString("CustomerType",userInfo.get("CustomerType"));
        editor.apply();
    }
    public HashMap<String, String> getUserInfo(){
        HashMap<String, String> user = new HashMap<String, String>();
        user.put("Email", preferences.getString("Email", "FishItUser"));
        user.put("CustomerName", preferences.getString("CustomerName", "user"));
        user.put("CustomerMobile", preferences.getString("CustomerMobile", null));
        user.put("CustomerAddress",preferences.getString("CustomerAddress","Lahore"));
        user.put("CustomerIdKey",preferences.getString("CustomerIdKey","CustomerIdKey"));
        user.put("CustomerPassword",preferences.getString("CustomerPassword","CustomerPassword"));
        user.put("CustomerType",preferences.getString("CustomerType","CustomerType"));
        return user;
    }
}
