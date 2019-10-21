package com.example.msunshine;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.example.msunshine.data.ExplicitIntent;

public class WeatherDetailActivity extends AppCompatActivity {

    private String weatherData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_detail);

        Intent intent = getIntent();
        if (intent.hasExtra(ExplicitIntent.STRING_WEATHER_DATA)) {
            weatherData = intent.getStringExtra(ExplicitIntent.STRING_WEATHER_DATA);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_share) {
            shareWeather(weatherData);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void shareWeather(String weatherData) {
        ShareCompat.IntentBuilder.from(this)
                .setChooserTitle("choose which app to share")
                .setType("text/*")
                .setText(weatherData)
                .startChooser();
    }
}
