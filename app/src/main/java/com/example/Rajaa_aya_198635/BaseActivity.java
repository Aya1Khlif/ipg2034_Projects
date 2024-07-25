package com.example.test6;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public abstract class BaseActivity extends AppCompatActivity {

    private static final String TAG = "BaseActivity";

    // Enum to define color schemes
    public enum ColorScheme {
        SCHEME_1,
        SCHEME_2,
        SCHEME_3
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: Applying preferences");
        applyPreferences(); // Apply saved preferences
    }

    // Method to apply user preferences
    protected void applyPreferences() {
        SharedPreferences preferences = getSharedPreferences("AppPreferences", MODE_PRIVATE);
        int colorSchemeOrdinal = preferences.getInt("colorScheme", ColorScheme.SCHEME_1.ordinal());
        ColorScheme colorScheme = ColorScheme.values()[colorSchemeOrdinal];
        boolean soundEnabled = preferences.getBoolean("soundEnabled", true);

        // Apply color scheme preference
        Log.d(TAG, "applyPreferences: Applying color scheme " + colorScheme);
        applyColorScheme(colorScheme);

        // Apply sound preference
        if (soundEnabled) {
            Log.d(TAG, "applyPreferences: Sound enabled");
            // Enable sound on button click
        } else {
            Log.d(TAG, "applyPreferences: Sound disabled");
            // Disable sound on button click
        }
    }

    // Method to apply color scheme to the layout
    protected void applyColorScheme(ColorScheme scheme) {
        ConstraintLayout layoutMain = findViewById(R.id.main_layout);
        ConstraintLayout layoutMainAlt = findViewById(R.id.main);
        ConstraintLayout layout = (layoutMain != null) ? layoutMain : layoutMainAlt;

        if (layout == null) {
            Log.d(TAG, "applyColorScheme: No layout found to apply colors");
            return; // No layout to apply colors to
        }

        Log.d(TAG, "applyColorScheme: Applying colors to layout");

        int backgroundColor, textColor, buttonColor, buttonTextColor;

        // Define colors based on selected scheme
        switch (scheme) {
            case SCHEME_1:
                backgroundColor = getResources().getColor(R.color.scheme1_background);
                textColor = getResources().getColor(R.color.scheme1_text);
                buttonColor = getResources().getColor(R.color.scheme1_button);
                buttonTextColor = getResources().getColor(R.color.scheme1_button_text);
                break;
            case SCHEME_2:
                backgroundColor = getResources().getColor(R.color.scheme2_background);
                textColor = getResources().getColor(R.color.scheme2_text);
                buttonColor = getResources().getColor(R.color.scheme2_button);
                buttonTextColor = getResources().getColor(R.color.scheme2_button_text);
                break;
            case SCHEME_3:
                backgroundColor = getResources().getColor(R.color.scheme3_background);
                textColor = getResources().getColor(R.color.scheme3_text);
                buttonColor = getResources().getColor(R.color.scheme3_button);
                buttonTextColor = getResources().getColor(R.color.scheme3_button_text);
                break;
            default:
                backgroundColor = getResources().getColor(R.color.scheme1_background);
                textColor = getResources().getColor(R.color.scheme1_text);
                buttonColor = getResources().getColor(R.color.scheme1_button);
                buttonTextColor = getResources().getColor(R.color.scheme1_button_text);
                break;
        }

        layout.setBackgroundColor(backgroundColor);

        // Update button colors in the layout
        updateButtonColors(layout, buttonColor, buttonTextColor);
    }

    // Method to update colors of all buttons in the layout
    private void updateButtonColors(ConstraintLayout layout, int buttonColor, int buttonTextColor) {
        for (int i = 0; i < layout.getChildCount(); i++) {
            View child = layout.getChildAt(i);
            if (child instanceof Button) {
                Button button = (Button) child;
                button.setBackgroundColor(buttonColor);
                button.setTextColor(buttonTextColor);
            }
        }
    }
}
