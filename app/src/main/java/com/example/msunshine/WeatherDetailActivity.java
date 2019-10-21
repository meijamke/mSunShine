package com.example.msunshine;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.msunshine.data.ExplicitIntent;

public class WeatherDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_detail);

        Intent intent = getIntent();
        if (intent.hasExtra(ExplicitIntent.STRING_WEATHER_DATA)) {
            String weatherData = intent.getStringExtra(ExplicitIntent.STRING_WEATHER_DATA);
        }
    }
}
