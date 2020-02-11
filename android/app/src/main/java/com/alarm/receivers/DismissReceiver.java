package com.alarm.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class DismissReceiver extends BroadcastReceiver {

    private static final String TAG = "AlarmDismissReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: snooze alarm and send event to js
        Log.d(TAG, "dismissed alarm notification for: " + intent.getStringExtra("ALARM_UID"));
    }
}
