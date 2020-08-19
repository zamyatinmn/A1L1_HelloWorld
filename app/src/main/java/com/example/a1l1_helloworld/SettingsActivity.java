package com.example.a1l1_helloworld;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;


public class SettingsActivity extends AppCompatActivity {
    private Spinner spinner;
    private CheckBox checkBoxWind;
    private CheckBox checkBoxHumidity;
    private CheckBox darkThemeCB;
    final static String cityKey = "cityKey";
    final static String windKey = "windKey";
    final static String humidityKey = "humidity";
    private static boolean isChecked = false;


    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isChecked){
            setTheme(R.style.DarkTheme);
        }else {
            setTheme(R.style.SettingsTheme);
        }
        setContentView(R.layout.settings_layout);
        initViews();
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            spinner.setSelection(getIntent().getIntExtra(MainActivity.cityPositionKey, 3));
        }
        darkThemeCB.setChecked(isChecked);
        checkBoxWind.setChecked(getIntent().getBooleanExtra(MainActivity.windKey, true));
        checkBoxHumidity.setChecked(getIntent().getBooleanExtra(MainActivity.humidityKey, true));
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        darkThemeCB.setOnClickListener(v -> {
            isChecked = darkThemeCB.isChecked();
            recreate();
        });
    }

    /**
     * Initialization of all used views
     */
    private void initViews() {
        spinner = findViewById(R.id.spinner);
        checkBoxWind = findViewById(R.id.checkBox_wind);
        checkBoxHumidity = findViewById(R.id.checkBox_humidity);
        darkThemeCB = findViewById(R.id.dark_theme);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            String text = spinner.getSelectedItem().toString();
            Intent dataIntent = new Intent();
            dataIntent.putExtra(cityKey, text);
            dataIntent.putExtra(windKey, checkBoxWind.isChecked());
            dataIntent.putExtra(humidityKey, checkBoxHumidity.isChecked());
            setResult(RESULT_OK, dataIntent);
            finish();
        }
        return true;
    }
}
