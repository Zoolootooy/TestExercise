package com.example.testexercise;

import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

import java.text.DateFormat;
import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MyService extends Service {


    final String LOG_TAG = "myLogs";
    ExecutorService es;
    String Time;
    int Counter;
    int End = 0;

    public void onCreate() {
        super.onCreate();
        Log.d(LOG_TAG, "Service onCreate");
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(LOG_TAG, "Service onStartCommand");

        SharedPreferences sPref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor ed = sPref.edit();
        Time = DateFormat.getDateTimeInstance().format(new Date());
        ed.putString("Time", Time);
        Log.d(LOG_TAG, "Time: " + Time);
        ed.commit();

        Intent in = new Intent("backTime");
        in.putExtra("Time", Time);
        sendBroadcast(in);


        someTask();

        return super.onStartCommand(intent, flags, startId);
    }

    public void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "Service onDestroy");
    }

    public IBinder onBind(Intent intent) {
        Log.d(LOG_TAG, "Service onBind");
        return null;
    }

    void someTask() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d(LOG_TAG, "Service: New Thread!");

                SharedPreferences sPref = getSharedPreferences("mysettings", Context.MODE_PRIVATE);
                Counter = sPref.getInt("Counter", 0);
                Log.d(LOG_TAG, "Service: Counter = " + Counter);

                while (End == 0){
                    Counter++;
                    //отправить в настройки
                    try {
                        TimeUnit.SECONDS.sleep(2);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Log.d(LOG_TAG, "Service: Counter = " + Counter);
                }


                SharedPreferences.Editor ed = sPref.edit();
                ed.putInt("Counter", Counter);
                ed.commit();


            }
        }).start();
    }
}