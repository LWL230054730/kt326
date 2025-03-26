package com.example.fyp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        // Close button
        ImageView closeButton = findViewById(R.id.close_button);
        closeButton.setOnClickListener(v -> finish());

        // Retrieve the username and userId from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "Guest"); // Default to "Guest" if no username is found
        String userId = sharedPreferences.getString("userId", "N/A"); // Default to "N/A" if no userId is found

        // Set the username and userId
        TextView usernameTextView = findViewById(R.id.username);
        usernameTextView.setText(username);

        // Button click listeners
        Button profileButton = findViewById(R.id.profile_button);
        Button settingsButton = findViewById(R.id.settings_button);
        Button logoutButton = findViewById(R.id.logout_button);

        profileButton.setOnClickListener(v ->
                Toast.makeText(this, "Profile clicked", Toast.LENGTH_SHORT).show()
        );

        settingsButton.setOnClickListener(v ->
                Toast.makeText(this, "Settings clicked", Toast.LENGTH_SHORT).show()
        );

        logoutButton.setOnClickListener(v -> {
            // Clear SharedPreferences on logout
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();

            // Redirect to LoginActivity
            Intent intent = new Intent(AccountActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear back stack
            startActivity(intent);

            Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show();

            // Finish the current activity
            finish();
        });
    }
}