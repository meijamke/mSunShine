package com.example.msunshine;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.msunshine.data.ExplicitIntentData;
import com.example.msunshine.utilities.ExplicitIntentActivityUtils;

public class WeatherDetailActivity extends AppCompatActivity {

    private String weatherData;
    private TextView mWeatherDetailTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_detail);

        mWeatherDetailTextView = findViewById(R.id.detail_text_view);
        Intent intent = getIntent();
        if (intent.hasExtra(ExplicitIntentData.STRING_WEATHER_DATA)) {
            weatherData = intent.getStringExtra(ExplicitIntentData.STRING_WEATHER_DATA);
            mWeatherDetailTextView.setText(weatherData);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:
                shareWeather(weatherData);
                return true;
            case R.id.action_setting:
                ExplicitIntentActivityUtils.toSetting(this);
                return true;
            default:
                break;
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

    /**
     * 在onCreateOptionsMenu()方法中，通过menu。finditem(int id)获取MenuItem
     * MenuItem直接调用setIntent，传入建好的Intent
     **/
    private Intent shareText() {
        ShareCompat.IntentBuilder intentBuilder = ShareCompat.IntentBuilder.from(this)
                .setChooserTitle("choose which app to share")
                .setType("text/*")
                .setText(weatherData);
        return intentBuilder.getIntent();
    }
}
