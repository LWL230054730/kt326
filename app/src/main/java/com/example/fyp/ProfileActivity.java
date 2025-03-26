package com.example.fyp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import de.hdodenhof.circleimageview.CircleImageView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    private CircleImageView profileImage; // 用戶頭像
    private TextView userNameText;       // 用戶名稱
    private TextView userBioText;        // 用戶簡介
    private TextView followersCountText; // 追蹤者數量
    private TextView followingCountText; // 追蹤中數量
    private TextView postsCountText;     // 帖子數量
    private ImageButton backButton;      // 返回按鈕
    private ImageButton settingsButton;  // 設定按鈕

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // 初始化控件
        profileImage = findViewById(R.id.profileImage);
        userNameText = findViewById(R.id.userNameText);
        userBioText = findViewById(R.id.userBioText);
        followersCountText = findViewById(R.id.followersCountText);
        followingCountText = findViewById(R.id.followingCountText);
        postsCountText = findViewById(R.id.postsCountText);
        backButton = findViewById(R.id.backButton);
        settingsButton = findViewById(R.id.settingsButton);

        // 檢查是否已登入
        if (!AuthManager.isLoggedIn()) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        String token = AuthManager.getAuthToken();
        if (token == null || token.isEmpty()) {
            Toast.makeText(this, "Authentication token missing. Please log in again.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        Log.d("ProfileActivity", "Loaded token: " + token);

        // 加載用戶資料
        loadUserData();

        // 註冊點擊事件處理器
        backButton.setOnClickListener(v -> onBackPressed()); // 返回到上一頁
        settingsButton.setOnClickListener(v -> openSettings()); // 打開設定頁
    }

    // 從後端加載用戶資料
    private void loadUserData() {
        String token = AuthManager.getAuthToken();
        if (token == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        // 創建 API 客戶端
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<UserProfileResponse> call = apiService.getUserProfile("Bearer " + token);

        // 發送 API 請求
        call.enqueue(new Callback<UserProfileResponse>() {
            @Override
            public void onResponse(Call<UserProfileResponse> call, Response<UserProfileResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    UserProfile user = response.body().getData();
                    updateUI(user); // 更新界面
                } else {
                    Toast.makeText(ProfileActivity.this, "Failed to load user data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserProfileResponse> call, Throwable t) {
                Toast.makeText(ProfileActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // 更新界面數據
    private void updateUI(UserProfile user) {
        userNameText.setText(user.getUserName());
        userBioText.setText(user.getBio());
        followersCountText.setText("Followers: " + user.getFollowersCount());
        followingCountText.setText("Following: " + user.getFollowingCount());
        postsCountText.setText("Posts: " + user.getPostsCount());

        // 使用 Glide 加載頭像
        Glide.with(this)
                .load(user.getAvatarUrl())
                .placeholder(R.drawable.default_avatar)
                .into(profileImage);
    }

    // 打開設定頁面
    private void openSettings() {
        Intent intent = new Intent(ProfileActivity.this, SettingsActivity.class);
        startActivity(intent);
    }
}