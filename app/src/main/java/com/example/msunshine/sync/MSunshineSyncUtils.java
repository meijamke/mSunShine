package com.example.msunshine.sync;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

import androidx.work.BackoffPolicy;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.example.msunshine.data.IntentData;
import com.example.msunshine.data.WeatherContract;

import java.util.concurrent.TimeUnit;

public class MSunshineSyncUtils {

    private static boolean isInitialize;

    private static final int TIME_REPEAT_INTERVAL_HOURS = 4;
    private static final int TIME_INIT_DELAY_HOURS = 4;
    private static final int TIME_BACKOFF_DELAY_HOURS = 4;
    private static final String TAG_WORKER_SYNC = "sync work";

    public static void initialize(final Context context, final String city) {
        if (isInitialize)
            return;
        isInitialize = true;

        periodWeatherSync(context, city);

        new Thread(new Runnable() {
            @Override
            public void run() {
                Cursor mCursor = context.getContentResolver().query(
                        WeatherContract.WeatherEntry.CONTENT_URI,
                        new String[]{WeatherContract.WeatherEntry.COLUMN_DATE},
                        WeatherContract.WeatherEntry.getSQLSelectTodayForwards(),
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
        intent.putExtra(IntentData.STRING_CITY_NAME, city);
        context.startService(intent);
    }

    private static void periodWeatherSync(Context context, String city) {

        Data inputData = new Data.Builder()
                .putString(IntentData.STRING_CITY_NAME, city)
                .build();

        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();

        PeriodicWorkRequest workRequest = new PeriodicWorkRequest.Builder(MSunshineSyncWorkManager.class, TIME_REPEAT_INTERVAL_HOURS, TimeUnit.HOURS)
                .setInitialDelay(TIME_INIT_DELAY_HOURS, TimeUnit.HOURS)
                .setInputData(inputData)
                .setConstraints(constraints)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, TIME_BACKOFF_DELAY_HOURS, TimeUnit.HOURS)
                .build();
        WorkManager.getInstance(context).enqueueUniquePeriodicWork(TAG_WORKER_SYNC, ExistingPeriodicWorkPolicy.REPLACE, workRequest);
    }
}
