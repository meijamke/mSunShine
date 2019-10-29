package com.example.msunshine.utilities;

import com.example.msunshine.R;

public class WeatherImageUtils {

    public static int getSmallIcResIdForWeatherCondition(String weatherCondition) {
        int resId = R.drawable.ic_logo;
        if (weatherCondition.equals("晴"))
            resId = R.drawable.ic_clear;
        if (weatherCondition.equals("阴"))
            resId = R.drawable.ic_light_clouds;
        if (weatherCondition.equals("多云"))
            resId = R.drawable.ic_clouds;
        if (weatherCondition.equals("阵雨"))
            resId = R.drawable.ic_light_rain;
        if (weatherCondition.equals("小雨"))
            resId = R.drawable.ic_light_rain;
        if (weatherCondition.equals("中雨"))
            resId = R.drawable.ic_rain;
        return resId;
    }

    public static int getLargeArtResIdForWeatherCondition(String weatherCondition) {
        int resId = R.drawable.ic_logo;
        if (weatherCondition.equals("晴"))
            resId = R.drawable.art_clear;
        if (weatherCondition.equals("阴"))
            resId = R.drawable.art_light_clouds;
        if (weatherCondition.equals("多云"))
            resId = R.drawable.art_clouds;
        if (weatherCondition.equals("阵雨"))
            resId = R.drawable.art_light_rain;
        if (weatherCondition.equals("小雨"))
            resId = R.drawable.art_light_rain;
        if (weatherCondition.equals("中雨"))
            resId = R.drawable.art_rain;
        return resId;
    }
}
