package com.example.msunshine;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ShareCompat;
import androidx.databinding.DataBindingUtil;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import com.example.msunshine.data.IntentData;
import com.example.msunshine.data.MSunshinePreference;
import com.example.msunshine.data.ParseWeatherData;
import com.example.msunshine.data.WeatherContract;
import com.example.msunshine.databinding.ActivityDetailBinding;
import com.example.msunshine.utilities.DateUtils;
import com.example.msunshine.utilities.ExplicitIntentActivityUtils;
import com.example.msunshine.utilities.WeatherImageUtils;

public class DetailActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    private ActivityDetailBinding mDetailBinding;

    private String weatherDate;
    private String weatherString;

    private static final int ID_WEATHER_DETAIL_CURSOR = 2;

    public static final String[] DETAIL_PROJECTION = {
            WeatherContract.WeatherEntry.COLUMN_DATE,
            WeatherContract.WeatherEntry.COLUMN_WEEK,
            WeatherContract.WeatherEntry.COLUMN_DAY_CONDITION,
            WeatherContract.WeatherEntry.COLUMN_NIGHT_CONDITION,
            WeatherContract.WeatherEntry.COLUMN_DAY_TEMP,
            WeatherContract.WeatherEntry.COLUMN_NIGHT_TEMP,
            WeatherContract.WeatherEntry.COLUMN_DAY_WIND_DIRECTION,
            WeatherContract.WeatherEntry.COLUMN_NIGHT_WIND_DIRECTION,
            WeatherContract.WeatherEntry.COLUMN_DAY_WIND_POWER,
            WeatherContract.WeatherEntry.COLUMN_NIGHT_WIND_POWER
    };


    public static final int INDEX_WEATHER_DATE = 0;
    public static final int INDEX_WEATHER_WEEK = 1;
    public static final int INDEX_WEATHER_DAY_CONDITION = 2;
    public static final int INDEX_WEATHER_NIGHT_CONDITION = 3;
    public static final int INDEX_WEATHER_DAY_TEMP = 4;
    public static final int INDEX_WEATHER_NIGHT_TEMP = 5;
    public static final int INDEX_WEATHER_DAY_WIND_DIRECTION = 6;
    public static final int INDEX_WEATHER_NIGHT_WIND_DIRECTION = 7;
    public static final int INDEX_WEATHER_DAY_WIND_POWER = 8;
    public static final int INDEX_WEATHER_NIGHT_WIND_POWER = 9;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail);

        weatherDate = getIntent().getStringExtra(IntentData.STRING_WEATHER_DATE);
        if (weatherDate == null)
            throw new NullPointerException("String date for detailActivity cannot be null");

        getSupportLoaderManager().initLoader(ID_WEATHER_DETAIL_CURSOR, null, this);
    }


    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle bundle) {

        switch (id) {
            case ID_WEATHER_DETAIL_CURSOR:
                return new CursorLoader(
                        this,
                        WeatherContract.WeatherEntry.CONTENT_URI,
                        DETAIL_PROJECTION,
                        WeatherContract.WeatherEntry.COLUMN_DATE + " =? ",
                        new String[]{weatherDate},
                        null);
            default:
                throw new RuntimeException("Unknown loader ID :" + id);
        }

    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {

        if (!(cursor != null && cursor.moveToFirst()))
            return;
        String dayCondition = cursor.getString(DetailActivity.INDEX_WEATHER_DAY_CONDITION);
        String nightCondition = cursor.getString(DetailActivity.INDEX_WEATHER_NIGHT_CONDITION);
        String dayTemperature = MSunshinePreference.formatTemperature(this, cursor.getString(DetailActivity.INDEX_WEATHER_DAY_TEMP));
        String nightTemperature = MSunshinePreference.formatTemperature(this, cursor.getString(DetailActivity.INDEX_WEATHER_NIGHT_TEMP));

        int weatherIcon;
        String weatherCondition;
        if (DateUtils.getNormalizedHourNow() >= getResources().getInteger(R.integer.hour_between_night_and_day) &&
                DateUtils.getNormalizedHourNow() <= getResources().getInteger(R.integer.hour_between_day_and_night)) {

            weatherIcon = WeatherImageUtils.getLargeArtResIdForWeatherCondition(dayCondition);
            weatherCondition = dayCondition;
        } else {
            weatherIcon = WeatherImageUtils.getLargeArtResIdForWeatherCondition(dayCondition);
            weatherCondition = nightCondition;
        }
        String date = cursor.getString(DetailActivity.INDEX_WEATHER_DATE);
        mDetailBinding.weatherPrimary.weatherIcon.setImageResource(weatherIcon);
        mDetailBinding.weatherPrimary.weatherDate.setText(date);
        mDetailBinding.weatherPrimary.weatherWeek.setText(cursor.getString(DetailActivity.INDEX_WEATHER_WEEK));
        mDetailBinding.weatherPrimary.weatherDescription.setText(weatherCondition);
        mDetailBinding.weatherPrimary.weatherHighTemperature.setText(dayTemperature);
        mDetailBinding.weatherPrimary.weatherLowTemperature.setText(nightTemperature);


        mDetailBinding.weatherDetail.dayWindDirectLabel.setText(ParseWeatherData.getWeatherInfo(this, R.string.day_wind_direction));
        mDetailBinding.weatherDetail.dayWindDirect.setText(cursor.getString(DetailActivity.INDEX_WEATHER_DAY_WIND_DIRECTION));

        mDetailBinding.weatherDetail.nightWindDirectLabel.setText(ParseWeatherData.getWeatherInfo(this, R.string.night_wind_direction));
        mDetailBinding.weatherDetail.nightWindDirect.setText(cursor.getString(DetailActivity.INDEX_WEATHER_NIGHT_WIND_DIRECTION));

        mDetailBinding.weatherDetail.dayWindPowerLabel.setText(ParseWeatherData.getWeatherInfo(this, R.string.day_wind_power));
        mDetailBinding.weatherDetail.dayWindPower.setText(cursor.getString(DetailActivity.INDEX_WEATHER_DAY_WIND_POWER));

        mDetailBinding.weatherDetail.nightWindPowerLabel.setText(ParseWeatherData.getWeatherInfo(this, R.string.night_wind_power));
        mDetailBinding.weatherDetail.nightWindPower.setText(cursor.getString(DetailActivity.INDEX_WEATHER_NIGHT_WIND_POWER));

        weatherString = String.format("%s - %s\n%s - %s",
                date, weatherCondition, dayTemperature, nightTemperature);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

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
                shareWeather(weatherString);
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
     * 分享内容的另一个方法：
     * 在onCreateOptionsMenu()方法中，通过menu。finditem(int id)获取MenuItem
     * MenuItem直接调用setIntent，传入建好的Intent
     **/
    private Intent shareText() {
        ShareCompat.IntentBuilder intentBuilder = ShareCompat.IntentBuilder.from(this)
                .setChooserTitle("choose which app to share")
                .setType("text/*")
                .setText(weatherString);
        return intentBuilder.getIntent();
    }
}
