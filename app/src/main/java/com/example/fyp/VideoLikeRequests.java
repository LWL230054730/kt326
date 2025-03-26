package com.example.fyp;

public class VideoLikeRequests {

    // 點擊 Like 請求類
    public static class LikeRequest {
        private int video_id;

        public LikeRequest(int video_id) {
            this.video_id = video_id;
        }

        public int getVideoId() {
            return video_id;
        }

        public void setVideoId(int video_id) {
            this.video_id = video_id;
        }
    }

    // 取消 Like 請求類
    public static class UnlikeRequest {
        private int video_id;

        public UnlikeRequest(int video_id) {
            this.video_id = video_id;
        }

        public int getVideoId() {
            return video_id;
        }

        public void setVideoId(int video_id) {
            this.video_id = video_id;
        }
    }

    // 檢查是否 Like 請求類
    public static class IsLikedRequest {
        private int video_id;

        public IsLikedRequest(int video_id) {
            this.video_id = video_id;
        }

        public int getVideoId() {
            return video_id;
        }

        public void setVideoId(int video_id) {
            this.video_id = video_id;
        }
    }
}