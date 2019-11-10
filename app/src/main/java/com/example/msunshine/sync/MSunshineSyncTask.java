package com.example.msunshine.sync;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;

import com.example.msunshine.data.MSunshinePreference;
import com.example.msunshine.data.WeatherContract;
import com.example.msunshine.utilities.NetworkUtils;
import com.example.msunshine.utilities.NotificationUtils;
import com.example.msunshine.utilities.ParseJSONUtils;

import java.net.URL;

class MSunshineSyncTask {

    synchronized static void syncWeather(Context context, String city) {
        URL url = NetworkUtils.buildWeatherUrl(city);
        try {

            String weatherJson = NetworkUtils.getResponseFromHttpUrl(url);
            ContentValues[] weatherValues = ParseJSONUtils.getWeatherContentValuesFromJSON(context, weatherJson);

            ContentResolver resolver = context.getContentResolver();
            resolver.delete(
                    WeatherContract.WeatherEntry.CONTENT_URI,
                    null,
                    null);
            if (weatherValues != null && weatherValues.length != 0)
                resolver.bulkInsert(
                        WeatherContract.WeatherEntry.CONTENT_URI,
                        weatherValues);

            if (MSunshinePreference.getPreferredWeatherNotification(context) &&
                    MSunshinePreference.overOneDaySinceLastNotificationTime(context))
                NotificationUtils.notifyUserTodayWeather(context);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
