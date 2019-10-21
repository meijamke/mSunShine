package com.example.msunshine.utilities;

import android.content.Context;
import android.content.Intent;

import com.example.msunshine.WeatherDetailActivity;

public class ExplictIntentToActivityUtils {
    public static void toWeatherDetail(Context context) {
        Intent intent = new Intent(context, WeatherDetailActivity.class);
        context.startActivity(intent);
    }
}
