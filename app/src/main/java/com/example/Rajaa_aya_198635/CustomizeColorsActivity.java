package com.example.test6;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioGroup;

public class CustomizeColorsActivity extends BaseActivity {

    private RadioGroup radioGroupColors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customize_colors);

        radioGroupColors = findViewById(R.id.radioGroupColors);
        Button btnApplyColors = findViewById(R.id.btn_apply_colors);

        // Load saved color scheme
        SharedPreferences preferences = getSharedPreferences("AppPreferences", MODE_PRIVATE);
        int savedSchemeOrdinal = preferences.getInt("colorScheme", ColorScheme.SCHEME_1.ordinal());
        ColorScheme savedScheme = ColorScheme.values()[savedSchemeOrdinal];
        selectRadioButton(savedScheme);

        btnApplyColors.setOnClickListener(v -> {
            ColorScheme selectedScheme = getSelectedColorScheme();
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt("colorScheme", selectedScheme.ordinal());
            editor.apply();

            // Apply color scheme immediately
            applyColorScheme(selectedScheme);

            // Navigate back to the login page
            Intent intent = new Intent(CustomizeColorsActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

        // Apply saved color scheme on startup
        applyColorScheme(savedScheme);
    }

    // Method to select the radio button based on the saved color scheme
    private void selectRadioButton(ColorScheme colorScheme) {
        if (colorScheme == ColorScheme.SCHEME_1) {
            radioGroupColors.check(R.id.radio_color_scheme_1);
        } else if (colorScheme == ColorScheme.SCHEME_2) {
            radioGroupColors.check(R.id.radio_color_scheme_2);
        } else if (colorScheme == ColorScheme.SCHEME_3) {
            radioGroupColors.check(R.id.radio_color_scheme_3);
        }
    }

    // Method to get the selected color scheme from the radio group
    private ColorScheme getSelectedColorScheme() {
        int selectedId = radioGroupColors.getCheckedRadioButtonId();
        if (selectedId == R.id.radio_color_scheme_1) {
            return ColorScheme.SCHEME_1;
        } else if (selectedId == R.id.radio_color_scheme_2) {
            return ColorScheme.SCHEME_2;
        } else if (selectedId == R.id.radio_color_scheme_3) {
            return ColorScheme.SCHEME_3;
        } else {
            return ColorScheme.SCHEME_1; // Default color scheme
        }
    }
}
