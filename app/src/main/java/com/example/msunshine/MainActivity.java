package com.example.msunshine;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.example.msunshine.utilities.NetworkUtils;
import com.example.msunshine.utilities.ParseJSONUtils;

import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private TextView mWeatherDataText;
    private EditText mSearchEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mWeatherDataText = findViewById(R.id.tv_weather_data);
        mSearchEditText = findViewById(R.id.et_search);
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

    public class FetchFromUrl extends AsyncTask<String, Void, String[]> {

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
            if (weatherData != null) {
                for (String weather : weatherData)
                    mWeatherDataText.append(weather);
            }
        }
    }
}
