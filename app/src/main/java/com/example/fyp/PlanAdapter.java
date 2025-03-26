package com.example.fyp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PlanAdapter extends RecyclerView.Adapter<PlanAdapter.PlanViewHolder> {

    private List<PlanResponse> plans;
    private final OnPlanClickListener onPlanClickListener;

    public interface OnPlanClickListener {
        void onPlanClick(PlanResponse plan);
    }

    public PlanAdapter(List<PlanResponse> plans, OnPlanClickListener onPlanClickListener) {
        this.plans = plans;
        this.onPlanClickListener = onPlanClickListener;
    }

    public void updatePlans(List<PlanResponse> newPlans) {
        this.plans = newPlans; // 更新適配器的數據源
        notifyDataSetChanged(); // 通知 RecyclerView 刷新
    }

    @NonNull
    @Override
    public PlanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_plan, parent, false);
        return new PlanViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlanViewHolder holder, int position) {
        PlanResponse plan = plans.get(position);
        holder.planName.setText(plan.getName());
        holder.itemView.setOnClickListener(v -> onPlanClickListener.onPlanClick(plan));
    }

    @Override
    public int getItemCount() {
        return plans.size();
    }

    static class PlanViewHolder extends RecyclerView.ViewHolder {
        TextView planName;

        public PlanViewHolder(@NonNull View itemView) {
            super(itemView);
            planName = itemView.findViewById(R.id.plan_name);
        }
    }
}