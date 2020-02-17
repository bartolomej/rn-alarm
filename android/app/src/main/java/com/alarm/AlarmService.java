package com.alarm;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class AlarmService extends Service {

    private static final String TAG = "AlarmService";
    private String alarmUid;
    private Thread backgroundThread;

    @Override
    public IBinder onBind(final Intent intent) {
        Log.d(TAG, "On bind " + intent.getExtras());
        return null;
    }

    // called only the first time we start the service
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "Creating service");
    }

    private Runnable alarmTask = new Runnable() {
        @Override
        public void run() {
            Log.d(TAG, "Running thread for alarm " + alarmUid);
            Manager.start(getApplicationContext(), alarmUid);
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "Stopping service");
    }

    // triggered every time we call startService() when we start our service
    @Override
    public int onStartCommand(final Intent intent, final int flags, final int startId) {
        super.onStartCommand(intent, flags, startId);
        Log.d(TAG, "On start command");
        this.alarmUid = intent.getStringExtra("ALARM_UID");
        Notification notification = Helper.getNotification(this, 0, this.alarmUid, "Service", "Playing music");
        backgroundThread = new Thread(alarmTask);
        backgroundThread.start();
        startForeground(1, notification);
        return START_STICKY;
    }

}