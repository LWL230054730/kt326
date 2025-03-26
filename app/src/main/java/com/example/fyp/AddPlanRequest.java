package com.example.fyp;

public class AddPlanRequest {
    private String name;
    private String type;
    private String level;

    public AddPlanRequest(String name, String type, String level) {
        this.name = name;
        this.type = type;
        this.level = level;
    }

    // Getters 和 Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
}