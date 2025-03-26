package com.example.fyp;

import android.app.Application;

public class Starter extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AuthManager.init(this); // 確保初始化
    }
}
