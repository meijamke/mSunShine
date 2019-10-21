package com.example.msunshine.utilities;

import android.content.Context;
import android.content.Intent;

import com.example.msunshine.MainActivity;
import com.example.msunshine.SettingActivity;
import com.example.msunshine.WeatherDetailActivity;
import com.example.msunshine.data.ExplicitIntent;

public class ExplicitIntentActivityUtils {

    public static void toWeatherDetail(Context context, String weatherData) {
        Intent intent = new Intent(context, WeatherDetailActivity.class);
        intent.putExtra(ExplicitIntent.STRING_WEATHER_DATA, weatherData);
        context.startActivity(intent);
    }

    public static void toMain(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    public static void toSetting(Context context) {
        Intent intent = new Intent(context, SettingActivity.class);
        context.startActivity(intent);
    }
}
