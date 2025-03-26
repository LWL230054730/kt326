package com.example.fyp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class VideoListFragment extends Fragment {

    private static final String ARG_PLAN_NAME = "plan_name";
    private static final String ARG_VIDEOS = "videos";

    public static VideoListFragment newInstance(String planName, List<VideoResponse> videos) {
        VideoListFragment fragment = new VideoListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PLAN_NAME, planName);
        args.putSerializable(ARG_VIDEOS, new ArrayList<>(videos));
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video_list, container, false);

        // 從參數中獲取數據並顯示
        String planName = getArguments().getString(ARG_PLAN_NAME);
        List<VideoResponse> videos = (List<VideoResponse>) getArguments().getSerializable(ARG_VIDEOS);

        // 顯示計劃名稱和視頻列表
        // 你的 RecyclerView 顯示邏輯...

        return view;
    }
}