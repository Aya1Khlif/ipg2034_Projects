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

public class AddMedicineActivity extends BaseActivity {

    // Log tag for debugging
    private static final String TAG = "AddMedicineActivity";

    // UI elements
    private EditText etMedicineName, etDescription, etQuantity, etCompanyName, etCabinetName, etShelfNumber;
    private Button btnAddMedicine;
    private DBHelper dbHelper;
    private Button btnSettings, btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medicine);

        // Bind UI elements to components
        etMedicineName = findViewById(R.id.et_medicine_name);
        etDescription = findViewById(R.id.et_description);
        etQuantity = findViewById(R.id.et_quantity);
        etCompanyName = findViewById(R.id.et_company_name);
        etCabinetName = findViewById(R.id.et_cabinet_name);
        etShelfNumber = findViewById(R.id.et_shelf_number);
        btnAddMedicine = findViewById(R.id.btn_add_medicine);
        btnSettings = findViewById(R.id.btn_settings);
        btnLogout = findViewById(R.id.btn_logout);

        // Initialize the database helper
        dbHelper = new DBHelper(this);

        // Set click listener for the Add Medicine button
        btnAddMedicine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMedicine();
            }
        });

        // Set click listener for the Settings button
        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddMedicineActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });

        // Apply user preferences for the UI
        applyPreferences();

        // Set click listener for the Logout button
        btnLogout.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), SplashActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }

    // Method to add a new medicine
    private void addMedicine() {
        try {
            // Get input values from the user
            String medicineName = etMedicineName.getText().toString().trim();
            String description = etDescription.getText().toString().trim();
            String quantityStr = etQuantity.getText().toString().trim();
            String companyName = etCompanyName.getText().toString().trim();
            String cabinetName = etCabinetName.getText().toString().trim();
            String shelfNumber = etShelfNumber.getText().toString().trim();

            // Check that all fields are filled
            if (medicineName.isEmpty() || description.isEmpty() || quantityStr.isEmpty() || companyName.isEmpty() || cabinetName.isEmpty() || shelfNumber.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            int quantity = Integer.parseInt(quantityStr);

            // Check if company and cabinet exist and add them if they don't
            int companyId = dbHelper.addOrGetCompanyId(companyName);
            int cabinetId = dbHelper.addOrGetCabinetId(cabinetName);
            int shelfId = dbHelper.addOrGetShelfId(cabinetId, shelfNumber);

            // Insert the new medicine into the database
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put("drug_name", medicineName);
            cv.put("description", description);
            cv.put("quantity", quantity);
            cv.put("company_id", companyId);
            cv.put("cabinet_id", cabinetId);
            cv.put("shelf_id", shelfId);

            long result = db.insert("Drugs", null, cv);
            if (result == -1) {
                Toast.makeText(this, "Failed to add medicine", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Medicine added successfully", Toast.LENGTH_SHORT).show();
                clearFields();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error adding medicine", e);
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    // Method to clear input fields
    private void clearFields() {
        etMedicineName.setText("");
        etDescription.setText("");
        etQuantity.setText("");
        etCompanyName.setText("");
        etCabinetName.setText("");
        etShelfNumber.setText("");
    }
}
