package com.alarm;

import android.content.Context;
import android.content.SharedPreferences;

import com.app.R;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

class Storage {

    static void saveAlarm(Context context, Alarm alarm) {
        SharedPreferences.Editor editor = getEditor(context);
        editor.putString(alarm.uid, Alarm.toJson(alarm));
        editor.apply();
    }

    static void saveDates(Context context, AlarmDates dates) {
        SharedPreferences.Editor editor = getEditor(context);
        editor.putString(dates.uid, AlarmDates.toJson(dates));
        editor.apply();
    }

    static Alarm[] getAllAlarms(Context context) {
        ArrayList<Alarm> alarms = new ArrayList<>();
        SharedPreferences preferences = getSharedPreferences(context);
        Map<String, ?> keyMap = preferences.getAll();
        for (Map.Entry<String, ?> entry : keyMap.entrySet()) {
            if (AlarmDates.isDatesId(entry.getKey())) continue;
            alarms.add(Alarm.fromJson((String)entry.getValue()));
        }
        return alarms.toArray(new Alarm[0]);
    }

    static Alarm getAlarm(Context context, String alarmUid) {
        SharedPreferences preferences = getSharedPreferences(context);
        return Alarm.fromJson(preferences.getString(alarmUid, null));
    }

    static AlarmDates getDates(Context context, String alarmUid) {
        SharedPreferences preferences = getSharedPreferences(context);
        String json = preferences.getString(AlarmDates.getDatesId(alarmUid), null);
        return AlarmDates.fromJson(json);
    }

    static void removeAlarm(Context context, String alarmUid) {
        remove(context, alarmUid);
    }

    static void removeDates(Context context, String alarmUid) {
        remove(context, AlarmDates.getDatesId(alarmUid));
    }

    private static void remove(Context context, String id) {
        SharedPreferences preferences = getSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(id);
        editor.apply();
    }

    private static SharedPreferences.Editor getEditor (Context context) {
        SharedPreferences sharedPreferences = getSharedPreferences(context);
        return sharedPreferences.edit();
    }

    private static SharedPreferences getSharedPreferences(Context context) {
        String fileKey = context.getResources().getString(R.string.notification_channel_id);
        return context.getSharedPreferences(fileKey, Context.MODE_PRIVATE);
    }

}
