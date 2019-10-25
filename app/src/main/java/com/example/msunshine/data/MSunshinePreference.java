package com.example.msunshine.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.msunshine.R;

public class MSunshinePreference {
	
    /**
     * 获取偏好的城市
     **/
    public static String getPreferredWeatherCity(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString(
                context.getString(R.string.pref_city_key),
                context.getString(R.string.pref_city_default));
    }

    /**
     * 获取偏好的温度单位
     **/
    private static String getPreferredTempUnits(Context context) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString(
                context.getString(R.string.pref_temp_units_key),
                context.getString(R.string.pref_temp_units_default));
    }

    /**
     * 提供的数据默认是摄氏度，将摄氏度转换成华氏度
     **/
    private static String celsiusToFahrenheit(Context context, String celsius) {
        String[] celsiusString = celsius.split(" ");
        String celsiusNumber = celsiusString[1].split("℃")[0];
        return celsiusString[0] + " " + String.format(context.getString(R.string.temperature_format), Integer.parseInt(celsiusNumber) * 1.8 + 32);
    }

    /**
     * 根据偏好设置的温度单位，返回相应的温度数值+温度单位
     **/
    public static String formatTemperature(Context context, String temperature) {
        if (context.getString(R.string.pref_temp_units_default).equals(getPreferredTempUnits(context)))
            return temperature;
        return celsiusToFahrenheit(context, temperature);
    }

}
