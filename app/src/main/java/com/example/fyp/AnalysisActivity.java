package com.example.fyp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class AnalysisActivity extends Fragment {
    private Button startCaptureButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 加載佈局檔案
        View view = inflater.inflate(R.layout.activity_analysis, container, false);

        // 初始化按鈕
        startCaptureButton = view.findViewById(R.id.start_capture_button);

        // 設置按鈕點擊事件
        startCaptureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳轉到 PoseDetectionActivity
                Intent intent = new Intent(getActivity(), PoseDetectionActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }
}