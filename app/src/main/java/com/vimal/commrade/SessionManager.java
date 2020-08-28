package com.vimal.commrade;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

public class SessionManager {

    SharedPreferences userSession;
    SharedPreferences.Editor editor;
    Context context;
    private static final String Is_Login = "IsLoggedIn";
    public static final String KEY_NAME = "Name";
    public static final String KEY_MOBILE = "Mobile";
    public static final String KEY_GENDER = "Gender";


    public SessionManager(Context _context) {
        context = _context;
        userSession = context.getSharedPreferences("userLogInSession", Context.MODE_PRIVATE);
        editor = userSession.edit();
    }

    public void createuserSession(String Name, String Mobile, String Gender) {
        editor.putBoolean(Is_Login, true);
        editor.putString(KEY_NAME, Name);
        editor.putString(KEY_MOBILE, Mobile);
        editor.putString(KEY_GENDER, Gender);
        editor.commit();

    }

    public HashMap<String, String> getUserDetailsFromSession() {
        HashMap<String, String> userData = new HashMap<String, String>();
        userData.put(KEY_NAME, userSession.getString(KEY_NAME, null));
        userData.put(KEY_MOBILE, userSession.getString(KEY_MOBILE, null));
        userData.put(KEY_GENDER, userSession.getString(KEY_GENDER, null));

        return userData;
    }

    public boolean checkLogin() {
        if (userSession.getBoolean(Is_Login, false)) {
            return true;
        } else {
            return false;
        }

    }

    public void Logout() {
        editor.clear();
        editor.commit();
    }

}
