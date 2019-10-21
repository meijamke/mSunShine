package com.example.msunshine;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.msunshine.utilities.ExplicitIntentActivityUtils;
import com.example.msunshine.utilities.NetworkUtils;
import com.example.msunshine.utilities.ParseJSONUtils;

import java.net.URL;

public class MainActivity extends AppCompatActivity implements ForecastAdapter.OnClickListItemListener, LoaderManager.LoaderCallbacks<String[]> {

    private static final int WEATHER_QUERY_SEARCH = 0;

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

        getSupportLoaderManager().initLoader(WEATHER_QUERY_SEARCH, null, this);
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
                getSupportLoaderManager().restartLoader(WEATHER_QUERY_SEARCH, null, this);
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

    @SuppressLint("StaticFieldLeak")
    @NonNull
    @Override
    public Loader<String[]> onCreateLoader(int i, @Nullable Bundle bundle) {
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
                    weatherData = ParseJSONUtils.getForecastWeatherStringFromJSON(urlResponse);
                } catch (Exception e) {
                    e.printStackTrace();
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

        if (weatherData != null) {
            showWeatherData();
            mForecastAdapter.setWeatherData(weatherData);
        } else
            showErrorMessage();
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String[]> loader) {

    }
}
