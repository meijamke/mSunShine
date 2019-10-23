package com.example.msunshine.sync;

import android.content.Context;
import android.content.Intent;

import com.example.msunshine.data.ExplicitIntentData;

public class MSunshineSyncUtils {

    public static void startImmediateSync(Context context, String city) {
        Intent intent = new Intent(context, MSunshineSyncIntentService.class);
        intent.putExtra(ExplicitIntentData.STRING_CITY_NAME, city);
        context.startService(intent);
    }
}
