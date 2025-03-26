package com.example.fyp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostDetailsActivity extends AppCompatActivity {

    private TextView postTitleTextView, postContentTextView, postLikeCountTextView;
    private Button likeButton;
    private int postId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);

        // 初始化 UI 元件
        postTitleTextView = findViewById(R.id.postTitleTextView);
        postContentTextView = findViewById(R.id.postContentTextView);
        postLikeCountTextView = findViewById(R.id.postLikeCountTextView);
        likeButton = findViewById(R.id.likeButton);

        // 獲取 Intent 傳遞的 Post ID
        postId = getIntent().getIntExtra("postId", -1);
        if (postId == -1) {
            Toast.makeText(this, "Invalid Post ID", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        if (!AuthManager.isLoggedIn()) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        // 加載帖子詳情
        fetchPostDetails();

        // 點擊 Like 按鈕
        likeButton.setOnClickListener(v -> toggleLike());
    }

    private void fetchPostDetails() {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.getPostDetails(postId).enqueue(new Callback<Post>() {
            @Override
            public void onResponse(@NonNull Call<Post> call, @NonNull Response<Post> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Post post = response.body();
                    postTitleTextView.setText(post.getTitle());
                    postContentTextView.setText(post.getContent());
                    postLikeCountTextView.setText(String.format("%d Likes", post.getLikeCount()));
                } else {
                    Toast.makeText(PostDetailsActivity.this, "Failed to load post details", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Post> call, @NonNull Throwable t) {
                Toast.makeText(PostDetailsActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void toggleLike() {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.toggleLike(postId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(PostDetailsActivity.this, "Like toggled", Toast.LENGTH_SHORT).show();
                    fetchPostDetails();
                } else {
                    Toast.makeText(PostDetailsActivity.this, "Failed to toggle like", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Toast.makeText(PostDetailsActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}