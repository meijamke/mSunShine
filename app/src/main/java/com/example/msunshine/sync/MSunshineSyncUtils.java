package com.example.msunshine.sync;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

import com.example.msunshine.data.ExplicitIntentData;
import com.example.msunshine.data.WeatherContract;

public class MSunshineSyncUtils {

    private static boolean isInitialize;

    public static void initialize(final Context context, final String city) {
        if (isInitialize)
            return;
        isInitialize = true;

        new Thread(new Runnable() {
            @Override
            public void run() {
                Cursor mCursor = context.getContentResolver().query(
                        WeatherContract.CONTENT_URI,
                        new String[]{WeatherContract.WeatherEntry.COLUMN_DATE},
                        WeatherContract.WeatherEntry.getSQLSelectTodayOnwords(),
                        null,
                        null
                );
                if (mCursor == null || mCursor.getCount() == 0)
                    startImmediateSync(context, city);
                else
                    mCursor.close();
            }
        }).start();
    }

    public static void startImmediateSync(Context context, String city) {
        Intent intent = new Intent(context, MSunshineSyncIntentService.class);
        intent.putExtra(ExplicitIntentData.STRING_CITY_NAME, city);
        context.startService(intent);
    }
}
