package com.example.test6;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class usermanagment extends BaseActivity {

    // Define UI elements
    private Button btn_add, btn_viewAll;
    private EditText et_Username, et_Password;
    private Spinner sp_Role;
    private ListView Lv_Alluser;
    private CustomUserAdapter allUser;
    private DBHelper dbHelper;
    private Button btnSettings, btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usermanagment);

        // Initialize UI elements
        btn_add = findViewById(R.id.btn_add);
        btn_viewAll = findViewById(R.id.btn_Viewall);
        et_Username = findViewById(R.id.newusername);
        et_Password = findViewById(R.id.newpassword);
        sp_Role = findViewById(R.id.Role);
        Lv_Alluser = findViewById(R.id.Lv_alluser);
        btnSettings = findViewById(R.id.btn_settings);
        btnLogout = findViewById(R.id.btn_logout);

        // Initialize database helper and load all users into the adapter
        dbHelper = new DBHelper(usermanagment.this);
        List<userModel> users = dbHelper.getAll();
        allUser = new CustomUserAdapter(usermanagment.this, users);
        Lv_Alluser.setAdapter(allUser);

        // Apply user preferences
        applyPreferences();

        // Setup role spinner with predefined roles
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.roles_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_Role.setAdapter(adapter);

        // Add user button click listener
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userModel user;
                try {
                    // Check if username or password fields are empty
                    if (et_Username.length() == 0 || et_Password.length() == 0) {
                        Toast.makeText(usermanagment.this, "Please fill in all the information", Toast.LENGTH_SHORT).show();
                    } else {
                        // Create new user model and add to database
                        String selectedRole = sp_Role.getSelectedItem().toString();
                        user = new userModel(-1, et_Username.getText().toString(), et_Password.getText().toString(), selectedRole);

                        DBHelper dbHelper = new DBHelper(usermanagment.this);
                        boolean adduser = dbHelper.adduser(user);
                        Toast.makeText(usermanagment.this, "Success= " + adduser, Toast.LENGTH_SHORT).show();
                        // Update user list
                        allUser.clear();
                        allUser.addAll(dbHelper.getAll());
                        allUser.notifyDataSetChanged();
                    }
                } catch (Exception e) {
                    Toast.makeText(usermanagment.this, "Error adding user", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Settings button click listener
        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(usermanagment.this, SettingsActivity.class);
                startActivity(intent);
            }
        });

        // Logout button click listener
        btnLogout.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), SplashActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        // View all users button click listener
        btn_viewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBHelper dbHelper = new DBHelper(usermanagment.this);
                allUser.clear();
                allUser.addAll(dbHelper.getAll());
                allUser.notifyDataSetChanged();
            }
        });

        // List view item click listener for deleting user
        Lv_Alluser.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                userModel user = (userModel) parent.getItemAtPosition(position);
                dbHelper.deleteUser(user);
                allUser.clear();
                allUser.addAll(dbHelper.getAll());
                allUser.notifyDataSetChanged();
                Toast.makeText(usermanagment.this, "Deleted: " + user, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
