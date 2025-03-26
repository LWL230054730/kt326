package com.example.fyp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlanActivity extends Fragment {

    private LinearLayout planListContainer;
    private ApiService apiService;
    private Button backButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the activity_plan.xml layout
        View view = inflater.inflate(R.layout.activity_plan, container, false);

        // Initialize the API service
        apiService = ApiClient.getClient().create(ApiService.class);

        // Initialize UI components
        planListContainer = view.findViewById(R.id.plan_list_container);
        backButton = view.findViewById(R.id.back_button);
        Button btnAddPlan = view.findViewById(R.id.btn_add_plan); // Initialize the btn_add_plan button

        // Set the back button to be invisible
        backButton.setVisibility(View.GONE);

        // Set click listener for the back button
        backButton.setOnClickListener(v -> loadExistingPlans());

        // Set click listener for the btn_add_plan button
        btnAddPlan.setOnClickListener(v -> {
            // Use FragmentManager to navigate to AddPlanActivity
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new AddPlanActivity()) // Replace with AddPlanActivity
                    .addToBackStack(null) // Add to back stack for user navigation
                    .commit();
        });

        // Load the user's plan list
        loadExistingPlans();

        return view;
    }

    // Load the user's plans from the backend using Token
    private void loadExistingPlans() {
        // Clear the container to avoid duplicate loading
        planListContainer.removeAllViews();

        // Hide the back button
        backButton.setVisibility(View.GONE);

        // Add a heading text at the top
        TextView heading = new TextView(getContext());
        heading.setText("This is your weekly training plan.");
        heading.setTextSize(20);
        heading.setPadding(16, 16, 16, 16);
        planListContainer.addView(heading);

        // Retrieve the token from AuthManager
        String token = AuthManager.getAuthToken();
        if (token == null) {
            Toast.makeText(getContext(), "Authentication failed. Please log in again.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Call the backend API to get the user's plans
        Call<List<PlanResponse>> call = apiService.getUserPlansWithToken("Bearer " + token); // Use Authorization header
        call.enqueue(new Callback<List<PlanResponse>>() {
            @Override
            public void onResponse(Call<List<PlanResponse>> call, Response<List<PlanResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<PlanResponse> plans = response.body();

                    // If no plans are found
                    if (plans.isEmpty()) {
                        TextView emptyMessage = new TextView(getContext());
                        emptyMessage.setText("No plans found. Please create a new plan.");
                        emptyMessage.setTextSize(16);
                        emptyMessage.setPadding(16, 16, 16, 16);
                        planListContainer.addView(emptyMessage);
                        return;
                    }

                    // Dynamically load the list of plans
                    int index = 1;
                    for (PlanResponse plan : plans) {
                        addPlanRow(index, plan.getName(), plan.getId());
                        index++;
                    }
                } else {
                    Toast.makeText(getContext(), "No plans found. Please create a new plan.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<PlanResponse>> call, Throwable t) {
                Toast.makeText(getContext(), "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Add each row of plan data
    private void addPlanRow(int index, String planName, int planId) {
        LinearLayout row = new LinearLayout(getContext());
        row.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        row.setOrientation(LinearLayout.HORIZONTAL);

        // 添加點擊事件
        row.setOnClickListener(v -> {
            // 添加調試日誌，確認 planId
            System.out.println("Clicked Plan ID: " + planId);

            // 傳遞 planId 加載視頻
            loadPlanVideos(planId, planName);
        });

        // 添加序號
        TextView columnNumber = new TextView(getContext());
        columnNumber.setText(String.valueOf(index));
        columnNumber.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
        columnNumber.setTextSize(20);
        columnNumber.setPadding(8, 8, 8, 8);
        row.addView(columnNumber);

        // 添加計劃名稱
        TextView columnPlanName = new TextView(getContext());
        columnPlanName.setText(planName);
        columnPlanName.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 3f));
        columnPlanName.setTextSize(20);
        columnPlanName.setPadding(8, 8, 8, 8);
        row.addView(columnPlanName);

        // 添加行到容器
        planListContainer.addView(row);
    }

    // Load all video details for the specified plan using Token
    private void loadPlanVideos(int planId, String planName) {
        System.out.println("Loading videos for Plan ID: " + planId);
        // Clear the container to display new content
        planListContainer.removeAllViews();

        // Show the back button
        backButton.setVisibility(View.VISIBLE);

        // Display the plan name
        TextView planTitle = new TextView(getContext());
        planTitle.setText("Videos for Plan: " + planName);
        planTitle.setTextSize(24);
        planTitle.setPadding(16, 16, 16, 16);
        planListContainer.addView(planTitle);

        // Retrieve the token from AuthManager
        String token = AuthManager.getAuthToken();
        if (token == null) {
            Toast.makeText(getContext(), "Authentication failed. Please log in again.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Call the backend API to get video details
        Call<List<VideoResponse>> call = apiService.getPlanVideosWithToken("Bearer " + token, new PlanIdRequest(planId));
        call.enqueue(new Callback<List<VideoResponse>>() {
            @Override
            public void onResponse(Call<List<VideoResponse>> call, Response<List<VideoResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<VideoResponse> videos = response.body();

                    // If no videos are found
                    if (videos.isEmpty()) {
                        TextView noVideos = new TextView(getContext());
                        noVideos.setText("No videos found for this plan.");
                        noVideos.setTextSize(16);
                        noVideos.setPadding(16, 16, 16, 16);
                        planListContainer.addView(noVideos);
                        return;
                    }

                    // Dynamically load video details
                    for (VideoResponse video : videos) {
                        addVideoDetail(video);
                    }
                } else {
                    Toast.makeText(getContext(), "Failed to load videos for plan: " + planName, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<VideoResponse>> call, Throwable t) {
                Toast.makeText(getContext(), "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Dynamically add video details
    private void addVideoDetail(VideoResponse video) {
        LinearLayout videoRow = new LinearLayout(getContext());
        videoRow.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        videoRow.setOrientation(LinearLayout.VERTICAL);
        videoRow.setPadding(16, 16, 16, 16);

        // Display the video title
        TextView videoTitle = new TextView(getContext());
        videoTitle.setText("Title: " + video.getTitle());
        videoTitle.setTextSize(18);
        videoRow.addView(videoTitle);

        // Display the video description
        TextView videoDescription = new TextView(getContext());
        videoDescription.setText("Description: " + video.getDescription());
        videoDescription.setTextSize(16);
        videoRow.addView(videoDescription);

        // Display the video duration
        TextView videoDuration = new TextView(getContext());
        videoDuration.setText("Duration: " + video.getDuration());
        videoDuration.setTextSize(16);
        videoRow.addView(videoDuration);

        // Display the video level
        TextView videoLevel = new TextView(getContext());
        videoLevel.setText("Level: " + video.getLevel());
        videoLevel.setTextSize(16);
        videoRow.addView(videoLevel);

        // Add the row to the container
        planListContainer.addView(videoRow);
    }
}