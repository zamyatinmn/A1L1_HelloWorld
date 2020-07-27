package com.example.a1l1_helloworld;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private Switch wtgSwitch;
    private int  windValue;
    private TextView textView;
    private final double COEFFICIENT = 1.5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        setOnClickBehavior();
    }

    private void setOnClickBehavior() {
        wtgSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                windValue = (int) ((double) windValue * COEFFICIENT);
                textView.setText(String.valueOf(windValue));
            }
        });
    }

    private void initViews() {
        Resources resources = getResources();
        wtgSwitch = findViewById(R.id.wtgSwitch);
        windValue = resources.getInteger(R.integer.windValue);
        textView = findViewById(R.id.windValue);
    }
}