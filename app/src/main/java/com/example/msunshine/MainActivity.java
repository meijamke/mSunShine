package com.example.msunshine;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.msunshine.utilities.NetworkUtils;
import com.example.msunshine.utilities.ParseJSONUtils;

import java.net.URL;

public class MainActivity extends AppCompatActivity {

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
        mForecastAdapter = new ForecastAdapter();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        mRecyclerView.setAdapter(mForecastAdapter);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        mSearchCity = findViewById(R.id.et_search);
        mErrorMsgDisplay = findViewById(R.id.tv_error_message);
        mSearchProgressBar = findViewById(R.id.pb_search_progress);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_search) {

            mForecastAdapter.setWeatherData(null);
            loadWeatherData();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void loadWeatherData() {
        String location = mSearchCity.getText().toString();
        new FetchFromUrl().execute(location);
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
    public class FetchFromUrl extends AsyncTask<String, Void, String[]> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mSearchProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String[] doInBackground(String... loc) {

            if (loc == null)
                return null;

            String location = loc[0];

            String[] weatherData = null;
            try {
                URL url = NetworkUtils.mBuildUrl(location);
                String urlRespone = NetworkUtils.getResponseFromHttpUrl(url);
                weatherData = ParseJSONUtils.getForecastWeatherStringFromJSON(urlRespone);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return weatherData;
        }

        @Override
        protected void onPostExecute(String[] weatherData) {

            mSearchProgressBar.setVisibility(View.INVISIBLE);

            if (weatherData != null) {
                showWeatherData();
                mForecastAdapter.setWeatherData(weatherData);
            } else
                showErrorMessage();
        }
    }
}
