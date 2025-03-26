package com.example.fyp;

import static com.example.fyp.NavBarHelper.setupNavBar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class PostFormActivity extends AppCompatActivity {

    private static final String TAG = "PostFormActivity";

    // UI 元件
    private ImageButton backButton;
    private Spinner boardSpinner;
    private EditText titleInput;
    private EditText contentInput;
    private Button postButton;
    private ImageButton navHome, navSearch, navProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_form);

        // 初始化 UI 元件
        initUI();

        // 設置返回按鈕點擊事件
        backButton.setOnClickListener(v -> finish()); // 返回上一個 Activity

        // 設置發佈按鈕點擊事件
        postButton.setOnClickListener(v -> handlePostButtonClick());

    }

    /**
     * 初始化 UI 元件
     */
    private void initUI() {
        // 初始化表單相關元件
        backButton = findViewById(R.id.back_button);
        boardSpinner = findViewById(R.id.board_spinner);
        titleInput = findViewById(R.id.title_input);
        contentInput = findViewById(R.id.content_input);
        postButton = findViewById(R.id.post_button);

    }

    /**
     * 處理發佈按鈕的點擊事件
     */
    private void handlePostButtonClick() {
        // 獲取用戶輸入
        String title = titleInput.getText().toString().trim();
        String content = contentInput.getText().toString().trim();

        // 驗證輸入是否有效
        if (TextUtils.isEmpty(title)) {
            Toast.makeText(this, "Title cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(content)) {
            Toast.makeText(this, "Content cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        // 獲取用戶選擇的分區（Spinner）
        String selectedBoard = boardSpinner.getSelectedItem() != null
                ? boardSpinner.getSelectedItem().toString()
                : "Unknown";

        // 模擬發佈操作
        Log.i(TAG, "Post submitted successfully!");
        Log.i(TAG, "Title: " + title);
        Log.i(TAG, "Content: " + content);
        Log.i(TAG, "Board: " + selectedBoard);

        // 顯示成功提示
        Toast.makeText(this, "Post submitted successfully!", Toast.LENGTH_SHORT).show();

        // 清空輸入框
        titleInput.setText("");
        contentInput.setText("");
    }
}