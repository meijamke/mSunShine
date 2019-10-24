package com.example.msunshine.sync;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.example.msunshine.data.ExplicitIntentData;

public class MSunshineSyncIntentService extends IntentService {

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     * <p>
     * name: Used to name the worker thread, important only for debugging.
     */
    public MSunshineSyncIntentService() {
        super("MSunshineSyncIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        MSunshineSyncTask.syncWeather(MSunshineSyncIntentService.this, intent.getStringExtra(ExplicitIntentData.STRING_CITY_NAME));
    }
}
