package com.example.msunshine;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.msunshine.data.ExplicitIntentData;
import com.example.msunshine.data.WeatherContract;
import com.example.msunshine.utilities.ExplicitIntentActivityUtils;

public class WeatherDetailActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    private String weatherDate;
    private String weatherString;
    private TextView mWeatherDetailTextView;

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
        setContentView(R.layout.activity_weather_detail);

        mWeatherDetailTextView = findViewById(R.id.detail_text_view);

        weatherDate = getIntent().getStringExtra(ExplicitIntentData.STRING_CITY_NAME);
        if (weatherDate == null)
            throw new NullPointerException("Uri for detailActivity cannot be null");

        getSupportLoaderManager().initLoader(ID_WEATHER_DETAIL_CURSOR, null, this);
    }


    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle bundle) {

        switch (id) {
            case ID_WEATHER_DETAIL_CURSOR:
                return new CursorLoader(
                        this,
                        WeatherContract.CONTENT_URI,
                        DETAIL_PROJECTION,
                        WeatherContract.WeatherEntry.COLUMN_DATE + " = " + weatherDate,
                        null,
                        null);
            default:
                throw new RuntimeException("Unknown loader ID :" + id);
        }

    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {

        if (!(cursor != null && cursor.moveToFirst()))
            return;

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(cursor.getString(WeatherDetailActivity.INDEX_WEATHER_DATE));
        stringBuilder.append("\n");
        stringBuilder.append(cursor.getString(WeatherDetailActivity.INDEX_WEATHER_WEEK));
        stringBuilder.append("\n");
        stringBuilder.append(cursor.getString(WeatherDetailActivity.INDEX_WEATHER_DAY_CONDITION));
        stringBuilder.append("\n");
        stringBuilder.append(cursor.getString(WeatherDetailActivity.INDEX_WEATHER_NIGHT_CONDITION));
        stringBuilder.append("\n");
        stringBuilder.append(cursor.getString(WeatherDetailActivity.INDEX_WEATHER_DAY_TEMP));
        stringBuilder.append("\n");
        stringBuilder.append(cursor.getString(WeatherDetailActivity.INDEX_WEATHER_NIGHT_TEMP));
        stringBuilder.append("\n");
        stringBuilder.append(cursor.getString(WeatherDetailActivity.INDEX_WEATHER_DAY_WIND_DIRECTION));
        stringBuilder.append("\n");
        stringBuilder.append(cursor.getString(WeatherDetailActivity.INDEX_WEATHER_NIGHT_WIND_DIRECTION));
        stringBuilder.append("\n");
        stringBuilder.append(cursor.getString(WeatherDetailActivity.INDEX_WEATHER_DAY_WIND_POWER));
        stringBuilder.append("\n");
        stringBuilder.append(cursor.getString(WeatherDetailActivity.INDEX_WEATHER_NIGHT_WIND_POWER));

        weatherString = stringBuilder.toString();
        mWeatherDetailTextView.setText(weatherString);
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
