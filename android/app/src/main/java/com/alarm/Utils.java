package com.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.alarm.receivers.AlarmReceiver;

import java.util.Calendar;

public class Utils {

    public static Calendar addDays (Calendar date, int days) {
        Calendar nextDate = (Calendar)date.clone();
        nextDate.add(Calendar.DATE, days);
        return nextDate;
    }

    public static Calendar getDate (int day, int hour, int minute) {
        Calendar date = Calendar.getInstance();
        date.set(Calendar.DAY_OF_WEEK, day);
        date.set(Calendar.HOUR_OF_DAY, hour);
        date.set(Calendar.MINUTE, minute);
        date.set(Calendar.SECOND, 0);
        return date;
    }

    public static Class getMainActivityClass(Context context) {
        String packageName = context.getPackageName();
        Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        try {
            String className = launchIntent.getComponent().getClassName();
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (NullPointerException e) {
            e.printStackTrace();
            return null;
        }
    }

}
