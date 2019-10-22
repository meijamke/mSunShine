package com.example.msunshine.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class WeatherDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "weather.db";
    private static final int DATABASE_VERSION = 1;

    private static final String CREATE_TABLE = "create table " +
            WeatherContract.WeatherEntry.TABLE_NAME + "(" +
            WeatherContract.WeatherEntry.COLUMN_DATE + " text not null," +
            WeatherContract.WeatherEntry.COLUMN_DAY_WEATHER + "text not null," +
            WeatherContract.WeatherEntry.COLUMN_NIGHT_WEATHER + "text not null," +
            WeatherContract.WeatherEntry.COLUMN_DAY_TEMP + "text not null," +
            WeatherContract.WeatherEntry.COLUMN_NIGHT_TEMP + "text not null," +
            WeatherContract.WeatherEntry.COLUMN_DAY_WIND_DIRECTION + "text not null," +
            WeatherContract.WeatherEntry.COLUMN_NIGHT_WIND_DIRECTION + "text not null," +
            WeatherContract.WeatherEntry.COLUMN_DAY_WIND_POWER + "text not null," +
            WeatherContract.WeatherEntry.COLUMN_NIGHT_WIND_POWER + "text not null," +
            "unique " + WeatherContract.WeatherEntry.COLUMN_DATE + " on conflict replace" +
            ");";

    public WeatherDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + WeatherContract.WeatherEntry.TABLE_NAME);
        onCreate(db);
    }
}
