package com.example.msunshine.data;

import android.provider.BaseColumns;

public class WeatherContract {

    static class WeatherEntry implements BaseColumns {
        static final String TABLE_NAME = "weather";
        static final String COLUMN_DATE = "date";
        static final String COLUMN_DAY_WEATHER = "day-weather";
        static final String COLUMN_NIGHT_WEATHER = "night-weather";
        static final String COLUMN_DAY_TEMP = "day-temperature";
        static final String COLUMN_NIGHT_TEMP = "night-temperature";
        static final String COLUMN_DAY_WIND_DIRECTION = "day-wind-direct";
        static final String COLUMN_NIGHT_WIND_DIRECTION = "night-wind-direct";
        static final String COLUMN_DAY_WIND_POWER = "day-wind-power";
        static final String COLUMN_NIGHT_WIND_POWER = "night-wind-power";
    }
}
