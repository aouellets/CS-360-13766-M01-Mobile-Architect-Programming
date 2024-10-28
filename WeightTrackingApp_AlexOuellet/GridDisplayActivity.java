package com.example.weighttrackingapp_alexouellet;

import android.database.Cursor;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class GridDisplayActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private GridAdapter gridAdapter;
    private List<WeightEntry> weightEntryList;
    private BottomNavigationView bottomNavigationView;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_display);

        recyclerView = findViewById(R.id.recyclerView);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        dbHelper = new DatabaseHelper(this);
        weightEntryList = loadWeightEntries();
        gridAdapter = new GridAdapter(weightEntryList);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(gridAdapter);

        setupBottomNavigation();
    }

    private List<WeightEntry> loadWeightEntries() {
        List<WeightEntry> entries = new ArrayList<>();
        Cursor cursor = dbHelper.getAllWeights();

        if (cursor.moveToFirst()) {
            do {
                String date = cursor.getString(cursor.getColumnIndex(DatabaseHelper.WEIGHT_COL_2));
                float weight = cursor.getFloat(cursor.getColumnIndex(DatabaseHelper.WEIGHT_COL_3));
                entries.add(new WeightEntry(String.valueOf(weight), date));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return entries;
    }

    private void setupBottomNavigation() {
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    startActivity(new Intent(GridDisplayActivity.this, MainActivity.class));
                    return true;
                case R.id.navigation_history:
                    return true;
                case R.id.navigation_settings:
                    startActivity(new Intent(GridDisplayActivity.this, SMSPermissionActivity.class));
                    return true;
            }
            return false;
        });
    }
}
