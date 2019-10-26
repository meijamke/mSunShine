package com.example.msunshine.sync;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.concurrent.futures.ResolvableFuture;
import androidx.work.Data;
import androidx.work.ListenableWorker;
import androidx.work.WorkerParameters;

import com.example.msunshine.data.IntentData;
import com.google.common.util.concurrent.ListenableFuture;

public class MSunshineSyncWorkManager extends ListenableWorker {

    private AsyncTask<Void, Void, Void> mBackgroundTask;
    private ResolvableFuture<Result> mResult;

    /**
     * @param appContext   The application {@link Context}
     * @param workerParams Parameters to setup the internal state of this worker
     */
    public MSunshineSyncWorkManager(@NonNull Context appContext, @NonNull WorkerParameters workerParams) {
        super(appContext, workerParams);
        mResult = ResolvableFuture.create();
    }


    @SuppressLint("StaticFieldLeak")
    @NonNull
    @Override
    public ListenableFuture<Result> startWork() {

        Data inputData = getInputData();
        final String city = inputData.getString(IntentData.STRING_CITY_NAME);

        mBackgroundTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                MSunshineSyncUtils.startImmediateSync(getApplicationContext(), city);
                return null;
            }
        };
        mBackgroundTask.execute();

        if (mBackgroundTask != null) {
            mResult.set(Result.success());
            return mResult;
        }
        mResult.set(Result.failure());
        return mResult;
    }

    @Override
    public void onStopped() {
        if (mBackgroundTask != null)
            mBackgroundTask.cancel(false);
        super.onStopped();
    }
}
