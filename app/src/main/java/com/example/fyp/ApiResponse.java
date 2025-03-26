package com.example.fyp;

import java.util.List;

public class ApiResponse {
    private List<Post> posts; // 對應後端的 "posts" 數組
    private Pagination pagination; // 對應後端的 "pagination" 對象

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }
}