package com.alarm;

import android.content.Context;
import android.util.Log;

import java.util.Date;

public class Service {

    private static final String TAG = "AlarmService";
    private static Sound sound;
    private static String activeAlarmUid;

    public static void schedule(Context context, Alarm alarm) {
        AlarmDates dates = alarm.getAlarmDates();
        for (Date date : dates.getDates()) {
            AlarmHelper.scheduleAlarm(context, alarm.uid, date.getTime(), dates.getNotificationId(date));
        }
        Storage.saveAlarm(context, alarm);
        Storage.saveDates(context, dates);
    }

    public static void update(Context context, Alarm alarm) {

    }

    public static void removeAll(Context context) {
        Alarm[] alarms = Storage.getAllAlarms(context);
        for (Alarm alarm : alarms) {
            remove(context, alarm.uid);
        }
    }

    public static void remove(Context context, String alarmUid) {
        Alarm alarm = Storage.getAlarm(context, alarmUid);
        AlarmDates dates = Storage.getDates(context, alarm.uid);
        for (Date date : dates.getDates()) {
            AlarmHelper.cancelAlarm(context, dates.getNotificationId(date));
        }
        Storage.removeAlarm(context, alarm.uid);
        Storage.removeDates(context, alarm.uid);
    }

    public static void enable(Context context, String alarmUid) {

    }

    public static void disable(Context context, String alarmUid) {

    }

    public static void start(Context context, String alarmUid) {
        activeAlarmUid = alarmUid;
        Alarm alarm = Storage.getAlarm(context, alarmUid);
        AlarmDates dates = Storage.getDates(context, alarmUid);
        AlarmHelper.sendNotification(context, alarm, dates.getCurrentNotificationId());
        sound = new Sound(context);
        sound.play("default");
    }

    public static void stop(Context context) {
        AlarmDates dates = Storage.getDates(context, activeAlarmUid);
        Date current = dates.getCurrentDate();
        Date updated = AlarmDates.setNextWeek(current);
        dates.update(current, updated);
        Storage.saveDates(context, dates);
        AlarmHelper.scheduleAlarm(context, dates.alarmUid, updated.getTime(), dates.getCurrentNotificationId());
        sound.stop();
        sound.release();
    }

    public static void snooze(Context context) {
        Alarm alarm = Storage.getAlarm(context, activeAlarmUid);
        AlarmDates dates = Storage.getDates(context, activeAlarmUid);
        Date current = dates.getCurrentDate();
        Date updated = AlarmDates.snooze(current, alarm.snoozeInterval);
        dates.update(current, updated);
        Storage.saveDates(context, dates);
        AlarmHelper.scheduleAlarm(context, dates.alarmUid, updated.getTime(), dates.getCurrentNotificationId());
        sound.stop();
        sound.release();
    }

}
