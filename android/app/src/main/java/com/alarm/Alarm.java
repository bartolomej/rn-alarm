package com.alarm;

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
    String title;
    String description;
    AlarmType type;

    public Alarm (String uid, ArrayList<Integer> days, int hour, int minutes, String title, String description, boolean repeating) {
        this.uid = uid;
        this.days = days;
        this.hour = hour;
        this.minutes = minutes;
        this.title = title;
        this.description = description;
        this.type = repeating ? AlarmType.REPEATING : AlarmType.SINGLE;
    }

    public AlarmType getType () {
        return this.type;
    }

    public Date[] getDates () {
        Date[] dates = new Date[this.days.size()];
        for (int i = 0; i < dates.length; i++) {
            Calendar date = Utils.getDate(this.days.get(i), this.hour, this.minutes);
            dates[i] = date.getTime();
        }
        return dates;
    }

    public static int getNotificationId (Date date) {
        return (int)date.getTime();
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
                this.uid.equals(alarm.uid) &&
                this.days.equals(alarm.days) &&
                this.title.equals(alarm.title) &&
                this.description.equals(alarm.description)
        );
    }
}
