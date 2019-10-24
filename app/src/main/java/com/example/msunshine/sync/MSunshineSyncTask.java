package com.example.msunshine.sync;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;

import com.example.msunshine.data.WeatherContract;
import com.example.msunshine.data.WeatherData;
import com.example.msunshine.utilities.NetworkUtils;
import com.example.msunshine.utilities.ParseJSONUtils;

import java.net.URL;

public class MSunshineSyncTask {

    synchronized static void syncWeather(Context context, String city) {
        URL url = NetworkUtils.buildWeatherUrl(city);
        try {

            String weatherJson = NetworkUtils.getResponseFromHttpUrl(url);
            String[] weatherString = ParseJSONUtils.getForecastWeatherStringFromJSON(context, weatherJson, ParseJSONUtils.TYPE_WEATHER_DETAIL);
            ContentValues[] weatherValues = WeatherData.stringToContentValues(weatherString, ParseJSONUtils.TYPE_WEATHER_DETAIL);

//            WeatherDbHelper mDbHelper=new WeatherDbHelper(context);
//            SQLiteDatabase db=mDbHelper.getWritableDatabase();
//            db.delete(
//                    WeatherContract.WeatherEntry.TABLE_NAME,
//                    null,
//                    null);

            ContentResolver resolver = context.getContentResolver();
            resolver.delete(
                    WeatherContract.CONTENT_URI,
                    null,
                    null);
            if (weatherValues != null && weatherValues.length != 0)
                resolver.bulkInsert(WeatherContract.CONTENT_URI, weatherValues);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
