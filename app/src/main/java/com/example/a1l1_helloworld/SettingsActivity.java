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
    Spinner spinner;
    CheckBox checkBoxWind;
    CheckBox checkBoxPrecipitation;
    final static String cityKey = "cityKey";
    final static String windKey = "windKey";
    final static String precipitationKey = "precipitation";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_layout);
        initViews();
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            spinner.setSelection(getIntent().getIntExtra(MainActivity.cityPositionKey, 3));
        }
        checkBoxWind.setChecked(getIntent().getBooleanExtra(MainActivity.windKey, true));
        checkBoxPrecipitation.setChecked(getIntent().getBooleanExtra(MainActivity.precipitationKey, true));
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

    }

    /**
     * Initialization of all used views
     */
    private void initViews() {
        spinner = findViewById(R.id.spinner);
        checkBoxWind = findViewById(R.id.checkBox_wind);
        checkBoxPrecipitation = findViewById(R.id.checkBox_precipitation);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            String text = spinner.getSelectedItem().toString();
            Intent dataIntent = new Intent();
            dataIntent.putExtra(cityKey, text);
            dataIntent.putExtra(windKey, checkBoxWind.isChecked());
            dataIntent.putExtra(precipitationKey, checkBoxPrecipitation.isChecked());
            setResult(RESULT_OK, dataIntent);
            finish();
        }
        return true;
    }
}
