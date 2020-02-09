package com.alarm;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

enum AlarmType { REPEATING, SINGLE }

public class Alarm implements Cloneable, Serializable {

    String uid;
    ArrayList<Integer> days;
    int hour;
    int minutes;
    int snoozeInterval;
    String title;
    String description;
    AlarmType type;

    public Alarm (String uid, ArrayList<Integer> days, int hour, int minutes, String title, String description, boolean repeating) {
        this.uid = uid;
        this.days = days;
        this.hour = hour;
        this.minutes = minutes;
        this.snoozeInterval = 1;
        this.title = title;
        this.description = description;
        this.type = repeating ? AlarmType.REPEATING : AlarmType.SINGLE;
    }

    public AlarmType getType () {
        return type;
    }

    public Date[] getDates () {
        Date[] dates = new Date[days.size()];
        for (int i = 0; i < dates.length; i++) {
            Calendar date = Utils.getDate(days.get(i), hour, minutes);
            dates[i] = date.getTime();
        }
        return dates;
    }

    public Date getCurrentDate () {
        Calendar calendar = Calendar.getInstance();
        int currentDay = calendar.get(Calendar.DAY_OF_WEEK);
        for (int day : days) {
            if (day == currentDay) {
                return Utils.getDate(day, hour, minutes).getTime();
            }
        }
        return null;
    }

    public static int getNotificationId (Date date) {
        return (int)date.getTime();
    }

    public AlarmDates getAlarmDates () {
        return new AlarmDates(uid, getDates());
    }

    public static Alarm fromJson (String json) {
        return new Gson().fromJson(json, Alarm.class);
    }

    public static String toJson (Alarm alarm) {
        return new Gson().toJson(alarm);
    }

    public Alarm clone () throws CloneNotSupportedException {
        return (Alarm) super.clone();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Alarm)) return false;
        Alarm alarm = (Alarm)o;
        return (
                this.hour == alarm.hour &&
                this.minutes == alarm.minutes &&
                this.snoozeInterval == alarm.snoozeInterval &&
                this.uid.equals(alarm.uid) &&
                this.days.equals(alarm.days) &&
                this.title.equals(alarm.title) &&
                this.description.equals(alarm.description)
        );
    }
}
