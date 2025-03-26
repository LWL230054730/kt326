package com.example.fyp;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    @SerializedName("message")
    private String message;

    @SerializedName("token")
    private String token;

    @SerializedName("user")
    private UserData user;

    public String getMessage() {
        return message;
    }

    public String getToken() {
        return token;
    }

    public UserData getUser() {
        return user;
    }
}