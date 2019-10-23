package com.example.msunshine.data;


import android.content.Context;

import com.example.msunshine.R;

public class WeatherInfo {

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
}
