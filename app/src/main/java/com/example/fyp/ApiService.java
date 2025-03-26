package com.example.fyp;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @POST("api/auth/register")
    Call<ResponseBody> registerUser(@Body RegisterRequest request);

    @POST("api/auth/login")
    Call<LoginResponse> loginUser(@Body LoginRequest loginRequest);

    @PUT("api/users/profile")
    Call<ResponseBody> updateProfile(
            @Header("Authorization") String token,
            @Body UserProfileUpdateRequest request
    );

    @POST("api/user/update-password")
    Call<ResponseBody> updatePassword(
            @Header("Authorization") String token,
            @Body UpdatePasswordRequest request
    );

    @GET("api/user/profile")
    Call<UserProfileResponse> getUserProfile(@Header("Authorization") String token);

    @GET("/api/posts")
    Call<ApiResponse> getPosts(
            @Query("page") int page,
            @Query("limit") int limit
    );
    @GET("/api/posts/{postId}")
    Call<Post> getPostDetails(@Path("postId") int postId);

    @POST("/api/posts")
    Call<Post> createPost(@Body Post post);

    @PUT("/api/posts/{postId}")
    Call<Post> updatePost(@Path("postId") int postId, @Body Post post);

    @DELETE("/api/posts/{postId}")
    Call<Void> deletePost(@Path("postId") int postId);

    @POST("/api/posts/{postId}/toggle-like")
    Call<Void> toggleLike(@Path("postId") int postId);

    @POST("/api/posts/{postId}/toggle-follow")
    Call<Void> toggleFollow(@Path("postId") int postId);

    @GET("/api/posts/search")
    Call<List<Post>> searchPosts(
            @Query("q") String query,
            @Query("page") int page,
            @Query("limit") int limit
    );

    @POST("api/createPlan")
    Call<CreatePlanResponse> createPlan(
            @Header("Authorization") String token,
            @Body AddPlanRequest request
    );

    @POST("api/assignPlan")
    Call<ResponseBody> assignPlan(
            @Header("Authorization") String token,
            @Body AssignPlanRequest request
    );

    @POST("api/user/plans")
    Call<List<PlanResponse>> getUserPlans(@Body UserIdRequest request);

    @POST("api/plan/videos")
    Call<List<VideoResponse>> getPlanVideos(@Body PlanIdRequest planIdRequest);

    @GET("api/videos")
    Call<List<Video>> getVideosByType(@Query("type") String type);

    @GET("api/videos")
    Call<List<Video>> getVideos();

    @POST("api/videos/like")
    Call<ResponseBody> likeVideo(@Body LikeVideoRequest likeVideoRequest);

    @POST("api/videos/unlike")
    Call<ResponseBody> unlikeVideo(@Body LikeVideoRequest likeVideoRequest);

    @GET("/api/videos/liked")
    Call<List<Video>> getLikedVideos();

    // 獲取用戶計劃列表
    @POST("/api/user/plans")
    Call<List<PlanResponse>> getUserPlansWithToken(@Header("Authorization") String authToken);

    // 獲取計劃視頻列表
    @POST("/api/plan/videos")
    Call<List<VideoResponse>> getPlanVideosWithToken(
            @Header("Authorization") String authToken,
            @Body PlanIdRequest planIdRequest
    );
}