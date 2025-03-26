package com.example.fyp;

public class Post {
    private int postId;
    private String title;
    private String content;
    private int likeCount;
    private int commentCount;
    private int viewCount;
    private String createdAt;

    // 新增字段
    private int boardId; // 分區 ID
    private String type; // 帖子類型
    private String visibility; // 可見性

    // Getters 和 Setters
    public int getPostId() { return postId; }

    public void setPostId(int postId) { this.postId = postId; }

    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }

    public void setContent(String content) { this.content = content; }

    public int getLikeCount() { return likeCount; }

    public void setLikeCount(int likeCount) { this.likeCount = likeCount; }

    public int getCommentCount() { return commentCount; }

    public void setCommentCount(int commentCount) { this.commentCount = commentCount; }

    public int getViewCount() { return viewCount; }

    public void setViewCount(int viewCount) { this.viewCount = viewCount; }

    public String getCreatedAt() { return createdAt; }

    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    public int getBoardId() { return boardId; }

    public void setBoardId(int boardId) { this.boardId = boardId; }

    public String getType() { return type; }

    public void setType(String type) { this.type = type; }

    public String getVisibility() { return visibility; }

    public void setVisibility(String visibility) { this.visibility = visibility; }
}