package com.example.fyp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TrainingRecordsAdapter extends RecyclerView.Adapter<TrainingRecordsAdapter.ViewHolder> {

    private final String[] trainingRecords;

    public TrainingRecordsAdapter(String[] trainingRecords) {
        this.trainingRecords = trainingRecords;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 加載每個訓練記錄項目的布局
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_training_record, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // 設置訓練記錄數據
        holder.trainingRecordText.setText(trainingRecords[position]);
    }

    @Override
    public int getItemCount() {
        return trainingRecords.length;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView trainingRecordText;

        ViewHolder(View itemView) {
            super(itemView);
            trainingRecordText = itemView.findViewById(R.id.trainingRecordText);
        }
    }
}