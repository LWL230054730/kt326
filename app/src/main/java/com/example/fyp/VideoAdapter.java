package com.example.fyp;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {

    private List<Video> videos; // 從後端獲取的影片列表
    private OnItemClickListener listener;

    // 定義點擊事件接口
    public interface OnItemClickListener {
        void onItemClick(Video video);
    }

    // 構造函數
    public VideoAdapter(List<Video> videos, OnItemClickListener listener) {
        this.videos = videos;
        this.listener = listener;
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video, parent, false);
        return new VideoViewHolder(view, parent.getContext());
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        Video video = videos.get(position);

        // 設定標題、時長、難度
        holder.tvTitle.setText(video.getTitle());
        holder.tvDuration.setText(video.getDuration());
        holder.tvLevel.setText(video.getLevel());
        holder.tvDescription.setText(video.getDescription());

        // 通過圖片名稱獲取對應的資源 ID
        int resourceId = holder.itemView.getContext().getResources().getIdentifier(
                video.getThumbnail(), // 後端返回的圖片名稱（如 "strength_video_3"）
                "drawable",           // 資源類型
                holder.itemView.getContext().getPackageName() // 包名
        );

        // 加載圖片到 ImageView
        if (resourceId != 0) { // 如果圖片資源存在
            holder.ivThumbnail.setImageResource(resourceId);
        } else { // 如果圖片資源不存在，顯示預設圖片
            holder.ivThumbnail.setImageResource(R.drawable.error);
        }

        // 初始化 Like 按鈕狀態
        holder.bindLikeButton();

        // 設定點擊事件
        holder.itemView.setOnClickListener(v -> listener.onItemClick(video));

        // 設定當前 video
        holder.video = video;
    }

    @Override
    public int getItemCount() {
        return videos.size();
    }

    static class VideoViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDuration, tvLevel, tvDescription;
        ImageView ivThumbnail, ivLike;
        boolean isLiked = false; // 預設未點擊 Like
        Video video; // 當前 ViewHolder 綁定的影片
        Context context;

        public VideoViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            this.context = context;

            // 綁定 UI 元件
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDuration = itemView.findViewById(R.id.tvDuration);
            tvLevel = itemView.findViewById(R.id.tvLevel);
            ivThumbnail = itemView.findViewById(R.id.ivThumbnail);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            ivLike = itemView.findViewById(R.id.ivLike);

            // Like 按鈕點擊事件
            ivLike.setOnClickListener(v -> toggleLike());
        }

        // 切換 Like 按鈕狀態
        private void toggleLike() {
            isLiked = !isLiked;

            if (isLiked) {
                ivLike.setImageResource(R.drawable.like_filled); // 已喜歡
                sendLikeRequest(); // 向後端發送 Like 請求
            } else {
                ivLike.setImageResource(R.drawable.like_outline); // 未喜歡
                sendUnlikeRequest(); // 向後端發送 Unlike 請求
            }
        }

        // 初始化 Like 按鈕狀態
        public void bindLikeButton() {
            if (isLiked) {
                ivLike.setImageResource(R.drawable.like_filled);
            } else {
                ivLike.setImageResource(R.drawable.like_outline);
            }
        }

        // 發送 Like 請求
        private void sendLikeRequest() {
            SharedPreferences sharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
            String userId = sharedPreferences.getString("userId", null);

            if (userId == null || video == null) {
                Log.e("VideoAdapter", "User ID or Video is null. Cannot send like request.");
                return;
            }

            LikeVideoRequest request = new LikeVideoRequest(userId, video.getId());
            ApiService apiService = ApiClient.getClient().create(ApiService.class);

            Call<ResponseBody> call = apiService.likeVideo(request);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        Log.d("VideoAdapter", "Like request successful for video " + video.getId());
                    } else {
                        Log.e("VideoAdapter", "Like request failed: " + response.errorBody());
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.e("VideoAdapter", "Error sending like request: ", t);
                }
            });
        }

        // 發送 Unlike 請求
        private void sendUnlikeRequest() {
            SharedPreferences sharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
            String userId = sharedPreferences.getString("userId", null);

            if (userId == null || video == null) {
                Log.e("VideoAdapter", "User ID or Video is null. Cannot send unlike request.");
                return;
            }

            LikeVideoRequest request = new LikeVideoRequest(userId, video.getId());
            ApiService apiService = ApiClient.getClient().create(ApiService.class);

            Call<ResponseBody> call = apiService.unlikeVideo(request);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        Log.d("VideoAdapter", "Unlike request successful for video " + video.getId());
                    } else {
                        Log.e("VideoAdapter", "Unlike request failed: " + response.errorBody());
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.e("VideoAdapter", "Error sending unlike request: ", t);
                }
            });
        }
    }
}