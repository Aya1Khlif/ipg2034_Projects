package com.example.test6;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.AdapterView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;

public class emp_view extends BaseActivity  {

    private EditText etSearch;
    private Spinner spCabinet, spShelf;
    private Button btnSearch, btnSettings, btnLogout;;
    private ListView lvMedicines;
    private DBHelper dbHelper;
    private ArrayAdapter<String> medicinesAdapter;
    private ArrayAdapter<String> cabinetsAdapter;
    private ArrayAdapter<String> shelvesAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_medicines);

        etSearch = findViewById(R.id.et_search);
        spCabinet = findViewById(R.id.sp_cabinet);
        spShelf = findViewById(R.id.sp_shelf);
        btnSearch = findViewById(R.id.btn_search);
        lvMedicines = findViewById(R.id.lv_medicines);
        dbHelper = new DBHelper(this);
        btnSettings = findViewById(R.id.btn_settings);
        btnLogout = findViewById(R.id.btn_logout);

        // Load all medicines initially
        loadAllMedicines();

        // Load cabinets into spinner
        loadCabinets();
        applyPreferences();

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

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchMedicines();
            }
        });
        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(emp_view.this, SettingsActivity.class);
                startActivity(intent);
            }
        });

        btnLogout.setOnClickListener(v -> {

            Intent intent = new Intent(getApplicationContext(), SplashActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }

    private void loadAllMedicines() {
        List<String> medicines = dbHelper.getAllMedicines();
        medicinesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, medicines);
        lvMedicines.setAdapter(medicinesAdapter);
    }

    private void searchMedicines() {
        String searchText = etSearch.getText().toString().trim();
        String cabinet = spCabinet.getSelectedItem().toString();
        String shelf = spShelf.getSelectedItem().toString();

        List<String> medicines = dbHelper.searchMedicines(searchText, cabinet, shelf);
        medicinesAdapter = new ArrayAdapter<>(emp_view.this, android.R.layout.simple_list_item_1, medicines);
        lvMedicines.setAdapter(medicinesAdapter);
    }

    private void loadCabinets() {
        List<String> cabinets = dbHelper.getAllCabinets();
        cabinets.add(0, "All"); // Adding "All" option at the beginning
        cabinetsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, cabinets);
        cabinetsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCabinet.setAdapter(cabinetsAdapter);
    }

    private void loadShelves(String cabinetName) {
        List<String> shelves = dbHelper.getShelvesByCabinet(cabinetName);
        shelves.add(0, "All"); // Adding "All" option at the beginning
        shelvesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, shelves);
        shelvesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spShelf.setAdapter(shelvesAdapter);
    }
}
