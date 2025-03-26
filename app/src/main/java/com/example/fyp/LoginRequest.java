package com.example.fyp;

import com.google.gson.annotations.SerializedName;

public class LoginRequest {
    @SerializedName("email")
    private String email;

    @SerializedName("userPw")
    private String userPw;

    public LoginRequest(String email, String userPw) {
        this.email = email;
        this.userPw = userPw;
    }

    public String getEmail() {
        return email;
    }

    public String getUserPw() {
        return userPw;
    }
}