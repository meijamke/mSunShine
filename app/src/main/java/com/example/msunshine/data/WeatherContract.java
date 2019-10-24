package com.example.msunshine.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class WeatherContract {

    static final String CONTENT_AUTHORITY = "com.example.msunshine.data";
    static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    static final String PATH_WEATHER = "weather";

    public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
            .appendPath(PATH_WEATHER)
            .build();
    static final String TYPE_WEATHER = CONTENT_AUTHORITY + "." + PATH_WEATHER;

    public static class WeatherEntry implements BaseColumns {
        public static final String TABLE_NAME = "weather";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_WEEK = "week";
        public static final String COLUMN_DAY_CONDITION = "dayWeather";
        public static final String COLUMN_NIGHT_CONDITION = "nightWeather";
        public static final String COLUMN_DAY_TEMP = "dayTemperature";
        public static final String COLUMN_NIGHT_TEMP = "nightTemperature";
        public static final String COLUMN_DAY_WIND_DIRECTION = "dayWindDirect";
        public static final String COLUMN_NIGHT_WIND_DIRECTION = "nightWindDirect";
        public static final String COLUMN_DAY_WIND_POWER = "dayWindPower";
        public static final String COLUMN_NIGHT_WIND_POWER = "nightWindPower";
    }
}
