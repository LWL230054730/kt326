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

public class AllVideosActivity extends AppCompatActivity {
    private static final String TAG = "AllVideosActivity";

    private RecyclerView rvVideos;
    private VideoAdapter adapter;
    private List<Video> allVideos = new ArrayList<>(); // 存储所有视频

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_videos); // 使用一个新的布局文件（或者复用现有布局）

        // 初始化 RecyclerView
        rvVideos = findViewById(R.id.rvVideos);
        rvVideos.setLayoutManager(new LinearLayoutManager(this));

        // 设置适配器
        adapter = new VideoAdapter(allVideos, new VideoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Video video) {
                // 点击视频时，跳转到播放界面
                Intent intent = new Intent(AllVideosActivity.this, YouTubePlayerActivity.class);
                intent.putExtra("VIDEO_ID", video.getUrl());
                startActivity(intent);
            }
        });
        rvVideos.setAdapter(adapter);

        // 返回按钮
        //findViewById(R.id.ivBack).setOnClickListener(v -> finish());

        // 从后端获取所有视频的数据
        fetchVideosFromApi();
    }

    private void fetchVideosFromApi() {
        // 创建 ApiService 实例
        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        // 调用后端 API，不传递 type 参数（获取全部视频）
        Call<List<Video>> call = apiService.getVideos();

        // 异步请求
        call.enqueue(new Callback<List<Video>>() {
            @Override
            public void onResponse(Call<List<Video>> call, Response<List<Video>> response) {
                Log.d(TAG, "Response code: " + response.code());
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "Videos received: " + response.body().size());
                    allVideos.addAll(response.body());
                    adapter.notifyDataSetChanged();
                } else {
                    Log.e(TAG, "Error: " + response.errorBody());
                    Toast.makeText(AllVideosActivity.this, "Failed to load videos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Video>> call, Throwable t) {
                Log.e(TAG, "Error fetching videos: ", t);
                Toast.makeText(AllVideosActivity.this, "Error fetching videos", Toast.LENGTH_SHORT).show();
            }
        });
    }
}