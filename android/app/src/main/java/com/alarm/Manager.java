package com.alarm;

import android.content.Context;
import android.util.Log;

import java.util.Date;

public class Manager {

    private static final String TAG = "AlarmManager";
    private static Sound sound;
    private static String activeAlarmUid;

    static String getActiveAlarm() {
        return activeAlarmUid;
    }

    static void schedule(Context context, Alarm alarm) {
        AlarmDates dates = alarm.getAlarmDates();
        for (Date date : dates.getDates()) {
            Helper.scheduleAlarm(context, alarm.uid, date.getTime(), dates.getNotificationId(date));
        }
        Storage.saveAlarm(context, alarm);
        Storage.saveDates(context, dates);
    }

    public static void reschedule(Context context) {
        Alarm[] alarms = Storage.getAllAlarms(context);
        for (Alarm alarm : alarms) {
            Storage.removeDates(context, alarm.uid);
            AlarmDates dates = alarm.getAlarmDates();
            Storage.saveDates(context, dates);
            for (Date date : dates.getDates()) {
                Helper.scheduleAlarm(context, alarm.uid, date.getTime(), dates.getNotificationId(date));
                Log.d(TAG, "rescheduling alarm: " + alarm.uid);
            }
        }
    }

    static void update(Context context, Alarm alarm) {
        AlarmDates prevDates = Storage.getDates(context, alarm.uid);
        AlarmDates dates = alarm.getAlarmDates();
        for (Date date : dates.getDates()) {
            Helper.scheduleAlarm(context, alarm.uid, date.getTime(), dates.getNotificationId(date));
        }
        Storage.saveAlarm(context, alarm);
        Storage.saveDates(context, dates);
        if (prevDates == null) return;
        for (Date date : prevDates.getDates()) {
            Helper.cancelAlarm(context, dates.getNotificationId(date));
        }
    }

    static void removeAll(Context context) {
        Alarm[] alarms = Storage.getAllAlarms(context);
        for (Alarm alarm : alarms) {
            remove(context, alarm.uid);
        }
    }

    static void remove(Context context, String alarmUid) {
        if (sound != null) {
            sound.stop();
        }
        Alarm alarm = Storage.getAlarm(context, alarmUid);
        AlarmDates dates = Storage.getDates(context, alarm.uid);
        Storage.removeAlarm(context, alarm.uid);
        Storage.removeDates(context, alarm.uid);
        if (dates == null) return;
        for (Date date : dates.getDates()) {
            int notificationID = dates.getNotificationId(date);
            Helper.cancelAlarm(context, notificationID);
        }
    }

    static void enable(Context context, String alarmUid) {
        Alarm alarm = Storage.getAlarm(context, alarmUid);
        if (!alarm.active) {
            alarm.active = true;
            Storage.saveAlarm(context, alarm);
        } else {
            Log.d(TAG, "Alarm already active - exiting job");
            return;
        }
        AlarmDates dates = alarm.getAlarmDates();
        Storage.saveDates(context, dates);
        for (Date date : dates.getDates()) {
            Helper.scheduleAlarm(context, alarmUid, date.getTime(), dates.getNotificationId(date));
        }
    }

    static void disable(Context context, String alarmUid) {
        Alarm alarm = Storage.getAlarm(context, alarmUid);
        if (alarm.active) {
            alarm.active = false;
            Storage.saveAlarm(context, alarm);
        } else {
            Log.d(TAG, "Alarm already inactive - exiting job");
            return;
        }
        AlarmDates dates = Storage.getDates(context, alarmUid);
        for (Date date : dates.getDates()) {
            Helper.cancelAlarm(context, dates.getNotificationId(date));
        }
    }

    static void start(Context context, String alarmUid) {
        activeAlarmUid = alarmUid;
        sound = new Sound(context);
        sound.play("default");

        Log.d(TAG, "Starting " + activeAlarmUid);
    }

    static void stop(Context context) {
        Log.d(TAG, "Stopping " + activeAlarmUid);

        sound.stop();
        Alarm alarm = Storage.getAlarm(context, activeAlarmUid);
        AlarmDates dates = Storage.getDates(context, activeAlarmUid);
        if (alarm.repeating) {
            Date current = dates.getCurrentDate();
            Date updated = AlarmDates.setNextWeek(current);
            dates.update(current, updated);
            Storage.saveDates(context, dates);
            Helper.scheduleAlarm(context, dates.alarmUid, updated.getTime(), dates.getCurrentNotificationId());
        } else {
            alarm.active = false;
            Storage.saveAlarm(context, alarm);
            Storage.removeDates(context, activeAlarmUid);
        }
        activeAlarmUid = null;
    }

    static void snooze(Context context) {
        Log.d(TAG, "Snoozing " + activeAlarmUid);

        sound.stop();
        Alarm alarm = Storage.getAlarm(context, activeAlarmUid);
        AlarmDates dates = Storage.getDates(context, activeAlarmUid);
        Date updated = AlarmDates.snooze(new Date(), alarm.snoozeInterval);
        dates.update(dates.getCurrentDate(), updated);
        Storage.saveDates(context, dates);
        Helper.scheduleAlarm(context, dates.alarmUid, updated.getTime(), dates.getCurrentNotificationId());
        activeAlarmUid = null;
    }

}