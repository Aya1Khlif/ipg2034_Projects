package com.example.test6;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;

public class ViewMedicinesActivity extends BaseActivity {

    // UI elements for search and filters
    private EditText etSearch;
    private Spinner spCabinet, spShelf;
    private Button btnSearch;

    // UI elements for displaying medicines
    private ListView lvMedicines;
    private DBHelper dbHelper;
    private ArrayAdapter<String> medicinesAdapter;

    // Log tag for debugging
    private static final String TAG = "ViewMedicinesActivity";
    private static final int EDIT_MEDICINE_REQUEST = 1;

    // UI elements for settings and logout
    private Button btnSettings, btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_medicines);

        // Bind UI elements to components
        etSearch = findViewById(R.id.et_search);
        spCabinet = findViewById(R.id.sp_cabinet);
        spShelf = findViewById(R.id.sp_shelf);
        btnSearch = findViewById(R.id.btn_search);
        lvMedicines = findViewById(R.id.lv_medicines);
        btnSettings = findViewById(R.id.btn_settings);
        btnLogout = findViewById(R.id.btn_logout);

        dbHelper = new DBHelper(this);

        // Load all medicines initially
        loadAllMedicines();

        // Apply user preferences for the UI
        applyPreferences();

        // Load cabinets into spinner
        loadCabinets();

        // Set listener for cabinet spinner
        spCabinet.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedCabinet = (String) parent.getItemAtPosition(position);
                loadShelves(selectedCabinet);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        // Set click listener for the search button
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchMedicines();
            }
        });

        // Set click listener for the medicines list view items
        lvMedicines.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedMedicine = (String) parent.getItemAtPosition(position);
                Intent intent = new Intent(ViewMedicinesActivity.this, edit_medicine.class);
                intent.putExtra("medicine_info", selectedMedicine);
                startActivityForResult(intent, EDIT_MEDICINE_REQUEST);
            }
        });

        // Set click listener for the Settings button
        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewMedicinesActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });

        // Set click listener for the Logout button
        btnLogout.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), SplashActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }

    // Handle result from edit medicine activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EDIT_MEDICINE_REQUEST && resultCode == RESULT_OK) {
            // Reload medicines to reflect the changes
            loadAllMedicines();
        }
    }

    // Load all medicines into the list view
    private void loadAllMedicines() {
        try {
            List<String> medicines = dbHelper.getAllMedicines();
            medicinesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, medicines);
            lvMedicines.setAdapter(medicinesAdapter);
        } catch (Exception e) {
            Log.e(TAG, "Error loading medicines", e);
            Toast.makeText(this, "Error loading medicines", Toast.LENGTH_SHORT).show();
        }
    }

    // Search medicines based on user input
    private void searchMedicines() {
        try {
            String searchText = etSearch.getText().toString().trim();
            String cabinet = spCabinet.getSelectedItem().toString();
            String shelf = spShelf.getSelectedItem().toString();

            List<String> medicines = dbHelper.searchMedicines(searchText, cabinet, shelf);
            medicinesAdapter = new ArrayAdapter<>(ViewMedicinesActivity.this, android.R.layout.simple_list_item_1, medicines);
            lvMedicines.setAdapter(medicinesAdapter);
        } catch (Exception e) {
            Log.e(TAG, "Error searching medicines", e);
            Toast.makeText(this, "Error searching medicines", Toast.LENGTH_SHORT).show();
        }
    }

    // Load cabinets into the cabinet spinner
    private void loadCabinets() {
        try {
            List<String> cabinets = dbHelper.getAllCabinets();
            cabinets.add(0, "All"); // Adding "All" option at the beginning
            ArrayAdapter<String> cabinetsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, cabinets);
            cabinetsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spCabinet.setAdapter(cabinetsAdapter);
        } catch (Exception e) {
            Log.e(TAG, "Error loading cabinets", e);
            Toast.makeText(this, "Error loading cabinets", Toast.LENGTH_SHORT).show();
        }
    }

    // Load shelves into the shelf spinner based on selected cabinet
    private void loadShelves(String cabinetName) {
        try {
            List<String> shelves = dbHelper.getShelvesByCabinet(cabinetName);
            shelves.add(0, "All"); // Adding "All" option at the beginning
            ArrayAdapter<String> shelvesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, shelves);
            shelvesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spShelf.setAdapter(shelvesAdapter);
        } catch (Exception e) {
            Log.e(TAG, "Error loading shelves", e);
            Toast.makeText(this, "Error loading shelves", Toast.LENGTH_SHORT).show();
        }
    }
}
