package com.example.msunshine.data;


import android.content.Context;

import com.example.msunshine.R;

public class ParseWeatherData {

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
}
