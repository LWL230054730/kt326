package com.example.fyp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class WorkoutFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_workout, container, false);

        // Yoga image click event
        ImageView yogaImage = view.findViewById(R.id.imageView3);
        yogaImage.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), YogaVideoActivity.class);
            startActivity(intent);
        });

        // HIIT image click event
        ImageView hiitImage = view.findViewById(R.id.imageView1);
        hiitImage.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), HIITVideoActivity.class);
            startActivity(intent);
        });

        // Strength image click event
        ImageView strengthImage = view.findViewById(R.id.imageView2);
        strengthImage.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), StrengthVideoActivity.class);
            startActivity(intent);
        });

        // All videos button click event
        ImageView allVideosButton = view.findViewById(R.id.all_videos_button);
        allVideosButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AllVideosActivity.class);
            startActivity(intent);
        });

        // Liked videos button click event
        ImageView likedVideosButton = view.findViewById(R.id.liked_videos_button);
        likedVideosButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), LikedVideosActivity.class);
            startActivity(intent);
        });

        return view;
    }
}