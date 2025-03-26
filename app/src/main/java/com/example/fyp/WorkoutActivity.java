package com.example.fyp;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class WorkoutActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private androidx.appcompat.widget.Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);

        // Initialize the Toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar); // Set it as the support ActionBar

        // Find the account icon and set a click event
        ImageView accountIcon = findViewById(R.id.account_icon);
        accountIcon.setOnClickListener(v -> {
            // Open the new AccountActivity
            Intent intent = new Intent(WorkoutActivity.this, ProfileActivity.class);
            startActivity(intent);
        });

        // Initialize the BottomNavigationView
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Set an option click listener for the BottomNavigationView
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;

                if (item.getItemId() == R.id.nav_workout) {
                    selectedFragment = new WorkoutFragment();
                    toolbar.setTitle("Workout");
                } else if (item.getItemId() == R.id.nav_analysis) {
                    selectedFragment = new AnalysisActivity();
                    toolbar.setTitle("Analysis");
                } else if (item.getItemId() == R.id.nav_plan) {
                    selectedFragment = new PlanActivity();
                    toolbar.setTitle("Plan");
                } else if (item.getItemId() == R.id.nav_community) {
                    // 修改為跳轉到 MainActivity
                    Intent intent = new Intent(WorkoutActivity.this, MainActivity.class);
                    startActivity(intent);
                    return false; // 停止後續 Fragment 替換
                } else if (item.getItemId() == R.id.nav_tracking) {
                    selectedFragment = new TrackingActivity();
                    toolbar.setTitle("Tracking");
                }

                // If the selected Fragment is not null, perform a transaction to replace it
                if (selectedFragment != null) {
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container, selectedFragment);
                    transaction.commit();
                }

                return true;
            }
        });

        // Display HomeFragment by default and set the title
        bottomNavigationView.setSelectedItemId(R.id.nav_workout);
        toolbar.setTitle("Workout");
    }
}