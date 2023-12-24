package com.axtemka.a3_4_threads.thread;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class MyWorker extends Worker {

    private static final String TAG = "MyWorker";

    public MyWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.d(TAG, "Work started");
        try {
            for (int i = 0; i < 3; i++) {
                Thread.sleep(3*1000);

                Log.d(TAG, i+" work is running");

                Data inputData = getInputData();
                String stringValue = getInputData().getString("keyA");
                int intValue = getInputData().getInt("keyB", 0);

                Log.d(TAG, "String : " + stringValue);
                Log.d(TAG, "Int : " + intValue);
            }
        } catch (InterruptedException e) {
            Log.e(TAG, e.getMessage());
            return Result.failure();
        }
        Log.d(TAG, "Work finished");

        Data data = new Data.Builder()
                .putString("keyC", "Hello from MyWorker")
                .build();
        return Result.success(data);
    }
}
