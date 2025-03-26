package com.example.fyp;

public class PlanResponse {
    private int planId; // 與 JSON 字段 "planId" 匹配
    private String planName; // 與 JSON 字段 "planName" 匹配

    // Getters 和 Setters
    public int getId() {
        return planId; // 使用 getId() 時返回 planId
    }

    public void setId(int planId) {
        this.planId = planId;
    }

    public String getName() {
        return planName; // 使用 getName() 時返回 planName
    }

    public void setName(String planName) {
        this.planName = planName;
    }
}