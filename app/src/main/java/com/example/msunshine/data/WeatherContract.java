package com.example.msunshine.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class WeatherContract {

    public static final String CONTENT_AUTHORITY = "com.example.msunshine.data";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_WEATHER = "weather";
    public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
            .appendPath(PATH_WEATHER)
            .build();
    static final String TYPE_WEATHER = CONTENT_AUTHORITY + "." + PATH_WEATHER;

    public static class WeatherEntry implements BaseColumns {
        public static final String TABLE_NAME = "weather";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_WEEK = "week";
        public static final String COLUMN_DAY_CONDITION = "day-weather";
        public static final String COLUMN_NIGHT_CONDITION = "night-weather";
        public static final String COLUMN_DAY_TEMP = "day-temperature";
        public static final String COLUMN_NIGHT_TEMP = "night-temperature";
        static final String COLUMN_DAY_WIND_DIRECTION = "day-wind-direct";
        static final String COLUMN_NIGHT_WIND_DIRECTION = "night-wind-direct";
        static final String COLUMN_DAY_WIND_POWER = "day-wind-power";
        static final String COLUMN_NIGHT_WIND_POWER = "night-wind-power";
    }
}
