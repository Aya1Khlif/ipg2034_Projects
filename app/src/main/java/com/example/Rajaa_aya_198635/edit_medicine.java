package com.example.test6;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class edit_medicine extends BaseActivity {

    // UI elements for medicine details
    private EditText etMedicineName, etDescription, etQuantity, etCompanyName, etCabinetName, etShelfNumber;
    private Button btnUpdateMedicine, btnDeleteMedicine;
    private DBHelper dbHelper;
    private int medicineId;
    private Button btnSettings, btnLogout;
    private static final String TAG = "EditMedicineActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_medicine);

        // Bind UI elements to components
        etMedicineName = findViewById(R.id.et_medicine_name);
        etDescription = findViewById(R.id.et_description);
        etQuantity = findViewById(R.id.et_quantity);
        etCompanyName = findViewById(R.id.et_company_name);
        etCabinetName = findViewById(R.id.et_cabinet_name);
        etShelfNumber = findViewById(R.id.et_shelf_number);
        btnSettings = findViewById(R.id.btn_settings);
        btnLogout = findViewById(R.id.btn_logout);
        btnUpdateMedicine = findViewById(R.id.btn_update_medicine);
        btnDeleteMedicine = findViewById(R.id.btn_delete_medicine); // New button for deleting medicine

        dbHelper = new DBHelper(this);
        applyPreferences();

        // Get medicine info from intent and populate fields
        String medicineInfo = getIntent().getStringExtra("medicine_info");
        Log.d(TAG, "Received medicine info: " + medicineInfo);
        populateFields(medicineInfo);

        // Set click listener for updating medicine
        btnUpdateMedicine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateMedicine();
            }
        });

        // Set click listener for deleting medicine
        btnDeleteMedicine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteMedicine();
            }
        });

        // Set click listener for Settings button
        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(edit_medicine.this, SettingsActivity.class);
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
    }

    // Method to populate fields with medicine info
    private void populateFields(String medicineInfo) {
        try {
            String[] parts = medicineInfo.split("\n");
            if (parts.length < 7) {
                throw new Exception("Invalid medicine info format");
            }
            medicineId = Integer.parseInt(parts[0].split(": ")[1]);
            etMedicineName.setText(parts[1].split(": ")[1]);
            etDescription.setText(parts[2].split(": ")[1]);
            etCompanyName.setText(parts[3].split(": ")[1]);
            etQuantity.setText(parts[4].split(": ")[1]);
            etCabinetName.setText(parts[5].split(": ")[1]);
            etShelfNumber.setText(parts[6].split(": ")[1]);
        } catch (Exception e) {
            Log.e(TAG, "Error populating fields", e);
            Toast.makeText(this, "Error populating fields: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    // Method to update medicine details
    private void updateMedicine() {
        try {
            String medicineName = etMedicineName.getText().toString().trim();
            String description = etDescription.getText().toString().trim();
            int quantity = Integer.parseInt(etQuantity.getText().toString().trim());
            String companyName = etCompanyName.getText().toString().trim();
            String cabinetName = etCabinetName.getText().toString().trim();
            String shelfNumber = etShelfNumber.getText().toString().trim();

            int companyId = dbHelper.addOrGetCompanyId(companyName);
            int cabinetId = dbHelper.addOrGetCabinetId(cabinetName);
            int shelfId = dbHelper.addOrGetShelfId(cabinetId, shelfNumber);

            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put("drug_name", medicineName);
            cv.put("description", description);
            cv.put("quantity", quantity);
            cv.put("company_id", companyId);
            cv.put("cabinet_id", cabinetId);
            cv.put("shelf_id", shelfId);

            long result = db.update("Drugs", cv, "drug_id = ?", new String[]{String.valueOf(medicineId)});
            if (result == -1) {
                Toast.makeText(this, "Failed to update medicine", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Medicine updated successfully", Toast.LENGTH_SHORT).show();
                Intent resultIntent = new Intent();
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error updating medicine", e);
            Toast.makeText(this, "Error updating medicine: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    // Method to delete medicine
    private void deleteMedicine() {
        try {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            long result = db.delete("Drugs", "drug_id = ?", new String[]{String.valueOf(medicineId)});
            if (result == -1) {
                Toast.makeText(this, "Failed to delete medicine", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Medicine deleted successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(edit_medicine.this, ViewMedicinesActivity.class);
                startActivity(intent);
                finish();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error deleting medicine", e);
            Toast.makeText(this, "Error deleting medicine: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
