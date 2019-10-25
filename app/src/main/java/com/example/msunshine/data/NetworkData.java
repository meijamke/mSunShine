package com.example.msunshine.data;


import android.content.ContentValues;
import android.content.Context;

import com.example.msunshine.R;
import com.example.msunshine.utilities.ParseJSONUtils;

public class NetworkData {

    public static String getWeekName(Context context, String date) {
        int number = Integer.parseInt(date);
        switch (number) {
            case 1:
                return context.getString(R.string.monday);
            case 2:
                return context.getString(R.string.tuesday);
            case 3:
                return context.getString(R.string.wednesday);
            case 4:
                return context.getString(R.string.thursday);
            case 5:
                return context.getString(R.string.friday);
            case 6:
                return context.getString(R.string.saturday);
            case 7:
                return context.getString(R.string.sunday);
            default:
                break;
        }
        return null;
    }

    public static String getWeatherInfo(Context context, int weatherResId) {
        String weatherInfo = "";
        switch (weatherResId) {
            case R.string.day_condition:
                weatherInfo = context.getString(R.string.day_condition);
                break;
            case R.string.night_condition:
                weatherInfo = context.getString(R.string.night_condition);
                break;
            case R.string.day_temp:
                weatherInfo = context.getString(R.string.day_temp);
                break;
            case R.string.night_temp:
                weatherInfo = context.getString(R.string.night_temp);
                break;
            case R.string.day_wind_direction:
                weatherInfo = context.getString(R.string.day_wind_direction);
                break;
            case R.string.night_wind_direction:
                weatherInfo = context.getString(R.string.night_wind_direction);
                break;
            case R.string.day_wind_power:
                weatherInfo = context.getString(R.string.day_wind_power);
                break;
            case R.string.night_wind_power:
                weatherInfo = context.getString(R.string.night_wind_power);
                break;
            default:
                break;
        }
        return weatherInfo;
    }

    public static ContentValues[] stringToContentValues(String[] data, int dataType) {
        ContentValues[] contentValues = new ContentValues[data.length];

        if (dataType == ParseJSONUtils.TYPE_WEATHER_SUMMARY)
            for (int i = 0; i < data.length; i++) {
                String[] temp = data[i].split("\n");
                ContentValues values = new ContentValues();
                values.put(WeatherContract.WeatherEntry.COLUMN_DATE, temp[0]);
                values.put(WeatherContract.WeatherEntry.COLUMN_WEEK, temp[1]);
                values.put(WeatherContract.WeatherEntry.COLUMN_DAY_CONDITION, temp[2]);
                values.put(WeatherContract.WeatherEntry.COLUMN_NIGHT_CONDITION, temp[3]);
                values.put(WeatherContract.WeatherEntry.COLUMN_DAY_TEMP, temp[4]);
                values.put(WeatherContract.WeatherEntry.COLUMN_NIGHT_TEMP, temp[5]);

                contentValues[i] = values;
            }

        if (dataType == ParseJSONUtils.TYPE_WEATHER_DETAIL)
            for (int i = 0; i < data.length; i++) {
                String[] temp = data[i].split("\n");
                ContentValues values = new ContentValues();
                values.put(WeatherContract.WeatherEntry.COLUMN_DATE, temp[0]);
                values.put(WeatherContract.WeatherEntry.COLUMN_WEEK, temp[1]);
                values.put(WeatherContract.WeatherEntry.COLUMN_DAY_CONDITION, temp[2]);
                values.put(WeatherContract.WeatherEntry.COLUMN_NIGHT_CONDITION, temp[3]);
                values.put(WeatherContract.WeatherEntry.COLUMN_DAY_TEMP, temp[4]);
                values.put(WeatherContract.WeatherEntry.COLUMN_NIGHT_TEMP, temp[5]);
                values.put(WeatherContract.WeatherEntry.COLUMN_DAY_WIND_DIRECTION, temp[6]);
                values.put(WeatherContract.WeatherEntry.COLUMN_NIGHT_WIND_DIRECTION, temp[7]);
                values.put(WeatherContract.WeatherEntry.COLUMN_DAY_WIND_POWER, temp[8]);
                values.put(WeatherContract.WeatherEntry.COLUMN_NIGHT_WIND_POWER, temp[9]);

                contentValues[i] = values;
            }
        return contentValues;
    }
}
