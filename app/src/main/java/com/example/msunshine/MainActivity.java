package com.example.msunshine;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.msunshine.data.MSunshinePreference;
import com.example.msunshine.data.WeatherContract;
import com.example.msunshine.data.WeatherDbHelper;
import com.example.msunshine.utilities.ExplicitIntentActivityUtils;
import com.example.msunshine.utilities.MSunshineDateUtils;
import com.example.msunshine.utilities.NetworkUtils;
import com.example.msunshine.utilities.ParseJSONUtils;

import java.net.URL;

public class MainActivity extends AppCompatActivity implements
        ForecastAdapter.OnClickListItemListener,
        LoaderManager.LoaderCallbacks<String[]>,
        SharedPreferences.OnSharedPreferenceChangeListener {

    public static final String[] MAIN_PROJECTION = new String[]{
            WeatherContract.WeatherEntry.COLUMN_DATE,
            WeatherContract.WeatherEntry.COLUMN_WEEK,
            WeatherContract.WeatherEntry.COLUMN_DAY_CONDITION,
            WeatherContract.WeatherEntry.COLUMN_NIGHT_CONDITION,
            WeatherContract.WeatherEntry.COLUMN_DAY_TEMP,
            WeatherContract.WeatherEntry.COLUMN_NIGHT_TEMP
    };
    public static final int INDEX_WEATHER_DATE = 0;
    public static final int INDEX_WEATHER_WEEK = 1;
    public static final int INDEX_WEATHER_DAY_CONDITION = 2;
    public static final int INDEX_WEATHER_NIGHT_CONDITION = 3;
    public static final int INDEX_WEATHER_DAY_TEMP = 4;
    public static final int INDEX_WEATHER_NIGHT_TEMP = 5;
    private static final int ID_WEATHER_STRING_LOADER = 0;
    private static boolean pref_flag = false;
    private RecyclerView mRecyclerView;
    private ForecastAdapter mForecastAdapter;
    private EditText mSearchCity;
    private TextView mErrorMsgDisplay;
    private ProgressBar mSearchProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.rv_forecast);
        mForecastAdapter = new ForecastAdapter(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        mRecyclerView.setAdapter(mForecastAdapter);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        mSearchCity = findViewById(R.id.et_search);
        mErrorMsgDisplay = findViewById(R.id.tv_error_message);
        mSearchProgressBar = findViewById(R.id.pb_search_progress);

        PreferenceManager.getDefaultSharedPreferences(this)
                .registerOnSharedPreferenceChangeListener(this);

        String city = MSunshinePreference.getPreferredWeatherCity(this);
        mSearchCity.setText(city);
        mSearchCity.setSelection(city.length());

        getSupportLoaderManager().initLoader(ID_WEATHER_STRING_LOADER, null, this);

    }

    /***
     * 使偏好生效
     * */
    @Override
    protected void onStart() {
        super.onStart();
        if (pref_flag) {
            String city = MSunshinePreference.getPreferredWeatherCity(this);
            mSearchCity.setText(city);
            mSearchCity.setSelection(city.length());
            getSupportLoaderManager().restartLoader(ID_WEATHER_STRING_LOADER, null, this);
            pref_flag = false;

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onClickItem(String weatherData) {
        ExplicitIntentActivityUtils.toWeatherDetail(this, weatherData);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                mForecastAdapter.setWeatherData(null);
                getSupportLoaderManager().restartLoader(ID_WEATHER_STRING_LOADER, null, this);
                return true;
            case R.id.action_setting:
                ExplicitIntentActivityUtils.toSetting(this);
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void showWeatherData() {
        mRecyclerView.setVisibility(View.VISIBLE);
        mErrorMsgDisplay.setVisibility(View.INVISIBLE);
    }

    public void showErrorMessage() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMsgDisplay.setVisibility(View.VISIBLE);
    }

    public ContentValues stringToContentValues(String data) {
        ContentValues values = new ContentValues();
        String[] strings = data.split("\n");
        for (int i = 0; i < strings.length; i++)
            values.put(MAIN_PROJECTION[i], strings[i]);
        return values;
    }

    /**
     * 若搜索成功，则显示搜索到的数据，同时将新数据写入SQLiteDatabase
     * 若搜索失败，则读取SQLiteDatabase，将从今天开始的天气数据取出显示
     **/
    @SuppressLint("StaticFieldLeak")
    @NonNull
    @Override
    public Loader<String[]> onCreateLoader(final int id, @Nullable Bundle bundle) {
        return new AsyncTaskLoader<String[]>(this) {

            String[] weatherData = null;

            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                if (weatherData != null)
                    deliverResult(weatherData);
                else {
                    mSearchProgressBar.setVisibility(View.VISIBLE);
                    forceLoad();
                }
            }

            @Override
            public String[] loadInBackground() {

                String location = mSearchCity.getText().toString();

                try {
                    URL url = NetworkUtils.buildWeatherUrl(location);
                    String urlResponse = NetworkUtils.getResponseFromHttpUrl(url);
                    weatherData = ParseJSONUtils.getForecastWeatherStringFromJSON(getContext(), urlResponse, ParseJSONUtils.TYPE_WEATHER_SUMMARY);
                    for (String weather : weatherData) {
                        ContentValues values = stringToContentValues(weather);
                        new WeatherDbHelper(getContext()).getWritableDatabase().insert(
                                WeatherContract.WeatherEntry.TABLE_NAME,
                                null,
                                values
                        );
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (weatherData == null) {
                    Cursor cursor = new WeatherDbHelper(getContext()).getReadableDatabase().query(
                            WeatherContract.WeatherEntry.TABLE_NAME,
                            MAIN_PROJECTION,
                            WeatherContract.WeatherEntry.COLUMN_DATE + ">=?",
                            MSunshineDateUtils.normalizedDateNow(),
                            null,
                            null,
                            WeatherContract.WeatherEntry.COLUMN_DATE + "ASC"
                    );
                    if (cursor != null && cursor.moveToFirst())
                        for (int i = 0; i < cursor.getCount(); i++)
                            weatherData[i] =
                                    cursor.getString(MainActivity.INDEX_WEATHER_DATE) +
                                            cursor.getString(MainActivity.INDEX_WEATHER_WEEK) +
                                            cursor.getString(MainActivity.INDEX_WEATHER_DAY_CONDITION) +
                                            cursor.getString(MainActivity.INDEX_WEATHER_NIGHT_CONDITION) +
                                            cursor.getString(MainActivity.INDEX_WEATHER_DAY_TEMP) +
                                            cursor.getString(MainActivity.INDEX_WEATHER_NIGHT_TEMP);
                    assert cursor != null;
                    cursor.close();
                }
                return weatherData;
            }

            @Override
            public void deliverResult(@Nullable String[] data) {
                weatherData = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String[]> loader, String[] weatherData) {

        mSearchProgressBar.setVisibility(View.INVISIBLE);
        mForecastAdapter.setWeatherData(weatherData);

        if (weatherData != null) {
            showWeatherData();
        } else
            showErrorMessage();
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String[]> loader) {

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        pref_flag = true;
    }
}
