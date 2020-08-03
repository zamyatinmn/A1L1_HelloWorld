package com.example.a1l1_helloworld;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Switch wtgSwitch;
    private int  windValue;
    private TextView textView;
    private final double COEFFICIENT = 1.5;
    private Button settingsBtn;
    private final String windValueDataKey = "windValueDataKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        setOnClickBehavior();
        Toast.makeText(this, "onCreate", Toast.LENGTH_SHORT).show();
        Log.d("TAG", "onCreate");
        setOnSwitchBehavior();
    }

    private void setOnSwitchBehavior() {
        wtgSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
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

    @Override
    protected void onStart() {
        Toast.makeText(this, "onStart", Toast.LENGTH_SHORT).show();
        Log.d("TAG", "onStart");
        super.onStart();
    }

    @Override
    protected void onPause() {
        Toast.makeText(this, "onPause", Toast.LENGTH_SHORT).show();
        Log.d("TAG", "onPause");
        super.onPause();
    }

    @Override
    protected void onResume() {
        Toast.makeText(this, "onResume", Toast.LENGTH_SHORT).show();
        Log.d("TAG", "onResume");
        super.onResume();
    }

    @Override
    protected void onStop() {
        Toast.makeText(this, "onStop", Toast.LENGTH_SHORT).show();
        Log.d("TAG", "onStop");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Toast.makeText(this, "onDestroy", Toast.LENGTH_SHORT).show();
        Log.d("TAG", "onDestroy");
        super.onDestroy();
    }

    private void setOnClickBehavior() {
        settingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initViews() {
        Resources resources = getResources();
        wtgSwitch = findViewById(R.id.wtgSwitch);
        windValue = resources.getInteger(R.integer.windValue);
        textView = findViewById(R.id.windValue);
        settingsBtn = findViewById(R.id.settingsBtn);
    }
}