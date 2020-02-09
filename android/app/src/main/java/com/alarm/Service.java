package com.alarm;

import android.content.Context;
import android.util.Log;

import java.util.Date;

public class Service {

    private static final String TAG = "AlarmService";

    public static void schedule (Context context, Alarm alarm) {
        Date[] dates = alarm.getDates();
        for (Date date : dates) {
            Log.d(TAG, "alarm set to fire on " + date.toString());
            AlarmHelper.scheduleAlarm(context, date.getTime(), Alarm.getNotificationId(date));
        }
        Storage.saveAlarm(context, alarm);
        Storage.saveScheduled(context, alarm.uid, dates);
    }

    public static void update (Context context, Alarm alarm) {

    }

    public static void remove (Context context, String alarmUid) {
        Alarm alarm = Storage.getAlarm(context, alarmUid);
        Date[] dates = Storage.getScheduled(context, alarm.uid);
        for (Date date : dates) {
            AlarmHelper.cancelAlarm(context, Alarm.getNotificationId(date));
        }
        Storage.removeAlarm(context, alarm.uid);
        Storage.removeScheduled(context, alarm.uid);
    }

    public static void enable (Context context, String alarmUid) {

    }

    public static void disable (Context context, String alarmUid) {

    }

    public static void stop (Context context, String alarmUid) {

    }

    public static void snooze (Context context, String alarmUid) {

    }

}
