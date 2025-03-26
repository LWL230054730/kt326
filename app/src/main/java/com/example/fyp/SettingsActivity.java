package com.example.fyp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.switchmaterial.SwitchMaterial;

public class SettingsActivity extends AppCompatActivity {

    private ImageButton backButton; // 返回按鈕
    private SwitchMaterial trainingReminderSwitch; // 訓練提醒開關
    private SwitchMaterial communityNotificationsSwitch; // 社群通知開關
    private Button editProfileButton; // 編輯個人資料按鈕
    private Button changePasswordButton; // 更改密碼按鈕
    private Button privacyPolicyButton; // 隱私政策按鈕
    private Button termsButton; // 服務條款按鈕
    private Button logoutButton; // 登出按鈕

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // 初始化控件
        backButton = findViewById(R.id.backButton);
        trainingReminderSwitch = findViewById(R.id.trainingReminderSwitch);
        communityNotificationsSwitch = findViewById(R.id.communityNotificationsSwitch);
        editProfileButton = findViewById(R.id.editProfileButton);
        changePasswordButton = findViewById(R.id.changePasswordButton);
        privacyPolicyButton = findViewById(R.id.privacyPolicyButton);
        termsButton = findViewById(R.id.termsButton);
        logoutButton = findViewById(R.id.logoutButton);

        // 設置返回按鈕的點擊事件
        backButton.setOnClickListener(v -> finish()); // 返回到上一頁

        // 設置開關的初始狀態
        trainingReminderSwitch.setChecked(true); // 默認啟用訓練提醒
        communityNotificationsSwitch.setChecked(false); // 默認禁用社群通知

        // 設置開關的切換事件
        trainingReminderSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            Toast.makeText(SettingsActivity.this,
                    "Training Reminder: " + (isChecked ? "Enabled" : "Disabled"),
                    Toast.LENGTH_SHORT).show();
        });

        communityNotificationsSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            Toast.makeText(SettingsActivity.this,
                    "Community Notifications: " + (isChecked ? "Enabled" : "Disabled"),
                    Toast.LENGTH_SHORT).show();
        });

        // 設置按鈕的點擊事件
        editProfileButton.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this, EditProfileActivity.class);
            startActivity(intent);
        });

        changePasswordButton.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this, ChangePasswordActivity.class);
            startActivity(intent);
        });

        privacyPolicyButton.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this, PrivacyPolicyActivity.class);
            startActivity(intent);
        });

        termsButton.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this, TermsOfServiceActivity.class);
            startActivity(intent);
        });

        logoutButton.setOnClickListener(v -> handleLogout());
    }

    // 登出邏輯
    private void handleLogout() {
        AuthManager.logout();

        // 跳轉到登錄頁面
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

        // 顯示登出成功提示
        Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();
    }
}