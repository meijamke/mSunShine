package com.example.msunshine;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.msunshine.data.MSunshinePreference;
import com.example.msunshine.utilities.NetworkUtils;
import com.example.msunshine.utilities.ParseJSONUtils;

import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private TextView mWeatherDataText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mWeatherDataText = findViewById(R.id.tv_weather_data);

        loadWeatherData();
    }

    public void loadWeatherData() {
        String location = MSunshinePreference.getDefaultPreference();
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
