package com.alarm;

import android.content.Context;
import android.content.SharedPreferences;

import com.app.R;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class Storage {

    public static void saveAlarm(Context context, Alarm alarm) {
        SharedPreferences preferences = getSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        String json = new Gson().toJson(alarm);
        editor.putString(alarm.uid, json);
        editor.apply();
    }

    public static void saveScheduled(Context context, String alarmUid, Date[] days) {
        SharedPreferences preferences = getSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        String json = new Gson().toJson(days);
        editor.putString(alarmUid + "_SCHEDULED", json);
        editor.apply();
    }

    public static Alarm[] getAllAlarms(Context context) {
        ArrayList<Alarm> alarms = new ArrayList<>();
        SharedPreferences preferences = getSharedPreferences(context);
        Map<String, ?> keyMap = preferences.getAll();
        for (Map.Entry<String, ?> entry : keyMap.entrySet()) {
            if (entry.getKey().contains("_SCHEDULED")) continue;
            alarms.add(new Gson().fromJson((String) entry.getValue(), Alarm.class));
        }
        return alarms.toArray(new Alarm[alarms.size()]);
    }

    public static Alarm getAlarm(Context context, String alarmUid) {
        SharedPreferences preferences = getSharedPreferences(context);
        String json = preferences.getString(alarmUid, null);
        return new Gson().fromJson(json, Alarm.class);
    }

    public static Date[] getScheduled(Context context, String alarmUid) {
        SharedPreferences preferences = getSharedPreferences(context);
        String json = preferences.getString(alarmUid + "_SCHEDULED", null);
        return new Gson().fromJson(json, Date[].class);
    }

    public static void removeAlarm(Context context, String alarmUid) {
        remove(context, alarmUid);
    }

    public static void removeScheduled(Context context, String alarmUid) {
        remove(context, alarmUid + "_SCHEDULED");
    }

    public static void remove(Context context, String id) {
        SharedPreferences preferences = getSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(id);
        editor.apply();
    }

    public static void removeAll(Context context) {
        SharedPreferences preferences = getSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }

    private static SharedPreferences getSharedPreferences(Context context) {
        String fileKey = context.getResources().getString(R.string.notification_channel_id);
        return context.getSharedPreferences(fileKey, Context.MODE_PRIVATE);
    }

}
