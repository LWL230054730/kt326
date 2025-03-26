package com.example.fyp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class TrackingActivity extends Fragment {

    private ListView historyList; // ListView to display training history
    private List<TrainingData> trainingHistory; // List to store training data

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_tracking, container, false);

        // Initialize the ListView
        historyList = view.findViewById(R.id.history_list);

        // Initialize the training history list
        trainingHistory = new ArrayList<>();
        loadMockData(); // Load mock data for demonstration

        // Display the training history in the ListView
        displayTrainingHistory();

        return view;
    }

    // Load mock data (hardcoded training data)
    private void loadMockData() {
        trainingHistory.add(new TrainingData("2025/02/01", "Squat", 10, 85));
        trainingHistory.add(new TrainingData("2025/02/02", "Push-Up", 12, 88));
        trainingHistory.add(new TrainingData("2025/02/03", "Jumping Jack", 15, 92));
        trainingHistory.add(new TrainingData("2025/02/04", "Plank", 18, 95));
        trainingHistory.add(new TrainingData("2025/02/05", "Crunches", 20, 90));
        trainingHistory.add(new TrainingData("2025/02/06", "Lunges", 16, 87));
        trainingHistory.add(new TrainingData("2025/02/07", "Burpees", 14, 82));
        trainingHistory.add(new TrainingData("2025/02/08", "Mountain Climbers", 22, 91));
        trainingHistory.add(new TrainingData("2025/02/09", "Push-Up", 10, 89));
        trainingHistory.add(new TrainingData("2025/02/10", "Squat", 12, 93));
    }

    // Display the training history in the ListView
    private void displayTrainingHistory() {
        // Prepare the data to display (Action + Date)
        List<String> displayList = new ArrayList<>();
        for (TrainingData data : trainingHistory) {
            displayList.add(String.format("%s - %s", data.getAction(), data.getDate()));
        }

        // Use ArrayAdapter to display the data in the ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_list_item_1, displayList);
        historyList.setAdapter(adapter);

        // Set click listener for each item in the ListView
        historyList.setOnItemClickListener((AdapterView<?> parent, View view, int position, long id) -> {
            // Get the selected training data
            TrainingData selectedData = trainingHistory.get(position);
            // Open the detail page for the selected training data
            showDetail(selectedData);
        });
    }

    // Navigate to the detail page (TrainingDetailActivity) with training data
    private void showDetail(TrainingData data) {
        Intent intent = new Intent(requireContext(), TrainingDetailActivity.class);
        intent.putExtra("date", data.getDate());
        intent.putExtra("action", data.getAction());
        intent.putExtra("reps", data.getReps());
        intent.putExtra("correctness", data.getCorrectness());
        startActivity(intent);
    }

    // Inner class to store training data
    private static class TrainingData {
        private final String date;
        private final String action; // Action name (e.g., Squat, Push-Up)
        private final int reps; // Number of repetitions
        private final int correctness; // Correctness percentage

        // Constructor to initialize training data
        public TrainingData(String date, String action, int reps, int correctness) {
            this.date = date;
            this.action = action;
            this.reps = reps;
            this.correctness = correctness;
        }

        public String getDate() {
            return date;
        }

        public String getAction() {
            return action;
        }

        public int getReps() {
            return reps;
        }

        public int getCorrectness() {
            return correctness;
        }
    }
}