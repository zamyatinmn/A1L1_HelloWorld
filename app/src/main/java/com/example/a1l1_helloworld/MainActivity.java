package com.example.a1l1_helloworld;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private Switch wtgSwitch;
    private Button settingsBtn;
    private Button wikiBtn;
    private TextView textView;
    private TextView city;
    private TextView windWord;
    private TextView windUnits;
    private final String windValueDataKey = "windValueDataKey";
    final static String cityPositionKey = "cityPositionKey";
    final static  String windKey = "windKey";
    private final static int requestCode = 13213;
    private final double COEFFICIENT = 1.5;
    String[] cities;
    private int windValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        setOnClickBehavior();
        setOnSwitchBehavior();
    }

    /**
     * This method set behavior for WTG switch
     * It take wind value and multiplies it by a coefficient
     * This allows user to see the wind speed at an altitude of 100m
     */
    private void setOnSwitchBehavior() {
        wtgSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    textView.setText(String.valueOf((int) ((double) windValue * COEFFICIENT)));
                } else {
                    textView.setText(String.valueOf(windValue));
                }

            }
        });
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle SaveInstanceState) {
        String value = textView.getText().toString();
        SaveInstanceState.putString(windValueDataKey, value);
        super.onSaveInstanceState(SaveInstanceState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        String value = savedInstanceState.getString(windValueDataKey);
        textView.setText(value);
    }

    /**
     * This method set behavior for "settings" button
     * It launch SettingsActivity with transfer views parameters
     */
    private void setOnClickBehavior() {
        settingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                setParametersForSettingsAct(intent);
                startActivityForResult(intent, requestCode);
            }
        });

        wikiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://ru.wikipedia.org/wiki/" + city.getText().toString();
                Uri uri = Uri.parse(url);
                Intent browser = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(browser);
            }
        });
    }

    /**
     *  This method take states from all views to transfer to SettingsActivity
     * @param intent Intent launching SettingsActivity
     */
    private void setParametersForSettingsAct(Intent intent) {
        for (int i = 0; i < cities.length; i++) {
            if (city.getText().equals(cities[i])) {
                intent.putExtra(cityPositionKey, i);
            }
        }
        if (windWord.getVisibility() == View.VISIBLE){
            intent.putExtra(windKey, true);
        } else {
            intent.putExtra(windKey, false);
        }
    }

    /**
     * Initialization of all used views
     */
    private void initViews() {
        Resources resources = getResources();
        wtgSwitch = findViewById(R.id.wtgSwitch);
        windValue = resources.getInteger(R.integer.windValue);
        textView = findViewById(R.id.windValue);
        settingsBtn = findViewById(R.id.settingsBtn);
        city = findViewById(R.id.city);
        wikiBtn = findViewById(R.id.wikiBtn);
        windWord = findViewById(R.id.windWord);
        windUnits = findViewById(R.id.windUnits);
        cities = resources.getStringArray(R.array.Cities);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == MainActivity.requestCode && resultCode == RESULT_OK && data != null) {
            String strData = data.getStringExtra(SettingsActivity.cityKey);
            if (!data.getBooleanExtra(SettingsActivity.windKey, false)) {
                textView.setVisibility(View.GONE);
                wtgSwitch.setVisibility(View.GONE);
                windUnits.setVisibility(View.GONE);
                windWord.setVisibility(View.GONE);
            } else {
                textView.setVisibility(View.VISIBLE);
                wtgSwitch.setVisibility(View.VISIBLE);
                windUnits.setVisibility(View.VISIBLE);
                windWord.setVisibility(View.VISIBLE);
            }
            city.setText(strData);
        }
    }


}