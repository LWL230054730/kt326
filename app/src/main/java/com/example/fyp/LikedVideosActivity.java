package com.example.fyp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LikedVideosActivity extends AppCompatActivity {
    private static final String TAG = "LikedVideosActivity";

    private RecyclerView rvVideos;
    private VideoAdapter adapter;
    private List<Video> likedVideos = new ArrayList<>(); // 存储已点赞的视频

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liked_videos); // 使用新的布局文件

        // 初始化 RecyclerView
        rvVideos = findViewById(R.id.rvVideos);
        rvVideos.setLayoutManager(new LinearLayoutManager(this));

        // 设置适配器
        adapter = new VideoAdapter(likedVideos, new VideoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Video video) {
                // 点击视频时，跳转到播放界面
                Intent intent = new Intent(LikedVideosActivity.this, YouTubePlayerActivity.class);
                intent.putExtra("VIDEO_ID", video.getUrl());
                startActivity(intent);
            }
        });
        rvVideos.setAdapter(adapter);

        // 从后端获取点赞视频的数据
        fetchLikedVideosFromApi();
    }

    private void fetchLikedVideosFromApi() {
        // 创建 ApiService 实例
        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        // 调用后端 API 获取已点赞视频数据
        Call<List<Video>> call = apiService.getLikedVideos();

        // 异步请求
        call.enqueue(new Callback<List<Video>>() {
            @Override
            public void onResponse(Call<List<Video>> call, Response<List<Video>> response) {
                Log.d(TAG, "Response code: " + response.code());
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "Liked videos received: " + response.body().size());
                    likedVideos.addAll(response.body());
                    adapter.notifyDataSetChanged();
                } else {
                    Log.e(TAG, "Error: " + response.errorBody());
                    Toast.makeText(LikedVideosActivity.this, "Failed to load liked videos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Video>> call, Throwable t) {
                Log.e(TAG, "Error fetching liked videos: ", t);
                Toast.makeText(LikedVideosActivity.this, "Error fetching liked videos", Toast.LENGTH_SHORT).show();
            }
        });
    }
}