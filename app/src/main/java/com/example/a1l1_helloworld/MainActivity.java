package com.example.a1l1_helloworld;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a1l1_helloworld.Model.WeatherRequest;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.gson.Gson;
import com.squareup.otto.Subscribe;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.Objects;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity implements IRVOnItemClick {

    private SwitchMaterial wtgSwitch;
    private MaterialButton settingsBtn;
    private MaterialButton wikiBtn;
    private TextView windValueView;
    private TextView city;
    private TextView windWord;
    private TextView windUnits;
    private TextView humidity;
    private TextView humidityValue;
    private TextView humidityUnits;
    private TextView tempValue;
    private final String windVisKey = "windValueDataKey";
    private final String humidityVisKey = "windValueDataKey";
    final static String cityPositionKey = "cityPositionKey";
    final static String windKey = "windKey";
    final static String humidityKey = "humidityKey";
    private final static int requestCode = 13213;
    private final double COEFFICIENT = 1.5;
    String[] cities;
    private int windValue;
    private FailConnectionDialog dialog;


    private static final String TAG = "WEATHER";
    private static final String API_KEY = "d1fd6772013e96880c7c68af2f56a08d";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(R.layout.cities_and_main_fragments);
        } else {
            setContentView(R.layout.activity_main);
        }
        initViews();
        setOnClickBehavior();
        setOnSwitchBehavior();

        if (savedInstanceState != null) {
            setWindDataVisibility(Objects.requireNonNull(savedInstanceState.getBundle("key")).getInt(windVisKey));
            setHumidityDataVisibility(savedInstanceState.getInt(humidityVisKey));
            city.setText(savedInstanceState.getString("from list"));
        }
        connect(city.getText().toString());
    }

    // TODO: 18.08.2020 Вынести работу с сетью в отдельный класс
    private void connect(String city){
        try {
            String weatherUrl = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&units=metric&appid=";
            final URL uri = new URL(weatherUrl + API_KEY);
            final Handler handler = new Handler(); // Запоминаем основной поток
            new Thread(() -> {
                HttpsURLConnection urlConnection = null;
                try {
                    urlConnection = (HttpsURLConnection) uri.openConnection();
                    urlConnection.setRequestMethod("GET"); // установка метода получения данных -GET
                    urlConnection.setReadTimeout(10000); // установка таймаута - 10 000 миллисекунд
                    BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream())); // читаем  данные в поток
                    String result = getLines(in);
                    // преобразование данных запроса в модель
                    Gson gson = new Gson();
                    final WeatherRequest weatherRequest = gson.fromJson(result, WeatherRequest.class);
                    // Возвращаемся к основному потоку
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            displayWeather(weatherRequest);
                        }
                    });
                } catch (Exception e) {
                    Log.e(TAG, "Fail connection", e);
                    e.printStackTrace();
                    dialog = new FailConnectionDialog();
                    dialog.show(getSupportFragmentManager(), "custom");
                } finally {
                    if (null != urlConnection) {
                        urlConnection.disconnect();
                    }
                }
            }).start();
        } catch (MalformedURLException e) {
            Log.e(TAG, "Fail URI", e);
            e.printStackTrace();
        }
    }

    private static String getLines(BufferedReader reader) {
        StringBuilder rawData = new StringBuilder(1024);
        String tempVariable;

        while (true) {
            try {
                tempVariable = reader.readLine();
                if (tempVariable == null) break;
                rawData.append(tempVariable).append("\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return rawData.toString();
    }

    private void displayWeather(WeatherRequest weatherRequest){
        String temperatureValue = String.format(Locale.getDefault(), "%.0f", weatherRequest.getMain().getTemp());
        tempValue.setText(temperatureValue);

        String humidityStr = String.format(Locale.getDefault(), "%d", weatherRequest.getMain().getHumidity());
        humidityValue.setText(humidityStr);

        String windSpeedStr = String.format(Locale.getDefault(), "%.0f", weatherRequest.getWind().getSpeed());
        windValueView.setText(windSpeedStr);
        windValue = (int) weatherRequest.getWind().getSpeed();
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getBus().register(this);
    }

    @Override
    protected void onStop() {
        EventBus.getBus().unregister(this);
        super.onStop();
    }

    /**
     * This method set behavior for WTG switch
     * It take wind value and multiplies it by a coefficient
     * This allows user to see the wind speed at an altitude of 100m
     */
    private void setOnSwitchBehavior() {
        wtgSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                windValueView.setText(String.valueOf((int) ((double) windValue * COEFFICIENT)));
            } else {
                windValueView.setText(String.valueOf(windValue));
            }
        });
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle SaveInstanceState) {
        Bundle bundle = new Bundle();
        bundle.putInt(windVisKey, windWord.getVisibility());
        SaveInstanceState.putBundle("key", bundle);
        SaveInstanceState.putInt(humidityVisKey, humidity.getVisibility());
        SaveInstanceState.putString("from list", city.getText().toString());
        super.onSaveInstanceState(SaveInstanceState);
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
     * This method take states from all views to transfer to SettingsActivity
     *
     * @param intent Intent launching SettingsActivity
     */
    private void setParametersForSettingsAct(Intent intent) {
        for (int i = 0; i < cities.length; i++) {
            if (city.getText().equals(cities[i])) {
                intent.putExtra(cityPositionKey, i);
            }
        }
        if (windWord.getVisibility() == View.VISIBLE) {
            intent.putExtra(windKey, true);
        } else {
            intent.putExtra(windKey, false);
        }
        if (humidity.getVisibility() == View.VISIBLE) {
            intent.putExtra(humidityKey, true);
        } else {
            intent.putExtra(humidityKey, false);
        }
    }

    /**
     * Initialization of all used views
     */
    private void initViews() {
        Resources resources = getResources();
        wtgSwitch = findViewById(R.id.wtgSwitch);
        windValue = resources.getInteger(R.integer.windValue);
        windValueView = findViewById(R.id.windValue);
        settingsBtn = findViewById(R.id.settingsBtn);
        city = findViewById(R.id.city);
        wikiBtn = findViewById(R.id.wikiBtn);
        windWord = findViewById(R.id.windWord);
        windUnits = findViewById(R.id.windUnits);
        cities = resources.getStringArray(R.array.Cities);
        humidity = findViewById(R.id.humidity);
        humidityValue = findViewById(R.id.humidityValue);
        humidityUnits = findViewById(R.id.humidityUnits);
        tempValue = findViewById(R.id.temperatureValue);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == MainActivity.requestCode && resultCode == RESULT_OK && data != null) {
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                String strData = data.getStringExtra(SettingsActivity.cityKey);
                city.setText(strData);
            }
            if (!data.getBooleanExtra(SettingsActivity.windKey, false)) {
                setWindDataVisibility(View.INVISIBLE);
            } else {
                setWindDataVisibility(View.VISIBLE);
            }
            if (!data.getBooleanExtra(SettingsActivity.humidityKey, false)) {
                setHumidityDataVisibility(View.INVISIBLE);
            } else {
                setHumidityDataVisibility(View.VISIBLE);
            }

        }
        connect(city.getText().toString());
    }

    private void setHumidityDataVisibility(int visibility) {
        humidity.setVisibility(visibility);
        humidityValue.setVisibility(visibility);
        humidityUnits.setVisibility(visibility);
    }

    private void setWindDataVisibility(int visibility) {
        windValueView.setVisibility(visibility);
        wtgSwitch.setVisibility(visibility);
        windUnits.setVisibility(visibility);
        windWord.setVisibility(visibility);
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void onSomeEvent(textEvent event) {
        city.setText(event.text);
        connect(city.getText().toString());
    }

    @Override
    public void onItemClicked(String itemText) {
        Toast.makeText(this, itemText, Toast.LENGTH_SHORT).show();
    }
}