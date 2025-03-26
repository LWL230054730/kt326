package com.example.fyp;

import android.os.Bundle;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddPlanActivity extends Fragment {

    private TextView tvQuestion;
    private Button btnOption1, btnOption2, btnOption3, btnNext;
    private String selectedOption, selectedLevel;
    private int currentStep = 0;

    // Retrofit API Service
    private ApiService apiService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_add_plan, container, false);

        // Initialize API Service
        apiService = ApiClient.getClient().create(ApiService.class);

        // Initialize UI components
        tvQuestion = view.findViewById(R.id.tvQuestion);
        btnOption1 = view.findViewById(R.id.btnOption1);
        btnOption2 = view.findViewById(R.id.btnOption2);
        btnOption3 = view.findViewById(R.id.btnOption3);
        btnNext = view.findViewById(R.id.btnNext);

        // Start the plan creation flow
        startPlanCreationFlow();

        return view;
    }

    private void startPlanCreationFlow() {
        currentStep = 0; // Initialize step
        tvQuestion.setText("Please select your training type:");
        btnOption1.setText("Yoga");
        btnOption2.setText("HIIT");
        btnOption3.setText("Strength");
        btnNext.setVisibility(View.GONE);

        // Set click events for options
        btnOption1.setOnClickListener(v -> {
            selectedOption = "Yoga";
            btnNext.setVisibility(View.VISIBLE);
        });

        btnOption2.setOnClickListener(v -> {
            selectedOption = "HIIT";
            btnNext.setVisibility(View.VISIBLE);
        });

        btnOption3.setOnClickListener(v -> {
            selectedOption = "Strength";
            btnNext.setVisibility(View.VISIBLE);
        });

        btnNext.setOnClickListener(v -> handleNextStep());
    }

    private void addPlanToServer(String type, String level) {
        String token = AuthManager.getAuthToken();
        if (token == null) {
            Toast.makeText(getContext(), "Authentication failed. Please log in again.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Generate dynamic plan name
        String planName = level + " " + type + " Plan";

        // Log the dynamic plan name and parameters for debugging
        Log.d("AddPlanActivity", "Plan Name: " + planName + ", Type: " + type + ", Level: " + level);

        AddPlanRequest request = new AddPlanRequest(planName, type, level);

        Call<CreatePlanResponse> createPlanCall = apiService.createPlan("Bearer " + token, request);
        createPlanCall.enqueue(new Callback<CreatePlanResponse>() {
            @Override
            public void onResponse(Call<CreatePlanResponse> call, Response<CreatePlanResponse> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Plan created successfully!", Toast.LENGTH_SHORT).show();
                    requireActivity().getSupportFragmentManager().popBackStack(); // Return to previous fragment
                } else {
                    try {
                        // Log the error response from the server
                        String errorMessage = response.errorBody().string();
                        Toast.makeText(getContext(), "Failed to create plan: " + errorMessage, Toast.LENGTH_SHORT).show();
                        Log.e("AddPlanActivity", "Error: " + errorMessage);
                    } catch (Exception e) {
                        Toast.makeText(getContext(), "Failed to create plan.", Toast.LENGTH_SHORT).show();
                        Log.e("AddPlanActivity", "Error parsing error body", e);
                    }
                }
            }

            @Override
            public void onFailure(Call<CreatePlanResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleNextStep() {
        currentStep++;

        if (currentStep == 1) {
            tvQuestion.setText("What is your exercise experience level?");
            btnOption1.setText("Beginner");
            btnOption2.setText("Intermediate");
            btnOption3.setText("Advanced");
            btnNext.setVisibility(View.GONE);

            btnOption1.setOnClickListener(v -> {
                selectedLevel = "Beginner";
                btnNext.setVisibility(View.VISIBLE);
            });

            btnOption2.setOnClickListener(v -> {
                selectedLevel = "Intermediate";
                btnNext.setVisibility(View.VISIBLE);
            });

            btnOption3.setOnClickListener(v -> {
                selectedLevel = "Advanced";
                btnNext.setVisibility(View.VISIBLE);
            });

        } else if (currentStep == 2) {
            addPlanToServer(selectedOption, selectedLevel);
        }
    }
}