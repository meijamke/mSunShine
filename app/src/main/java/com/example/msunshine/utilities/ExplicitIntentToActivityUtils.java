package com.example.msunshine.utilities;

import android.content.Context;
import android.content.Intent;

import com.example.msunshine.WeatherDetailActivity;
import com.example.msunshine.data.ExplicitIntent;

public class ExplicitIntentToActivityUtils {

    public static void toWeatherDetail(Context context, String weatherData) {
        Intent intent = new Intent(context, WeatherDetailActivity.class);
        intent.putExtra(ExplicitIntent.STRING_WEATHER_DATA, weatherData);
        context.startActivity(intent);
    }

}
