package d.pintoptech.liftnetwork.prefs;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by CHARMING on 7/13/2018.
 */

public class UserSession {
    private static final String TAG = UserSession.class.getSimpleName();
    private static final String PREF_NAME = "login_data";
    private static final String KEY_IS_LOGGED_IN = "isloggedin";
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    Context ctx;

    public UserSession(Context ctx){
        this.ctx = ctx;
        prefs = ctx.getSharedPreferences(PREF_NAME, ctx.MODE_PRIVATE);
        editor = prefs.edit();
    }

    public void setLoggedIn(Boolean isLoggedIn){
        editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn);
        editor.apply();
    }

    public boolean isUserLoggedIn(){
        return prefs.getBoolean(KEY_IS_LOGGED_IN,false);
    }
}
