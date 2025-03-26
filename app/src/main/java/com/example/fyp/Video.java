package com.example.fyp;

public class Video {
    private String id;
    private String title;
    private String duration;
    private String type;
    private String description;
    private String url;
    private String thumbnail; //Local Resource ID
    private String level;

    public Video(String id,String title, String duration, String type, String description, String url, String thumbnail, String level) {
        this.id=id;
        this.title = title;
        this.duration = duration;
        this.type = type;
        this.description = description;
        this.url = url;
        this.thumbnail = thumbnail;
        this.level = level;
    }

    public String getId(){
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDuration() {
        return duration;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public String getUrl() {
        return url;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getLevel() {
        return level;
    }
}