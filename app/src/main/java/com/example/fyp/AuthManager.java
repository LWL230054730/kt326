package com.example.fyp;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class AuthManager {
    private static final String PREFS_NAME = "auth_prefs";
    private static final String USER_PREFS_NAME = "user_prefs"; // 如果存儲用戶數據的 SharedPreferences 是不同的名稱
    private static final String KEY_TOKEN = "auth_token";
    private static final String KEY_USER_EMAIL = "user_email";
    private static final String KEY_IS_LOGGED_IN = "is_logged_in";
    private static final String KEY_FIRST_NAME = "first_name"; // 新增 Key
    private static final String KEY_LAST_NAME = "last_name"; // 新增 Key
    private static final String KEY_PHONE = "phone"; // 新增 Key
    private static Context appContext;

    // 初始化 AuthManager（在 Application 中調用）
    public static void init(Context context) {
        appContext = context.getApplicationContext(); // 使用 Application Context
    }

    // 保存令牌及用戶資訊
    public static void saveAuthToken(String token, String email) {
        SharedPreferences sharedPreferences = appContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_TOKEN, token); // 保存令牌
        editor.putString(KEY_USER_EMAIL, email); // 保存 Email
        editor.putBoolean(KEY_IS_LOGGED_IN, true); // 設定登入狀態
        editor.apply();
        Log.d("AuthManager", "Token saved: " + token + ", Email: " + email);
    }

    // 保存用戶詳細資料
    public static void saveUserDetails(String firstName, String lastName, String phone) {
        SharedPreferences sharedPreferences = appContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_FIRST_NAME, firstName);
        editor.putString(KEY_LAST_NAME, lastName);
        editor.putString(KEY_PHONE, phone);
        editor.apply();
        Log.d("AuthManager", "User details saved: First Name: " + firstName + ", Last Name: " + lastName + ", Phone: " + phone);
    }

    // 獲取令牌
    public static String getAuthToken() {
        SharedPreferences sharedPreferences = appContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_TOKEN, null);
    }

    // 獲取用戶 Email
    public static String getUserEmail() {
        SharedPreferences sharedPreferences = appContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USER_EMAIL, null);
    }

    // 獲取用戶 First Name
    public static String getUserFirstName() {
        SharedPreferences sharedPreferences = appContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_FIRST_NAME, null);
    }

    // 獲取用戶 Last Name
    public static String getUserLastName() {
        SharedPreferences sharedPreferences = appContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_LAST_NAME, null);
    }

    // 獲取用戶 Phone
    public static String getUserPhone() {
        SharedPreferences sharedPreferences = appContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_PHONE, null);
    }

    // 檢查是否已登入
    public static boolean isLoggedIn() {
        SharedPreferences sharedPreferences = appContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    // 清除登入狀態
    public static void logout() {
        // 清空 auth_prefs
        SharedPreferences sharedPreferences = appContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        // 清空 user_prefs（如果有其他存儲 Token 的地方）
        SharedPreferences userPrefs = appContext.getSharedPreferences(USER_PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor userEditor = userPrefs.edit();
        userEditor.clear();
        userEditor.apply();

        Log.d("AuthManager", "Logout called. All preferences cleared.");
    }
}