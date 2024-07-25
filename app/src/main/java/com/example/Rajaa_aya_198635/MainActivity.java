package com.example.test6;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends BaseActivity {
    EditText usernameEdt;
    EditText passwordEdt;
    Button btglogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI elements
        usernameEdt = findViewById(R.id.username);
        passwordEdt = findViewById(R.id.password);
        btglogin = findViewById(R.id.btn_login);

        // Manually apply preferences to ensure they are applied after setting the layout
        applyPreferences();
    }

    public void go(View view) {
        String username = usernameEdt.getText().toString();
        String password = passwordEdt.getText().toString();

        // Validate user input
        if (username.isEmpty() || password.isEmpty()) {
            Toast toast = Toast.makeText(MainActivity.this, "enter username and password", Toast.LENGTH_LONG);
            toast.show();
        } else {
            // Check hardcoded credentials for basic user
            if (username.equals("user") && password.equals("user")) {
                Intent intent = new Intent(MainActivity.this, usermanagment.class);
                startActivity(intent);
                finish();
            } else {
                // Check credentials for employee and pharmacist roles
                DBHelper dbh = new DBHelper(MainActivity.this);
                Boolean checkemp = dbh.Loginemp(username, password);
                if (checkemp) {
                    Intent intent = new Intent(MainActivity.this, emp_view.class);
                    startActivity(intent);
                    finish();
                } else {
                    DBHelper dbh1 = new DBHelper(MainActivity.this);
                    Boolean checkPharmacist = dbh1.LoginPharmacist(username, password);
                    if (checkPharmacist) {
                        Intent intent = new Intent(MainActivity.this, Pharmacist_View.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(this, "user or password incorrect", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }
}
