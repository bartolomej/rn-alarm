package com.alarm;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class AlarmDates {

    private static final String postfix = "_DATES";
    String uid;
    String alarmUid;
    Date[] dates;
    int[] notificationIds;

    public AlarmDates (String alarmUid, Date[] dates) {
        this.uid = alarmUid + postfix;
        this.alarmUid = alarmUid;
        this.dates = dates;
        this.notificationIds = new int[dates.length];
        for (int i = 0; i < dates.length; i++) {
            this.notificationIds[i] = randomId();
        }
    }

    public static String getDatesId (String alarmUid) {
        return alarmUid + postfix;
    }

    public int getNotificationId (Date date) {
        for (int i = 0; i < dates.length; i++) {
            if (dates[i].equals(date)) {
                return notificationIds[i];
            }
        }
        return -1;
    }

    public static Date setNextWeek (Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, 7);
        return calendar.getTime();
    }

    public static Date snooze (Date date, int minutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, minutes);
        return calendar.getTime();
    }

    public static boolean isDatesId (String id) {
        return id.contains(postfix);
    }

    public int getCurrentNotificationId () {
        Date current = getCurrentDate();
        return getNotificationId(current);
    }

    public Date getCurrentDate () {
        Calendar calendar = Calendar.getInstance();
        int currentDay = calendar.get(Calendar.DAY_OF_WEEK);
        for (Date date : dates) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            if (cal.get(Calendar.DAY_OF_WEEK) == currentDay) {
                return date;
            }
        }
        return null;
    }

    public ArrayList<Date> getDates () {
        return new ArrayList<>(Arrays.asList(dates));
    }

    public void update (Date old, Date updated) {
        for (int i = 0; i < dates.length; i++) {
            if (dates[i].equals(old)) {
                dates[i] = updated;
                return;
            }
        }
    }

    public static AlarmDates fromJson (String json) {
        return new Gson().fromJson(json, AlarmDates.class);
    }

    public static String toJson (AlarmDates dates) {
        return new Gson().toJson(dates);
    }

    private static int randomId () {
        return (int)(Math.random() * 10000000);
    }
}
