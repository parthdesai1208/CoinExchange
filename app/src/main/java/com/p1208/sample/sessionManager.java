package com.p1208.sample;

import android.content.Context;
import android.content.SharedPreferences;

class SessionManager {

    //  public static SessionManager sessionManager;

    // Shared Preferences
    //  private SharedPreferences pref;

    // Editor for Shared preferences
    private static SharedPreferences.Editor editor;

    //require
    private SessionManager() {
    }

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "is_login";

   /* public static SessionManager getInstance() {
        if (sessionManager == null) {
            sessionManager = new SessionManager();
        }
        return sessionManager;
    }*/

/*
    public void initSessionManager(Context context) {
        this.mContext = context.getApplicationContext();
        pref = mContext.getSharedPreferences(context.getApplicationInfo().packageName, PRIVATE_MODE);
        editor = pref.edit();
        editor.apply();
    }
*/

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(context.getApplicationInfo().packageName, Context.MODE_PRIVATE);
    }

    public static void savelogin(Context mContext, String name, String password) {
        editor = getSharedPreferences(mContext).edit();
        editor.putString(Utill.NAME_KEY, name);
        editor.putString(Utill.PASS_KEY, password);
        editor.apply();
    }

    public static void savePref(Context mContext, String name, int age, String password, String imagePath) {
        editor = getSharedPreferences(mContext).edit();
        editor.putString(Utill.NAME_KEY, name);
        editor.putInt(Utill.AGE_KEY, age);
        editor.putString(Utill.PASS_KEY, password);
        editor.putString(Utill.IMAGE_KEY, imagePath);
        editor.apply();
    }

    public static void setProfileimagepath(Context mContext, String profileimagepath) {
        editor = getSharedPreferences(mContext).edit();
        editor.putString(Utill.IMAGE_KEY, profileimagepath);
        editor.apply();
    }

    public static String getProfileImagePath(Context mContext) {
        return getSharedPreferences(mContext).getString(Utill.IMAGE_KEY, "");
    }

    public static void setimagepath1(Context mContext, String img) {
        editor = getSharedPreferences(mContext).edit();
        editor.putString(Utill.IMAGE_KEY1, img);
        editor.apply();
    }

    public static String getimagepath1(Context mContext) {
        return getSharedPreferences(mContext).getString(Utill.IMAGE_KEY1, "");
    }

    public static void setimagepath2(Context mContext, String img) {
        editor = getSharedPreferences(mContext).edit();
        editor.putString(Utill.IMAGE_KEY2, img);
        editor.apply();
    }

    public static String getimagepath2(Context mContext) {
        return getSharedPreferences(mContext).getString(Utill.IMAGE_KEY2, "");
    }

    public static void setimagepath3(Context mContext, String img) {
        editor = getSharedPreferences(mContext).edit();
        editor.putString(Utill.IMAGE_KEY3, img);
        editor.apply();
    }

    public static String getimagepath3(Context mContext) {
        return getSharedPreferences(mContext).getString(Utill.IMAGE_KEY3, "");
    }

    public static void setimagepath4(Context mContext, String img) {
        editor = getSharedPreferences(mContext).edit();
        editor.putString(Utill.IMAGE_KEY4, img);
        editor.apply();
    }

    public static String getimagepath4(Context mContext) {
        return getSharedPreferences(mContext).getString(Utill.IMAGE_KEY4, "");
    }

    public static void setimagecount(Context mContext, int mcount) {
        editor = getSharedPreferences(mContext).edit();
        editor.putInt(Utill.IMAGECOUNTKEY, mcount);
        editor.apply();
    }

    public static int getimagecount(Context mContext) {
        return getSharedPreferences(mContext).getInt(Utill.IMAGECOUNTKEY, 0);
    }

    public static String getname(Context mContext) {
        return getSharedPreferences(mContext).getString(Utill.NAME_KEY, "Name");
    }

    public static String getpassword(Context mContext) {
        return getSharedPreferences(mContext).getString(Utill.PASS_KEY, "Password");
    }

    public static int getAge(Context mContext) {
        return getSharedPreferences(mContext).getInt(Utill.AGE_KEY, 0);
    }

    public static void startSession(Context mContext) {
        editor = getSharedPreferences(mContext).edit();
        editor.putBoolean(IS_LOGIN, true);
        editor.apply();
    }

    public static boolean isLoggedIn(Context mContext) {
        return getSharedPreferences(mContext).getBoolean(IS_LOGIN, false);
    }

    /**
     * Clear session details
     */
    public static void clearsession(Context mContext) {
        editor = getSharedPreferences(mContext).edit();
        editor.putBoolean(IS_LOGIN, false);
        editor.apply();
    }

    public static void logout(Context mContext) {
        editor = getSharedPreferences(mContext).edit();
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.apply();
    }

}