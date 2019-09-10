package com.example.msunshine;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

    private TextView mWeatherDataText;
    private EditText mSearchEditText;

    private TextView mErrorMsgShowTextView;
    private ProgressBar mSearchProgressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mWeatherDataText = findViewById(R.id.tv_weather_data);
        mSearchEditText = findViewById(R.id.et_search);

        mErrorMsgShowTextView = findViewById(R.id.tv_error_message);
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
            loadWeatherData();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void loadWeatherData() {
        String location = mSearchEditText.getText().toString();
        new FetchFromUrl().execute(location);
    }

    public void showWeatherData() {
        mWeatherDataText.setVisibility(View.VISIBLE);
        mErrorMsgShowTextView.setVisibility(View.INVISIBLE);
    }

    public void showErrorMessage() {
        mWeatherDataText.setVisibility(View.INVISIBLE);
        mErrorMsgShowTextView.setVisibility(View.VISIBLE);
    }

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
            mWeatherDataText.setText("");

            if (weatherData != null) {
                showWeatherData();
                for (String weather : weatherData)
                    mWeatherDataText.append(weather);
            } else
                showErrorMessage();
        }
    }
}
