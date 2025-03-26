package com.example.fyp;
public class AssignPlanRequest {
    private int planId;

    // 修改構造方法
    public AssignPlanRequest(int planId) {
        this.planId = planId;
    }

    // 刪除 userId 的 Getter 和 Setter
    public int getPlanId() {
        return planId;
    }

    public void setPlanId(int planId) {
        this.planId = planId;
    }
}