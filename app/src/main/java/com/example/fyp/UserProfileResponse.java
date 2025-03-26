package com.example.fyp;

public class UserProfileResponse {

    private boolean success;
    private UserProfile data;

    public boolean isSuccess() {
        return success;
    }

    public UserProfile getData() {
        return data;
    }
}

class UserProfile {

    private int userId;
    private String userName;
    private String bio;
    private String email;
    private String phone;
    private String avatarUrl;
    private int followersCount;
    private int followingCount;
    private int postsCount;

    public int getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getBio() {
        return bio;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    public int getFollowingCount() {
        return followingCount;
    }

    public int getPostsCount() {
        return postsCount;
    }
}