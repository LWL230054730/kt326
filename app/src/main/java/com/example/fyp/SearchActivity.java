package com.example.fyp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {

    private EditText searchEditText;
    private Button searchButton;
    private RecyclerView searchResultsRecyclerView;
    private PostsAdapter postsAdapter;
    private List<Post> searchResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search); // 確保文件名正確

        // 初始化視圖
        searchEditText = findViewById(R.id.searchEditText);
        searchButton = findViewById(R.id.searchButton);
        searchResultsRecyclerView = findViewById(R.id.searchResultsRecyclerView);

        if (!AuthManager.isLoggedIn()) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        // 設置 RecyclerView
        searchResults = new ArrayList<>();
        postsAdapter = new PostsAdapter(searchResults, post -> {
            // 點擊帖子邏輯，例如跳轉到詳情頁
            Toast.makeText(this, "Clicked: " + post.getTitle(), Toast.LENGTH_SHORT).show();
        });
        searchResultsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        searchResultsRecyclerView.setAdapter(postsAdapter);

        // 搜尋按鈕點擊事件
        searchButton.setOnClickListener(v -> {
            String query = searchEditText.getText().toString().trim();
            if (TextUtils.isEmpty(query)) {
                Toast.makeText(this, "請輸入搜尋內容", Toast.LENGTH_SHORT).show();
                return;
            }
            fetchSearchResults(query, 1, 10); // 默認第 1 頁，每頁 10 條
        });
    }

    /**
     * 從後端 API 獲取搜尋結果
     *
     * @param query 搜尋關鍵字
     * @param page  當前頁碼
     * @param limit 每頁顯示的數據量
     */
    private void fetchSearchResults(String query, int page, int limit) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        // 使用 searchPosts 方法調用後端 API
        apiService.searchPosts(query, page, limit).enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(@NonNull Call<List<Post>> call, @NonNull Response<List<Post>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    searchResults.clear();
                    searchResults.addAll(response.body());
                    postsAdapter.notifyDataSetChanged(); // 更新 RecyclerView
                } else {
                    Toast.makeText(SearchActivity.this, "未找到相關結果", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Post>> call, @NonNull Throwable t) {
                Log.e("SearchActivity", "Error: " + t.getMessage());
                Toast.makeText(SearchActivity.this, "搜尋失敗，請稍後重試", Toast.LENGTH_SHORT).show();
            }
        });
    }
}