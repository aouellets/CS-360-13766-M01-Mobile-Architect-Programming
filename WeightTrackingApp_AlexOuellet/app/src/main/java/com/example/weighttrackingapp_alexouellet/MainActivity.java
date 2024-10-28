package com.example.weighttrackingapp_alexouellet;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AlertDialog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final int SMS_PERMISSION_REQUEST_CODE = 100;
    private EditText weightEditText;
    private TextView lastEntryText;
    private Button saveWeightButton;
    private BottomNavigationView bottomNavigationView;
    private SharedPreferences sharedPreferences;
    private DatabaseHelper databaseHelper;

    private static final String PREF_INITIAL_WEIGHT = "initialWeight";
    private static final String PREF_GOAL_WEIGHT = "goalWeight";
    private static final String PREF_GOAL_TYPE = "goalType";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeViews();
        setupSharedPreferencesAndDatabase();
        updateLastEntryText();
        setupClickListeners();
        setupBottomNavigation();
        setupWeightGoal();
    }

    // Initialize UI components
    private void initializeViews() {
        weightEditText = findViewById(R.id.weightEditText);
        lastEntryText = findViewById(R.id.lastEntryText);
        saveWeightButton = findViewById(R.id.saveWeightButton);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
    }

    // Set up SharedPreferences and DatabaseHelper
    private void setupSharedPreferencesAndDatabase() {
        sharedPreferences = getSharedPreferences("WeightPrefs", MODE_PRIVATE);
        databaseHelper = new DatabaseHelper(this);
    }

    // Set up click listeners for buttons
    private void setupClickListeners() {
        saveWeightButton.setOnClickListener(v -> saveWeight());
    }

    // Save weight entry to the database
    private void saveWeight() {
        String weightStr = weightEditText.getText().toString().trim();
        
        if (TextUtils.isEmpty(weightStr)) {
            weightEditText.setError("Weight cannot be empty");
            return;
        }

        try {
            float weight = Float.parseFloat(weightStr);
            
            if (weight <= 0 || weight > 1000) {  // Adjust the upper limit as needed
                weightEditText.setError("Please enter a valid weight (0-1000 kg)");
                return;
            }

            String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
            
            if (databaseHelper.addWeightEntry(currentDate, weight)) {
                Toast.makeText(this, "Weight saved successfully", Toast.LENGTH_SHORT).show();
                weightEditText.setText("");
                updateLastEntryText();
                checkWeightGoal(weight);
            } else {
                Toast.makeText(this, "Error saving weight", Toast.LENGTH_SHORT).show();
            }
        } catch (NumberFormatException e) {
            weightEditText.setError("Please enter a valid number");
        }
    }

    // Get current date in yyyy-MM-dd format
    private String getCurrentDate() {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
    }

    // Check if weight goal is achieved and send SMS notification if necessary
    private void checkWeightGoal(float currentWeight) {
        float initialWeight = sharedPreferences.getFloat(PREF_INITIAL_WEIGHT, 0);
        float goalWeight = sharedPreferences.getFloat(PREF_GOAL_WEIGHT, 0);
        String goalType = sharedPreferences.getString(PREF_GOAL_TYPE, "");

        if (initialWeight == 0 || goalWeight == 0 || goalType.isEmpty()) {
            // Weight goal not set up yet
            return;
        }

        boolean goalAchieved = false;
        String message = "";

        if (goalType.equals("loss")) {
            if (currentWeight <= goalWeight && currentWeight < initialWeight) {
                goalAchieved = true;
                message = "Congratulations! You've reached your weight loss goal of " + goalWeight + " kg!";
            }
        } else if (goalType.equals("gain")) {
            if (currentWeight >= goalWeight && currentWeight > initialWeight) {
                goalAchieved = true;
                message = "Congratulations! You've reached your weight gain goal of " + goalWeight + " kg!";
            }
        }

        if (goalAchieved) {
            sendSMSNotification(message);
            showGoalAchievedDialog(message);
        }
    }

    // Send SMS notification
    private void sendSMSNotification(String message) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
            String phoneNumber = sharedPreferences.getString("phoneNumber", "");
            if (!TextUtils.isEmpty(phoneNumber)) {
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(phoneNumber, null, message, null, null);
                Toast.makeText(this, "SMS notification sent!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Phone number not set. Please update in settings.", Toast.LENGTH_LONG).show();
            }
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, SMS_PERMISSION_REQUEST_CODE);
        }
    }

    // Handle permission request result
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == SMS_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "SMS Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "SMS Permission Denied. Some features may not work.", Toast.LENGTH_LONG).show();
            }
        }
    }

    // Update the last entry text view with the most recent weight entry
    private void updateLastEntryText() {
        Cursor cursor = databaseHelper.getAllWeights();
        if (cursor.moveToFirst()) {
            float lastWeight = cursor.getFloat(cursor.getColumnIndex(DatabaseHelper.WEIGHT_COL_3));
            String lastDate = cursor.getString(cursor.getColumnIndex(DatabaseHelper.WEIGHT_COL_2));
            lastEntryText.setText(String.format("Last Entry: %.1f kg on %s", lastWeight, lastDate));
        } else {
            lastEntryText.setText("No previous entries");
        }
        cursor.close();
    }

    // Set up bottom navigation
    private void setupBottomNavigation() {
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    return true;
                case R.id.navigation_history:
                    startActivity(new Intent(MainActivity.this, GridDisplayActivity.class));
                    return true;
                case R.id.navigation_settings:
                    startActivity(new Intent(MainActivity.this, SMSPermissionActivity.class));
                    return true;
            }
            return false;
        });
    }

    // Call this method when setting up the activity
    private void setupWeightGoal() {
        if (!sharedPreferences.contains(PREF_INITIAL_WEIGHT)) {
            // Prompt user to enter initial weight and goal weight
            showWeightGoalDialog();
        }
    }

    private void showWeightGoalDialog() {
        // Implement a dialog to get initial weight, goal weight, and goal type (loss or gain)
        // Save these values to SharedPreferences
        // This is a placeholder for the actual implementation
    }

    private void showGoalAchievedDialog(String message) {
        new AlertDialog.Builder(this)
            .setTitle("Goal Achieved!")
            .setMessage(message)
            .setPositiveButton("OK", null)
            .show();
    }
}
