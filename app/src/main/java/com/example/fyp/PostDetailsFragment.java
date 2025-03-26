package com.example.fyp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostDetailsFragment extends Fragment {

    private TextView postTitleTextView, postContentTextView, postLikeCountTextView;
    private Button likeButton;
    private int postId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_post_details, container, false);

        // 初始化 UI 元件
        postTitleTextView = view.findViewById(R.id.postTitleTextView);
        postContentTextView = view.findViewById(R.id.postContentTextView);
        postLikeCountTextView = view.findViewById(R.id.postLikeCountTextView);
        likeButton = view.findViewById(R.id.likeButton);

        // 獲取傳遞的 Post ID
        if (getArguments() != null) {
            postId = getArguments().getInt("postId", -1);
        }

        if (postId == -1) {
            Toast.makeText(getContext(), "Invalid Post ID", Toast.LENGTH_SHORT).show();
            requireActivity().getSupportFragmentManager().popBackStack();
            return view;
        }

        if (!AuthManager.isLoggedIn()) {
            Intent intent = new Intent(getContext(), LoginActivity.class);
            startActivity(intent);
            requireActivity().finish();
            return view;
        }

        // 加載帖子詳情
        fetchPostDetails();

        // 點擊 Like 按鈕
        likeButton.setOnClickListener(v -> toggleLike());

        return view;
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
                    Toast.makeText(getContext(), "Failed to load post details", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Post> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void toggleLike() {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.toggleLike(postId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Like toggled", Toast.LENGTH_SHORT).show();
                    fetchPostDetails();
                } else {
                    Toast.makeText(getContext(), "Failed to toggle like", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}