package com.example.fyp;

import android.app.Activity;
import android.content.Intent;
import android.widget.ImageButton;

public class NavBarHelper {
    public static void setupNavBar(Activity activity) {
        ImageButton navTraining = activity.findViewById(R.id.nav_training);
        ImageButton navCommunity = activity.findViewById(R.id.nav_community);
        ImageButton navCompetition = activity.findViewById(R.id.nav_competition);
        ImageButton navProfile = activity.findViewById(R.id.nav_profile);

        navTraining.setOnClickListener(v -> {
            Intent intent = new Intent(activity, WorkoutActivity.class);
            activity.startActivity(intent);
        });

        navCommunity.setOnClickListener(v -> {
            Intent intent = new Intent(activity, PostsListActivity.class);
            activity.startActivity(intent);
        });

        navCompetition.setOnClickListener(v -> {
            Intent intent = new Intent(activity, CompetitionActivity.class);
            activity.startActivity(intent);
        });

        navProfile.setOnClickListener(v -> {
            Intent intent = new Intent(activity, ProfileActivity.class);
            activity.startActivity(intent);
        });
    }
}