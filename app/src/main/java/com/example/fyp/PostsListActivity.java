package com.example.fyp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostsListActivity extends AppCompatActivity {

    private RecyclerView postsRecyclerView;
    private PostsAdapter postsAdapter;
    private List<Post> postList;
    private EditText searchEditText;
    private Button searchButton, createPostButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts_list);

        // 初始化 UI 元件
        postsRecyclerView = findViewById(R.id.postsRecyclerView);
        searchEditText = findViewById(R.id.searchEditText);
        searchButton = findViewById(R.id.searchButton);
        createPostButton = findViewById(R.id.createPostButton);

        postsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        postList = new ArrayList<>();
        postsAdapter = new PostsAdapter(postList, post -> {
            // 點擊帖子時跳轉到詳情頁
            Intent intent = new Intent(this, PostDetailsActivity.class);
            intent.putExtra("postId", post.getPostId());
            startActivity(intent);
        });
        postsRecyclerView.setAdapter(postsAdapter);

        // 從後端獲取帖子列表
        fetchPostsFromApi(1, 10);

        // 搜索功能
        searchButton.setOnClickListener(v -> {
            String query = searchEditText.getText().toString().trim();
            if (!query.isEmpty()) {
                searchPosts(query, 1, 10);
            } else {
                Toast.makeText(this, "Please enter a search query", Toast.LENGTH_SHORT).show();
            }
        });

        // 創建帖子功能
        createPostButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, PostFormActivity.class);
            startActivity(intent);
        });
    }

    private void fetchPostsFromApi(int page, int limit) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        apiService.getPosts(page, limit).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse apiResponse = response.body();
                    postList.clear();
                    if (apiResponse.getPosts() != null) {
                        postList.addAll(apiResponse.getPosts());
                    }
                    postsAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(PostsListActivity.this, "Failed to load posts", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.e("PostsListActivity", "Error: " + t.getMessage());
                Toast.makeText(PostsListActivity.this, "Error loading posts", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void searchPosts(String query, int page, int limit) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        apiService.searchPosts(query, page, limit).enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    postList.clear();
                    postList.addAll(response.body());
                    postsAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(PostsListActivity.this, "Failed to load search results", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                Toast.makeText(PostsListActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}