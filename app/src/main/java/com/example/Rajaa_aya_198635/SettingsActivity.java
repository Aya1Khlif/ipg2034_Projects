package com.example.test6;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Switch;
import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends BaseActivity  {

    private Switch switchSound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Button btnCustomizeColors = findViewById(R.id.btn_customize_colors);
        switchSound = findViewById(R.id.switch_sound);

        // Load sound preference
        SharedPreferences preferences = getSharedPreferences("AppPreferences", MODE_PRIVATE);
        boolean soundEnabled = preferences.getBoolean("soundEnabled", true);
        switchSound.setChecked(soundEnabled);

        btnCustomizeColors.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this, CustomizeColorsActivity.class);
            startActivity(intent);
        });
        applyPreferences();

        switchSound.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("soundEnabled", isChecked);
            editor.apply();
        });
    }
}
