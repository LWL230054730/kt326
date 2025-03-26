package com.example.fyp;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    private EditText usernameEditText;
    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText emailEditText;
    private EditText phoneEditText;
    private EditText passwordEditText;
    private Button registerButton;
    private ApiService apiService;

    public EditText confirmPasswordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // 初始化 API Service
        apiService = ApiClient.getClient().create(ApiService.class);

        // 初始化視圖
        initViews();

        // 設置點擊事件
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }

    private void initViews() {
        usernameEditText = findViewById(R.id.usernameEditText);
        firstNameEditText = findViewById(R.id.firstNameEditText);
        lastNameEditText = findViewById(R.id.lastNameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        registerButton = findViewById(R.id.registerButton);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
    }

    private void showSuccessMessage() {
        Snackbar snackbar = Snackbar.make(
                findViewById(android.R.id.content),
                "Registration Successful!",
                Snackbar.LENGTH_LONG
        );

        // 設置 Snackbar 樣式
        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(getResources().getColor(R.color.green_500));
        TextView textView = snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        textView.setTextSize(16);

        snackbar.show();
    }

    private void showErrorMessage(String errorMessage) {
        Snackbar snackbar = Snackbar.make(
                findViewById(android.R.id.content),
                errorMessage,
                Snackbar.LENGTH_LONG
        );

        // 設置 Snackbar 樣式
        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(getResources().getColor(R.color.red));
        TextView textView = snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        textView.setTextSize(16);

        snackbar.show();
    }

    private boolean validateInput() {
        // 檢查用戶名
        if (usernameEditText.getText().toString().trim().isEmpty()) {
            usernameEditText.setError("Username is required");
            return false;
        }

        // 檢查名字
        if (firstNameEditText.getText().toString().trim().isEmpty()) {
            firstNameEditText.setError("First name is required");
            return false;
        }

        // 檢查姓氏
        if (lastNameEditText.getText().toString().trim().isEmpty()) {
            lastNameEditText.setError("Last name is required");
            return false;
        }

        // 檢查電郵
        String email = emailEditText.getText().toString().trim();
        if (email.isEmpty()) {
            emailEditText.setError("Email is required");
            return false;
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Please enter a valid email");
            return false;
        }

        // 檢查電話
        if (phoneEditText.getText().toString().trim().isEmpty()) {
            phoneEditText.setError("Phone number is required");
            return false;
        }

        // 檢查密碼
        String password = passwordEditText.getText().toString().trim();
        if (password.isEmpty()) {
            passwordEditText.setError("Password is required");
            return false;
        }
        if (password.length() < 6) {
            passwordEditText.setError("Password must be at least 6 characters");
            return false;
        }

        String cPassword = confirmPasswordEditText.getText().toString().trim();
        if(!password.equals(cPassword)){
            confirmPasswordEditText.setError("Password must be same");
            return false;
        }


        return true;
    }

    private void registerUser() {
        if (!validateInput()) {
            return;
        }

        // 獲取輸入的值
        String username = usernameEditText.getText().toString().trim();
        String firstName = firstNameEditText.getText().toString().trim();
        String lastName = lastNameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        // 禁用註冊按鈕防止重複提交
        registerButton.setEnabled(false);

        // 創建註冊請求對象
        RegisterRequest registerRequest = new RegisterRequest(
                username, firstName, lastName, email, phone, password
        );

        // 發送註冊請求
        apiService.registerUser(registerRequest).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                // 重新啟用註冊按鈕
                registerButton.setEnabled(true);

                if (response.isSuccessful()) {
                    showSuccessMessage();
                    // 延遲關閉頁面
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    }, 1000); // 2秒後關閉
                } else {
                    try {
                        String errorBody = response.errorBody().string();
                        showErrorMessage("Registration failed: " + errorBody);
                    } catch (IOException e) {
                        e.printStackTrace();
                        showErrorMessage("Registration failed");
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // 重新啟用註冊按鈕
                registerButton.setEnabled(true);
                showErrorMessage("Network error: " + t.getMessage());
            }
        });
    }
}