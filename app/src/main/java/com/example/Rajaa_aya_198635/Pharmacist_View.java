package com.example.test6;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Pharmacist_View extends BaseActivity {

    // Declare buttons for different actions
    private Button btnViewMedicines;
    private Button btnAddMedicine;
    private Button btnSettings, btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pharmacist_view);

        // Initialize buttons
        btnViewMedicines = findViewById(R.id.btn_view_medicines);
        btnAddMedicine = findViewById(R.id.btn_add_medicine);
        btnSettings = findViewById(R.id.btn_settings);
        btnLogout = findViewById(R.id.btn_logout);

        // Apply user preferences
        applyPreferences();

        // Set click listener for View Medicines button
        btnViewMedicines.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Pharmacist_View.this, ViewMedicinesActivity.class);
                startActivity(intent);
            }
        });

        // Set click listener for Add Medicine button
        btnAddMedicine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Pharmacist_View.this, AddMedicineActivity.class);
                startActivity(intent);
            }
        });

        // Set click listener for Settings button
        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Pharmacist_View.this, SettingsActivity.class);
                startActivity(intent);
            }
        });

        // Set click listener for Logout button
        btnLogout.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), SplashActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        // Apply window insets to handle system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
