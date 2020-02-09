package com.alarm.receivers;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.alarm.Service;

public class AlarmReceiver extends BroadcastReceiver {

    private static final String TAG = "AlarmReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        String alarmUid = intent.getStringExtra("ALARM_UID");
        Service.start(context, alarmUid);
        Log.d(TAG, "received alarm: " + alarmUid);
    }
}
