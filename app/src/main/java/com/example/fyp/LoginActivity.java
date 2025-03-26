package com.example.fyp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText emailInput; // Email 輸入框
    private TextInputEditText passwordInput; // 密碼輸入框
    private ProgressBar progressBar; // 進度條
    private TextView registerLink; // 註冊連結

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 初始化控件
        emailInput = findViewById(R.id.emailInputEditText); // 確保 ID 和 XML 文件一致
        passwordInput = findViewById(R.id.passwordEditText);
        progressBar = findViewById(R.id.progressBar);
        registerLink = findViewById(R.id.registerLink);

        // 設置登錄按鈕的點擊事件
        findViewById(R.id.loginButton).setOnClickListener(v -> handleLogin());

        // 註冊連結點擊事件，跳轉到註冊頁面
        registerLink.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    private void handleLogin() {
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        // 驗證輸入
        if (!isInputValid(email, password)) {
            return;
        }

        // 顯示進度條
        progressBar.setVisibility(View.VISIBLE);

        // 調用 API 進行登錄
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<LoginResponse> call = apiService.loginUser(new LoginRequest(email, password));

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                // 隱藏進度條
                progressBar.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse loginResponse = response.body();

                    // 保存用戶登入狀態
                    saveLoginState(loginResponse);

                    Toast.makeText(LoginActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();

                    // 跳轉到主頁面
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    // 顯示登錄失敗提示
                    Toast.makeText(LoginActivity.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                // 隱藏進度條
                progressBar.setVisibility(View.GONE);

                // 顯示網絡錯誤提示
                Toast.makeText(LoginActivity.this, "Failed to connect to the server", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // 驗證輸入是否有效
    private boolean isInputValid(String email, String password) {
        if (email.isEmpty()) {
            emailInput.setError("Email is required");
            return false;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailInput.setError("Invalid email format");
            return false;
        }

        if (password.isEmpty()) {
            passwordInput.setError("Password is required");
            return false;
        }

        if (password.length() < 6) {
            passwordInput.setError("Password must be at least 6 characters");
            return false;
        }

        return true;
    }

    // 保存登錄狀態到 AuthManager
    private void saveLoginState(LoginResponse loginResponse) {
        // 使用 AuthManager 保存令牌和用戶數據
        AuthManager.saveAuthToken(
                loginResponse.getToken(), // 保存返回的令牌
                loginResponse.getUser().getEmail() // 保存用戶 Email
        );
    }
}