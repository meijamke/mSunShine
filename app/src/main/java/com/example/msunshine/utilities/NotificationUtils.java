package com.example.msunshine.utilities;


import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.TaskStackBuilder;

import com.example.msunshine.DetailActivity;
import com.example.msunshine.R;
import com.example.msunshine.data.MSunshinePreference;
import com.example.msunshine.data.WeatherContract;

public class NotificationUtils {

    private static final int INDEX_WEATHER_DAY_CONDITION = 0;
    private static final int INDEX_WEATHER_NIGHT_CONDITION = 1;
    private static final int INDEX_WEATHER_DAY_TEMP = 2;
    private static final int INDEX_WEATHER_NIGHT_TEMP = 3;
    private static final int ID_NOTIFICATION_WEATHER = 1;
    private static String[] NOTIFICATION_PROJECTION = {
            WeatherContract.WeatherEntry.COLUMN_DAY_CONDITION,
            WeatherContract.WeatherEntry.COLUMN_NIGHT_CONDITION,
            WeatherContract.WeatherEntry.COLUMN_DAY_TEMP,
            WeatherContract.WeatherEntry.COLUMN_NIGHT_TEMP
    };

    public static void notifyUserTodayWeather(Context context) {

        //Date format:yyyy-MM-dd
        String today = MSunshineDateUtils.getNormalizedDayNow();

        Cursor mCursor = context.getContentResolver().query(
                WeatherContract.WeatherEntry.CONTENT_URI.buildUpon().appendPath(today).build(),
                NOTIFICATION_PROJECTION,
                null,
                null,
                null);
        if (mCursor != null && mCursor.moveToFirst()) {
            String dayCondition = mCursor.getString(INDEX_WEATHER_DAY_CONDITION);
            String nightCondition = mCursor.getString(INDEX_WEATHER_NIGHT_CONDITION);
            String dayTemp = MSunshinePreference.formatTemperature(context, mCursor.getString(INDEX_WEATHER_DAY_TEMP));
            String nightTemp = MSunshinePreference.formatTemperature(context, mCursor.getString(INDEX_WEATHER_NIGHT_TEMP));

            int smallDayIcResId = MSunshineWeatherUtils.getSmallIcResIdForWeatherCondition(dayCondition);
            int largeDayArtResId = MSunshineWeatherUtils.getLargeArtResIdForWeatherCondition(dayCondition);
            Bitmap largeIconBitmap = BitmapFactory.decodeResource(context.getResources(), largeDayArtResId);

            String notificationTitle = context.getString(R.string.app_name);
            String notificationText = String.format("%s-%s\n%s-%s",
                    dayCondition, nightCondition, dayTemp, nightTemp);

            Intent intent = new Intent(context, DetailActivity.class);
            TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(context);
            taskStackBuilder.addNextIntentWithParentStack(intent);
            PendingIntent pendingIntent = taskStackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                    .setContentTitle(notificationTitle)
                    .setContentText(notificationText)
                    .setSmallIcon(smallDayIcResId)
                    .setLargeIcon(largeIconBitmap)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);
            Notification notification = notificationBuilder.build();

            NotificationManagerCompat.from(context).notify(ID_NOTIFICATION_WEATHER, notification);

            MSunshinePreference.saveLastNotificationTime(context);
        }
        mCursor.close();
    }
}
