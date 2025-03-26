package com.example.fyp;

import com.google.gson.annotations.SerializedName;

public class UserData {
    @SerializedName("userName")
    private String userName;

    @SerializedName("firstName")
    private String firstName;

    public String getUserName() {
        return userName;
    }

    public String getFirstName() {
        return firstName;
    }

    @SerializedName("email")
    private String email;

    public String getEmail() {
        return email;
    }

}
