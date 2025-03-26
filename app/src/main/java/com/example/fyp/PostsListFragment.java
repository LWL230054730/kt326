package com.example.fyp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostsListFragment extends Fragment {

    private RecyclerView postsRecyclerView;
    private PostsAdapter postsAdapter;
    private List<Post> postList;
    private EditText searchEditText;
    private Button searchButton, createPostButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 加載 Fragment 的佈局
        return inflater.inflate(R.layout.fragment_posts_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 初始化 UI 元件
        postsRecyclerView = view.findViewById(R.id.postsRecyclerView);
        searchEditText = view.findViewById(R.id.searchEditText);
        searchButton = view.findViewById(R.id.searchButton);
        createPostButton = view.findViewById(R.id.createPostButton);

        // 設置 RecyclerView
        postsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        postList = new ArrayList<>();
        postsAdapter = new PostsAdapter(postList, post -> {
            // 點擊帖子時跳轉到 PostDetailsFragment
            PostDetailsFragment postDetailsFragment = new PostDetailsFragment();
            Bundle args = new Bundle();
            args.putInt("postId", post.getPostId()); // 傳遞帖子 ID
            postDetailsFragment.setArguments(args);

            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, postDetailsFragment) // 替換容器中的 Fragment
                    .addToBackStack(null) // 添加到返回棧
                    .commit();
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
                Toast.makeText(requireContext(), "Please enter a search query", Toast.LENGTH_SHORT).show();
            }
        });

       /* // 創建帖子功能
        createPostButton.setOnClickListener(v -> {
            PostFormFragment postFormFragment = new PostFormFragment();

            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, postFormFragment) // 跳轉到創建帖子頁面
                    .addToBackStack(null) // 添加到返回棧
                    .commit();
        }); */
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
                    Toast.makeText(requireContext(), "Failed to load posts", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.e("PostsListFragment", "Error: " + t.getMessage());
                Toast.makeText(requireContext(), "Error loading posts", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(requireContext(), "Failed to load search results", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                Toast.makeText(requireContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}