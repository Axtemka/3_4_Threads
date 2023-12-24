package com.axtemka.a3_4_threads;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.util.Log;
import android.view.View;

import com.axtemka.a3_4_threads.databinding.ActivityMainBinding;
import com.axtemka.a3_4_threads.thread.MyWorker;
import com.google.android.material.progressindicator.BaseProgressIndicator;

import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private UUID threadId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Work in progress");
                try {
                    Thread.sleep(10*1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                Log.d(TAG, "Work finished!");
            }
        });

        binding.btnStartInThread.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Data data = new Data.Builder()
                        .putString("keyA", "value1")
                        .putInt("keyB", 2)
                        .build();

                OneTimeWorkRequest oneTimeWorkRequest = new OneTimeWorkRequest.Builder(MyWorker.class)
                        .setInputData(data)
                        .build();

                threadId = oneTimeWorkRequest.getId();

                WorkManager
                        .getInstance(getApplicationContext())
                        .enqueue(oneTimeWorkRequest);
            }
        });
        binding.getResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (threadId != null){
                    WorkManager.getInstance(getApplicationContext())
                            .getWorkInfoByIdLiveData(threadId)
                            .observe(MainActivity.this, new Observer<WorkInfo>() {
                                @Override
                                public void onChanged(WorkInfo workInfo) {
                                    if (workInfo != null){
                                        Log.d(TAG, "Working state: " + workInfo.getState());
                                        String msg = workInfo.getOutputData().getString("keyC");
                                        Log.d(TAG, "Result message: " + msg);
                                    }
                                }
                            });
                }
            }
        });
    }
}