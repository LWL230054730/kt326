package com.example.fyp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HIITVideoActivity extends AppCompatActivity {

    private static final String TAG = "HIITVideoActivity";

    private RecyclerView rvVideos;
    private VideoAdapter adapter;
    private List<Video> hiitVideos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hiit_video);

        rvVideos = findViewById(R.id.rvVideos);
        rvVideos.setLayoutManager(new LinearLayoutManager(this));

        adapter = new VideoAdapter(hiitVideos, new VideoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Video video) {
                Intent intent = new Intent(HIITVideoActivity.this, YouTubePlayerActivity.class);
                intent.putExtra("VIDEO_ID", video.getUrl());
                startActivity(intent);
            }
        });
        rvVideos.setAdapter(adapter);

        // 返回按鈕
        findViewById(R.id.ivBack).setOnClickListener(v -> finish());

        // 從後端獲取影片資料
        fetchVideosFromApi();
    }

    private void fetchVideosFromApi() {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<List<Video>> call = apiService.getVideosByType("HIIT");

        call.enqueue(new Callback<List<Video>>() {
            @Override
            public void onResponse(Call<List<Video>> call, Response<List<Video>> response) {
                Log.d(TAG, "Response code: " + response.code());
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "Videos received: " + response.body().size());
                    hiitVideos.addAll(response.body());
                    adapter.notifyDataSetChanged();
                } else {
                    Log.e(TAG, "Error: " + response.errorBody());
                    Toast.makeText(HIITVideoActivity.this, "Failed to load videos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Video>> call, Throwable t) {
                Log.e(TAG, "Error fetching videos: ", t);
                Toast.makeText(HIITVideoActivity.this, "Error fetching videos", Toast.LENGTH_SHORT).show();
            }
        });
    }
}