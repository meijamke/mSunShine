package com.example.msunshine.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceManager;

import com.example.msunshine.R;

public class MSunshinePreference {

    public static String getPreferredWeatherCity(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString(
                context.getString(R.string.pref_city_key),
                context.getString(R.string.pref_city_default));
    }

    public static String getPredferedTempUnits(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString(
                context.getString(R.string.pref_temp_units_key),
                context.getString(R.string.pref_temp_units_default));
    }
}
