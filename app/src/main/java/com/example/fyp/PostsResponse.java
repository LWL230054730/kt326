package com.example.fyp;

import java.util.List;

public class PostsResponse {
    private List<Post> posts;
    private Pagination pagination;

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

    public static class Pagination {
        private int current;
        private int total;
        private int totalPosts;
        private int pageSize;

        public int getCurrent() {
            return current;
        }

        public void setCurrent(int current) {
            this.current = current;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public int getTotalPosts() {
            return totalPosts;
        }

        public void setTotalPosts(int totalPosts) {
            this.totalPosts = totalPosts;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }
    }
}