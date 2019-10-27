package com.example.msunshine.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.format.DateUtils;

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
        String celsiusNumber = celsius.split("℃")[0];
        return String.format(context.getString(R.string.temperature_format), Integer.parseInt(celsiusNumber) * 1.8 + 32);
    }

    /**
     * 根据偏好设置的温度单位，返回相应的温度数值+温度单位
     **/
    public static String formatTemperature(Context context, String temperature) {
        if (context.getString(R.string.pref_temp_units_default).equals(getPreferredTempUnits(context)))
            return temperature;
        return celsiusToFahrenheit(context, temperature);
    }

    /**
     * 获取用户设置的天气通知偏好值
     **/
    public static boolean getPreferredWeatherNotification(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getBoolean(context.getString(R.string.pref_notification_key),
                context.getResources().getBoolean(R.bool.pref_notification_default));
    }

    /**
     * 保存上一次通知的系统时间（毫秒）
     **/
    public static void saveLastNotificationTime(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(
                context.getString(R.string.last_notification_key),
                System.currentTimeMillis());
        editor.apply();
    }

    /**
     * 两次通知的时间是否超过一天，若第一次开启通知，默认上一次开启时间1970.1.1午夜12点
     **/
    public static boolean overOneDaySinceLastNotificationTime(Context context) {

        long nowInMillis = System.currentTimeMillis();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        long lastTime = sharedPreferences.getLong(
                context.getString(R.string.last_notification_key),
                0);
        return (nowInMillis - lastTime) >= DateUtils.DAY_IN_MILLIS;
    }
}
