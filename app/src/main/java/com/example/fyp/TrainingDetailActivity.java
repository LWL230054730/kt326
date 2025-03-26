package com.example.fyp;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class TrainingDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training_detail);

        // 获取传递的数据
        String action = getIntent().getStringExtra("action");
        String date = getIntent().getStringExtra("date");
        int reps = getIntent().getIntExtra("reps", 0);
        int correctness = getIntent().getIntExtra("correctness", 0);

        // 显示详细数据
        ((TextView) findViewById(R.id.detail_action)).setText("Action: " + action);
        ((TextView) findViewById(R.id.detail_date)).setText("Date: " + date);
        ((TextView) findViewById(R.id.detail_reps)).setText("Reps: " + reps);
        ((TextView) findViewById(R.id.detail_correctness)).setText("Correctness: " + correctness + "%");

        // 启用标题栏返回按钮
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        findViewById(R.id.back_button).setOnClickListener(v -> finish());
    }
}