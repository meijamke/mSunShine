package com.example.msunshine.utilities;

import android.content.Context;
import android.content.Intent;

import com.example.msunshine.MainActivity;
import com.example.msunshine.SettingActivity;
import com.example.msunshine.DetailActivity;
import com.example.msunshine.data.ExplicitIntentData;

public class ExplicitIntentActivityUtils {

    public static void toDetail(Context context, String weatherData) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(ExplicitIntentData.STRING_CITY_NAME, weatherData);
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
